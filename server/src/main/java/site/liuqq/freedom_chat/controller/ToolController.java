package site.liuqq.freedom_chat.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.liuqq.freedom_chat.mapper.UserMapper;
import site.liuqq.freedom_chat.pojo.Result;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.utils.RedisConstants;
import site.liuqq.freedom_chat.utils.Tools;

import static site.liuqq.freedom_chat.utils.RedisConstants.*;

@RestController
@RequestMapping("/api")
public class ToolController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //图片验证码接口
    @GetMapping("/captcha")
    public ResponseEntity<byte[]> captcha(String account){
        try {
            // 创建 Kaptcha 实例
            DefaultKaptcha kaptcha = new DefaultKaptcha();
            // 配置 Kaptcha
            Properties properties = new Properties();
            properties.setProperty("kaptcha.image.width", "200");
            properties.setProperty("kaptcha.image.height", "50");
            kaptcha.setConfig(new Config(properties));
            // 生成验证码
            String captchaText = kaptcha.createText();
            BufferedImage captchaImage = kaptcha.createImage(captchaText);

            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            ImageIO.write(captchaImage,"png",stream);

            byte[] image=stream.toByteArray();

            HttpHeaders headers=new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            //设置redis key
            stringRedisTemplate.opsForValue().set(VERIFY_CODE+account,captchaText,30, TimeUnit.SECONDS);

            Thread.sleep(750);

            //响应验证码图片
            return new ResponseEntity<>(image,headers, HttpStatus.OK);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    //邮箱验证码接口
    @GetMapping("/mailVerifyCode")
    public Result mailVerifyCode(String email){
        //验证邮箱是否合法
        boolean isValid= Tools.validateEmail(email);
        if(!isValid){
            return Result.error("非法的邮箱");
        }
        //验证获取是否频繁，必须大于一定间隔
        if (stringRedisTemplate.opsForValue().get(VERIFY_CODE+email)!=null){
            return Result.error("验证码发送过于频繁");
        }
        //现在可以发送邮件了
        //生成验证码
        String code=Tools.generateRandomNumberString(6);
        //保存到redis
        stringRedisTemplate.opsForValue().set(VERIFY_CODE+email,code,30, TimeUnit.SECONDS);
//        boolean result=true;
        boolean result=Tools.sendMail(email,"【自由聊天】您的邮箱验证码是"+code);

        return result?Result.success():Result.error("未知错误");
    }

    //用于核实token是否有效的接口
    @GetMapping("/checkToken")
    public Result checkToken(@RequestHeader("token") String token){
        User user=Tools.checkJwtToken(token);
        return user!=null?Result.success():Result.error("token无效");
    }

    //检查邮箱是否注册过或用户ID是否存在的接口
    @GetMapping("/checkAccount")
    public Result checkAccount(String account,HttpSession session){

        User user = userMapper.selectByAccount(account);
        if (user==null){
            return Result.error("此账户未注册");
        }
        return Result.success(user.getEmail());
    }

    //检查邮箱验证码是否正确的接口
    @GetMapping("/checkEmailVerifyCode")
    public Result checkEmailVerifyCode(String email,String code){

        String code1 = stringRedisTemplate.opsForValue().get(VERIFY_CODE+email);

        if (code.equals(code1)){
            return Result.success();
        }else {
            return Result.error("验证码错误");
        }
    }
}
