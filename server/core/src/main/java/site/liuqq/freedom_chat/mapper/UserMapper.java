package site.liuqq.freedom_chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import site.liuqq.freedom_chat.pojo.User;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from users")
    List<User> selectAll();

    @Select("select uid,username,avatar,personal_signature,email,registration_time,phone_number from users where uid=#{uid}")
    User selectByUid(String uid);

    @Select("select * from users where email=#{email}")
    User selectByEmail(String email);

    @Select("select * from users where email=#{account} or uid=#{account}")
    User selectByAccount(String account);

    //根据关键字(uid或username)搜索用户
    @Select("select * from users where uid like concat('%',#{keyword},'%') or username like concat('%',#{keyword},'%')")
    List<User> searchUsers(String keyword);

    @Select("select * from users where (uid=#{account} or email=#{account} or phone_number=#{account}) and password=#{password}")
    User getByAccountAndPassword(User user);

    @Insert("insert into users (uid,username,password,registration_time,personal_signature,email) values (#{uid},#{username},#{password},#{registrationTime},#{personalSignature},#{email})")
    void insert1(User user);

    //动态sql，采用配置文件，实现用户信息的更新操作
    void update(User user);

    //修改密码
    @Update("update users set password=#{password} where uid=#{uid}")
    void updatePassword(User user);
}
