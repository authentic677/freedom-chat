package site.liuqq.freedom_chat.service;

import site.liuqq.freedom_chat.pojo.Contact;
import site.liuqq.freedom_chat.pojo.Result;

public interface ContactService {

    Result listByUid(String uid);

    Result listByUid2(Contact contact);
}
