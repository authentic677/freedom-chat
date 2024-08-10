<template>
    <div class="contactSearch">
        <div class="box">
            <div class="top">
                全网搜索

                <svg @click="close" t="1709953152423" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4283" width="200" height="200"><path d="M851.416 217.84l-45.256-45.248L512 466.744l-294.152-294.16-45.256 45.256L466.744 512l-294.152 294.16 45.248 45.256L512 557.256l294.16 294.16 45.256-45.256L557.256 512z" fill="#272536" p-id="4284"></path></svg>
            </div>
            <div class="bottom">
                <div class="part1">
                    <SearchBox placeholder="UID / 用户名 / GID / 群名" @enter="search" />
                
                    <div class="condition">
                        <div class="c" :class="{selected:condition===0}" @click="condition=0">
                            <div class="text">全部</div>
                            <div class="bottom"></div>
                        </div>
                        <div class="c" :class="{selected:condition===1}" @click="condition=1">
                            <div class="text">用户</div>
                            <div class="bottom"></div>
                        </div>
                        <div class="c" :class="{selected:condition===2}" @click="condition=2">
                            <div class="text">群聊</div>
                            <div class="bottom"></div>
                        </div>
                    </div>
                </div>
                <div class="part2">
                    
                    <div class="tag" v-if="users2.length!==0">用户</div>
                    <div class="item" v-for="item in users2" :key="item.uid">
                        <div class="left">
                            <img :src="config.minioUrl+item.avatar" alt="">
                            <div class="info">
                                <div class="name" v-html="item.username"></div>
                                <div class="describe">uid: <span v-html="item._uid"></span></div>
                            </div>
                        </div>
                        <div class="right">
                            <div @click="addUser(item)" class="btn">添加</div>
                        </div>
                    </div>

                    <div class="tag" v-if="groups2.length!==0">群聊</div>
                    <div class="item" v-for="item in groups2" :key="item.gid">
                        <div class="left">
                            <img :src="config.minioUrl+item.avatar" alt="">
                            <div class="info">
                                <div class="name" v-html="item.name"></div>
                                <div class="describe">gid: <span v-html="item._gid"></span></div>
                            </div>
                        </div>
                        <div class="right">
                            <div @click="addGroup(item)" class="btn">加入</div>
                        </div>
                    </div>


                </div>
            </div>
        </div>

        <AddContact v-if="isAdd" :user="currentUser" @close="isAdd=false" @note="note"/>
        <AddGroup v-if="isAddGroup" :group="currentGroup" @close="isAddGroup=false" @note="note2" />
    </div>
</template>

<script>
import SearchBox from "./SearchBox.vue";
import AddContact from './AddContact.vue'
import AddGroup from "./AddGroup.vue";
import config from "../config/config.js";

