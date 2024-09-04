package site.liuqq.freedom_chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import site.liuqq.freedom_chat.conf.CustomConfig;
import site.liuqq.freedom_chat.utils.Tools;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.time.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class PlainTest {

    public static void main(String[] args) throws Exception {


        Resend resend = new Resend("re_Q4viMZtB_JUdFVK2tcvbqMTP4pUMEtc8p");


        CreateEmailOptions build = CreateEmailOptions.builder()
                .from("freedom-chat <admin@liuqq.site>")
                .to("945491917@qq.com")
                .subject("自由聊天")
                .html("<p>您的邮箱验证码是：<strong>"+123+"</strong></p>")
                .build();

        CreateEmailResponse send = resend.emails().send(build);

        System.out.println(send.getId());


    }
}
