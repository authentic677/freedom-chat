package site.liuqq.freedom_chat.mapper;

import org.apache.ibatis.annotations.*;
import site.liuqq.freedom_chat.pojo.Contact;
import site.liuqq.freedom_chat.pojo.ContactNotice;

import java.util.List;

@Mapper
public interface ContactNoticeMapper {

    @Insert("insert into contact_notices (uid1,uid2,flag,application_id) values (#{uid1},#{uid2},#{flag},#{applicationId})")
    void insert(ContactNotice contactNotice);

    @Update("update contact_notices set flag=#{flag} where uid1=#{uid1} and uid2=#{uid2}")
    void update(ContactNotice contactNotice);

    @Delete("delete from contact_notices where uid1=#{uid1} and uid2=#{uid2}")
    void delete(ContactNotice contactNotice);

    @Select("select contact_notices.id,uid1,uid2,time,leave_message,flag,username,avatar,status,application_id\n " +
            "from contact_notices\n" +
            "join application on application_id=application.id " +
            "join users on uid2=uid\n" +
            "where uid1=#{uid}")
    List<ContactNotice> listByUid(String uid);

}
