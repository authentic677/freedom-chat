package xyz677123.freedomchat.content_platform.controller;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz677123.freedomchat.content_platform.pojo.Video;
import xyz677123.freedomchat.content_platform.service.VideoService;

import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    VideoService videoService;

    @PostMapping("/test")
    public String test(@RequestBody List<Map<String, String>> list) {

        list.forEach(e->{
            boolean update = videoService.lambdaUpdate()
                    .eq(Video::getId, e.get("id"))
                    .set(Video::getVideo, "/content-platform/"+e.get("name"))
                    .update();
        });

        return "test";
    }
}
