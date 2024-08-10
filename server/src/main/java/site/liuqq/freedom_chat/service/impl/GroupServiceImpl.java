package site.liuqq.freedom_chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.liuqq.freedom_chat.conf.CustomConfig;
import site.liuqq.freedom_chat.mapper.GroupMapper;
import site.liuqq.freedom_chat.pojo.group.Group;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.pojo.group.GroupMember;
import site.liuqq.freedom_chat.service.GroupService;
import site.liuqq.freedom_chat.utils.Tools;

import java.util.List;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper,Group> implements GroupService {

    @Autowired
    GroupMapper groupMapper;
    @Autowired
    private CustomConfig customConfig;
    @Autowired
    private GroupMemberServiceImpl groupMemberServiceImpl;

    @Override
    public Result searchGroups(String keyword) {

        if(keyword!=null&&!keyword.equals("")){
            System.out.println(keyword);
            List<Group> groups = groupMapper.searchGroups(keyword);
            return Result.success(groups);

        }else{
            return Result.error("不正确的搜索参数");
        }
    }

    //新增群
    public Result insert(Group group){

        group.setGid(Tools.generateRandomNumberString(10));
        group.setAvatar("/avatar/0-120x120.jpg");
        group.setTime(Tools.now(customConfig.getZone()));

        //mybatis-plus
        save(group);
        //还有一张表呢
        GroupMember groupMember = new GroupMember();
        groupMember.setGid(group.getGid());
        groupMember.setMemberUid(group.getCreatorUid());
        groupMember.setJoinTime(group.getTime());
        groupMember.setRole("owner");
        groupMember.setSpeakTime(group.getTime());

        groupMemberServiceImpl.save(groupMember);

        return Result.success();
    }
}
