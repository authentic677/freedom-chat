package site.liuqq.freedom_chat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("message_notices")
public class MessageNotice {

    @TableId(type = IdType.AUTO)
    int id;
    String uid1;
    String uid2;
    int messageId;
    int count;
    LocalDateTime toppingTime;

     //连接查询message表时可能需要的字段
    @TableField(exist = false)
    LocalDateTime time;
    @TableField(exist = false)
    String type;
    @TableField(exist = false)
    String content;

    //连接查询users表时可能需要的字段
    @TableField(exist = false)
    String avatar;
    @TableField(exist = false)
    String username;

    @TableField(exist = false)
    String aaa;
}
