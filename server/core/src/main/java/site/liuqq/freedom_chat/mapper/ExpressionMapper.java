package site.liuqq.freedom_chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import site.liuqq.freedom_chat.pojo.Expression;

import java.util.List;

@Mapper
public interface ExpressionMapper {

    @Select("select e.id,e.path,e.description from expr_list_and_expr el " +
            "join expression e on e.id=el.expression_id " +
            "where el.expr_list_id=#{id} ")
    List<Expression> query(int id);
}
