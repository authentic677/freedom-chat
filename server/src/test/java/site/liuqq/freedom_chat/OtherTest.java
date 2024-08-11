package site.liuqq.freedom_chat;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.socket.WebSocketSession;


import site.liuqq.freedom_chat.conf.CustomConfig;
import site.liuqq.freedom_chat.pojo.Message;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.pojo.UserChatRecord;
import site.liuqq.freedom_chat.pojo.group.GroupApplicant;
import site.liuqq.freedom_chat.pojo.group.GroupMember;
import site.liuqq.freedom_chat.service.impl.GroupApplicantServiceImpl;
import site.liuqq.freedom_chat.service.impl.GroupMemberServiceImpl;
import site.liuqq.freedom_chat.service.impl.MessageServiceImpl;
import site.liuqq.freedom_chat.service.impl.UserChatRecordServiceImpl;
import site.liuqq.freedom_chat.utils.Tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
public class OtherTest {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    private GroupMemberServiceImpl groupMemberServiceImpl;
    @Autowired
    private GroupApplicantServiceImpl groupApplicantServiceImpl;
    @Autowired
    private UserChatRecordServiceImpl userChatRecordServiceImpl;
    @Autowired
    private MessageServiceImpl messageServiceImpl;

    @Test
    public void test(){


        //获取他们之间最新一条的消息
        List<UserChatRecord> list = userChatRecordServiceImpl
                .lambdaQuery()
                .eq(UserChatRecord::getUid1, "9627223950")
                .eq(UserChatRecord::getUid2, "5465763738")
                .list();
        //处理外键
        list.forEach(e->{
            Message message = messageServiceImpl
                    .lambdaQuery()
                    .eq(Message::getId, e.getMessageId())
                    .one();
            e.setMessage(message);
        });
        //取最值
        LocalDateTime max=list.getFirst().getMessage().getTime();
        int index=0;
        for(int i=0;i<list.size();i++){

            if(list.get(i).getMessage().getTime().isAfter(max)){
                max=list.get(i).getMessage().getTime();
                index=i;
            }
        }
        System.out.println(list.get(index));

    }
}
