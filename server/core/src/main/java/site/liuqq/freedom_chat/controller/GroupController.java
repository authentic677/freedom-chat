package site.liuqq.freedom_chat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.conf.CustomConfig;
import site.liuqq.freedom_chat.pojo.group.*;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.service.GroupService;
import site.liuqq.freedom_chat.service.impl.*;
import site.liuqq.freedom_chat.utils.Tools;

@RestController
@RequestMapping("/api")
public class GroupController {
    @Autowired
    GroupService groupService;
    @Autowired
    private GroupServiceImpl groupServiceImpl;
    @Autowired
    private CustomConfig customConfig;
    @Autowired
    private GroupMemberServiceImpl groupMemberServiceImpl;
    @Autowired
    private GroupMessageServiceImpl groupMessageServiceImpl;
    @Autowired
    private GroupMessageNoticeServiceImpl groupMessageNoticeServiceImpl;
    @Autowired
    private GroupApplicantServiceImpl groupApplicantServiceImpl;

    //根据关键字(群名，群号)搜索群的接口
    @GetMapping("/groups")
    Result searchGroups(String keyword, HttpSession session){
        return groupService.searchGroups(keyword);
    }

    //创建群聊
    @PostMapping("/group")
    @Transactional
    Result addGroup(@RequestBody Group group, HttpSession session){

        if (group.getName()==null|| group.getName().isEmpty()){
            return Result.error("群名不能为空");
        }

        //获取我方uid
        String uid=((User)session.getAttribute("user")).getUid();
        group.setCreatorUid(uid);
        group.setGid(Tools.generateRandomNumberString(10));
        group.setAvatar("/avatar/0-120x120.jpg");
        group.setTime(Tools.now(customConfig.getZone()));

        //mybatis-plus
        groupServiceImpl.save(group);
        //还有一张表呢
        GroupMember groupMember = new GroupMember();
        groupMember.setGid(group.getGid());
        groupMember.setMemberUid(group.getCreatorUid());
        groupMember.setJoinTime(group.getTime());
        groupMember.setRole("owner");
        groupMember.setSpeakTime(group.getTime());

        groupMemberServiceImpl.save(groupMember);

        //添加一条创建群成功的系统消息呢
        GroupMessage message = new GroupMessage();
        message.setGid(group.getGid());
        message.setMemberUid(group.getGid());
        message.setTime(group.getTime());
        message.setType("system");
        message.setContent("群聊创建成功");

        groupMessageServiceImpl.save(message);
        //群消息通知也要新增和修改呢
        GroupMessageNotice groupMessageNotice = new GroupMessageNotice();
        groupMessageNotice.setUid(uid);
        groupMessageNotice.setGid(group.getGid());
        groupMessageNotice.setMessageId(message.getId());
        groupMessageNotice.setCount(1);

        groupMessageNoticeServiceImpl.save(groupMessageNotice);

        return Result.success();
    }

    //群管理者修改群相关信息
    @PutMapping("/groupAdmin")
    Result update(@RequestBody Group group, HttpSession session){

        //获取我方uid
        String uid=((User)session.getAttribute("user")).getUid();

        //修改之前要做权限判定的，这里省略

        LambdaUpdateWrapper<Group> wrapper = new LambdaUpdateWrapper<>();
        //更新字段，注意了如果下面的条件没有一个满足的，那么生成的sql会出现语法错误，导致sql异常
        if(group.getName()!=null&& !group.getName().isEmpty()){
            wrapper.set(Group::getName, group.getName());
        }
        if(group.getDescription()!=null&& !group.getDescription().isEmpty()){
            wrapper.set(Group::getDescription, group.getDescription());
        }
        //条件
        wrapper.eq(Group::getGid, group.getGid());
        //更新
        groupServiceImpl.update(wrapper);

        return Result.success();
    }

    //删除群聊
    @DeleteMapping("/group/{gid}")
    @Transactional
    Result delete(@PathVariable String gid, HttpSession session){
        String uid=((User)session.getAttribute("user")).getUid();

        //鉴权，略

        //操作5张表
        {
            LambdaQueryWrapper<GroupMessageNotice> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GroupMessageNotice::getGid,gid);
            groupMessageNoticeServiceImpl.remove(wrapper);
        }
        {
            LambdaQueryWrapper<GroupMessage> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GroupMessage::getGid,gid);
            groupMessageServiceImpl.remove(wrapper);
        }
        {
            LambdaQueryWrapper<GroupMember> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GroupMember::getGid,gid);
            groupMemberServiceImpl.remove(wrapper);
        }
        {
            LambdaQueryWrapper<GroupApplicant> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GroupApplicant::getGid,gid);
            groupApplicantServiceImpl.remove(wrapper);
        }
        {
            LambdaQueryWrapper<Group> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Group::getGid,gid);
            groupServiceImpl.remove(wrapper);
        }


        return Result.success();
    }
}
