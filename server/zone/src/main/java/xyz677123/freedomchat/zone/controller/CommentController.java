package xyz677123.freedomchat.zone.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.common.pojo.User;
import xyz677123.freedomchat.zone.client.UserClient;
import xyz677123.freedomchat.zone.pojo.Comment;
import xyz677123.freedomchat.zone.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    CommentService commentService;
    @Autowired
    private UserClient userClient;

    @GetMapping("/comments")
    public Result getComments() {

        String uid=(String) StpUtil.getLoginId();

        List<Comment> list = commentService.lambdaQuery()
                .eq(Comment::getUid1, uid)
                .list();
        ObjectMapper objectMapper = new ObjectMapper();
        //处理外键
        list.forEach(e->{
            //feign请求
            Result result = userClient.getUser(e.getUid2());

            Object data = result.getData(); //这里实际上是一个Map类型
            User user = objectMapper.convertValue(data, User.class);
            e.setUser2(user);
        });

        return Result.success(list);
    }

    @PostMapping("/comment")
    public Result addComment(@RequestBody Comment comment) {

        String uid=(String) StpUtil.getLoginId();

        comment.setUid2(uid);
        comment.setCreatedAt(LocalDateTime.now());

        commentService.save(comment);

        return Result.success();
    }

    @DeleteMapping("/comment/{id}")
    public Result deleteComment(@PathVariable Integer id) {

        String uid=(String) StpUtil.getLoginId();

        commentService.removeById(id);

        return Result.success();
    }
}
