package site.liuqq.freedom_chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.liuqq.freedom_chat.mapper.ExprListMapper;
import site.liuqq.freedom_chat.pojo.ExprList;
import site.liuqq.freedom_chat.common.Result;
import site.liuqq.freedom_chat.service.ExprListService;

import java.util.List;

@Service
public class ExprListImpl implements ExprListService {

    @Autowired
    ExprListMapper exprListMapper;

    @Override
    public Result queryByUid(String uid) {
        List<ExprList> exprLists = exprListMapper.queryByUid(uid);

        return Result.success(exprLists);
    }
}
