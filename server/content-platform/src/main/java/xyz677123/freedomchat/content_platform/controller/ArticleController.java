package xyz677123.freedomchat.content_platform.controller;

import cn.dev33.satoken.stp.StpUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.common.util.Tools;
import xyz677123.freedomchat.content_platform.pojo.Article;
import xyz677123.freedomchat.content_platform.pojo.Gongzhonghao;
import xyz677123.freedomchat.content_platform.service.ArticleService;
import xyz677123.freedomchat.content_platform.service.GongzhonghaoService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;
    @Autowired
    MinioClient minioClient;
    @Autowired
    private GongzhonghaoService gongzhonghaoService;

    @GetMapping("/articles")
    public Result getArticles() {

        String uid=(String) StpUtil.getLoginId();

        //结果集
        List<Article> result=new ArrayList<>();

        //获取用户下面的公众号
        List<Gongzhonghao> gongzhonghaos = gongzhonghaoService.lambdaQuery()
                .eq(Gongzhonghao::getUid, uid)
                .list();
        //遍历公众号以获取文章
        gongzhonghaos.forEach(e->{
            List<Article> articles = articleService.lambdaQuery()
                    .eq(Article::getGongzhonghaoId, e.getId())
                    .list();
            result.addAll(articles);
        });

        return Result.success(result);
    }

    @PostMapping("/article")
    public Result addArticle(@RequestBody Article article) {

        String uid=(String) StpUtil.getLoginId();

        article.setView(0L);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdateAt(LocalDateTime.now());

        articleService.save(article);

        return Result.success(article);
    }

    @PutMapping("/article/{id}/cover")
    @Transactional
    public Result updateArticle(@PathVariable Integer id,@RequestParam MultipartFile file) throws Exception{

        String uid=(String) StpUtil.getLoginId();

        //如果原先有则要删除
        Article article = articleService.getById(id);
        if (article.getCover()!=null){
            minioClient.removeObject(
                    RemoveObjectArgs
                            .builder()
                            .bucket("content-platform")
                            .object(article.getCover().replace("/content-platform/",""))
                            .build()
            );
        }

        String objectName = Tools.getCurrentYearMonth()+"/"+file.getContentType().replace("/","-")+"/"+ UUID.randomUUID()+"/" +file.getOriginalFilename();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket("content-platform")
                .object(objectName)
                .stream(file.getInputStream(),file.getSize(),-1)
                .build()
        );

        boolean update = articleService.lambdaUpdate()
                .eq(Article::getId, id)
                .set(Article::getCover, "/content-platform/" + objectName)
                .update();

        return Result.success();
    }

    @PutMapping("/article")
    public Result updateArticle(@RequestBody Article article) {

        String uid=(String) StpUtil.getLoginId();

        return Result.success();
    }

    @DeleteMapping("/article/{id}")
    @Transactional
    public Result deleteArticle(@PathVariable Integer id) throws Exception{

        String uid=(String) StpUtil.getLoginId();

        Article article = articleService.getById(id);
        //minio上面的文章的封面删了
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket("content-platform")
                .object(article.getCover().replace("/content-platform/",""))
                .build());

        articleService.removeById(id);

        return Result.success();
    }

}
