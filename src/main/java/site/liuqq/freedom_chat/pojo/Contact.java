package site.liuqq.freedom_chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    int id;
    String uid1;
    String uid2;
    LocalDateTime time;
    String note;

    String username;
    String avatar;
    String personalSignature;
    Integer status; //表示用户是否在线
}
