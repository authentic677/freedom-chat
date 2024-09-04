package site.liuqq.freedom_chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import site.liuqq.freedom_chat.pojo.group.Group;

import java.util.List;

@Mapper
public interface GroupMapper extends BaseMapper<Group> {

    //根据关键字(gid或name)搜索群
    //这里巨坑，groups说是关键字要加``否则提示你语法错误。。。
    @Select("select * from `groups` where gid like concat('%',#{keyword},'%') or name like concat('%',#{keyword},'%')")
    List<Group> searchGroups(String keyword);
}
