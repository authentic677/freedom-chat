package site.liuqq.freedom_chat.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.UserChatRecord;
import xyz677123.freedomchat.common.pojo.Result;
import site.liuqq.freedom_chat.service.UserChatRecordService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class UserChatRecordController {
    @Autowired
    UserChatRecordService userChatRecordService;

    @PostMapping("/userChatRecord")
    Result insert(@RequestBody UserChatRecord userChatRecord){
        String uid= (String) StpUtil.getLoginId();
        //设置发起方的uid
        userChatRecord.setUid2(uid);
        userChatRecord.setTime(LocalDateTime.now());
        //uid1是前端传过来的，是接收者
        return userChatRecordService.insert(userChatRecord);
    }

    @GetMapping("/userChatRecords")
    Result query(String targetUid){
        String uid= (String) StpUtil.getLoginId();

        UserChatRecord userChatRecord =new UserChatRecord();
        userChatRecord.setUid1(targetUid);
        //设置发起方的uid
        userChatRecord.setUid2(uid);

        return userChatRecordService.query(userChatRecord);
    }
}
