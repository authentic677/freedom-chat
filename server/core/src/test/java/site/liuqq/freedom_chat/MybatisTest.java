package site.liuqq.freedom_chat;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.liuqq.freedom_chat.mapper.*;
import site.liuqq.freedom_chat.pojo.Contact;
import site.liuqq.freedom_chat.pojo.UserChatRecord;

import java.util.List;

@Slf4j
@SpringBootTest
public class MybatisTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MessageNoticeMapper messageNoticeMapper;
    @Autowired
    private UserChatRecordMapper userChatRecordMapper;
    @Autowired
    private ApplicationMapper applicationMapper;
    @Autowired
    private ContactMapper contactMapper;


    @Test
    public void testSelectAll(){
        Contact contact = new Contact();
        contact.setUid1("9627223950");
        contact.setUid2("5465763738");
        List<Contact> contacts = contactMapper.selectContact(contact);

        for (Contact contact1 : contacts) {
            System.out.println(contact1);
        }


    }
}