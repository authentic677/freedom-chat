package site.liuqq.freedom_chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.liuqq.freedom_chat.conf.CustomConfig;
import site.liuqq.freedom_chat.mapper.GroupMapper;
import site.liuqq.freedom_chat.pojo.group.Group;
import xyz677123.freedomchat.common.pojo.Result;
import site.liuqq.freedom_chat.service.GroupService;

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

}
