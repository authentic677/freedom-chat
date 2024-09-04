package site.liuqq.freedom_chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import site.liuqq.freedom_chat.pojo.ExprList;

import java.util.List;

@Mapper
public interface ExprListMapper {

    @Select("select * from expr_list where uid=#{uid} or flag=1")
    List<ExprList> queryByUid(String uid);

}
