package xyz677123.freedomchat.content_platform.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("channels")
public class Channel {
    @TableId(type = IdType.AUTO)
    Integer id;
    String avatar;
    String banner;
    String name;
    String description;
    String uid;

    @TableField(exist = false)
    Long followCount;
    @TableField(exist = false)
    Long fanCount;
    @TableField(exist = false)
    Long likeCount;
    @TableField(exist = false)
    Long videoCount;
}
