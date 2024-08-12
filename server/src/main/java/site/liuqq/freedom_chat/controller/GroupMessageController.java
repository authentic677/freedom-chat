package site.liuqq.freedom_chat.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.conf.CustomConfig;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.pojo.group.GroupMessage;
import site.liuqq.freedom_chat.service.impl.GroupMessageServiceImpl;
import site.liuqq.freedom_chat.service.impl.UserServiceImpl;
import site.liuqq.freedom_chat.utils.Tools;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GroupMessageController {

    private final GroupMessageServiceImpl groupMessageServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final CustomConfig customConfig;

    public GroupMessageController(GroupMessageServiceImpl groupMessageServiceImpl, UserServiceImpl userServiceImpl, CustomConfig customConfig) {
        this.groupMessageServiceImpl = groupMessageServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.customConfig = customConfig;
    }

    //创建一条群聊消息
    @PostMapping("/groupMessage")
    Result insert(@RequestBody GroupMessage groupMessage, HttpSession session){

        String uid = ((User) session.getAttribute("user")).getUid();

        //前端填充了gid，type，和content，不做鉴权

        groupMessage.setMemberUid(uid);
        groupMessage.setTime(Tools.now(customConfig.getZone()));

        groupMessageServiceImpl.save(groupMessage);

        return Result.success();
    }

    //获取群聊消息
    @GetMapping("/groupMessages/{gid}")
    Result getGroupMessage(@PathVariable String gid, HttpSession session){

        String uid = ((User) session.getAttribute("user")).getUid();

        //鉴权略

        List<GroupMessage> list = groupMessageServiceImpl
                .lambdaQuery()
                .eq(GroupMessage::getGid, gid)
                .list();
        //处理外键
        list.forEach(e->{
            User user = userServiceImpl
                    .lambdaQuery()
                    .eq(User::getUid, e.getMemberUid())
                    .one();
            e.setUser(user);
        });

        return Result.success(list);
    }

}
