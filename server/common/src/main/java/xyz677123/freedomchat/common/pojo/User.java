package xyz677123.freedomchat.common.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class User {
    private String uid;
    private String username;
    private String password;
    private String avatar;
    private String personalSignature;
    private String email;
    private LocalDateTime registrationTime;
    private String phoneNumber;

    @TableField(exist = false)
    private String account; //登录用的，因为用户输入uid/email/phoneNumber均可
    @TableField(exist = false)
    private String verifyCode; //登录用的，存验证码文本
    @TableField(exist = false)
    private Integer isOnline; //是否在线
}
