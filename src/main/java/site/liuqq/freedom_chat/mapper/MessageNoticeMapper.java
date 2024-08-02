package site.liuqq.freedom_chat.mapper;

import org.apache.ibatis.annotations.*;
import site.liuqq.freedom_chat.pojo.MessageNotice;

import java.util.List;

@Mapper
public interface MessageNoticeMapper {

    //注意这里要用left join(左外连接，省略left有问题)
    @Select("select mn.id,mn.uid1,mn.uid2,mn.count,m.time,m.type,m.content,u.avatar,u.username " +
            "from message_notices as mn " +
            "left join messages as m on m.id=mn.message_id " +
            "left join users as u on u.uid=mn.uid2 " +
            " where mn.uid1=#{uid}")
    List<MessageNotice> selectByUid(String uid);

    @Insert("insert into message_notices (uid1,uid2,message_id,count) values (#{uid1},#{uid2},#{messageId},#{count})")
    void insert0(MessageNotice messageNotice);

    @Insert("insert into message_notices (uid1,uid2) values (#{uid1},#{uid2})")
    void insert(MessageNotice messageNotice);

    @Update("update message_notices set message_id=#{messageId},count=#{count} where uid1=#{uid1} and uid2=#{uid2}")
    void update0(MessageNotice messageNotice);

    @Update("update message_notices set count=#{count} where uid1=#{uid1} and uid2=#{uid2}")
    void update3(MessageNotice messageNotice);

    @Delete("delete from message_notices where uid1=#{uid1} and uid2=#{uid2}")
    void delete(MessageNotice messageNotice);
}
