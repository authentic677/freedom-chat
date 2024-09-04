package site.liuqq.freedom_chat.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.liuqq.freedom_chat.mapper.ContactMapper;
import site.liuqq.freedom_chat.pojo.Contact;
import site.liuqq.freedom_chat.pojo.ContactNotice;
import site.liuqq.freedom_chat.pojo.UserChatRecord;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.websocket.WebSocketConnectionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataUpdateNotify {


    private ContactMapper contactMapper;

    @Autowired
    public void setContactMapper(ContactMapper contactMapper) {
        this.contactMapper = contactMapper;
    }

    public static void contactNoticeNotify(ContactNotice contactNotice){
        Map<String,Object> map=new HashMap<>();
        map.put("command","dataUpdate");
        List<String> dataPoints=new ArrayList<>();
        dataPoints.add("contactNotice");
        map.put("dataPoints",dataPoints);
        WebSocketConnectionManager.notify(map,contactNotice.getUid1());
        WebSocketConnectionManager.notify(map,contactNotice.getUid2());
    }

    public static void contactNotify(Contact contact){
        Map<String,Object> map=new HashMap<>();
        map.put("command","dataUpdate");
        List<String> dataPoints=new ArrayList<>();
        dataPoints.add("contact");
        map.put("dataPoints",dataPoints);
        WebSocketConnectionManager.notify(map,contact.getUid1());
        WebSocketConnectionManager.notify(map,contact.getUid2());
    }

    public static void contactNotify(String uid){
        Map<String,Object> map=new HashMap<>();
        map.put("command","dataUpdate");
        List<String> dataPoints=new ArrayList<>();
        dataPoints.add("contact");
        map.put("dataPoints",dataPoints);
        WebSocketConnectionManager.notify(map,uid);
    }

    public static void messageNotify(UserChatRecord userChatRecord){
        Map<String,Object> map=new HashMap<>();
        map.put("command","dataUpdate");
        List<String> dataPoints=new ArrayList<>();
        dataPoints.add("message");
        map.put("dataPoints",dataPoints);
        WebSocketConnectionManager.notify(map, userChatRecord.getUid1());
        WebSocketConnectionManager.notify(map, userChatRecord.getUid2());
    }

    public static void messageNoticeNotify(UserChatRecord userChatRecord){
        Map<String,Object> map=new HashMap<>();
        map.put("command","dataUpdate");
        List<String> dataPoints=new ArrayList<>();
        dataPoints.add("messageNotice");
        map.put("dataPoints",dataPoints);
        WebSocketConnectionManager.notify(map, userChatRecord.getUid1());
        WebSocketConnectionManager.notify(map, userChatRecord.getUid2());
    }

    public void userOnlineOrOfflineNotify(User user){

        System.out.println("user online or offline :");
        System.out.println(user);
        //获取它的好友信息
        List<Contact> contacts = contactMapper.listByUid(user.getUid());

        for (Contact contact : contacts) {
            //如果在线就发
            boolean online = WebSocketConnectionManager.isOnline(contact.getUid2());
            if(online){
                contactNotify(contact.getUid2());
            }

        }

    }

    //群组聊天消息更新
    public static void groupMessageNotify(List<String> uids){
        Map<String,Object> map=new HashMap<>();
        map.put("command","dataUpdate");
        List<String> dataPoints=new ArrayList<>();
        dataPoints.add("groupMessage");
        map.put("dataPoints",dataPoints);

        for (String uid : uids) {
            WebSocketConnectionManager.notify(map, uid);
        }
    }

    public static void groupMessageNoticeNotify(List<String> uids){
        Map<String,Object> map=new HashMap<>();
        map.put("command","dataUpdate");
        List<String> dataPoints=new ArrayList<>();
        dataPoints.add("messageNotice");
        map.put("dataPoints",dataPoints);

        for (String uid : uids) {
            WebSocketConnectionManager.notify(map, uid);
        }
    }
}
