package site.liuqq.freedom_chat.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.liuqq.freedom_chat.common.Result;
import site.liuqq.freedom_chat.service.ExpressionService;

@RestController
@RequestMapping("/api")
public class ExpressionController {
    @Autowired
    ExpressionService expressionService;

    @GetMapping("/expression")
    Result query(int expressionListId){

        return expressionService.query(expressionListId);
    }
}
