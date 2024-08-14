<script>
import Navigator from "../../components/Navigator.vue";
import SearchBox from "../../components/SearchBox.vue";
import {displayTime, emitter} from "../../utils/utils.js";
import axios from "axios";
import config from "../../config/config.js";
import MessageNoticeListItem from "../../components/MessageNoticeListItem.vue";

export default {
    name: "Message",
    computed: {
        config() {
            return config
        }
    },
    components: {MessageNoticeListItem, SearchBox, Navigator},
    data(){
        return {
            messageNotice:[],
            messageNotice2:[],

            groupMessageNotice:[],

            renderList:[], //渲染列表

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
            let renderList=[]

            //两个数据
            //数据1
            {
                let res=await fetch('/api/groupMessageNotices',{
                    headers:{
                        token:localStorage.getItem('token')
                    }
                })
                let json=await res.json()

                console.log('groupMessageNotice数据',json);

                if(json.code===1){

                    renderList=renderList.concat(json.data.map(e=>{
                        return {
                            id:e.id,

                            type:'group',
                            gid:e.gid,
                            routeTo:`/message/group/${e.gid}`,
                            //展示列表项所需数据
                            avatar:e.group.avatar,
                            name:e.group.name,
                            content:e.groupMessage.content,
                            time:displayTime(new Date(e.groupMessage.time)),
                            count:e.count
                        }
                    }))
                }
            }
            //数据2
            {
                let res=await fetch('/api/messageNotices',{
                    headers:{
                        token:localStorage.getItem('token')
                    }
                })
                let json=await res.json()

                console.log('messageNotice数据',json);
                if(json.code===1){
                    // this.messageNotice=json.data

                    renderList=renderList.concat(json.data.map(e=>{
                        return {
                            id:e.id,

                            type:'user',
                            uid:e.uid2,
                            routeTo:`/message/user/${e.uid2}`,
                            //展示列表项所需数据
                            avatar:e.avatar,
                            name:e.username,
                            content:this.showContent(e.content),
                            time:displayTime(new Date(e.time)),
                            count:e.count
                        }
                    }))
                }
            }

            this.renderList=renderList
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
            if(item.type==='user'){
                let res=await axios.put(`/api/messageNotice/clearCount/${item.uid}`,{},{
                    headers:{
                        token:localStorage.getItem('token'),
                    }
                })
                let json=res.data

                if(json.code===1){
                    item.count=0
                }
            }else if (item.type==='group'){
                let res=await axios.put(`/api/groupMessageNotice/clearCount/${item.gid}`,{},{
                    headers:{
                        token:localStorage.getItem('token'),
                    }
                })
                let json=res.data

                if(json.code===1){
                    item.count=0
                }
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
        async copyId(){
            let value=this.rightClickItem.type==='user'?this.rightClickItem.uid:this.rightClickItem.gid;

            console.log('复制ID',value);
            await navigator.clipboard.writeText(value)

            this.$message({
                message:'复制成功',
                type:'success'
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

                    <MessageNoticeListItem
                        v-for="item in renderList" :key="item.id"

                        @click="select(item)"
                        @contextmenu.prevent
                        @mouseup.stop="handleRightClick($event,item)"

                        :item="item"
                    />

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