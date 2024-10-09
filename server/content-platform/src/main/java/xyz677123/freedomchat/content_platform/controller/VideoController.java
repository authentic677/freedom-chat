package xyz677123.freedomchat.content_platform.controller;

import cn.dev33.satoken.stp.StpUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.common.util.Tools;
import xyz677123.freedomchat.content_platform.mapper.VideoMapper;
import xyz677123.freedomchat.content_platform.pojo.Channel;
import xyz677123.freedomchat.content_platform.pojo.Video;
import xyz677123.freedomchat.content_platform.service.ChannelService;
import xyz677123.freedomchat.content_platform.service.VideoService;

import java.util.List;
import java.util.UUID;

@RestController
public class VideoController {

    @Autowired
    VideoService videoService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private MinioClient minioClient;

    @GetMapping("/videos")
    public Result video() {

        String uid= (String) StpUtil.getLoginId();

        Channel channel = channelService.lambdaQuery()
                .eq(Channel::getUid, uid)
                .one();

        List<Video> videos = videoService.lambdaQuery()
                .eq(Video::getChannelId, channel.getId())
                .list();

        return Result.success(videos);
    }

    @GetMapping("/video")
    public Result video(@RequestParam String mode) {

        if (mode.equals("rand")){
            Video video = videoMapper.randomRetrieve();
            //处理外键
            Channel channel = channelService.getById(video.getChannelId());
            video.setChannel(channel);
            return Result.success(video);
        }

        return Result.success();
    }

    @PutMapping("/video")
    public Result updateVideo() {

        String uid= (String) StpUtil.getLoginId();

        return Result.success();
    }

    @PostMapping("/video")
    @Transactional
    public Result addVideo(
            @RequestParam String title,
            @RequestParam String summary,
            @RequestParam String visibility,
            @RequestParam MultipartFile video,
            @RequestParam MultipartFile cover
    ) throws Exception{

        String uid= (String) StpUtil.getLoginId();

        //获取频道信息
        Channel one = channelService.lambdaQuery()
                .eq(Channel::getUid, uid)
                .one();

        //保存二进制文件到minio
        String objectName1 = Tools.getCurrentYearMonth()+"/"+video.getContentType().replace("/","-")+"/"+ UUID.randomUUID()+"/" +video.getOriginalFilename();
        minioClient.putObject(PutObjectArgs.builder()
                        .bucket("content-platform")
                        .object(objectName1)
                        .stream(video.getInputStream(),video.getSize(),-1)
                        .build());

        String objectName2 = Tools.getCurrentYearMonth()+"/"+cover.getContentType().replace("/","-")+"/"+ UUID.randomUUID()+"/" +cover.getOriginalFilename();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket("content-platform")
                .object(objectName2)
                .stream(cover.getInputStream(),cover.getSize(),-1)
                .build());

        Video video1 = Video.builder()
                .title(title)
                .summary(summary)
                .visibility(visibility)
                .video("/content-platform/" + objectName1)
                .cover("/content-platform/" + objectName2)
                .channelId(one.getId())
                .build();

        videoService.save(video1);


        return Result.success();
    }

    @DeleteMapping("/video/{id}")
    public Result deleteVideo(@PathVariable Integer id) {

        String uid= (String) StpUtil.getLoginId();

        videoService.removeById(id);

        return Result.success();
    }
}
