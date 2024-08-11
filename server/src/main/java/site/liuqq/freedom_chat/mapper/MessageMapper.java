package site.liuqq.freedom_chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import site.liuqq.freedom_chat.pojo.Message;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into messages (time,type,content) values (#{time},#{type},#{content})")
    void insert0(Message message);
}
