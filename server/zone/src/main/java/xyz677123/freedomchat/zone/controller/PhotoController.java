package xyz677123.freedomchat.zone.controller;

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
import xyz677123.freedomchat.zone.pojo.Photo;
import xyz677123.freedomchat.zone.service.PhotoService;

import java.util.List;
import java.util.UUID;

@RestController
public class PhotoController {

    @Autowired
    PhotoService photoService;
    @Autowired
    MinioClient minioClient;

    @GetMapping("/photos")
    public Result getPhotos(@RequestParam Integer albumId) {

        String uid=(String) StpUtil.getLoginId();

        List<Photo> list = photoService.lambdaQuery()
                .eq(Photo::getAlbumId, albumId)
                .list();

        return Result.success(list);
    }

    @PostMapping("/photo")
    public Result addPhoto(
            @RequestParam Integer albumId,
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam MultipartFile file
    ) throws Exception{

        String uid=(String) StpUtil.getLoginId();

        //把图片保存到minio上
        String objectName = Tools.getCurrentYearMonth()+"/"+type.replace("/","-")+"/"+ UUID.randomUUID()+"/" +name;
        minioClient.putObject(PutObjectArgs.builder()
                .bucket("photo")
                .object(objectName)
                .stream(file.getInputStream(),file.getSize(),-1)
                .build()
        );

        Photo photo = Photo.builder().albumId(albumId).src("/photo/" + objectName).name(name).build();

        photoService.save(photo);

        return Result.success();
    }

    @PutMapping("/photo")
    public Result putPhoto(@RequestBody Photo photo) {


        String uid=(String) StpUtil.getLoginId();

        return Result.success();
    }

    @DeleteMapping("/photo/{id}")
    @Transactional
    public Result deletePhoto(@PathVariable int id) throws Exception{

        String uid=(String) StpUtil.getLoginId();

        Photo photo = photoService.getById(id);

        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket("photo")
                .object(photo.getSrc().replace("/photo/",""))
                .build());

        photoService.removeById(id);


        return Result.success();
    }

}
