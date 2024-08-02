package site.liuqq.freedom_chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChatRecord {
    int id;
    String uid1;
    String uid2;
    int messageId;
    int flag;

    LocalDateTime time;
    String type;
    String content;

    //连接查询时可能涉及的字段
    String username1;
    String avatar1;

    String username2;
    String avatar2;
}
