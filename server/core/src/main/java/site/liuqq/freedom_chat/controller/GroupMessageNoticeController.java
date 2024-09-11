package site.liuqq.freedom_chat.controller;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.pojo.group.Group;
import site.liuqq.freedom_chat.pojo.group.GroupMessage;
import site.liuqq.freedom_chat.pojo.group.GroupMessageNotice;
import site.liuqq.freedom_chat.service.impl.GroupMessageNoticeServiceImpl;
import site.liuqq.freedom_chat.service.impl.GroupMessageServiceImpl;
import site.liuqq.freedom_chat.service.impl.GroupServiceImpl;
import site.liuqq.freedom_chat.service.impl.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GroupMessageNoticeController {

    @Autowired
    private GroupMessageNoticeServiceImpl groupMessageNoticeServiceImpl;
    @Autowired
    private GroupServiceImpl groupServiceImpl;
    @Autowired
    private GroupMessageServiceImpl groupMessageServiceImpl;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/groupMessageNotices")
    Result query(HttpSession session){

        String uid= (String) StpUtil.getLoginId();

        List<GroupMessageNotice> list = groupMessageNoticeServiceImpl
                .lambdaQuery()
                .eq(GroupMessageNotice::getUid, uid)
                .list();
        //处理外键
        list.forEach(e->{
            Group group = groupServiceImpl
                    .lambdaQuery()
                    .eq(Group::getGid, e.getGid())
                    .one();
            e.setGroup(group);
            GroupMessage groupMessage = groupMessageServiceImpl
                    .lambdaQuery()
                    .eq(GroupMessage::getId, e.getMessageId())
                    .one();
            User user = userServiceImpl
                    .lambdaQuery()
                    .eq(User::getUid, groupMessage.getMemberUid())
                    .one();
            groupMessage.setUser(user);
            e.setGroupMessage(groupMessage);
        });

        return Result.success(list);
    }

    //清理未读数量
    @PutMapping("/groupMessageNotice/clearCount/{gid}")
    Result clearCount(@PathVariable("gid") String gid,HttpSession session){

        String uid = (String) StpUtil.getLoginId();

        boolean update = groupMessageNoticeServiceImpl
                .lambdaUpdate()
                .eq(GroupMessageNotice::getUid, uid)
                .eq(GroupMessageNotice::getGid, gid)
                .set(GroupMessageNotice::getCount, 0)
                .update();
        if (update){
            return Result.success();
        }else{
            return Result.error("清零失败");
        }

    }

}
