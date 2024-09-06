package site.liuqq.freedom_chat.controller;

import jakarta.servlet.http.HttpSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.conf.CustomConfig;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.pojo.group.GroupMember;
import site.liuqq.freedom_chat.pojo.group.GroupMessage;
import site.liuqq.freedom_chat.pojo.group.GroupMessageNotice;
import site.liuqq.freedom_chat.service.impl.GroupMemberServiceImpl;
import site.liuqq.freedom_chat.service.impl.GroupMessageNoticeServiceImpl;
import site.liuqq.freedom_chat.service.impl.GroupMessageServiceImpl;
import site.liuqq.freedom_chat.service.impl.UserServiceImpl;
import site.liuqq.freedom_chat.common.DataUpdateNotify;
import site.liuqq.freedom_chat.common.Tools;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GroupMessageController {

    private final GroupMessageServiceImpl groupMessageServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final CustomConfig customConfig;
    private final GroupMemberServiceImpl groupMemberServiceImpl;
    private final GroupMessageNoticeServiceImpl groupMessageNoticeServiceImpl;
    private final SqlSessionTemplate sqlSessionTemplate;

    public GroupMessageController(GroupMessageServiceImpl groupMessageServiceImpl, UserServiceImpl userServiceImpl, CustomConfig customConfig, GroupMemberServiceImpl groupMemberServiceImpl, GroupMessageNoticeServiceImpl groupMessageNoticeServiceImpl, SqlSessionTemplate sqlSessionTemplate) {
        this.groupMessageServiceImpl = groupMessageServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.customConfig = customConfig;
        this.groupMemberServiceImpl = groupMemberServiceImpl;
        this.groupMessageNoticeServiceImpl = groupMessageNoticeServiceImpl;
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    //创建一条群聊消息
    @PostMapping("/groupMessage")
    @Transactional
    Result insert(@RequestBody GroupMessage groupMessage, HttpSession session){

        String uid = ((User) session.getAttribute("user")).getUid();

        //前端填充了gid，type，和content，不做鉴权

        groupMessage.setMemberUid(uid);
        groupMessage.setTime(Tools.now(customConfig.getZone()));

        groupMessageServiceImpl.save(groupMessage);

        //获取此组成员
        List<GroupMember> list = groupMemberServiceImpl
                .lambdaQuery()
                .eq(GroupMember::getGid, groupMessage.getGid())
                .list();
        //groupMessageNotice表呢，没有则新增，存在则更新
        for (GroupMember groupMember : list) {
            String memberUid = groupMember.getMemberUid();
            //判断此人的组消息通知是否存在
            GroupMessageNotice one = groupMessageNoticeServiceImpl
                    .lambdaQuery()
                    .eq(GroupMessageNotice::getUid, memberUid)
                    .eq(GroupMessageNotice::getGid,groupMessage.getGid())
                    .one();
            if (one!=null){ //存在
                groupMessageNoticeServiceImpl
                        .lambdaUpdate()
                        .eq(GroupMessageNotice::getUid, one.getUid())
                        .eq(GroupMessageNotice::getUid, memberUid)
                        .set(GroupMessageNotice::getMessageId,groupMessage.getId())
                        .set(GroupMessageNotice::getCount,one.getCount()+1)
                        .update();
            }else{
                GroupMessageNotice groupMessageNotice = new GroupMessageNotice();
                groupMessageNotice.setUid(memberUid);
                groupMessageNotice.setGid(groupMessage.getGid());
                groupMessageNotice.setMessageId(groupMessage.getId());
                groupMessageNotice.setCount(1);

                groupMessageNoticeServiceImpl.save(groupMessageNotice);
            }
        }


        //数据更新通知
        DataUpdateNotify.groupMessageNotify(list.stream().map(e->e.getMemberUid()).toList());
        DataUpdateNotify.groupMessageNoticeNotify(list.stream().map(e->e.getMemberUid()).toList());

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
