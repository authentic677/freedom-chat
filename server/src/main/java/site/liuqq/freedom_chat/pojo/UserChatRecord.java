package site.liuqq.freedom_chat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChatRecord {
    @TableId(type = IdType.AUTO)
    int id;
    String uid1;
    String uid2;
    int messageId;
    int flag;

    @TableField(exist = false)
    LocalDateTime time;
    @TableField(exist = false)
    String type;
    @TableField(exist = false)
    String content;

    //连接查询时可能涉及的字段
    @TableField(exist = false)
    String username1;
    @TableField(exist = false)
    String avatar1;

    @TableField(exist = false)
    String username2;
    @TableField(exist = false)
    String avatar2;

    @TableField(exist = false)
    Message message;
}
