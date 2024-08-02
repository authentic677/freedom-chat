package site.liuqq.freedom_chat.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.Group;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.service.GroupService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GroupController {
    @Autowired
    GroupService groupService;

    @GetMapping("/groups")
    Result searchGroups(String keyword, HttpSession session){
        return groupService.searchGroups(keyword);
    }
}
