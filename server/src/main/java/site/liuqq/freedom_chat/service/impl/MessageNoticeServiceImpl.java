package site.liuqq.freedom_chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.liuqq.freedom_chat.mapper.ContactMapper;
import site.liuqq.freedom_chat.mapper.MessageNoticeMapper;
import site.liuqq.freedom_chat.pojo.Contact;
import site.liuqq.freedom_chat.pojo.MessageNotice;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.service.MessageNoticeService;

import java.util.List;

@Service
public class MessageNoticeServiceImpl extends ServiceImpl<MessageNoticeMapper,MessageNotice> implements MessageNoticeService {

    @Autowired
    private MessageNoticeMapper messageNoticeMapper;

    @Autowired
    private ContactMapper contactMapper;

    @Override
    public Result selectByUid(String uid, String targetUid) {

        List<MessageNotice> messageNotices = messageNoticeMapper.selectByUid(uid);


        if(targetUid!=null){
            //判断targetUid是否是TA好友
            boolean flag=false;
            for (Contact contact : contactMapper.listByUid(uid)) {
                if(contact.getUid2().equals(targetUid)){
                    flag=true;
                    break;
                }
            }
            if(flag){ //如果是好友，再判断messageNotices有没有TA，如果没有再创建

                for (MessageNotice messageNotice : messageNotices) {
                    if(messageNotice.getUid2().equals(targetUid)){
                        flag=false;
                        break;
                    }
                }
                if(flag){
                    //该插入了
                    MessageNotice messageNotice=new MessageNotice();
                    messageNotice.setUid1(uid);
                    messageNotice.setUid2(targetUid);
                    messageNoticeMapper.insert00(messageNotice);
                    //刷新数据
                    messageNotices=messageNoticeMapper.selectByUid(uid);
                }
            }

        }



        return Result.success(messageNotices);
    }

    public Result clearCount(MessageNotice messageNotice){

        //验证给出的数是否合法
        int count = messageNotice.getCount();
        if(count!=0){
            Result.error("非法的修改");
        }
        messageNoticeMapper.update3(messageNotice);

        return Result.success();
    }

    @Override
    public Result delete(MessageNotice messageNotice) {


        messageNoticeMapper.delete(messageNotice);

        return Result.success();
    }


}
