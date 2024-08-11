package site.liuqq.freedom_chat.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.*;
import site.liuqq.freedom_chat.service.MessageNoticeService;
import site.liuqq.freedom_chat.service.impl.MessageNoticeServiceImpl;
import site.liuqq.freedom_chat.service.impl.MessageServiceImpl;
import site.liuqq.freedom_chat.service.impl.UserChatRecordServiceImpl;
import site.liuqq.freedom_chat.utils.Tools;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.uid;

@Slf4j
@RestController
@RequestMapping("/api")
public class MessageNoticeController {

    @Autowired
    private MessageNoticeService messageNoticeService;
    @Autowired
    private MessageNoticeServiceImpl messageNoticeServiceImpl;
    @Autowired
    private MessageServiceImpl messageServiceImpl;
    @Autowired
    private UserChatRecordServiceImpl userChatRecordServiceImpl;

    //某用户在它的消息通知列表中添加另一个用户
    //增
    @PostMapping("/messageNotice/{uid2}")
    Result insert(@PathVariable String uid2,HttpSession session){

        String uid1 = ((User) session.getAttribute("user")).getUid();

        //不存在则新增，否则不做任何事
        //1.判断是否存在
        MessageNotice one = messageNoticeServiceImpl
                .lambdaQuery()
                .eq(MessageNotice::getUid1, uid1)
                .eq(MessageNotice::getUid2, uid2)
                .one();
        if(one != null){
            return Result.success();
        }
        //2.不存在，则新增
        MessageNotice messageNotice = new MessageNotice();
        messageNotice.setUid1(uid1);
        messageNotice.setUid2(uid2);
        //设置关联的message
        {
            //获取他们之间最新一条的消息
            List<UserChatRecord> list = userChatRecordServiceImpl
                    .lambdaQuery()
                    .eq(UserChatRecord::getUid1, uid1)
                    .eq(UserChatRecord::getUid2, uid2)
                    .list();
            if(!list.isEmpty()){
                //处理外键
                list.forEach(e->{
                    Message message = messageServiceImpl
                            .lambdaQuery()
                            .eq(Message::getId, e.getMessageId())
                            .one();
                    e.setMessage(message);
                });
                //取最值
                LocalDateTime max=list.getFirst().getMessage().getTime();
                int index=0;
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getMessage().getTime().isAfter(max)){
                        max=list.get(i).getMessage().getTime();
                        index=i;
                    }
                }
                //设置值
                messageNotice.setMessageId(list.get(index).getMessageId());
            }
        }
        messageNoticeServiceImpl.save(messageNotice);

        return Result.success();
    }

    //查
    @GetMapping("/messageNotices")
    Result selectByUid(@RequestHeader String token,String targetUid){
        User user = Tools.checkJwtToken(token);
        String uid=user.getUid();

        return messageNoticeService.selectByUid(uid,targetUid);
    }

    //改
    @PutMapping("/messageNotice")
    Result clearCount(@RequestBody MessageNotice messageNotice,@RequestHeader String token){
        User user = Tools.checkJwtToken(token);
        String uid=user.getUid();

        messageNotice.setUid1(uid);

        return messageNoticeService.clearCount(messageNotice);
    }

    //删
    @DeleteMapping("/messageNotice")
    Result delete(@RequestBody MessageNotice messageNotice,@RequestHeader String token){

        User user = Tools.checkJwtToken(token);
        String uid=user.getUid();

        messageNotice.setUid1(uid);

        return messageNoticeService.delete(messageNotice);
    }
}
