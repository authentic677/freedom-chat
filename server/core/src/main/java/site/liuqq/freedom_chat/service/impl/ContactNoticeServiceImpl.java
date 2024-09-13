package site.liuqq.freedom_chat.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.liuqq.freedom_chat.conf.CustomConfig;
import site.liuqq.freedom_chat.mapper.*;
import site.liuqq.freedom_chat.pojo.*;
import site.liuqq.freedom_chat.service.ContactNoticeService;
import site.liuqq.freedom_chat.common.DataUpdateNotify;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.common.util.Tools;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ContactNoticeServiceImpl implements ContactNoticeService {

    @Autowired
    ContactNoticeMapper contactNoticeMapper;
    @Autowired
    ContactMapper contactMapper;
    @Autowired
    ApplicationMapper applicationMapper;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    UserChatRecordMapper userChatRecordMapper;
    @Autowired
    MessageNoticeMapper messageNoticeMapper;
    @Autowired
    CustomConfig customConfig;

    @Override
    public Result insert(ContactNotice contactNotice) {
        //添加好友的前提是它们还不是好友，判断他们是否已经是好友了，这个要去contact表找，contactNotice表不准的
        Contact contact = new Contact();
        contact.setUid1(contactNotice.getUid1());
        contact.setUid2(contactNotice.getUid2());
        Contact contact1 = contactMapper.selectByPair(contact);
        if(contact1!=null){
            return Result.error("你们已经是好友了");
        }

        //先要获取一些信息
        int applicationId=-1; //关联的application id
        //1.获取我方列表信息
        boolean flag=false;//表示你的联系人通知列表里是否有他
        boolean flag1=false;//在有的前提下，是你验证别人(true)还是别人验证你(false)
        int flag2=0;//在有的前提下，这个申请的状态是pending(0)，agree(1),还是refuse(2),注意agree状态不代表一定是好友关系，可能同意之后删了
        List<ContactNotice> contactNotices = contactNoticeMapper.listByUid(contactNotice.getUid2());
        for (ContactNotice notice : contactNotices) {
            if(notice.getUid2().equals(contactNotice.getUid1())){
                flag=true;
                applicationId=notice.getApplicationId();
                if(notice.getFlag()==0){
                    flag1=true;
                    if(notice.getStatus().equals("agree")){
                        flag2=1;
                    }
                    if(notice.getStatus().equals("refuse")){
                        flag2=2;
                    }

                }
                break;
            }
        }
        //2、获取对方列表信息
        boolean flag3=false;//表示对方联系人通知列表是否有你
        boolean flag4=false;//在有的前提下，是对方验证你(true)还是你验证对方(false)
        int flag5=0;//在有的前提下，这个申请的状态是pending(0)，agree(1),还是refuse(2),注意agree状态不代表一定是好友关系，可能同意之后删了
        List<ContactNotice> contactNotices2 = contactNoticeMapper.listByUid(contactNotice.getUid1());
        for (ContactNotice notice : contactNotices2) {
            if(notice.getUid2().equals(contactNotice.getUid2())){
                flag3=true;
                applicationId=notice.getApplicationId();
                if(notice.getFlag()==0){
                    flag4=true;
                    if(notice.getStatus().equals("agree")){
                        flag5=1;
                    }
                    if(notice.getStatus().equals("refuse")){
                        flag2=2;
                    }

                }
                break;
            }
        }
        //如果对方已经发出了一个对你的请求,该请求还在pending状态，且你还没删，即你看得到，那么直接同意了
        if(flag&&flag1&&flag2==0){
            System.out.println("direct agree");
            return Result.success();
        }
        //如果这次是重复发出，则更新
        if(flag||flag3){ //这肯定是已存在申请，需要更新
            //更新申请
            Application application=new Application();
            application.setStatus("pending");
            application.setLeaveMessage(contactNotice.getLeaveMessage());
            application.setId(applicationId);
            application.setTime(Tools.now(customConfig.getZone()));
            applicationMapper.update(application);
            //更新申请关联的notice
            if(flag&&!flag3){ //你没删，对方删了
                log.info("接收方删了");
                //更新你的，新增对方的
                ContactNotice contactNotice1=new ContactNotice();
                contactNotice1.setUid1(contactNotice.getUid2());
                contactNotice1.setUid2(contactNotice.getUid1());
                contactNotice1.setFlag(1);
                contactNoticeMapper.update(contactNotice1);

                contactNotice1.setUid1(contactNotice.getUid1());
                contactNotice1.setUid2(contactNotice.getUid2());
                contactNotice1.setFlag(0);
                contactNotice1.setApplicationId(applicationId);
                contactNoticeMapper.insert(contactNotice1);
            }
            if (flag3&&!flag){ //你删了，对方没删
                log.info("发起方删了");
                //新增你的，更新对方的
                ContactNotice contactNotice1=new ContactNotice();
                contactNotice1.setUid1(contactNotice.getUid1());
                contactNotice1.setUid2(contactNotice.getUid2());
                contactNotice1.setFlag(0);
                contactNoticeMapper.update(contactNotice1);

                contactNotice1.setUid1(contactNotice.getUid2());
                contactNotice1.setUid2(contactNotice.getUid1());
                contactNotice1.setFlag(1);
                contactNotice1.setApplicationId(applicationId);
                contactNoticeMapper.insert(contactNotice1);
            }
            if(flag&&flag3){ //双方都没删
                log.info("都没删");
                //只需两个都更新一下
                ContactNotice contactNotice1=new ContactNotice();
                contactNotice1.setUid1(contactNotice.getUid1());
                contactNotice1.setUid2(contactNotice.getUid2());
                contactNotice1.setFlag(0);
                contactNoticeMapper.update(contactNotice1);

                contactNotice1.setUid1(contactNotice.getUid2());
                contactNotice1.setUid2(contactNotice.getUid1());
                contactNotice1.setFlag(1);
                contactNoticeMapper.update(contactNotice1);
            }
        }else{ //这表示双方都删了，申请不存在，要新增
            log.info("双方都删了"); //这里所涉及的几个sql操作最好用事务机制
            //先新增申请，需要主键返回
            Application application=new Application();
            application.setLeaveMessage(contactNotice.getLeaveMessage());
            application.setTime(Tools.now(customConfig.getZone()));
            applicationMapper.insert(application);

            //新增关联的
            ContactNotice contactNotice1=new ContactNotice();
            contactNotice1.setUid1(contactNotice.getUid1());
            contactNotice1.setUid2(contactNotice.getUid2());
            contactNotice1.setFlag(0);
            contactNotice1.setApplicationId(application.getId());
            contactNoticeMapper.insert(contactNotice1);

            contactNotice1.setUid1(contactNotice.getUid2());
            contactNotice1.setUid2(contactNotice.getUid1());
            contactNotice1.setFlag(1);
            contactNotice1.setApplicationId(application.getId());
            contactNoticeMapper.insert(contactNotice1);

        }

        DataUpdateNotify.contactNoticeNotify(contactNotice);

        return Result.success();
    }

    @Override
    public Result listByUid(String uid) {
        List<ContactNotice> contactNotices = contactNoticeMapper.listByUid(uid);


        return Result.success(contactNotices);
    }


    @Override
    public Result update(ContactNotice contactNotice) {

        if(contactNotice.getStatus().equals("agree")){
            Contact contact=null;
            boolean flag0=false;//这里是在判断同意的人，他的联系人消息里是否有被他同意的人contactNotice.uid1是同意的人
            List<ContactNotice> contactNotices = contactNoticeMapper.listByUid(contactNotice.getUid1());
            for (ContactNotice notice : contactNotices) {
                if(notice.getUid2().equals(contactNotice.getUid2())){
                    flag0=true;
                    //修改application
                    Application application=new Application();
                    application.setId(notice.getApplicationId());
                    application.setLeaveMessage(notice.getLeaveMessage());
                    application.setStatus(contactNotice.getStatus());
                    application.setTime(Tools.now(customConfig.getZone()));
                    applicationMapper.update(application);
                    //contact表新增两条记录
                    LocalDateTime time=Tools.now(customConfig.getZone());
                    //新增第一条
                    contact=new Contact();
                    contact.setUid1(contactNotice.getUid1());
                    contact.setUid2(contactNotice.getUid2());
                    contact.setTime(time);
                    contactMapper.insert(contact);
                    //新增第二条
                    contact=new Contact();
                    contact.setUid1(contactNotice.getUid2());
                    contact.setUid2(contactNotice.getUid1());
                    contact.setTime(time);
                    contactMapper.insert(contact);

                    //生成一个系统消息，在他俩的聊天框里
                    Message message=new Message();
                    message.setType("system");
                    message.setTime(Tools.now(customConfig.getZone()));
                    message.setContent("你们已经成功添加为好友，现在可以开始聊天了！");
                    messageMapper.insert0(message);
                    //第一条
                    UserChatRecord userChatRecord=new UserChatRecord();
                    userChatRecord.setUid1(contactNotice.getUid1());
                    userChatRecord.setUid2(contactNotice.getUid2());
                    userChatRecord.setMessageId(message.getId());
                    userChatRecord.setFlag(-1);
                    userChatRecordMapper.insert0(userChatRecord);
                    //第二条
                    userChatRecord=new UserChatRecord();
                    userChatRecord.setUid1(contactNotice.getUid2());
                    userChatRecord.setUid2(contactNotice.getUid1());
                    userChatRecord.setMessageId(message.getId());
                    userChatRecord.setFlag(-1);
                    userChatRecordMapper.insert0(userChatRecord);
                    //还有聊天消息列表的项也要不存在即创建，为什么会存在，因为可能之前加了好友然后删掉了
                    boolean flag=false;//标识是否存在
                    //对方的列表是否存在我
                    for (MessageNotice messageNotice : messageNoticeMapper.selectByUid(contactNotice.getUid2())) {
                        if(messageNotice.getUid2().equals(contactNotice.getUid1())){
                            flag=true;//存在即更新
                            messageNotice.setMessageId(message.getId());
                            messageNotice.setCount(messageNotice.getCount()+1);
                            messageNoticeMapper.update0(messageNotice);
                        }
                    }
                    if(!flag){//不存在则创建
                        MessageNotice messageNotice = new MessageNotice();
                        messageNotice.setUid1(contactNotice.getUid2());
                        messageNotice.setUid2(contactNotice.getUid1());
                        messageNotice.setMessageId(message.getId());
                        messageNotice.setCount(1);
                        messageNoticeMapper.insert0(messageNotice);
                    }
                    flag=false;
                    //我方的列表是否存在对方
                    for (MessageNotice messageNotice : messageNoticeMapper.selectByUid(contactNotice.getUid1())) {
                        if(messageNotice.getUid2().equals(contactNotice.getUid2())){
                            flag=true;//存在即更新
                            messageNotice.setMessageId(message.getId());
                            messageNotice.setCount(messageNotice.getCount()+1);
                            messageNoticeMapper.update0(messageNotice);
                        }
                    }
                    if(!flag){//不存在则创建
                        MessageNotice messageNotice = new MessageNotice();
                        messageNotice.setUid1(contactNotice.getUid1());
                        messageNotice.setUid2(contactNotice.getUid2());
                        messageNotice.setMessageId(message.getId());
                        messageNotice.setCount(1);
                        messageNoticeMapper.insert0(messageNotice);
                    }

                    break;
                }
            }
            if(flag0){
                //数据更新通知
                DataUpdateNotify.contactNoticeNotify(contactNotice);
                DataUpdateNotify.contactNotify(contact);
                return Result.success();
            }else{
                return Result.error("你的联系人通知列表里没有他");
            }
        }else if (contactNotice.getStatus().equals("refuse")){

            boolean flag=false;//这里是在判断拒绝的人，他的联系人消息里是否有被他拒绝的人contactNotice.uid1是拒绝的人
            List<ContactNotice> contactNotices = contactNoticeMapper.listByUid(contactNotice.getUid1());
            for (ContactNotice notice : contactNotices) {
                if(notice.getUid2().equals(contactNotice.getUid2())){
                    flag=true;
                    //修改application
                    Application application=new Application();
                    application.setId(notice.getApplicationId());
                    application.setLeaveMessage(notice.getLeaveMessage());
                    application.setStatus(contactNotice.getStatus());
                    application.setTime(Tools.now(customConfig.getZone()));
                    applicationMapper.update(application);

                    break;
                }
            }
            if(flag){
                //数据更新通知
                DataUpdateNotify.contactNoticeNotify(contactNotice);
                return Result.success();
            }else{
                return Result.error("你的联系人通知列表里没有他");
            }

        }else {
            return Result.error("invalid status value");
        }
    }

    @Override
    public Result delete(ContactNotice contactNotice) {
        boolean flag=false;//标识发起方的联系人通知列表里是否有对方
        List<ContactNotice> contactNotices = contactNoticeMapper.listByUid(contactNotice.getUid1());
        for (ContactNotice notice : contactNotices) {
            if(notice.getUid2().equals(contactNotice.getUid2())){
                flag=true;
                //执行删除操作
                ContactNotice contactNotice1=new ContactNotice();
                contactNotice1.setUid1(contactNotice.getUid1());
                contactNotice1.setUid2(contactNotice.getUid2());
                contactNoticeMapper.delete(contactNotice1);
                //尝试删除application，如果是因为外键约束而失败，就算了
                try {
                    applicationMapper.delete(notice.getApplicationId());
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                break;
            }
        }
        if(flag){
            //数据更新通知
            DataUpdateNotify.contactNoticeNotify(contactNotice);
            return Result.success();
        }else{
            return Result.error("你的联系人通知列表里没有他");
        }
    }
}
