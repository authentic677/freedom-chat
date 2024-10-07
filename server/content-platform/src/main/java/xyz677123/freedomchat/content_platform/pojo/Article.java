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
@TableName("articles")
public class Article {
    @TableId(type = IdType.AUTO)
    Integer id;
    String title;
    String cover;
    String content;
    String gongzhonghaoId;
    Long view;
    LocalDateTime createdAt;
    LocalDateTime updateAt;
}
