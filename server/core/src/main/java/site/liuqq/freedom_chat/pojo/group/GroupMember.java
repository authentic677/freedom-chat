package site.liuqq.freedom_chat.pojo.group;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.liuqq.freedom_chat.pojo.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("group_members")
public class GroupMember {
    Integer id;
    String gid;
    String memberUid;
    LocalDateTime joinTime;
    String nickname;
    String note;
    String role;
    LocalDateTime speakTime;

    @TableField(exist = false)
    User user;
    @TableField(exist = false)
    Group group;
}
