package site.liuqq.freedom_chat.provider;

import org.apache.ibatis.jdbc.SQL;
import site.liuqq.freedom_chat.pojo.Contact;

import java.util.Map;

public class ContactSqlProvider {
    public String selectContact(Contact contact){
        return new SQL(){
            {
                SELECT("id","uid1","uid2","time","note","username","avatar","personal_signature");
                FROM("contacts");
                JOIN("users on uid2=uid");
                WHERE("uid1=#{uid1}");
                if(contact.getUid2() != null){
                    WHERE("uid2=#{uid2}");
                }
            }
        }.toString();
    }
}
