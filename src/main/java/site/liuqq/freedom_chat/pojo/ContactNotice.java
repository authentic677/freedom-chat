package site.liuqq.freedom_chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactNotice {

    int id;
    String uid1;
    String uid2;
    LocalDateTime time;
    String leaveMessage;
    String status;
    int flag;
    int applicationId;

    String username;
    String avatar;
}
