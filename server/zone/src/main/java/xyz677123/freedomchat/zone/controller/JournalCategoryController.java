package xyz677123.freedomchat.zone.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.zone.pojo.Journal;
import xyz677123.freedomchat.zone.pojo.JournalCategory;
import xyz677123.freedomchat.zone.service.JournalCategoryService;
import xyz677123.freedomchat.zone.service.JournalService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.sf.jsqlparser.parser.feature.Feature.update;

@RestController
public class JournalCategoryController {

    @Autowired
    JournalCategoryService journalCategoryService;
    @Autowired
    private JournalService journalService;

    //用户创建日志分类
    @PostMapping("/journalCategory")
    public Result createJournalCategory(@RequestParam String name) {

        String uid=(String) StpUtil.getLoginId();

        JournalCategory journalCategory = JournalCategory
                .builder()
                .uid(uid)
                .name(name)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        journalCategoryService.save(journalCategory);

        return Result.success();
    }

    //用户查询日志分类
    @GetMapping("/journalCategorys")
    public Result getJournalCategories() {

        String uid=(String) StpUtil.getLoginId();

        List<JournalCategory> list = journalCategoryService.lambdaQuery()
                .eq(JournalCategory::getUid, uid)
                .list();
        //除了分类的数据外，还要把各分类下有多少篇日志也要查出来
        list.forEach(e->{
            List<Journal> list1 = journalService.lambdaQuery()
                    .eq(Journal::getCategoryId, e.getId())
                    .eq(Journal::getIsDeleted,0)
                    .list();
            e.setJournalCount(list1.size());
        });

        return Result.success(list);
    }

    //获取用户默认分类和全部的日志数
    @GetMapping("journalCategory/count")
    public Result getJournalCategoryCount() {

        String uid=(String) StpUtil.getLoginId();

        Map<String,Object> map=new HashMap<>();

        List<Journal> list = journalService.lambdaQuery()
                .eq(Journal::getUid, uid)
                .eq(Journal::getIsDeleted,0)
                .list();
        long defaultCount = list.stream().filter(e ->e.getCategoryId() == 0).count();

        map.put("total",list.size());
        map.put("default", defaultCount);

        return Result.success(map);
    }

    //用户修改分类
    @PutMapping("/journalCategory")
    public Result updateJournalCategory(@RequestBody JournalCategory journalCategory) {

        String uid=(String) StpUtil.getLoginId();

        boolean update = journalCategoryService.lambdaUpdate()
                .set(JournalCategory::getName, journalCategory.getName())
                .set(JournalCategory::getUpdateAt, LocalDateTime.now())
                .eq(JournalCategory::getId, journalCategory.getId())
                .update();


        return update?Result.success():Result.error("更改失败");
    }

    //用户删除分类
    @DeleteMapping("/journalCategory/{id}")
    public Result deleteJournalCategory(@PathVariable Integer id) {

        String uid=(String) StpUtil.getLoginId();

        boolean remove= journalCategoryService.removeById(id);


        return remove?Result.success():Result.error("删除失败");
    }
}
