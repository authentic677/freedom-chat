package site.liuqq.freedom_chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.service.ExprListService;
import site.liuqq.freedom_chat.utils.Tools;

@RestController
@RequestMapping("/api")
public class ExprListController {
    @Autowired
    ExprListService exprListService;

    @GetMapping("/exprList")
    Result query(@RequestHeader String token){
        User user = Tools.checkJwtToken(token);


        return exprListService.queryByUid(user.getUid());
    }
}
