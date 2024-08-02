package site.liuqq.freedom_chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.liuqq.freedom_chat.mapper.ContactMapper;
import site.liuqq.freedom_chat.pojo.Contact;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.service.ContactService;
import site.liuqq.freedom_chat.websocket.WebSocketConnectionManager;

import java.util.List;
import java.util.Random;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactMapper contactMapper;

    @Override
    public Result listByUid(String uid) {

        List<Contact> contacts = contactMapper.listByUid(uid);

        for (Contact contact : contacts) {
            //设置用户在线状态
            boolean flag= WebSocketConnectionManager.isOnline(contact.getUid2());
            contact.setStatus(flag?1:0);

        }

        return Result.success(contacts);
    }

    @Override
    public Result listByUid2(Contact contact) {

        List<Contact> contacts = contactMapper.selectContact(contact);

        for (Contact contact1 : contacts) {
            //设置用户在线状态
            boolean flag= WebSocketConnectionManager.isOnline(contact1.getUid2());
            contact1.setStatus(flag?1:0);
        }

        return Result.success(contacts);
    }
}
