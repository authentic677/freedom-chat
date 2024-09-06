package site.liuqq.freedom_chat.service;

import site.liuqq.freedom_chat.pojo.UserChatRecord;
import site.liuqq.freedom_chat.common.Result;

public interface UserChatRecordService {

    Result insert(UserChatRecord userChatRecord);

    Result insert2(UserChatRecord userChatRecord);

    Result query(UserChatRecord userChatRecord);
}
