package site.liuqq.freedom_chat.pojo.group;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz677123.freedomchat.common.pojo.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("group_messages")
public class GroupMessage {
    @TableId(type = IdType.AUTO)
    Integer id;
    String gid;
    String memberUid;
    LocalDateTime time;
    String type;
    String content;

    @TableField(exist = false)
    User user;
}
