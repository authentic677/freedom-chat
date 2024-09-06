package site.liuqq.freedom_chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.UserChatRecord;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.service.UserChatRecordService;
import site.liuqq.freedom_chat.common.Tools;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class UserChatRecordController {
    @Autowired
    UserChatRecordService userChatRecordService;

    @PostMapping("/userChatRecord")
    Result insert(@RequestBody UserChatRecord userChatRecord, @RequestHeader String token){
        User user= Tools.checkJwtToken(token);
        //设置发起方的uid
        userChatRecord.setUid2(user.getUid());
        userChatRecord.setTime(LocalDateTime.now());
        //uid1是前端传过来的，是接收者
        return userChatRecordService.insert(userChatRecord);
    }

    @GetMapping("/userChatRecords")
    Result query(String targetUid,@RequestHeader String token){
        User user= Tools.checkJwtToken(token);

        UserChatRecord userChatRecord =new UserChatRecord();
        userChatRecord.setUid1(targetUid);
        //设置发起方的uid
        userChatRecord.setUid2(user.getUid());

        return userChatRecordService.query(userChatRecord);
    }
}
