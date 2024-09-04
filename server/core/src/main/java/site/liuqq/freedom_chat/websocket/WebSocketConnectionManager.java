package site.liuqq.freedom_chat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import site.liuqq.freedom_chat.pojo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebSocketConnectionManager {
    private static final Map<String,WebSocketSession> sessionMap=new HashMap<>();

    //所有方法都是同步方法，旨在避免线程安全问题

    public synchronized static void notify(Map<String,Object> data,String uid){
        sessionMap.forEach((key,session)->{ //迭代找出目标对象，发送websocket消息
            User user=(User) session.getAttributes().get("user");
            if(user!=null&&user.getUid().equals(uid)){
                try {
                    ObjectMapper objectMapper=new ObjectMapper();
                    String s = objectMapper.writeValueAsString(data);
                    //发送
                    session.sendMessage(new TextMessage(s));
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    public synchronized static boolean isOnline(String uid){
        //此处效率有点低，有待优化
        for (String s : sessionMap.keySet()) {
            User user=(User) sessionMap.get(s).getAttributes().get("user");
            if(user!=null&&user.getUid().equals(uid)){
                return true;
            }
        }
        return false;
    }

    public synchronized static void addSession(WebSocketSession session){
        sessionMap.put(session.getId(),session);
    }
    public synchronized static void rmvSession(WebSocketSession session){
        sessionMap.remove(session.getId());
    }
}
