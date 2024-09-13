package site.liuqq.freedom_chat.pojo.group;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz677123.freedomchat.common.pojo.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`groups`")
public class Group {
    String gid;
    String name;
    String avatar;
    String creatorUid;
    LocalDateTime time;
    String description;
    String applicantNote;
    boolean allMute;

    @TableField(exist = false)
    Integer number; //群人数
    @TableField(exist = false)
    List<User> members; //成员
}
