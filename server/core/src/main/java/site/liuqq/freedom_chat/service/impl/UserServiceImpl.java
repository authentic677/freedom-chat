package site.liuqq.freedom_chat.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.liuqq.freedom_chat.conf.CustomConfig;

import site.liuqq.freedom_chat.mapper.UserMapper;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.service.UserService;
import site.liuqq.freedom_chat.common.Tools;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CustomConfig customConfig;

    @Override
    public List<User> list() {
        return userMapper.selectAll();
    }

    @Override
    public List<User> searchUsers(String keyword) {
        return userMapper.searchUsers(keyword);
    }

    @Override
    public Result login(User user) {
        try {

            //计算hash
            user.setPassword(Tools.passwordHash(user.getPassword()));
            User user1=userMapper.getByAccountAndPassword(user);

            if (user1!=null){
                StpUtil.login(user1.getUid());
                return Result.success(Tools.makeJwtToken(user1));
            }else {
                return Result.error("用户名或密码错误");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Result email_login(User user) {

        //验证邮箱是否注册了
        User user1 = userMapper.selectByEmail(user.getEmail());
        if(user1==null){
            return Result.error("邮箱未注册");
        }

        return Result.success(Tools.makeJwtToken(user1));
    }

    @Override
    public Result register(User user) {
        //用户名是不是空的？
        if(user.getUsername()==null||user.getUsername().equals("")){
            return Result.error("用户名不能为空");
        }
        //验证密码是否符合要求
        if(user.getPassword()==null||!Tools.validatePwd(user.getPassword())){
            return Result.error("不符合要求的密码");
        }
        //设置密码哈希
        user.setPassword(Tools.passwordHash(user.getPassword()));
        //验证邮箱是否重复
        User user1 = userMapper.selectByEmail(user.getEmail());
        if(user1!=null){
            return Result.error("此邮箱已被注册");
        }

        //设置字段值
        String uid=Tools.generateRandomNumberString(10);
        user.setUid(uid);

        user.setRegistrationTime(Tools.now(customConfig.getZone()));
        user.setPersonalSignature("因为个性，所以没有签名");
        //存入数据库
        userMapper.insert1(user);

        //设置响应数据
        User user2=new User();
        user2.setUid(uid);
        return Result.success(user2);
    }


    @Override
    public Result updateUserInfo(User user) {
        //用户名是不是空的？
        if(user.getUsername()==null||user.getUsername().equals("")){
            return Result.error("用户名不能为空");
        }
        //验证邮箱是否重复
        User user1 = userMapper.selectByEmail(user.getEmail());
        if(user1!=null&&!user1.getUid().equals(user.getUid())){
            return Result.error("此邮箱已被注册");
        }

        userMapper.update(user);
        return Result.success();
    }

    @Override
    public Result resetPwd(User user) {

        //验证邮箱是否注册了
        User user1 = userMapper.selectByEmail(user.getEmail());
        if(user1==null){
            return Result.error("邮箱未注册");
        }

        //验证密码是否符合要求
        if(user.getPassword()==null||!Tools.validatePwd(user.getPassword())){
            return Result.error("不符合要求的密码");
        }

        //持久化修改
        user.setPassword(Tools.passwordHash(user.getPassword()));
        user.setUid(user1.getUid());
        userMapper.updatePassword(user);

        return Result.success();
    }
}
