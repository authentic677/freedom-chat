package site.liuqq.freedom_chat.service;

import site.liuqq.freedom_chat.pojo.ContactNotice;
import site.liuqq.freedom_chat.common.Result;

public interface ContactNoticeService {

    Result insert(ContactNotice contactNotice);

    Result listByUid(String uid);

    Result update(ContactNotice contactNotice);

    Result delete(ContactNotice contactNotice);
}
