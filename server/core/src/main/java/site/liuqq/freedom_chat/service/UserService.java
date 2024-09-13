package site.liuqq.freedom_chat.service;

import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.common.pojo.User;

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
