package site.liuqq.freedom_chat.service;

import site.liuqq.freedom_chat.pojo.UserChatRecord;
import xyz677123.freedomchat.common.pojo.Result;

public interface UserChatRecordService {

    Result insert(UserChatRecord userChatRecord);

    Result insert2(UserChatRecord userChatRecord);

    Result query(UserChatRecord userChatRecord);
}
