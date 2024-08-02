package site.liuqq.freedom_chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    String gid;
    String name;
    String avatar;
    String creatorUid;
    LocalDateTime time;
    String description;
    String applicantNote;
    boolean allMute;
}
