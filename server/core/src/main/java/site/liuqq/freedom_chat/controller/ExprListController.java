package site.liuqq.freedom_chat.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz677123.freedomchat.common.pojo.Result;
import site.liuqq.freedom_chat.service.ExprListService;

@RestController
@RequestMapping("/api")
public class ExprListController {
    @Autowired
    ExprListService exprListService;

    @GetMapping("/exprList")
    Result query(@RequestHeader String token){

        String uid=(String) StpUtil.getLoginId();

        return exprListService.queryByUid(uid);
    }
}
