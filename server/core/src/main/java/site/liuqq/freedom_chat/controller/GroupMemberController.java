package site.liuqq.freedom_chat.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.pojo.group.Group;
import site.liuqq.freedom_chat.pojo.group.GroupMember;
import site.liuqq.freedom_chat.service.impl.GroupMemberServiceImpl;
import site.liuqq.freedom_chat.service.impl.GroupServiceImpl;
import site.liuqq.freedom_chat.service.impl.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GroupMemberController {

    @Autowired
    GroupMemberServiceImpl groupMemberService;
    @Autowired
    private GroupServiceImpl groupServiceImpl;
    @Autowired
    private GroupMemberServiceImpl groupMemberServiceImpl;
    @Autowired
    private UserServiceImpl userServiceImpl;

    //获取用户拥有的群聊
    @GetMapping("/group")
    Result group(HttpSession session){

        //获取我方uid
        String uid=((User)session.getAttribute("user")).getUid();

        List<GroupMember> list = groupMemberService
                .lambdaQuery()
                .eq(GroupMember::getMemberUid, uid)
                .list();

        list.forEach(e->{
            Group one = groupServiceImpl.lambdaQuery().eq(Group::getGid, e.getGid()).one();
            e.setGroup(one);
        });

        return Result.success(list);
    }

    //获取用户指定的群聊信息
    @GetMapping("/group/{gid}")
    Result specifyGroup(@PathVariable String gid, HttpSession session){

        //获取我方uid
        String uid=((User)session.getAttribute("user")).getUid();

        GroupMember groupMember = groupMemberService
                .lambdaQuery()
                .eq(GroupMember::getMemberUid, uid)
                .eq(GroupMember::getGid, gid)
                .one();
        //处理外键
        Group one = groupServiceImpl.lambdaQuery().eq(Group::getGid, groupMember.getGid()).one();
        //设置群成员
        List<GroupMember> list = groupMemberServiceImpl
                .lambdaQuery()
                .eq(GroupMember::getGid, one.getGid())
                .list();
        List<User> list2=list.stream().map(e-> {
            String memberUid = e.getMemberUid();
            return userServiceImpl.lambdaQuery().eq(User::getUid, memberUid).one();
        }).toList();
        one.setMembers(list2);
        //获取群人数
        one.setNumber(list.size());

        groupMember.setGroup(one);

        return Result.success(groupMember);
    }

    //某用户修改对某群聊的信息
    @PutMapping("/group")
    Result update(@RequestBody GroupMember groupMember, HttpSession session){

        //获取我方uid
        String uid=((User)session.getAttribute("user")).getUid();

        LambdaUpdateWrapper<GroupMember> wrapper = new LambdaUpdateWrapper<>(GroupMember.class);
        //更新字段
        if(groupMember.getNote()!=null&& !groupMember.getNote().isEmpty()){
            wrapper.set(GroupMember::getNote, groupMember.getNote());
        }
        if(groupMember.getNickname()!=null&& !groupMember.getNickname().isEmpty()){
            wrapper.set(GroupMember::getNickname, groupMember.getNickname());
        }
        //条件
        wrapper.eq(GroupMember::getMemberUid, uid);
        wrapper.eq(GroupMember::getGid, groupMember.getGid());
        //更新
        groupMemberServiceImpl.update(wrapper);

        return Result.success();
    }

}
