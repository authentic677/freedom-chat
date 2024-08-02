<template>
    <div class="friendNotification">
        <div class="title">好友通知</div>
        <div class="content">

            <div class="item"
                 v-for="item in contactNotices2" :key="item.id"
                 @mousedown.stop="mousedown($event,item)"
                 @contextmenu.prevent
            >
                
                <div class="info">
                    <img :src="config.minioUrl+item.avatar" alt="">
                    <div class="detail">
                        <div class="name">
                            <span class="user">{{item.username}}</span>
                            &nbsp;
                            <span class="text"> {{item.text}} </span>
                            &nbsp;
                            <span class="time">{{displayTime(new Date(item.time))}}</span>
                        </div>
                        <div class="description" v-if="item.descriptionShow">留言：{{item.leaveMessage}}</div>
                    </div>
                </div>
                <div class="status">
                    <AgreeOrRefuseBtn v-if="item.statusShowType1" @agree="agree(item)" @refuse="refuse(item)" />
                    <div v-if="item.statusShowType2">已同意</div>
                    <div v-if="item.statusShowType3">已拒绝</div>
                    <div v-if="item.statusShowType4">等待验证</div>
                </div>
                
            </div>
            
            <div class="rightMenu" v-show="rightMenuShow" :style="{left:x,top:y}">
                <div class="del" @mousedown="del">删除</div>
            </div>
        </div>
    </div>
</template>

<script>
import AgreeOrRefuseBtn from "../../components/AgreeOrRefuseBtn.vue"
import {displayTime, emitter} from "../../utils/utils.js"
import config from "../../config/config.js";
export default {
    name:'FriendNotification',
    data(){
        return {
            contactNotices:[],

            rightMenuShow:false,//控制右键菜单是否显示
            x:0,
            y:0,
            rightClickItem:null
        }
    },
    methods:{
        displayTime,
        async agree(item){
            let res=await fetch('/api/contactNotice',{
                method:'PUT',
                headers:{
                    'Content-Type':'application/json;charset=utf-8',
                    token:localStorage.getItem('token')
                },
                body:JSON.stringify({
                    id:item.id,
                    uid2:item.uid2,
                    status:'agree'
                })
            })
            let json=await res.json()

            console.log(json);
        },
        async refuse(item){
            let res=await fetch('/api/contactNotice',{
                method:'PUT',
                headers:{
                    'Content-Type':'application/json;charset=utf-8',
                    token:localStorage.getItem('token')
                },
                body:JSON.stringify({
                    id:item.id,
                    uid2:item.uid2,
                    status:'refuse'
                })
            })
            let json=await res.json()

            console.log(json);
        },
        async getData(){
            let res=await fetch('/api/contactNotices',{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            console.log(json);

            this.contactNotices=json.data
        },
        async del(){
            console.log("删除按钮");
            console.log(this.rightClickItem);

            let res=await fetch('/api/contactNotice',{
                method:'DELETE',
                headers:{
                    'Content-Type':'application/json;charset=utf-8',
                    token:localStorage.getItem('token')
                },
                body:JSON.stringify({
                    id:this.rightClickItem.id,
                    uid2:this.rightClickItem.uid2
                })
            })
            let json=await res.json()

            console.log(json);
        },
        mousedown(e,item){
            if(e.button===2){
                // this.shouldShow=true
                this.rightMenuShow=true
                this.x=e.x+'px'
                this.y=e.y+'px'
                console.log(this.x,this.y);

                this.rightClickItem=item
            }
        },
        myRight(event){
            event.preventDefault();
        },
        myRightHandler(){
            this.rightMenuShow=false
        }
    },
    computed:{
        config() {
            return config
        },
        contactNotices2(){
            return this.contactNotices.map(e=>{
                
                if(e.flag==0){

                    e.descriptionShow=true
                    e.text='请求加为好友'

                    if(e.status=='pending'){
                        e.statusShowType1=true
                    }else if(e.status=='agree'){
                        e.statusShowType2=true
                    }else if(e.status=='refuse'){
                        e.statusShowType3=true
                    }
                    
                }else{
                    e.descriptionShow=true
                    if(e.status=='pending'){
                        e.text='正在验证你的邀请'
                        e.statusShowType4=true
                    }else if(e.status=='agree'){
                        e.text='同意了你的好友申请'
                        
                    }else if(e.status=='refuse'){
                        e.text='拒绝了你的好友申请'
                        e.descriptionShow=false
                    }
                }
                return e
            })
        }
    },
    created(){
        this.getData()

        emitter.on('contactNoticeUpdate',this.getData)

        // 阻止默认的右键弹出菜单
        // document.addEventListener("contextmenu", this.myRight);
        document.addEventListener("mousedown", this.myRightHandler);
    },
    beforeUnmount(){
        //解绑事件
        // document.removeEventListener("contextmenu", this.myRight);
        document.removeEventListener("mousedown", this.myRightHandler);
        emitter.off('contactNoticeUpdate',this.getData)
    },
    components:{
        AgreeOrRefuseBtn
    }
}
</script>

<style scoped lang="less">

.friendNotification{
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

        .item{
            width: 740px;
            box-sizing: border-box;
            padding: 20px 32px;
            background-color: white;
            margin-bottom: 20px;
            border-radius: 5px;

            display: flex;
            justify-content: space-between;
            align-items: center;

            .info{
                display: flex;

                img{
                    width: 50px;
                    height: 50px;
                    border-radius: 50%;
                    margin-right: 10px;
                }
                .detail{

                    .name{
                        margin-bottom: 3px;

                        .user{
                            color: #0099FF;
                        }
                        .time{
                            color: #999999;
                        }
                    }
                    .description{
                        color: #999999;
                    }
                }
            }
            .status{
                color: #999999;
            }
            
                
            
        }

        .rightMenu{
            position: fixed;
            padding: 5px;
            background-color: white;
            box-shadow: 0px 0px 5px rgba(0,0,0,0.6);
            border-radius: 5px;

            .del{
                padding: 5px;
            }
            .del:hover{
                background-color: #F5F5F5;
            }
        }
    }
}


</style>