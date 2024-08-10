package site.liuqq.freedom_chat.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.pojo.group.Group;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.User;
import site.liuqq.freedom_chat.pojo.group.GroupMember;
import site.liuqq.freedom_chat.service.GroupService;
import site.liuqq.freedom_chat.service.impl.GroupServiceImpl;

@RestController
@RequestMapping("/api")
public class GroupController {
    @Autowired
    GroupService groupService;
    @Autowired
    private GroupServiceImpl groupServiceImpl;

    //根据关键字(群名，群号)搜索群的接口
    @GetMapping("/groups")
    Result searchGroups(String keyword, HttpSession session){
        return groupService.searchGroups(keyword);
    }

    //创建群聊
    @PostMapping("/group")
    Result addGroup(@RequestBody Group group, HttpSession session){

        if (group.getName()==null|| group.getName().isEmpty()){
            return Result.error("群名不能为空");
        }

        //获取我方uid
        String uid=((User)session.getAttribute("user")).getUid();
        group.setCreatorUid(uid);

        return groupServiceImpl.insert(group);
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
}
