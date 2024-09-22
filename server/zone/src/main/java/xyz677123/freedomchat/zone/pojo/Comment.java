package xyz677123.freedomchat.zone.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz677123.freedomchat.common.pojo.User;

import java.time.LocalDateTime;

//这是留言
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("comments")
public class Comment {
    @TableId(type = IdType.AUTO)
    Integer id;
    String uid1;
    String uid2;
    String content;
    LocalDateTime createdAt;
    String visibility;
    Integer parentId;

    @TableField(exist = false)
    User user2;
}
