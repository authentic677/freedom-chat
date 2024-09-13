package site.liuqq.freedom_chat.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.ContactNotice;
import xyz677123.freedomchat.common.pojo.Result;
import site.liuqq.freedom_chat.service.ContactNoticeService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class ContactNoticeController {
    @Autowired
    ContactNoticeService contactNoticeService;

    //添加好友的接口
    @PostMapping("/contactNotice")
    Result insert(@RequestBody ContactNotice contactNotice, @RequestHeader String token){

        //设置发起方的uid
        String uid=(String) StpUtil.getLoginId();
        contactNotice.setUid2(uid);

        contactNotice.setTime(LocalDateTime.now());
        System.out.println(contactNotice);
        //service层
        return contactNoticeService.insert(contactNotice);

    }

    //查询的接口
    @GetMapping("/contactNotices")
    Result selectAll(){

        String uid=(String) StpUtil.getLoginId();

        return contactNoticeService.listByUid(uid);
    }

    //同意或拒绝的接口
    @PutMapping("/contactNotice")
    Result update(@RequestBody ContactNotice contactNotice){

        String uid=(String) StpUtil.getLoginId();
        contactNotice.setUid1(uid);
        return contactNoticeService.update(contactNotice);
    }

    @DeleteMapping("/contactNotice")
    Result delete(@RequestBody ContactNotice contactNotice){

        String uid=(String) StpUtil.getLoginId();
        contactNotice.setUid1(uid);


        return contactNoticeService.delete(contactNotice);
    }
}
