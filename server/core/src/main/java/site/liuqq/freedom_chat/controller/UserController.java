package site.liuqq.freedom_chat.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.service.UserService;
import site.liuqq.freedom_chat.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static site.liuqq.freedom_chat.common.RedisConstants.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserServiceImpl userServiceImpl;

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

    //获取请求的用户自己的信息
    @GetMapping("/user/self")
    public Result user(HttpSession session){

        String uid = ((User) session.getAttribute("user")).getUid();

        User one = userServiceImpl
                .lambdaQuery()
                .eq(User::getUid, uid)
                .one();
        return Result.success(one);
    }

    //某用户查询指定uid用户的信息
    @GetMapping("/user/{uid}")
    public Result queryUser(@PathVariable String uid,HttpSession session){

        String uid0 = ((User) session.getAttribute("user")).getUid();

        //不会查询所有的字段，敏感字段不能查询
        User one = userServiceImpl
                .lambdaQuery()
                .select(User::getUid,User::getUsername,User::getAvatar,User::getPersonalSignature)
                .eq(User::getUid, uid)
                .one();
        return Result.success(one);
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

}
