package site.liuqq.freedom_chat.controller;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.ContactNotice;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.service.ContactNoticeService;
import site.liuqq.freedom_chat.utils.Tools;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class ContactNoticeController {
    @Autowired
    ContactNoticeService contactNoticeService;

    //添加好友的接口
    @PostMapping("/contactNotice")
    Result insert(@RequestBody ContactNotice contactNotice, @RequestHeader String token){
        User user= Tools.checkJwtToken(token);
        //设置发起方的uid
        contactNotice.setUid2(user.getUid());

        contactNotice.setTime(LocalDateTime.now());
        System.out.println(contactNotice);
        //service层
        return contactNoticeService.insert(contactNotice);

    }

    //查询的接口
    @GetMapping("/contactNotices")
    Result selectAll(@RequestHeader String token){
        User user = Tools.checkJwtToken(token);
        String uid=user.getUid();

        return contactNoticeService.listByUid(uid);
    }

    //同意或拒绝的接口
    @PutMapping("/contactNotice")
    Result update(@RequestBody ContactNotice contactNotice,@RequestHeader String token){
        User user = Tools.checkJwtToken(token);
        contactNotice.setUid1(user.getUid());
        return contactNoticeService.update(contactNotice);
    }

    @DeleteMapping("/contactNotice")
    Result delete(@RequestBody ContactNotice contactNotice,@RequestHeader String token){
        User user = Tools.checkJwtToken(token);
        contactNotice.setUid1(user.getUid());


        return contactNoticeService.delete(contactNotice);
    }
}
