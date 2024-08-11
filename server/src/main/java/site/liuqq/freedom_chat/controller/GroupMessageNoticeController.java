package site.liuqq.freedom_chat.controller;

import jakarta.servlet.http.HttpSession;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.pojo.group.GroupMessageNotice;
import site.liuqq.freedom_chat.service.impl.GroupMessageNoticeServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GroupMessageNoticeController {

    @Autowired
    private GroupMessageNoticeServiceImpl groupMessageNoticeServiceImpl;

    @GetMapping("/groupMessageNotice")
    Result query(HttpSession session){

        String uid = ((User) session.getAttribute("user")).getUid();

        List<GroupMessageNotice> list = groupMessageNoticeServiceImpl
                .lambdaQuery()
                .eq(GroupMessageNotice::getUid, uid)
                .list();

        return Result.success(list);
    }

}
