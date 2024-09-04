package site.liuqq.freedom_chat.mapper;

import org.apache.ibatis.annotations.*;
import site.liuqq.freedom_chat.pojo.Application;

@Mapper
public interface ApplicationMapper {

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into application (time,leave_message) values (#{time},#{leaveMessage})")
    void insert(Application application);

    @Update("update application set time=#{time},leave_message=#{leaveMessage},status=#{status} where id=#{id}")
    void update(Application application);

    @Delete("delete from application where id=#{id}")
    void delete(int id);
}
