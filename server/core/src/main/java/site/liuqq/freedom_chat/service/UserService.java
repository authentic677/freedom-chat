package site.liuqq.freedom_chat.service;

import site.liuqq.freedom_chat.common.Result;
import site.liuqq.freedom_chat.pojo.User;

import java.util.List;

public interface UserService  {

    List<User> list();

    List<User> searchUsers(String keyword);

    Result login(User user);

    Result email_login(User user);

    Result register(User user);


    Result updateUserInfo(User user);

    Result resetPwd(User user);
}
