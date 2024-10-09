package xyz677123.freedomchat.content_platform.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.common.pojo.User;
import xyz677123.freedomchat.common.util.Tools;
import xyz677123.freedomchat.content_platform.client.UserClient;
import xyz677123.freedomchat.content_platform.pojo.Channel;
import xyz677123.freedomchat.content_platform.pojo.Gongzhonghao;
import xyz677123.freedomchat.content_platform.pojo.Video;
import xyz677123.freedomchat.content_platform.service.ChannelService;
import xyz677123.freedomchat.content_platform.service.VideoService;

import java.util.UUID;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@RestController
public class ChannelController {

    @Autowired
    ChannelService channelService;
    @Autowired
    UserClient userClient;
    @Autowired
    MinioClient minioClient;
    @Autowired
    private VideoService videoService;

    @GetMapping("/channel")
    public Result getChannel() {

        String uid= (String) StpUtil.getLoginId();

        //一个用户拥有一个频道，如果没有则创建
        Channel channel = channelService.lambdaQuery()
                .eq(Channel::getUid, uid)
                .one();
        if (channel == null) {
            //feign请求
            Result result = userClient.getUser(uid);
            ObjectMapper objectMapper = new ObjectMapper();
            Object data = result.getData();
            User user = objectMapper.convertValue(data, User.class);

            channel = Channel.builder()
                    .uid(uid)
                    .name(user.getUsername())
                    .avatar(user.getAvatar())
                    .description("暂无描述")
                    .build();
            channelService.save(channel);
        }

        //统计数字
        //频道视频数
        Long count = videoService.lambdaQuery()
                .eq(Video::getChannelId, channel.getId())
                .count();
        channel.setVideoCount(count);
        //频道视频获赞数
        channel.setLikeCount(0L);
        //频道粉丝数
        channel.setFanCount(0L);
        //频道所关注的人数
        channel.setFollowCount(0L);

        return Result.success(channel);
    }

    @PostMapping("/channel")
    public Result addChannel(@RequestBody Channel channel) {

        String uid= (String) StpUtil.getLoginId();

        return Result.success();
    }

    @PutMapping("/channel")
    @Transactional
    public Result updateChannel(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile avatar,
            @RequestParam(required = false) MultipartFile banner
    ) throws Exception {

        String uid= (String) StpUtil.getLoginId();

        LambdaUpdateWrapper<Channel> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Channel::getName,name);
        wrapper.set(Channel::getDescription,description);
        //更新二进制对象
        Channel one = channelService.lambdaQuery().eq(Channel::getUid, uid).one();
        if (banner!=null){
            //删掉重新上传
            if(one.getBanner()!=null){ //注意横幅有可能是空的
                minioClient.removeObject(RemoveObjectArgs.builder()
                        .bucket("content-platform")
                        .object(one.getBanner().replace("/content-platform/",""))
                        .build());
            }
            //重新上传
            String objectName = Tools.getCurrentYearMonth()+"/"+banner.getContentType().replace("/","-")+"/"+ UUID.randomUUID()+"/" +banner.getOriginalFilename();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket("content-platform")
                    .object(objectName)
                    .stream(banner.getInputStream(),banner.getSize(),-1)
                    .build()
            );
            wrapper.set(Channel::getBanner,"/content-platform/" + objectName);
        }
        if (avatar!=null){
            //删掉重新上传
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket("content-platform")
                    .object(one.getAvatar().replace("/content-platform/",""))
                    .build());
            //重新上传
            String objectName = Tools.getCurrentYearMonth()+"/"+avatar.getContentType().replace("/","-")+"/"+ UUID.randomUUID()+"/" +avatar.getOriginalFilename();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket("content-platform")
                    .object(objectName)
                    .stream(avatar.getInputStream(),avatar.getSize(),-1)
                    .build()
            );
            wrapper.set(Channel::getAvatar,"/content-platform/" + objectName);
        }

        channelService.update(wrapper);

        return Result.success();
    }

    @DeleteMapping("/channel/{id}")
    public Result deleteChannel(@PathVariable("id") Integer id) {

        String uid= (String) StpUtil.getLoginId();

        return Result.success();
    }
}
