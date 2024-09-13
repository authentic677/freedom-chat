package xyz677123.freedomchat.zone.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.common.util.Tools;
import xyz677123.freedomchat.zone.pojo.Like;
import xyz677123.freedomchat.zone.pojo.Post;
import xyz677123.freedomchat.zone.service.LikeService;
import xyz677123.freedomchat.zone.service.PostService;

import java.util.*;

@RestController
public class PostController {

    @Autowired
    PostService postService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private MinioClient minioClient;

    //获取用户所发布的说说
    @GetMapping("/posts")
    public Result postList() {
        //获取请求所关联的用户
        String uid=(String) StpUtil.getLoginId();

        List<Post> list = postService.lambdaQuery()
                .eq(Post::getUid, uid)
                .eq(Post::getParent,-1)
                .orderByDesc(Post::getTime)
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

    //单个说说
    @GetMapping("/post/{postId}")
    public Result post(@PathVariable Integer postId) {
        Post post = postService.lambdaQuery()
                .eq(Post::getId, postId)
                .one();

        //获取评论数
        List<Post> comment = postService.lambdaQuery()
                .eq(Post::getParent, post.getId())
                .list();
        post.setCommentCount(comment.size());
        //获取点赞数

        List<Like> like = likeService.lambdaQuery()
                .eq(Like::getPostId, post.getId())
                .list();
        post.setLikeCount(like.size());

        return Result.success(post);
    }

    //用户发布说说
    @PostMapping("/post")
    @Transactional
    public Result post(@RequestParam String text,@RequestParam(required = false) MultipartFile[] attachments) throws Exception {

        String uid=(String) StpUtil.getLoginId();

        //保存附件
        List<String> attachmentUrls = new ArrayList<>();
        if (attachments!=null){
            for (MultipartFile attachment : attachments) {
                String objectName = Tools.getCurrentYearMonth()+"/"+attachment.getContentType()+"/"+ UUID.randomUUID()+"/" +attachment.getOriginalFilename();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket("post")
                        .object(objectName)
                        .stream(attachment.getInputStream(),attachment.getSize(),-1)
                        .build()
                );
                attachmentUrls.add("post/"+objectName);
            }
        }

        Map<String,Object> map = new HashMap<>();
        map.put("text",text);
        map.put("attachments",attachmentUrls);
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(map);

        Post post = Post.builder()
                .uid(uid)
                .time(Tools.now("Asia/Shanghai"))
                .content(content)
                .view(0L)
                .parent(-1)
                .build();

        postService.save(post);


        return Result.success();
    }

    //获取说说的直接评论
    @GetMapping("/commentPost/{id}")
    public Result comment(@PathVariable Integer id){

        List<Post> commentPost = postService.lambdaQuery()
                .eq(Post::getParent, id)
                .list();

        //评论和点赞数
        commentPost.forEach(e->{
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


        return Result.success(commentPost);
    }

    //用户发布说说评论
    @PostMapping("/commentPost")
    @Transactional
    public Result commentPost(
            @RequestParam Integer parent,
            @RequestParam String text,
            @RequestParam(required = false) MultipartFile[] attachments
    ) throws Exception {

        String uid=(String) StpUtil.getLoginId();

        //保存附件
        List<String> attachmentUrls = new ArrayList<>();
        if (attachments!=null){
            for (MultipartFile attachment : attachments) {
                String objectName = Tools.getCurrentYearMonth()+"/"+attachment.getContentType()+"/"+ UUID.randomUUID()+"/" +attachment.getOriginalFilename();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket("post")
                        .object(objectName)
                        .stream(attachment.getInputStream(),attachment.getSize(),-1)
                        .build()
                );
                attachmentUrls.add("post/"+objectName);
            }
        }

        Map<String,Object> map = new HashMap<>();
        map.put("text",text);
        map.put("attachments",attachmentUrls);
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(map);

        Post post = Post.builder()
                .uid(uid)
                .time(Tools.now("Asia/Shanghai"))
                .content(content)
                .view(0L)
                .parent(parent)
                .build();

        postService.save(post);

        return Result.success();
    }
}
