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
@TableName("journals")
public class Journal {
    @TableId(type = IdType.AUTO)
    Integer id;
    String uid;
    String title;
    String content;
    String status;
    String letterPaper;
    String visibility;
    byte allowComments;
    Integer categoryId;
    byte isPrivate;
    byte isDeleted;
    LocalDateTime deletedAt;
    byte isPinned;
    LocalDateTime pinnedAt;
    LocalDateTime createdAt;
    LocalDateTime updateAt;
}
