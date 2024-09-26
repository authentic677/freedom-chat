package xyz677123.freedomchat.content_platform.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.content_platform.pojo.Article;
import xyz677123.freedomchat.content_platform.service.ArticleService;

import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @GetMapping("/articles")
    public Result getArticles() {

        String uid=(String) StpUtil.getLoginId();

        return Result.success();
    }

    @PostMapping("/article")
    public Result addArticle(@RequestBody Article article) {

        String uid=(String) StpUtil.getLoginId();

        return Result.success();
    }

    @PutMapping("/article")
    public Result updateArticle(@RequestBody Article article) {

        String uid=(String) StpUtil.getLoginId();

        return Result.success();
    }

    @DeleteMapping("/article/{id}")
    public Result deleteArticle(@PathVariable Integer id) {

        String uid=(String) StpUtil.getLoginId();

        articleService.removeById(id);

        return Result.success();
    }

}
