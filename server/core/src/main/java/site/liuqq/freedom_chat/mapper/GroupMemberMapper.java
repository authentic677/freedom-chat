package site.liuqq.freedom_chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import site.liuqq.freedom_chat.pojo.group.GroupMember;

@Mapper
public interface GroupMemberMapper extends BaseMapper<GroupMember> {
}
