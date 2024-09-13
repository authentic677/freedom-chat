package xyz677123.freedomchat.zone.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.common.util.Tools;
import xyz677123.freedomchat.zone.pojo.Like;
import xyz677123.freedomchat.zone.service.LikeService;

@RestController
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/like/{postId}")
    public Result like(@PathVariable Integer postId) {

        String uid=(String) StpUtil.getLoginId();

        //如果没点赞则创建，否则删除

        Like one = likeService.lambdaQuery()
                .eq(Like::getPostId, postId)
                .eq(Like::getUid, uid)
                .one();
        if (one==null){
            //新增
            Like like = Like.builder()
                    .postId(postId)
                    .time(Tools.now("Asia/Shanghai"))
                    .uid(uid)
                    .build();
            likeService.save(like);
        }else {
            //删除
            likeService.removeById(one.getId());
        }

        return Result.success();
    }
}
