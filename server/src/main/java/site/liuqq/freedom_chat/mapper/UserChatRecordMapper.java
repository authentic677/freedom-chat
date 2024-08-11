package site.liuqq.freedom_chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import site.liuqq.freedom_chat.pojo.UserChatRecord;

import java.util.List;

@Mapper
public interface UserChatRecordMapper extends BaseMapper<UserChatRecord> {

    @Insert("insert into user_chat_record (uid1,uid2,message_id,flag) values (#{uid1},#{uid2},#{messageId},#{flag})")
    void insert0(UserChatRecord userChatRecord);

    @Select("select u.id,u.uid1,u.uid2,u.flag,m.time,m.type,m.content,u1.username as username1,u1.avatar as avatar1,u2.username as username2,u2.avatar as avatar2 " +
            "from user_chat_record as u " +
            "join users as u1 on u1.uid=u.uid1 " +
            "join users as u2 on u2.uid=u.uid2 " +
            "join messages as m on m.id=u.message_id " +
            "where uid1=#{uid2} and uid2=#{uid1} " +
            "order by time")
    List<UserChatRecord> query(UserChatRecord userChatRecord);
}
