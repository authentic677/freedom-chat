package xyz677123.freedomchat.zone.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.zone.mapper.AlbumMapper;
import xyz677123.freedomchat.zone.pojo.Album;
import xyz677123.freedomchat.zone.pojo.Photo;
import xyz677123.freedomchat.zone.service.AlbumService;
import xyz677123.freedomchat.zone.service.PhotoService;

import java.util.List;

@RestController
public class AlbumController {

    @Autowired
    AlbumService albumService;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private AlbumMapper albumMapper;

    @GetMapping("/albums")
    public Result albums() {

        String uid=(String) StpUtil.getLoginId();

        List<Album> list = albumService.lambdaQuery()
                .eq(Album::getUid, uid)
                .list();
        //处理外键
        list.forEach(e->{
            Photo photo = photoService.getById(e.getCoverPhotoId());
            e.setCoverPhoto(photo);

            //设置数量
            long count = photoService.count(new LambdaQueryWrapper<Photo>().eq(Photo::getAlbumId, e.getId()));
            e.setPhotoCount((int)count);
        });


        return Result.success(list);
    }

    @GetMapping("/album/{id}")
    public Result album(@PathVariable String id) {

        String uid=(String) StpUtil.getLoginId();

        Album album = albumService.getById(id);
        //处理外键
        Photo photo = photoService.getById(album.getCoverPhotoId());
        album.setCoverPhoto(photo);
        //设置数量
        long count = photoService.count(new LambdaQueryWrapper<Photo>().eq(Photo::getAlbumId, album.getId()));
        album.setPhotoCount((int)count);

        return Result.success(album);
    }

    @PostMapping("/album")
    public Result addAlbum(@RequestBody Album album) {

        String uid=(String) StpUtil.getLoginId();

        album.setUid(uid);
        albumService.save(album);

        return Result.success();
    }

    @PutMapping("/album")
    @Transactional
    public Result updateAlbum(@RequestBody Album album) {

        String uid=(String) StpUtil.getLoginId();

        if (album.getCoverPhotoId()!=null){
            boolean update = albumService.lambdaUpdate()
                    .set(Album::getCoverPhotoId, album.getCoverPhotoId())
                    .eq(Album::getId, album.getId())
                    .update();
        }

        return Result.success();
    }

    @DeleteMapping("/album/{id}")
    public Result deleteAlbum(@PathVariable int id) {

        String uid=(String) StpUtil.getLoginId();

        albumService.removeById(id);

        return Result.success();
    }
}
