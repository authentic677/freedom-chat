package site.liuqq.freedom_chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String uid;
    private String username;
    private String password;
    private String avatar;
    private String personalSignature;
    private String email;
    private LocalDateTime registrationTime;
    private String phoneNumber;

    private String account; //登录用的，因为用户输入uid/email/phoneNumber均可
    private String verifyCode; //登录用的，存验证码文本
    private Integer isOnline; //是否在线
}
