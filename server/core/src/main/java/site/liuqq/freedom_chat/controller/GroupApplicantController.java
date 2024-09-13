package site.liuqq.freedom_chat.controller;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.liuqq.freedom_chat.conf.CustomConfig;
import xyz677123.freedomchat.common.pojo.Result;
import xyz677123.freedomchat.common.pojo.User;
import site.liuqq.freedom_chat.pojo.group.Group;
import site.liuqq.freedom_chat.pojo.group.GroupApplicant;
import site.liuqq.freedom_chat.pojo.group.GroupMember;
import site.liuqq.freedom_chat.service.impl.GroupApplicantServiceImpl;
import site.liuqq.freedom_chat.service.impl.GroupMemberServiceImpl;
import site.liuqq.freedom_chat.service.impl.GroupServiceImpl;
import site.liuqq.freedom_chat.service.impl.UserServiceImpl;
import xyz677123.freedomchat.common.util.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GroupApplicantController {

    @Autowired
    private CustomConfig customConfig;
    @Autowired
    private GroupApplicantServiceImpl groupApplicantServiceImpl;
    @Autowired
    private GroupMemberServiceImpl groupMemberServiceImpl;
    @Autowired
    private GroupServiceImpl groupServiceImpl;
    @Autowired
    private UserServiceImpl userServiceImpl;


    //查询已经发出了向哪些群的申请
    @GetMapping("/groupApplicants/applicantSide")
    public Result getGroupApplicants(HttpSession session) {

        String uid=(String) StpUtil.getLoginId();

        List<GroupApplicant> list = groupApplicantServiceImpl
                .lambdaQuery()
                .eq(GroupApplicant::getApplicantUid, uid)
                .list();
        //解决一下外键
        list.forEach(e->{
            Group group = groupServiceImpl
                    .lambdaQuery()
                    .eq(Group::getGid, e.getGid())
                    .one();
            e.setGroup(group);
        });

        return Result.success(list);
    }

    //查询被管理的群有哪些申请者
    @GetMapping("/groupApplicants/adminSide")
    public Result getGroupApplicants2(HttpSession session) {

        String uid=(String) StpUtil.getLoginId();

        //查询这个用户具有管理权限的群聊
        List<GroupMember> list = groupMemberServiceImpl
                .lambdaQuery()
                .eq(GroupMember::getMemberUid, uid)
                .and(wrapper->wrapper
                        .eq(GroupMember::getRole,"owner")
                        .or()
                        .eq(GroupMember::getRole,"admin"))
                .list();
        if(list.size()==0){
            return Result.success(new ArrayList<>());
        }
        //再查询这些群有哪些申请者
        List<String> gids=list.stream().map(GroupMember::getGid).collect(Collectors.toList());
        List<GroupApplicant> list1 = groupApplicantServiceImpl
                .lambdaQuery()
                .in(GroupApplicant::getGid, gids)
                .list();
        //处理一下外键
        list1.forEach(e->{
            Group group = groupServiceImpl
                    .lambdaQuery()
                    .eq(Group::getGid, e.getGid())
                    .one();
            e.setGroup(group);
            User user = userServiceImpl
                    .lambdaQuery()
                    .eq(User::getUid, e.getApplicantUid())
                    .one();
            e.setUser(user);
        });

        return Result.success(list1);
    }

    //申请加群
    @PostMapping("/groupApplicant")
    public Result groupApplicant(@RequestBody GroupApplicant groupApplicant, HttpSession session) {

        String uid=(String) StpUtil.getLoginId();

        groupApplicant.setApplicantUid(uid);
        groupApplicant.setApplyTime(Tools.now(customConfig.getZone()));

        //首先应该判断这个人是否已经是该群成员，如果是则报错
        GroupMember one1 = groupMemberServiceImpl
                .lambdaQuery()
                .eq(GroupMember::getGid, groupApplicant.getGid())
                .eq(GroupMember::getMemberUid, groupApplicant.getApplicantUid())
                .one();
        if (one1 != null) {
            return Result.error("你已经加入该群了");
        }

        //存在即更新不存在则插入
        //1.先看是否存在，查询一下就知道
        GroupApplicant one = groupApplicantServiceImpl
                .lambdaQuery()
                .eq(GroupApplicant::getGid, groupApplicant.getGid())
                .eq(GroupApplicant::getApplicantUid, groupApplicant.getApplicantUid())
                .one();
        if (one!=null){ //存在即更新
            System.out.println("更新");
            boolean update = groupApplicantServiceImpl
                    .lambdaUpdate()
                    .eq(GroupApplicant::getGid, groupApplicant.getGid())
                    .eq(GroupApplicant::getApplicantUid, groupApplicant.getApplicantUid())
                    .set(GroupApplicant::getMessage, groupApplicant.getMessage())
                    .set(GroupApplicant::getApplyTime, groupApplicant.getApplyTime())
                    .set(GroupApplicant::getStatus,"pending")
                    .update();
            if (update){
                return Result.success();
            }else {
                return Result.error("处理失败");
            }
        }else { //不存在即插入
            System.out.println("插入");
            groupApplicantServiceImpl.save(groupApplicant);
        }

        return Result.success();
    }

    //删除加群申请
    @DeleteMapping("/groupApplicant/{id}")
    public Result delete(@PathVariable Integer id, HttpSession session){

        String uid=(String) StpUtil.getLoginId();

        groupApplicantServiceImpl.removeById(id);

        return Result.success();
    }

    //同意加群申请
    @PutMapping("/groupApplicant/agree/{id}")
    public Result agree(@PathVariable Integer id, HttpSession session){

        String uid=(String) StpUtil.getLoginId();

        //其实需要验证用户是否有这个权限的，这里先暂时省略

        boolean update = groupApplicantServiceImpl
                .lambdaUpdate()
                .eq(GroupApplicant::getId, id)
                .set(GroupApplicant::getStatus, "approved")
                .update();
        //群成员表插入记录
        //先获取此条申请信息
        GroupApplicant groupApplicant = groupApplicantServiceImpl.getById(id);
        //插入
        GroupMember groupMember = new GroupMember();
        groupMember.setGid(groupApplicant.getGid());
        groupMember.setMemberUid(groupApplicant.getApplicantUid());
        groupMember.setJoinTime(Tools.now(customConfig.getZone()));
        groupMember.setRole("ordinary");
        groupMember.setSpeakTime(groupMember.getJoinTime());
        groupMemberServiceImpl.save(groupMember);

        return Result.success();
    }

    //拒绝加群申请
    @PutMapping("/groupApplicant/refuse/{id}")
    public Result refuse(@PathVariable Integer id, HttpSession session){

        String uid=(String) StpUtil.getLoginId();

        //其实需要验证用户是否有这个权限的，这里先暂时省略

        boolean update = groupApplicantServiceImpl
                .lambdaUpdate()
                .eq(GroupApplicant::getId, id)
                .set(GroupApplicant::getStatus, "rejected")
                .update();


        return Result.success();
    }
}