package site.liuqq.freedom_chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import site.liuqq.freedom_chat.mapper.ExpressionMapper;
import site.liuqq.freedom_chat.pojo.Expression;
import xyz677123.freedomchat.common.pojo.Result;
import site.liuqq.freedom_chat.service.ExpressionService;

import java.io.InputStream;
import java.util.Base64;
import java.util.List;

@Service
public class ExpressionServiceImpl implements ExpressionService {
    @Autowired
    ExpressionMapper expressionMapper;
    @Autowired
    ResourceLoader resourceLoader;
    @Override
    public Result query(int id) {
        List<Expression> query = expressionMapper.query(id);

        try {
            for (Expression expression : query) {
                //转成base64编码
                Resource resource = resourceLoader.getResource("classpath:static" + expression.getPath());
                InputStream inputStream = resource.getInputStream();
                // 读取文件内容
                byte[] bytes = inputStream.readAllBytes();
                String s = Base64.getEncoder().encodeToString(bytes);

                expression.setPath("data:image;base64,"+s);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }



        

        return Result.success(query);
    }
}
