package site.liuqq.freedom_chat.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.service.UserService;
import site.liuqq.freedom_chat.utils.Tools;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static site.liuqq.freedom_chat.utils.RedisConstants.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //搜索添加好友的接口
    @GetMapping("/users")
    public List<User> searchUsers(String keyword,@RequestHeader String token){

        if(keyword!=null&&!keyword.equals("")){
            System.out.println(keyword);
            List<User> list = userService.searchUsers(keyword);

            return list;
        }else{
            return new ArrayList<>();
        }

    }

    //登录的接口 - 密码登录
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        //从redis获取验证码
        String captchaText = stringRedisTemplate.opsForValue().get(VERIFY_CODE + user.getAccount());
        if(captchaText==null){
            return Result.error("验证码过期或未获取验证码");
        }
        //验证值
        if(!user.getVerifyCode().equalsIgnoreCase(captchaText)){
            return Result.error("验证码错误");
        }
        return  userService.login(user);
    }

    //登录的接口 - 邮箱登录
    @PostMapping("/email_login")
    public Result emailLogin(@RequestBody User user){

        //从redis获取验证码
        String code = stringRedisTemplate.opsForValue().get(VERIFY_CODE + user.getEmail());
        if(code==null){
            return Result.error("未获取验证码或验证码过期");
        }
        //验证值是否正确
        if(!code.equalsIgnoreCase(user.getVerifyCode())){
            return Result.error("验证码错误");
        }

        return userService.email_login(user);
    }

    //注册的接口
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        //验证验证码
        String code=stringRedisTemplate.opsForValue().get(VERIFY_CODE+user.getEmail());
        if(code==null){
            return Result.error("验证码过期或未获取验证码");
        }
        String code1=user.getVerifyCode();
        if(!code.equals(code1)){
            return Result.error("验证码错误");
        }
        //交给service层
        return userService.register(user);
    }

    //获取个人信息的接口
    @GetMapping("/user")
    public Result user(@RequestHeader String token){
        User user = Tools.checkJwtToken(token);
        String uid=user.getUid();

        return userService.selectByUid(uid);
    }

    //修改用户个人信息的接口
    @PutMapping("/user")
    public Result update(@RequestHeader String token,@RequestBody User user){
        User user1 = Tools.checkJwtToken(token);
        user.setUid(user1.getUid());

        //如果用户改了邮箱就要验证验证码
        Result result = userService.selectByUid(user1.getUid());
        User user2= (User)result.getData();
        if(!user2.getEmail().equals(user.getEmail())){
            //验证验证码
            Object attribute = ((HashMap<String,String>)(servletContext.getAttribute("emailMap"))).get(user.getEmail());
            if(attribute==null){
                return Result.error("验证码过期或还未获取验证码");
            }
            String code=(String) attribute;
            String code1=user.getVerifyCode();
            if(!code.equals(code1)){
                return Result.error("验证码错误");
            }
        }

        return userService.updateUserInfo(user);
    }

    //重置密码的接口
    @PostMapping("/resetPwd")
    public Result resetPwd(@RequestBody User user){

        String code=stringRedisTemplate.opsForValue().get(VERIFY_CODE+user.getEmail());
        if(code==null){
            return Result.error("未获取验证码或验证码过期");
        }
        //验证值是否正确
        if(!code.equalsIgnoreCase(user.getVerifyCode())){
            return Result.error("验证码错误");
        }

        return userService.resetPwd(user);
    }

    //获取某个uid用户的信息的接口
    @GetMapping("/user/{uid}")
    public Result getUserInfo(@PathVariable String uid){
        return userService.selectByUid(uid);
    }
}
