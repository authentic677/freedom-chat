package xyz677123.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.common.Result;
import xyz677123.pojo.Post;
import xyz677123.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping("/post")
    public Result postList(@PathVariable String uid) {

        List<Post> list = postService.lambdaQuery()
                .eq(Post::getUid, uid)
                .eq(Post::getParent,-1)
                .list();

        return Result.success(list);
    }

    @PostMapping("/post")
    public Result post(@RequestBody Post post) {

        postService.save(post);

        return Result.success();
    }
}
