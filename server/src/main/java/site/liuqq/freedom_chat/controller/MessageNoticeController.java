package site.liuqq.freedom_chat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.MessageNotice;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.service.MessageNoticeService;
import site.liuqq.freedom_chat.utils.Tools;

@Slf4j
@RestController
@RequestMapping("/api")
public class MessageNoticeController {

    @Autowired
    private MessageNoticeService messageNoticeService;

    @GetMapping("/messageNotices")
    Result selectByUid(@RequestHeader String token,String targetUid){
        User user = Tools.checkJwtToken(token);
        String uid=user.getUid();

        return messageNoticeService.selectByUid(uid,targetUid);
    }

    @PutMapping("/messageNotice")
    Result clearCount(@RequestBody MessageNotice messageNotice,@RequestHeader String token){
        User user = Tools.checkJwtToken(token);
        String uid=user.getUid();

        messageNotice.setUid1(uid);

        return messageNoticeService.clearCount(messageNotice);
    }

    @DeleteMapping("/messageNotice")
    Result delete(@RequestBody MessageNotice messageNotice,@RequestHeader String token){

        User user = Tools.checkJwtToken(token);
        String uid=user.getUid();

        messageNotice.setUid1(uid);

        return messageNoticeService.delete(messageNotice);
    }
}
