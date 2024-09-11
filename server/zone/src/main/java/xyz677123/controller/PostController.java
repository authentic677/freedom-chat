package xyz677123.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import xyz677123.filter.MainFilter;
import xyz677123.pojo.Like;
import xyz677123.pojo.Post;
import xyz677123.service.LikeService;
import xyz677123.service.PostService;

import java.util.List;

@RestController
public class PostController {

    @Autowired
    PostService postService;
    @Autowired
    private LikeService likeService;

    //获取用户所发布的说说
    @GetMapping("/posts")
    public Result postList() {
        //获取请求所关联的用户
        String uid=(String) StpUtil.getLoginId();

        List<Post> list = postService.lambdaQuery()
                .eq(Post::getUid, uid)
                .eq(Post::getParent,-1)
                .list();

        list.forEach(e->{
            //获取评论数
            List<Post> comment = postService.lambdaQuery()
                    .eq(Post::getParent, e.getId())
                    .list();
            e.setCommentCount(comment.size());
            //获取点赞数

            List<Like> like = likeService.lambdaQuery()
                    .eq(Like::getPostId, e.getId())
                    .list();
            e.setLikeCount(like.size());
        });

        return Result.success(list);
    }

    @GetMapping("/comments/{postId}")
    public Result comment(@PathVariable Integer postId) {
        List<Post> list = postService.lambdaQuery()
                .eq(Post::getParent, postId)
                .list();

        return Result.success(list);
    }

    @PostMapping("/post")
    public Result post(@RequestBody Post post) {

        postService.save(post);

        return Result.success();
    }
}
