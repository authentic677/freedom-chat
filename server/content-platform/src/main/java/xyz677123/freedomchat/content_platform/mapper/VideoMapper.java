package xyz677123.freedomchat.content_platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import xyz677123.freedomchat.content_platform.pojo.Video;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {

    @Select("SELECT * FROM videos ORDER BY RAND() LIMIT 1")
    Video randomRetrieve();
}
