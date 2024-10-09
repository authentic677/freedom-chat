package xyz677123.freedomchat.content_platform.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("videos")
public class Video {
    @TableId(type = IdType.AUTO)
    Integer id;
    String title;
    String summary;
    String cover;
    String video;
    String visibility;
    Integer channelId;
    LocalDateTime time;

    @TableField(exist = false)
    Channel channel;
}
