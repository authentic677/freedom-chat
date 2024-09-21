package xyz677123.freedomchat.zone.pojo;

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
@TableName("journal_categorys")
public class JournalCategory {
    @TableId(type = IdType.AUTO)
    Integer id;
    String uid;
    String name;
    LocalDateTime createdAt;
    LocalDateTime updateAt;
}
