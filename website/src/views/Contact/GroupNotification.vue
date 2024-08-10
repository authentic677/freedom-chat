<script>
import AgreeOrRefuseBtn from "../../components/AgreeOrRefuseBtn.vue";
import {displayTime, emitter} from "../../utils/utils.js";
import config from "../../config/config.js";
import FriendOrGroupNotificationItem from "../../components/FriendOrGroupNotificationItem.vue";

export default {
    name: "GroupNotification",
    data(){
        return {
            list:{
                applicantSideList:[],
                adminSideList:[],
            },
            renderList:[],
        }
    },
    methods:{
        async getData(){
            //请求两次获取两个数据
            //第一个
            {
                let res=await fetch('/api/groupApplicants/applicantSide',{
                    headers:{
                        token:localStorage.getItem('token')
                    }
                })
                let json=await res.json()

                console.log(json);

                this.list.applicantSideList=json.data
            }
            //第二个
            {
                let res=await fetch('/api/groupApplicants/adminSide',{
                    headers:{
                        token:localStorage.getItem('token')
                    }
                })
                let json=await res.json()

                console.log(json);

                this.list.adminSideList=json.data
            }

        },
        async del(item){
            let res=await fetch(`/api/groupApplicant/${item.id}`,{
                method:'DELETE',
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            console.log(json)
        },
        async agree(item){
            let res=await fetch(`/api/groupApplicant/agree/${item.id}`,{
                method:'PUT',
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            console.log(json)
        },
        async refuse(item){
            let res=await fetch(`/api/groupApplicant/refuse/${item.id}`,{
                method:'PUT',
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            console.log(json)
        }
    },
    watch:{
        list:{
            handler(newVal,oldVal){
                console.log(newVal)
                this.renderList=[] //清空一下渲染列表，好像没必要？
                newVal.applicantSideList.forEach(e=>{
                    let statusShowType
                    switch (e.status){
                        case 'pending':statusShowType=3;break
                        case 'approved':statusShowType=1;break
                        case 'rejected':statusShowType=2;break
                    }
                    this.renderList.push({
                        id:e.id,

                        avatar:config.minioUrl+e.group.avatar,
                        name:e.group.name,
                        text:'正在验证你的申请',
                        time:e.applyTime,
                        message:e.message,
                        statusShowType
                    })
                })
                newVal.adminSideList.forEach(e=>{
                    let statusShowType
                    switch (e.status){
                        case 'pending':statusShowType=0;break
                        case 'approved':statusShowType=1;break
                        case 'rejected':statusShowType=2;break
                    }
                    this.renderList.push({
                        id:e.id,

                        avatar:config.minioUrl+e.user.avatar,
                        name:e.user.username,
                        text:`请求加入群聊 ${e.group.name}`,
                        time:e.applyTime,
                        message:e.message,
                        statusShowType
                    })
                })
            },
            deep:true
        }
    },
    computed:{
        config() {
            return config
        },
    },
    created(){
        this.getData()

        // emitter.on('contactNoticeUpdate',this.getData)

        // 阻止默认的右键弹出菜单
        // document.addEventListener("mousedown", this.myRightHandler);
    },
    beforeUnmount(){
        //解绑事件
        // document.removeEventListener("mousedown", this.myRightHandler);
        // emitter.off('contactNoticeUpdate',this.getData)
    },
    components: {FriendOrGroupNotificationItem, AgreeOrRefuseBtn}
}
</script>

<template>
<div class="groupNotification">
    <div class="title">群通知</div>
    <div class="content">

        <FriendOrGroupNotificationItem
            v-for="item in renderList" :key="item.id"
            :item="item"
            @del="del(item)"
            @agree="agree(item)"
            @refuse="refuse(item)"
        />

    </div>
</div>
</template>

<style scoped lang="less">
.groupNotification{
    height: 100%;
    box-sizing: border-box;
    background-color: hsl(0, 0%, 95%);

    display: flex;
    flex-direction: column;

    .title{
        font-size: 20px;
        padding: 42px 25px;
    }
    .content{
        flex-grow: 1;
        display: flex;
        flex-direction: column;

        align-items: center;
        overflow: auto;


    }
}
</style>