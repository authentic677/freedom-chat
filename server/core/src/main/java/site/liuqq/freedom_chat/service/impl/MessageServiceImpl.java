package site.liuqq.freedom_chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.liuqq.freedom_chat.mapper.MessageMapper;
import site.liuqq.freedom_chat.pojo.Message;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> {
}
