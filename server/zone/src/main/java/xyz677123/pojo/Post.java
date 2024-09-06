package xyz677123.pojo;

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
@TableName("posts")
public class Post {
    @TableId(type = IdType.AUTO)
    Integer id;
    String uid;
    LocalDateTime time;
    String content;
    Long view;
    Integer parent;
}
