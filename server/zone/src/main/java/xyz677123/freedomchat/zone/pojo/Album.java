package xyz677123.freedomchat.zone.pojo;

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
@TableName("albums")
public class Album {
    @TableId(type = IdType.AUTO)
    Integer id;
    String uid;
    String name;
    String description;
    String visibility;
    Integer coverPhotoId;

    @TableField(exist = false)
    Photo coverPhoto;
    @TableField(exist = false)
    Integer photoCount;
}
