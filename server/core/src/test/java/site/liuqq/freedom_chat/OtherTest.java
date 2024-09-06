package site.liuqq.freedom_chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


import site.liuqq.freedom_chat.conf.CustomConfig;
import site.liuqq.freedom_chat.service.impl.GroupApplicantServiceImpl;
import site.liuqq.freedom_chat.service.impl.GroupMemberServiceImpl;
import site.liuqq.freedom_chat.service.impl.MessageServiceImpl;
import site.liuqq.freedom_chat.service.impl.UserChatRecordServiceImpl;

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
    @Autowired
    private CustomConfig customConfig;

    @Value("${site.liuqq.freedomchat.zone}")
    String zone;

    @Test
    public void test(){

        String emailDomain = customConfig.getEmailDomain();
        System.out.println(emailDomain);

        String resendApiKey = customConfig.getResendApiKey();
        System.out.println(resendApiKey);

        System.out.println(customConfig.getZone());

        System.out.println(zone);

    }
}
