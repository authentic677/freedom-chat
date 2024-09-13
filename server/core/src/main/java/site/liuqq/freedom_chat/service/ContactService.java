package site.liuqq.freedom_chat.service;

import site.liuqq.freedom_chat.pojo.Contact;
import xyz677123.freedomchat.common.pojo.Result;

public interface ContactService {

    Result listByUid(String uid);

    Result listByUid2(Contact contact);
}
