package xyz677123.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import xyz677123.filter.MainFilter;
import xyz677123.pojo.Post;
import xyz677123.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/zone")
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping("/posts")
    public Result postList() {
        //获取请求所关联的用户
        User user = MainFilter.getThreadLocal().get();

        List<Post> list = postService.lambdaQuery()
                .eq(Post::getUid, user.getUid())
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
