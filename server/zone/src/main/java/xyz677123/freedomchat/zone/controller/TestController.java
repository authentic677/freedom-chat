package xyz677123.freedomchat.zone.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz677123.freedomchat.zone.service.PostService;

@RestController
@RequestMapping("/test")
@SaCheckLogin
public class TestController {

    @Autowired
    private PostService postService;
    @Autowired
    private MinioClient minioClient;

    @PostMapping("/post")
    @Transactional
    public String test(@RequestParam(required = false) String[] n,@RequestParam String text,@RequestParam(required = false) MultipartFile[] attachments) throws Exception {



        return "ok";
    }

    @GetMapping("/t")
    public String t(){

        return "ok2";
    }
}
