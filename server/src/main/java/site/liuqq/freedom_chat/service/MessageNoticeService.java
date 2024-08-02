package site.liuqq.freedom_chat.service;

import site.liuqq.freedom_chat.pojo.MessageNotice;
import site.liuqq.freedom_chat.pojo.Result;

public interface MessageNoticeService {

    Result selectByUid(String uid,String targetUid);

    Result clearCount(MessageNotice messageNotice);

    Result delete(MessageNotice messageNotice);
}
