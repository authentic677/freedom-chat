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
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.pojo.group.GroupApplicant;
import site.liuqq.freedom_chat.pojo.group.GroupMember;
import site.liuqq.freedom_chat.service.impl.GroupApplicantServiceImpl;
import site.liuqq.freedom_chat.service.impl.GroupMemberServiceImpl;
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

    @Test
    public void test(){

        List<GroupApplicant> list = groupApplicantServiceImpl
                .lambdaQuery()
                .eq(GroupApplicant::getApplicantUid, "5465763738")
                .list();
        System.out.println(list);
    }
}