export default {
    name:'ContactSearch',
    computed: {
        config() {
            return config
        }
    },
    data(){
        return {
            users:[], //从服务器获取的原始搜索数据
            groups:[], //从服务器获取的原始搜索数据
            users2:[],
            groups2:[],

            //表示用户当前欲添加的对象
            currentUser:null,
            currentGroup:null,

            //下述是搜索条件
            keyword:'', //搜索关键字
            condition:0,//标识当前的显示过滤条件 0全部 1用户 2群聊

            isAdd:false, //标识添加联系人框是否出现
            isAddGroup:false, //标识添加群框是否出现
            callback:null
        }
    },
    methods:{
        close(){
            this.$emit('close')
        },
        async search(content){
            console.log(content);
            this.keyword=content
        },
        async userSearch(content){
            let res=await fetch('/api/users?keyword='+encodeURIComponent(content),{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()


            console.log(json);

            this.users=json


        },
        async groupSearch(content){
            let res=await fetch('/api/groups?keyword='+encodeURIComponent(content),{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()


            console.log(json);
            if(json.code===1){
                this.groups=json.data
            }
            
        },
        async addUser(user){ //与用户成为好友的申请
            
            //获取用户给对方的留言
            let note=await new Promise((resolve,reject)=>{
                
                this.currentUser=user
                this.isAdd=true
                this.callback=resolve
            })

            console.log('留言',note)

            //网络请求
            let res=await fetch('/api/contactNotice',{
                method:'POST',
                headers:{
                    'Content-Type':'application/json;charset=utf-8',
                    token:localStorage.getItem("token")
                },
                body:JSON.stringify({
                    uid1:user.uid,
                    uid2:null, //我自己的uid通过token告诉后端
                    leaveMessage:note
                })
            })

            let json=await res.json()

            console.log(json);

            if(json.code===1){
                this.$message({
                    message:'发送成功',
                    type:'success'
                })
            }else{
                this.$message({
                    message:json.msg,
                    type:'error'
                })
            }
        },
        async addGroup(group){ //入群申请
            
            //获取用户给对方的留言
            let note=await new Promise((resolve,reject)=>{
                
                this.currentGroup=group
                this.isAddGroup=true
                this.callback=resolve
            })

            console.log('入群留言',note)

            //网络请求
            let res=await fetch('/api/groupApplicant',{
                method:'POST',
                headers:{
                    'Content-Type':'application/json;charset=utf-8',
                    token:localStorage.getItem("token")
                },
                body:JSON.stringify({
                    gid:group.gid,
                    message:note
                })
            })

            let json=await res.json()

            console.log(json);

            if(json.code===1){
                this.$message({
                    message:'发送成功',
                    type:'success'
                })
            }else{
                this.$message({
                    message:json.msg,
                    type:'error'
                })
            }
        },
        note(e){ //note表示给对方的留言
            this.isAdd=false
            this.callback(e)
        },
        note2(e){
            this.isAddGroup=false //关闭获取入群申请留言对话框
            this.callback(e)
        },
        putUsers2(){
            if(this.condition==1){ //独显
                this.groups2=[]
            }
            this.users2= this.users.map(e=>{
                if(this.keyword!=null){ //为了搜索结果高亮
                    e.username=e.username.replace(this.keyword,`<span style="color: #0099FF;">${this.keyword}</span>`)
                    e._uid=e.uid.replace(this.keyword,`<span style="color: #0099FF;">${this.keyword}</span>`)
                    return e
                }
            })
        },
        putGroups2(){
            if(this.condition==2){ //独显
                this.users2=[]
            }
            this.groups2= this.groups.map(e=>{
                if(this.keyword!=null){ //为了搜索结果高亮
                    e.name=e.name.replace(this.keyword,`<span style="color: #0099FF;">${this.keyword}</span>`)
                    e._gid=e.gid.replace(this.keyword,`<span style="color: #0099FF;">${this.keyword}</span>`)
                    return e
                }
            })
        },
        async searchHandler(){
            switch (this.condition) {
                case 0:
                    await this.userSearch(this.keyword)
                    await this.groupSearch(this.keyword)
                    this.putUsers2()
                    this.putGroups2()
                    break;
                case 1:
                    await this.userSearch(this.keyword)
                    this.putUsers2()
                    break;
                case 2:
                    await this.groupSearch(this.keyword)
                    this.putGroups2()
                    break;
                default:
                    break;
            }
        }
    },
    watch:{
        keyword(){
            this.searchHandler()
        },
        condition(){
            this.searchHandler()
        }
    },
    
    components:{
        SearchBox,
        AddContact,
        AddGroup
    }
}
</script>

<style scoped lang="less">
.contactSearch{
    position: fixed;
    left: 0px;
    top: 0px;
    width: 100vw;
    height: 100vh;
    background: rgba(0,0,0,0.6);
    
    display: flex;
    justify-content: center;
    align-items: center;

    .box{
        width: 850px;
        height: 80%;
        background: white;
        border-radius: 5px;
        box-shadow: 0px 0px 10px rgba(0,0,0,0.3);

        display: flex;
        flex-direction: column;

        .top{
            padding: 10px;
            text-align: center;
            position: relative;

            svg{
                width: 1rem;
                height: 1rem;

                position: absolute;
                top: 10px;
                right: 10px;
            }
        }

        .bottom{
            flex-grow: 1;
            overflow: auto;
            display: flex;
            flex-direction: column;

            .part1{
                padding: 2px 25px 20px;
                border-bottom: 1px solid #F5F5F5;

                .condition{
                    display: flex;
                    margin-top: 20px;

                    .c{
                        margin-right: 50px;

                        cursor: pointer;

                        .text{
                            margin-bottom: 10px;
                        }
                    }
                    .c::after{
                        content: "";
                        display: block;
                        height: 3px;
                        background-color: transparent;
                    }
                    .c.selected{
                        color: #0099FF;
                    }
                    .c.selected::after{
                        background-color: #0099FF;
                    }
                }
                
            }
            .part2{
                padding: 12px;
                flex-grow: 1;
                overflow: auto;

                .item{
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    padding: 12px;
                    border-radius: 5px;

                    .left{
                        display: flex;
                        align-items: center;


                        img{
                            margin-right: 10px;
                            width: 50px;
                            height: 50px;
                            border-radius: 50%;
                        }
                        .info{

                            .name{
                                font-size: 18px;
                                margin-bottom: 3px;
                            }
                            .describe{
                                color: #999999;
                                font-size: 14px;
                            }
                        }
                        
                    }
                    .right{

                        .btn{
                            padding: 7px 20px;
                            border: 1px solid #CCCCCC;
                            border-radius: 5px;
                        }
                        .btn:hover{
                            background-color: #EBEBEB;
                        }
                    }
                }
                .item:hover{
                    background-color: #F5F5F5;
                }

                .tag{
                    padding-left: 12px;
                    color: #999999;
                }
            }
        }
    }
    
}
</style>