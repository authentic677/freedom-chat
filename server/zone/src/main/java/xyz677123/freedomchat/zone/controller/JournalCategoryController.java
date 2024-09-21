package xyz677123.freedomchat.zone.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.zone.pojo.JournalCategory;
import xyz677123.freedomchat.zone.service.JournalCategoryService;

import java.time.LocalDateTime;
import java.util.List;

import static net.sf.jsqlparser.parser.feature.Feature.update;

@RestController
public class JournalCategoryController {

    @Autowired
    JournalCategoryService journalCategoryService;

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


        return Result.success(list);
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
