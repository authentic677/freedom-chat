package xyz677123.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.liuqq.freedom_chat.common.Tools;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import xyz677123.common.Utils;
import xyz677123.filter.MainFilter;
import xyz677123.pojo.Like;
import xyz677123.pojo.Post;
import xyz677123.service.LikeService;
import xyz677123.service.PostService;

import java.time.LocalDateTime;
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

    @GetMapping("/comments/{postId}")
    public Result comment(@PathVariable Integer postId) {
        List<Post> list = postService.lambdaQuery()
                .eq(Post::getParent, postId)
                .list();

        return Result.success(list);
    }

    @PostMapping("/post")
    @Transactional
    public Result post(@RequestParam String text,@RequestParam(required = false) MultipartFile[] attachments) throws Exception {

        String uid=(String) StpUtil.getLoginId();

        //保存附件
        List<String> attachmentUrls = new ArrayList<>();
        if (attachments!=null){
            for (MultipartFile attachment : attachments) {
                String objectName = Utils.getCurrentYearMonth()+"/"+attachment.getContentType()+"/"+ UUID.randomUUID()+"/" +attachment.getOriginalFilename();
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
}
