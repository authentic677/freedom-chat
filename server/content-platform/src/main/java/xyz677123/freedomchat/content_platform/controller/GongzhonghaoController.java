package xyz677123.freedomchat.content_platform.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.common.util.Tools;
import xyz677123.freedomchat.content_platform.pojo.Gongzhonghao;
import xyz677123.freedomchat.content_platform.service.GongzhonghaoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class GongzhonghaoController {

    @Autowired
    GongzhonghaoService gongzhonghaoService;
    @Autowired
    MinioClient minioClient;

    @GetMapping("/gongzhonghaos")
    public Result gongzhonghaos() {

        String uid=(String) StpUtil.getLoginId();

        List<Gongzhonghao> list = gongzhonghaoService.lambdaQuery()
                .eq(Gongzhonghao::getUid, uid)
                .list();


        return Result.success(list);
    }

    @PostMapping("/gongzhonghao")
    @Transactional
    public Result postGongzhonghao(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam MultipartFile avatar
    ) throws Exception{

        String uid=(String) StpUtil.getLoginId();

        //把公众号头像保存到minio上
        String objectName = Tools.getCurrentYearMonth()+"/"+avatar.getContentType().replace("/","-")+"/"+ UUID.randomUUID()+"/" +name;
        minioClient.putObject(PutObjectArgs.builder()
                .bucket("content-platform")
                .object(objectName)
                .stream(avatar.getInputStream(),avatar.getSize(),-1)
                .build()
        );

        Gongzhonghao gongzhonghao = Gongzhonghao.builder()
                .uid(uid)
                .name(name)
                .description(description)
                .avatar("/content-platform/" + objectName)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        gongzhonghaoService.save(gongzhonghao);

        return Result.success();
    }

    @PutMapping("/gongzhonghao")
    @Transactional
    public Result gongzhonghao(
            @RequestParam Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile avatar
    ) throws Exception {

        String uid=(String) StpUtil.getLoginId();

        LambdaUpdateWrapper<Gongzhonghao> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Gongzhonghao::getId,id);
        if (name!=null){
            wrapper.set(Gongzhonghao::getName,name);
        }
        if (description!=null){
            wrapper.set(Gongzhonghao::getDescription,description);
        }
        if (avatar!=null){
            Gongzhonghao gongzhonghao = gongzhonghaoService.getById(id);
            //minio上的图片删了重新上传
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket("content-platform")
                    .object(gongzhonghao.getAvatar().replace("/content-platform/",""))
                    .build());
            //重新上传
            String objectName = Tools.getCurrentYearMonth()+"/"+avatar.getContentType().replace("/","-")+"/"+ UUID.randomUUID()+"/" +name;
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket("content-platform")
                    .object(objectName)
                    .stream(avatar.getInputStream(),avatar.getSize(),-1)
                    .build()
            );
            wrapper.set(Gongzhonghao::getAvatar,"/content-platform/" + objectName);
        }

        wrapper.set(Gongzhonghao::getUpdateAt,LocalDateTime.now());

        gongzhonghaoService.update(wrapper);

        return Result.success();
    }

    @DeleteMapping("/gongzhonghao/{id}")
    @Transactional
    public Result deleteGongzhonghao(@PathVariable Integer id) throws Exception {

        String uid=(String) StpUtil.getLoginId();

        Gongzhonghao gongzhonghao = gongzhonghaoService.getById(id);
        //minio上的图片也要删
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket("content-platform")
                .object(gongzhonghao.getAvatar().replace("/content-platform/",""))
                .build());

        gongzhonghaoService.removeById(id);

        return Result.success();
    }

}
