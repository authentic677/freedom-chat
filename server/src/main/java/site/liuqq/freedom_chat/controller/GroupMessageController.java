package site.liuqq.freedom_chat.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.liuqq.freedom_chat.pojo.Result;

@RestController
@RequestMapping("/api")
public class GroupMessageController {

    @PostMapping("/groupMemberMessage")
    Result insert(){



        return Result.success();
    }

}
