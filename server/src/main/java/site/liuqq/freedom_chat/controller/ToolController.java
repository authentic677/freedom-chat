package site.liuqq.freedom_chat.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.utils.Tools;

@RestController
@RequestMapping("/api")
public class ToolController {

    @Autowired
    private ServletContext servletContext; //用于在不同的会话之间都能共享数据
    @Autowired
    private UserMapper userMapper;

    //图片验证码接口
    @GetMapping("/captcha")
    public ResponseEntity<byte[]> captcha(HttpSession session){
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

            //设置session属性
            session.setAttribute("captchaText",captchaText);
            session.setAttribute("captchaTime", LocalDateTime.now());

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
        //验证获取是否频繁，必须间隔大于30秒
        Object emailMap = servletContext.getAttribute("emailMap");
        HashMap<String,String> map=(HashMap<String,String>)emailMap;
        String code = map.get(email);
        if(code!=null){
            return Result.error("获取太频繁，稍后再试");
        }
        //现在可以发送邮件了
        code=Tools.generateRandomNumberString(6);
        map.put(email,code);
        //10秒后删除
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                map.remove(email);
            }
        };
        timer.schedule(task,30*1000);

        System.out.println(code);
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
    public Result checkEmailVerifyCode(String email,String code,HttpSession session){

        Object emailMap = servletContext.getAttribute("emailMap");
        HashMap<String,String> map=(HashMap<String,String>)emailMap;
        String code1 = map.get(email);

        if (code.equals(code1)){
            return Result.success();
        }else {
            return Result.error("验证码错误");
        }
    }
}
