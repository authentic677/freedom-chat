package site.liuqq.freedom_chat.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import xyz677123.freedomchat.common.pojo.Result;
import site.liuqq.freedom_chat.pojo.UserChatRecord;
import site.liuqq.freedom_chat.service.UserChatRecordService;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/minio")
public class MinioController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    MinioClient minioClient;
    @Autowired
    UserChatRecordService userChatRecordService;

    @PostMapping("/upload/{uuid}")
    public Result upload(@PathVariable String uuid, HttpServletRequest request) {

        String uid= (String) StpUtil.getLoginId();

        try {
            InputStream inputStream = request.getInputStream();
            //拿uuid查询redis
            String s = stringRedisTemplate.opsForValue().get(uuid);
            if (s == null) {
                return Result.error("invalid uuid");
            }
            //将json数据反序列化
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> map = objectMapper.readValue(s, Map.class);
            //自定义自己的元数据，必须以x-amz-meta前缀开头
            Map<String,String> headers=new HashMap<>();
            headers.put("x-amz-meta-name",(String) map.get("name"));
            headers.put("x-amz-meta-size", Integer.toString((Integer) map.get("size")));

            //上传
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket((String) map.get("bucketName"))
                            .object((String) map.get("objectName"))
                            .stream(inputStream, request.getContentLength(), -1)
                            .headers(headers)
                            .build()
            );
            //添加消息记录
            UserChatRecord userChatRecord=new UserChatRecord();
            userChatRecord.setUid2(uid);
            userChatRecord.setUid1((String) map.get("peerUid"));
            userChatRecord.setType("file");
            userChatRecord.setContent(map.get("bucketName")+"/"+map.get("objectName"));
            Result insert = userChatRecordService.insert2(userChatRecord);

            //删除redis的键
            stringRedisTemplate.delete(uuid);

        }catch (Exception e){
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }

        return Result.success();
    }

}
