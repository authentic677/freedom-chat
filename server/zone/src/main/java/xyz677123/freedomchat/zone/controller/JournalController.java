package xyz677123.freedomchat.zone.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.zone.pojo.Journal;
import xyz677123.freedomchat.zone.service.JournalService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class JournalController {

    @Autowired
    JournalService journalService;

    //查询多个日志
    @GetMapping("/journals")
    public Result journals(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Byte isPrivate,
            @RequestParam(required = false) Byte isDeleted,
            @RequestParam(required = false) Integer categoryId
            )
    {

        String uid=(String) StpUtil.getLoginId();

        LambdaQueryChainWrapper<Journal> wrapper = journalService.lambdaQuery().eq(Journal::getUid, uid);
        if (status!=null){
            wrapper.eq(Journal::getStatus, status);
        }
        if (isPrivate!=null){
            wrapper.eq(Journal::getIsPrivate, isPrivate);
        }
        if (isDeleted!=null){
            wrapper.eq(Journal::getIsDeleted, isDeleted);
        }
        if (categoryId!=null&&!categoryId.equals(-1)){
            wrapper.eq(Journal::getCategoryId, categoryId);
        }

        List<Journal> list = wrapper.list();

        return Result.success(list);
    }

    //查询单个日志
    @GetMapping("/journal/{id}")
    public Result journal(@PathVariable Integer id) {

        String uid=(String) StpUtil.getLoginId();

        Journal one = journalService.lambdaQuery()
                .eq(Journal::getId, id)
                .one();

        return Result.success(one);
    }

    //新增日志
    @PostMapping("/journal")
    public Result journal(@RequestBody Journal journal) {

        String uid=(String) StpUtil.getLoginId();

        journal.setUid(uid);
        journal.setIsDeleted((byte) 0);
        if (journal.getIsPinned()==1){
            journal.setPinnedAt(LocalDateTime.now());
        }
        journal.setCreatedAt(LocalDateTime.now());
        journal.setUpdateAt(LocalDateTime.now());

        journalService.save(journal);

        return Result.success();
    }

    //修改日志
    @PutMapping("/journal")
    public Result putJournal(@RequestBody Journal journal) {

        String uid=(String) StpUtil.getLoginId();

        if (journal.getCategoryId()!=null){
            boolean update = journalService.lambdaUpdate()
                    .eq(Journal::getId, journal.getId())
                    .set(Journal::getCategoryId, journal.getCategoryId())
                    .update();
        }

        return Result.success();
    }

    //删除日志
    @DeleteMapping("/journal/{id}")
    public Result deleteJournal(@PathVariable Integer id) {

        String uid=(String) StpUtil.getLoginId();

        //此处并不是真正的删除，而是逻辑删除哦
//        journalService.removeById(id);
        journalService.lambdaUpdate()
                .eq(Journal::getId, id)
                .set(Journal::getIsDeleted, (byte) 1)
                .set(Journal::getDeletedAt,LocalDateTime.now())
                .update();

        return Result.success();
    }
}
