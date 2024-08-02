package site.liuqq.freedom_chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageNotice {

    int id;
    String uid1;
    String uid2;
    int messageId;
    int count;
    LocalDateTime toppingTime;

     //连接查询message表时可能需要的字段
    LocalDateTime time;
    String type;
    String content;

    //连接查询users表时可能需要的字段
    String avatar;
    String username;

    String aaa;
}
