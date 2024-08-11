package site.liuqq.freedom_chat.pojo.group;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("group_message_notice")
public class GroupMessageNotice {
    @TableId(type = IdType.AUTO)
    Integer id;
    String uid;
    String gid;
    Integer messageId;
    Integer count;
    LocalDateTime toppingTime;
}
