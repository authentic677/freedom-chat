<script>
import Navigator from "../../components/Navigator.vue";
import SearchBox from "../../components/SearchBox.vue";
import {displayTime, emitter} from "../../utils/utils.js";
import axios from "axios";
import config from "../../config/config.js";

export default {
    name: "Message",
    computed: {
        config() {
            return config
        }
    },
    components: {SearchBox, Navigator},
    data(){
        return {
            messageNotice:[],
            messageNotice2:[],
            highLightItemId:null, //标识当前高亮的项

            rightClickShow:false, //表示右键item的菜单是否显示
            x:0,
            y:0,
            rightClickItem:null
        }
    },
    watch:{
        messageNotice(){
            console.log('messageNotice2 监视属性')
            this.messageNotice2=this.messageNotice.map(e=>{
                //优雅地显示时间
                e.time2=displayTime(new Date(e.time))
                //关于这里用time2的原因，是为了不覆盖原来的time值，因为计算属性里引用了highLightItemId，会导致重复计算的，如果覆盖了time值。那么就会出现NAN

                //当前高亮的项的count值一定永远为0
                if(this.highLightItemId===e.id){
                    //本地改
                    e.count=0
                    //通知服务器那边改
                    this.clearCount(e)
                }
                return e
            }).sort((a,b)=>{//按时间从大到小排序
                return new Date(b.time).getTime()-new Date(a.time).getTime()
            })
        }
    },
    methods:{
        async getData(){
            let res=await fetch('/api/messageNotices',{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            console.log(json);
            if(json.code===1){
                this.messageNotice=json.data
            }
        },
        select(item){
            this.highLightItemId=item.id

            this.clearCount(item)
        },
        async clearCount(item){ //!!!这个请求是导致切换聊天窗口缓慢的罪魁祸首!!!
            if(item.count===0){ //提高效率，如果为0就不用发请求清除
                return
            }
            //发请求
            let res=await axios.put(`/api/messageNotice`,{
                uid1:item.uid1,
                uid2:item.uid2,
                count:0
            },{
                headers:{
                    token:localStorage.getItem('token'),
                    'Content-Type':'application/json;charset=UTF-8',
                }
            })
            let json=res.data

            console.log('clear count',json);

            if(json.code===1){
                item.count=0
            }
        },
        //消息项右键的菜单显示回调
        handleRightClick(e,item){
            console.log('显示句柄',e);
            if(e.button===2){
                this.rightClickShow=true
                this.x=e.x
                this.y=e.y

                this.rightClickItem=item
            }
        },
        //消息项右键的菜单关闭回调
        handleClear(){
            console.log('清除句柄');
            this.rightClickShow=false
        },
        //以下是右键菜单的回调
        copyId(){
            console.log('复制ID');
            navigator.clipboard.writeText(this.rightClickItem.uid2).then(()=>{
                this.$message({
                    message:'复制成功',
                    type:'success'
                })
            }).catch(err=>{
                this.$message({
                    message:'复制失败',
                    type:'error'
                })
            })
        },
        topping(){
            console.log('置顶');
        },
        notDisturb(){
            console.log('免打扰');
        },
        async remove(){
            console.log('移除');

            let res=await fetch('/api/messageNotice',{
                method:'DELETE',
                headers:{
                    token:localStorage.getItem('token'),
                    'Content-Type':'application/json;charset=utf-8'
                },
                body:JSON.stringify({
                    uid2:this.rightClickItem.uid2
                })
            })
            let json=await res.json()

            console.log(json);
            if(json.code===1){
                this.getData()
            }
        },
        showContent(content){ //需要替换内容中的表情内容为文字内容
            let div=document.createElement('div')
            div.innerHTML=content

            let c=div.children
            for(let i=0;i<c.length;i++){
                if(c[i].nodeName==='IMG'){
                    div.replaceChild(document.createTextNode('[表情]'),c[i])
                }
            }

            return div.innerText
        }
    },
    created(){
        this.getData()

        emitter.on('messageNoticeUpdate',this.getData)
        document.addEventListener('mouseup',this.handleClear)
    },
    beforeUnmount(){

        emitter.off('messageNoticeUpdate',this.getData)
        document.removeEventListener('mouseup',this.handleClear)
    },
}
</script>

<template>
    <div class="message">
        <div class="left">
            <navigator/>
        </div>
        <div class="right">
            <div class="list">
                <div class="search">
                    <SearchBox placeholder="在消息中搜索" class="s" />
                </div>
                <div class="msgList">

                    <router-link
                        v-for="item in messageNotice2" :key="item.id"
                        :to="`/message/user/${item.uid2}`"
                    >
                        <div
                            class="item"
                            :class="{highLight:highLightItemId===item.id}"
                            @click="select(item)"
                            @contextmenu.prevent="(()=>{})"
                            @mouseup.stop="handleRightClick($event,item)"
                        >
                            <div class="avatar">
                                <img :src="config.minioUrl+item.avatar" alt="">
                            </div>

                            <div class="info">
                                <div class="name">{{item.username}}</div>
                                <div class="msg">{{showContent(item.content)}}</div>
                            </div>

                            <div class="right2">
                                <div class="time">{{item.time2==null?'':item.time2}}</div>
                                <div class="count" :class="{opacity:item.count===0}">{{item.count}}</div>
                            </div>
                        </div>
                    </router-link>

                    <div class="rightClick" v-if="rightClickShow" :style="{left:x+'px',top:y+'px'}">
                        <div class="item" @mousedown="copyId">复制ID</div>
                        <div class="item" @mousedown="topping">置顶</div>
                        <div class="item" @mousedown="notDisturb">设置免打扰</div>
                        <div class="item" @mousedown="remove">从消息列表中移除</div>
                    </div>
                </div>
            </div>
            <div class="chat-window">
                <router-view></router-view>
            </div>
        </div>
    </div>
</template>

<style scoped lang="less">
.message{
    height: 100vh;
    display: flex;

    .left{

    }
    .right{
        flex-grow: 1;

        display: flex;

        .list{
            display: flex;
            flex-direction: column;
            background-color: white;
            border-right: 1px solid #E9E9E9;
            width: 400px;

            flex-shrink: 0;

            .search{
                display: flex;
                padding: 45px 20px 20px;
                height: 35px;

                .s{
                    flex-grow: 1;
                }
            }
            .msgList{
                flex-grow: 1;
                overflow: auto;

                a{
                    color: black;
                    text-decoration: none;
                    display: block;
                }
                a:hover{
                    cursor: default;
                    background-color: #EBEBEB;
                }
                a.router-link-active {
                    color: white;
                    background-color: #0099FF;

                    .item{
                        .msg{
                            color: white;
                        }
                    }
                }
                .item{
                    padding: 20px;
                    display: flex;

                    .avatar{
                        margin-right: 10px;

                        img{
                            width: 50px;
                            height: 50px;
                            border-radius: 50%;
                        }
                    }
                    .info{
                        flex-grow: 1;
                        overflow: hidden;

                        .name{
                            font-size: 18px;
                            margin-bottom: 2px;
                        }
                        .msg{
                            color: #999999;

                            text-overflow: ellipsis;
                            white-space: nowrap;
                            overflow: hidden;
                        }
                    }
                    .right2{
                        display: flex;
                        flex-direction: column;
                        justify-content: center;
                        align-items:flex-end;

                        .time{
                            margin-bottom: 2px;
                        }
                        .count{
                            padding: 2px 6px;
                            background-color: #F74C30;
                            border-radius: 10px;
                            font-size: 12px;
                            font-weight: bold;
                            color: white;
                        }
                        .count.opacity{
                            opacity: 0;
                        }
                    }
                }


                .rightClick{
                    position: fixed;
                    width: 205px;
                    background-color: white;
                    box-shadow: 0px 0px 15px rgba(0,0,0,0.3);
                    padding: 5px;
                    border-radius: 5px;

                    .item{
                        padding: 5px;
                    }
                    .item:hover{
                        background-color: #F5F5F5;
                    }
                }
            }
        }
        .chat-window{
            flex-grow: 1;

            background-color: #F2F2F2;
            background-image: url('/logo3.png');
            background-repeat: no-repeat;
            background-position: center;
            background-size: 150px;
        }
    }
}
</style>