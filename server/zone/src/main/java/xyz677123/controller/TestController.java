package xyz677123.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz677123.pojo.Post;
import xyz677123.service.PostService;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private PostService postService;
    @Autowired
    private MinioClient minioClient;

    @PostMapping("/post")
    @Transactional
    public String test(@RequestParam(required = false) String[] n,@RequestParam String text,@RequestParam(required = false) MultipartFile[] attachments) throws Exception {



        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Post post = objectMapper.readValue(text, Post.class);

        postService.save(post);

        if (attachments!=null){
            int i=0;
            for (MultipartFile attachment : attachments) {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket("post")
                        .object(n[i])
                        .stream(attachment.getInputStream(),attachment.getSize(),-1)
                        .build()
                );
                i++;
            }
        }


        return "ok";
    }
}
