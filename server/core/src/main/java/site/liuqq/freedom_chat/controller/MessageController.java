package site.liuqq.freedom_chat.controller;

import cn.dev33.satoken.stp.StpUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.pojo.UserChatRecord;
import site.liuqq.freedom_chat.service.UserChatRecordService;
import site.liuqq.freedom_chat.common.Tools;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private UserChatRecordService userChatRecordService;
    @Autowired
    private MinioClient minioClient;

    @PostMapping("/message/{peerUid}")
    public Result sendMessage(@PathVariable String peerUid, HttpSession session, HttpServletRequest request) throws Exception {
        //获取我方uid
        String uid=(String) StpUtil.getLoginId();

        //消息内容输入流
        InputStream inputStream=request.getInputStream();

        //获取消息元数据
        String type = request.getHeader("x-amz-meta-type");
        String name = request.getHeader("x-amz-meta-name");
        String size = request.getHeader("x-amz-meta-size");

        if ("img".equals(type)){
            String objectName=Tools.getCurrentYearMonth()+"/img/"+ UUID.randomUUID()+"/"+name;

            //自定义自己的元数据，必须以x-amz-meta前缀开头
            Map<String,String> headers=new HashMap<>();
            headers.put("x-amz-meta-name",name);
            headers.put("x-amz-meta-size",size);
            //上传对象
            minioClient.putObject(PutObjectArgs
                    .builder()
                            .bucket("chat")
                            .object(objectName)
                            .stream(inputStream,request.getContentLength(),-1)
                            .headers(headers)
                    .build()
            );

            //用户聊天记录表插入两条数据
            UserChatRecord userChatRecord = new UserChatRecord();
            userChatRecord.setUid2(uid);
            userChatRecord.setUid1(peerUid);
            userChatRecord.setType("img");
            userChatRecord.setContent("chat/"+objectName);
            userChatRecordService.insert2(userChatRecord);

            return Result.success();
        }else {
            return Result.error("invalid message type");
        }

    }

}
