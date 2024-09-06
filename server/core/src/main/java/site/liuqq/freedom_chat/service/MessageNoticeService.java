package site.liuqq.freedom_chat.service;

import site.liuqq.freedom_chat.pojo.MessageNotice;
import site.liuqq.freedom_chat.common.Result;

public interface MessageNoticeService {

    Result selectByUid(String uid,String targetUid);


    Result delete(MessageNotice messageNotice);
}
