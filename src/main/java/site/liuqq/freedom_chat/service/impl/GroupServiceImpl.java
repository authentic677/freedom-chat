package site.liuqq.freedom_chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.liuqq.freedom_chat.mapper.GroupMapper;
import site.liuqq.freedom_chat.pojo.Group;
import site.liuqq.freedom_chat.pojo.Result;
import site.liuqq.freedom_chat.service.GroupService;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupMapper groupMapper;

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
