package site.liuqq.freedom_chat.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Service;
import site.liuqq.freedom_chat.pojo.Contact;
import site.liuqq.freedom_chat.provider.ContactSqlProvider;

import java.util.List;

@Mapper
public interface ContactMapper {

    @Select("select id,uid1,uid2,time,note,username,avatar,personal_signature\n" +
            "from contacts\n" +
            "join users on uid2=uid\n" +
            "where uid1=#{uid}")
    List<Contact> listByUid(String uid);

    @Select("select * from contacts where uid1=#{uid1} and uid2=#{uid2}")
    Contact selectByPair(Contact contact);

    @Insert("insert into contacts (uid1,uid2,time) values (#{uid1},#{uid2},#{time})")
    void insert(Contact contact);

    //试试新的使用方式，这种可以轻松构建动态sql 只给uid1时也行，同时给uid1和uid2也行
    @SelectProvider(type = ContactSqlProvider.class,method = "selectContact")
    List<Contact> selectContact(Contact contact);
}
