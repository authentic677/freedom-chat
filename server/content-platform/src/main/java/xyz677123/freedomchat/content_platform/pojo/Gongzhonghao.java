package xyz677123.freedomchat.content_platform.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("gongzhonghaos")
public class Gongzhonghao {
    @TableId(type = IdType.AUTO)
    Integer id;
    String avatar;
    String name;
    String description;
    String uid;
    LocalDateTime createdAt;
    LocalDateTime updateAt;
}
