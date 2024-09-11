package site.liuqq.freedom_chat.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.Contact;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.service.ContactService;
import site.liuqq.freedom_chat.common.Tools;

@RestController
@RequestMapping("/api")
public class ContactController {

    @Autowired
    ContactService contactService;

    //查询用户所拥有的好友的信息的接口
    @GetMapping("/contacts")
    public Result list(){
        String uid=(String) StpUtil.getLoginId();
        return contactService.listByUid(uid);
    }

    //查询用户所拥有的好友的信息的接口 新接口，比list1新增了功能
    @GetMapping("/contacts2")
    public Result list2(String uid2){

        String uid=(String) StpUtil.getLoginId();

        Contact contact = new Contact();
        contact.setUid1(uid);
        contact.setUid2(uid2);

        return contactService.listByUid2(contact);
    }

}
