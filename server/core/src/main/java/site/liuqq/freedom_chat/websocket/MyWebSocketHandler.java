package site.liuqq.freedom_chat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.common.DataUpdateNotify;
import site.liuqq.freedom_chat.common.Tools;

import java.util.HashMap;
import java.util.Map;

@Component
public class MyWebSocketHandler implements WebSocketHandler {

    @Autowired
    private DataUpdateNotify dataUpdateNotify;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("websocket connect established");
        //添加
        WebSocketConnectionManager.addSession(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String msg=(String) message.getPayload();

        ObjectMapper objectMapper=new ObjectMapper();

        try {
            Map<String,String> map=objectMapper.readValue(msg, Map.class);

            System.out.println("map: "+map);
            
            //逻辑处理
            String command=map.get("command");
            if(command.equals("auth")){
                //告诉服务器连接着的身份
                User user = Tools.checkJwtToken(map.get("token"));
                if(user!=null){ //更新操作
                    session.getAttributes().put("user",user); //此连接用户认证了
                    //响应
                    Map<String,String> map1=new HashMap<>();
                    map1.put("command",command);
                    map1.put("result","ok");
                    String json=objectMapper.writeValueAsString(map1);
                    session.sendMessage(new TextMessage(json));

                    //用户上线通知
                    dataUpdateNotify.userOnlineOrOfflineNotify(user);
                }
            } else if (command.equals("video-request")) {
                //此连接是否用户认证,一般都会哈，
                User user=(User)session.getAttributes().get("user");
                String s = map.get("callee"); //被呼叫者uid
                //验证呼叫者和被呼叫着是否是好友关系，此处省略

                //向被呼叫者发送ws消息，告诉是谁呼叫的，即呼叫者的uid
                Map<String,Object> data=new HashMap<>();
                data.put("command","video-request");
                data.put("caller",user.getUid());
                WebSocketConnectionManager.notify(data,s);
            } else if (command.equals("video-response")) {
                //此连接是否用户认证,一般都会哈，
                User user=(User)session.getAttributes().get("user");
                String s = map.get("caller"); //被呼叫者uid
                //验证呼叫者和被呼叫着是否是好友关系，此处省略

                //向呼叫者发送ws消息，传递被呼叫者的应答
                Map<String,Object> data=new HashMap<>();
                data.put("command","video-response");
                data.put("status",map.get("status"));
                data.put("id",map.get("id"));
                WebSocketConnectionManager.notify(data,s);
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("websocket connection error");
        //用户离线通知
        dataUpdateNotify.userOnlineOrOfflineNotify((User) session.getAttributes().get("user"));

        //移除
        WebSocketConnectionManager.addSession(session);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("websocket connection close");
        //用户离线通知
        dataUpdateNotify.userOnlineOrOfflineNotify((User) session.getAttributes().get("user"));

        WebSocketConnectionManager.rmvSession(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
