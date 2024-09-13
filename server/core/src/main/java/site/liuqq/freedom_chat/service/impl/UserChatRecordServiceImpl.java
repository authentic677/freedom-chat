package site.liuqq.freedom_chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import site.liuqq.freedom_chat.conf.CustomConfig;
import site.liuqq.freedom_chat.mapper.ContactMapper;
import site.liuqq.freedom_chat.mapper.MessageMapper;
import site.liuqq.freedom_chat.mapper.UserChatRecordMapper;
import site.liuqq.freedom_chat.mapper.MessageNoticeMapper;
import site.liuqq.freedom_chat.pojo.*;
import site.liuqq.freedom_chat.service.UserChatRecordService;
import site.liuqq.freedom_chat.common.DataUpdateNotify;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.common.util.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserChatRecordServiceImpl extends ServiceImpl<UserChatRecordMapper,UserChatRecord> implements UserChatRecordService {
    @Autowired
    private UserChatRecordMapper userChatRecordMapper;
    @Autowired
    private ContactMapper contactMapper;
    @Autowired
    private MessageNoticeMapper messageNoticeMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private CustomConfig customConfig;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result insert(UserChatRecord userChatRecord) {
        //验证他们是否是好友关系
        Contact contact=new Contact();
        contact.setUid1(userChatRecord.getUid1());
        contact.setUid2(userChatRecord.getUid2());

        Contact contact1 = contactMapper.selectByPair(contact);
        if(contact1==null){
            return Result.error("你们不是好友");
        }
        //验证客户端发来的type字段是否合法
        String type= userChatRecord.getType();
        if(type==null){
            return Result.error("消息类型为空");
        }else if (type.equals("text")){
            //发送一条消息需要三个sql
            //1.messages表insert一条记录，需要主键返回
            Message message = new Message();
            message.setTime(Tools.now(customConfig.getZone()));
            message.setType("text");
            message.setContent(userChatRecord.getContent());
            messageMapper.insert0(message);
            //2.userChatRecord表新增两条记录
            //发送方新增1条
            UserChatRecord userChatRecord1 = new UserChatRecord();
            userChatRecord1.setUid1(userChatRecord.getUid2());
            userChatRecord1.setUid2(userChatRecord.getUid1());
            userChatRecord1.setMessageId(message.getId());
            userChatRecord1.setFlag(0);
            userChatRecordMapper.insert0(userChatRecord1);
            //接受方新增1条
            userChatRecord1 = new UserChatRecord();
            userChatRecord1.setUid1(userChatRecord.getUid1());
            userChatRecord1.setUid2(userChatRecord.getUid2());
            userChatRecord1.setMessageId(message.getId());
            userChatRecord1.setFlag(1);
            userChatRecordMapper.insert0(userChatRecord1);
            //下面是消息通知表的修改
            //存在即更新，不存在则创建
            boolean flag=false;//表示是否存在
            //我方
            List<MessageNotice> messageNotices = messageNoticeMapper.selectByUid(userChatRecord.getUid2());
            for (MessageNotice messageNotice : messageNotices) {
                if(messageNotice.getUid2().equals(userChatRecord.getUid1())){
                    //存在
                    flag=true;
                    //那么更新
                    messageNotice.setMessageId(message.getId());
                    messageNoticeMapper.update0(messageNotice);
                }
            }
            if(!flag){//不存在呀，新增
                System.out.println("按道理来说，我方不应该不存在");
            }
            flag=false;//保证flag的值
            //对方
            messageNotices = messageNoticeMapper.selectByUid(userChatRecord.getUid1());
            for (MessageNotice messageNotice : messageNotices) {
                if(messageNotice.getUid2().equals(userChatRecord.getUid2())){
                    //
                    flag=true;
                    //那么更新
                    messageNotice.setMessageId(message.getId());
                    messageNotice.setCount(messageNotice.getCount()+1);
                    messageNoticeMapper.update0(messageNotice);
                }
            }
            if(!flag){//不存在呀，新增
                MessageNotice messageNotice = new MessageNotice();
                messageNotice.setUid1(userChatRecord.getUid1());
                messageNotice.setUid2(userChatRecord.getUid2());
                messageNotice.setMessageId(message.getId());
                messageNotice.setCount(1);
                messageNoticeMapper.insert0(messageNotice);
            }
            //数据更新
            DataUpdateNotify.messageNotify(userChatRecord);
            DataUpdateNotify.messageNoticeNotify(userChatRecord);
            return Result.success();
        }else if (type.equals("file")){
            //文件元信息是json格式的，需要反序列化
            try {
                String content = userChatRecord.getContent(); //获取消息内容
                ObjectMapper objectMapper = new ObjectMapper();
                List<Map<String,Object>> fileMeta = objectMapper.readValue(content, List.class);

                List<String> objectNames=new ArrayList<>(); //遍历的输出
                fileMeta.forEach(e->{ //遍历
                    try {
                        //生成对象存储中的唯一对象名，格式(chat/<当前年月>/<文件类型>/<随机UUID>/<文件名>)
                        String name = (String)e.get("name");
                        Integer size = (Integer)e.get("size");
                        String type1=((String)e.getOrDefault("type","binary")).replaceAll("/","-");
                        String uuid = UUID.randomUUID().toString();
                        String objectName=Tools.getCurrentYearMonth()+"/"+type1+"/"+uuid+"/"+name;
                        //新增map键值对
                        e.put("objectName",objectName);
                        e.put("bucketName","chat");
                        e.put("peerUid",userChatRecord.getUid1());

                        //序列化为json
                        String newFileMeta=objectMapper.writeValueAsString(e);

                        //设置且附带过期时间
                        stringRedisTemplate.opsForValue().set(uuid,newFileMeta,1, TimeUnit.HOURS);

                        objectNames.add(uuid);
                    }catch (Exception e1){
                        log.error(e1.getMessage());
                    }
                });

                return Result.success(objectNames);
            }catch (Exception e){
                log.error(e.getMessage());
                return Result.error("server error");
            }
        } else{
            return Result.error("不支持的消息类型");
        }

    }

    //此方法几乎完全copy insert方法的if条件为text部分，只不过不再进行type判断了
    @Override
    public Result insert2(UserChatRecord userChatRecord){
        //发送一条消息需要三个sql
        //1.messages表insert一条记录，需要主键返回
        Message message = new Message();
        message.setTime(Tools.now(customConfig.getZone()));
        message.setType(userChatRecord.getType());
        message.setContent(userChatRecord.getContent());
        messageMapper.insert0(message);
        //2.userChatRecord表新增两条记录
        //发送方新增1条
        UserChatRecord userChatRecord1 = new UserChatRecord();
        userChatRecord1.setUid1(userChatRecord.getUid2());
        userChatRecord1.setUid2(userChatRecord.getUid1());
        userChatRecord1.setMessageId(message.getId());
        userChatRecord1.setFlag(0);
        userChatRecordMapper.insert0(userChatRecord1);
        //接受方新增1条
        userChatRecord1 = new UserChatRecord();
        userChatRecord1.setUid1(userChatRecord.getUid1());
        userChatRecord1.setUid2(userChatRecord.getUid2());
        userChatRecord1.setMessageId(message.getId());
        userChatRecord1.setFlag(1);
        userChatRecordMapper.insert0(userChatRecord1);
        //下面是消息通知表的修改
        //存在即更新，不存在则创建
        boolean flag=false;//表示是否存在
        //我方
        List<MessageNotice> messageNotices = messageNoticeMapper.selectByUid(userChatRecord.getUid2());
        for (MessageNotice messageNotice : messageNotices) {
            if(messageNotice.getUid2().equals(userChatRecord.getUid1())){
                //存在
                flag=true;
                //那么更新
                messageNotice.setMessageId(message.getId());
                messageNoticeMapper.update0(messageNotice);
            }
        }
        if(!flag){//不存在呀，新增
            System.out.println("按道理来说，我方不应该不存在");
        }
        flag=false;//保证flag的值
        //对方
        messageNotices = messageNoticeMapper.selectByUid(userChatRecord.getUid1());
        for (MessageNotice messageNotice : messageNotices) {
            if(messageNotice.getUid2().equals(userChatRecord.getUid2())){
                //
                flag=true;
                //那么更新
                messageNotice.setMessageId(message.getId());
                messageNotice.setCount(messageNotice.getCount()+1);
                messageNoticeMapper.update0(messageNotice);
            }
        }
        if(!flag){//不存在呀，新增
            MessageNotice messageNotice = new MessageNotice();
            messageNotice.setUid1(userChatRecord.getUid1());
            messageNotice.setUid2(userChatRecord.getUid2());
            messageNotice.setMessageId(message.getId());
            messageNotice.setCount(1);
            messageNoticeMapper.insert0(messageNotice);
        }
        //数据更新
        DataUpdateNotify.messageNotify(userChatRecord);
        DataUpdateNotify.messageNoticeNotify(userChatRecord);
        return Result.success();
    }

    @Override
    public Result query(UserChatRecord userChatRecord) {
        //验证他们是否是好友关系
        Contact contact=new Contact();
        contact.setUid1(userChatRecord.getUid1());
        contact.setUid2(userChatRecord.getUid2());
        Contact contact1 = contactMapper.selectByPair(contact);
        if(contact1==null){
            return Result.error("你们不是好友");
        }

        List<UserChatRecord> query = userChatRecordMapper.query(userChatRecord);




        return Result.success(query);
    }
}
