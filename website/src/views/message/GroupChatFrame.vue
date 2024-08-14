<script>
import FileMsgDisplay from "../../components/FileMsgDisplay.vue";
import MsgSend from "../../components/MsgSend.vue";
import {displayTime, emitter} from "../../utils/utils.js";
import config from "../../config/config.js";

export default {
    name: "GroupChatFrame",
    computed: {
        config() {
            return config
        }
    },
    data(){
        return {
            groupInfo: {
                group:{}
            },
            renderList:[]
        }
    },
    methods:{
        async getData(){

            //获取群组信息
            {
                let res=await fetch(`/api/group/${this.$route.params.gid}`,{
                    headers:{
                        token:localStorage.getItem('token')
                    }
                })
                let json=await res.json()

                console.log('群信息',json)

                console.log(json)

                if (json.code===1){
                    this.groupInfo=json.data
                }
            }
            //获取群聊天消息
            let res=await fetch(`/api/groupMessages/${this.$route.params.gid}`,{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            console.log('群消息数据',json)

            //转换为渲染数据
            this.renderList=json.data.map((e,index,arr)=>{
                let flag,type,time,content,isShowTime,avatar

                {
                    let me=JSON.parse(localStorage.getItem('user'))
                    if(e.memberUid===e.gid){ //说明是系统消息
                        flag=-1
                    }else if(e.memberUid===me.uid){ //我方发送的消息
                        flag=0
                        avatar=me.avatar
                    }else{ //对方发送的消息
                        flag=1
                        avatar=e.user.avatar
                    }
                }
                type=e.type
                content=e.content
                {
                    //判断是否显示时间
                    if(index!==0){

                        let diff=new Date(e.time)-new Date(arr[index-1].time)

                        if(diff>1000*60*5){
                            isShowTime=true
                        }else{
                            isShowTime=false
                        }
                    }else{
                        isShowTime=true
                    }
                }
                if(isShowTime){
                    time=displayTime(new Date(e.time))
                }
                return {
                    isShowTime,
                    flag,
                    content,
                    time,
                    type,
                    avatar
                }
            })

            this.$nextTick( ()=> {
                // DOM 更新完毕之后的回调操作
                this.$refs.msgShow.scrollTop=this.$refs.msgShow.scrollHeight
            });
        }
    },
    created() {
        this.getData()

        emitter.on('groupMessage',this.getData)
    },
    beforeUnmount() {

        emitter.off('groupMessage',this.getData)
    },
    components: {MsgSend, FileMsgDisplay}
}
</script>

<template>
    <div class="group-chat-frame">
        <div class="top">
            <div class="left">{{groupInfo.group.name}}</div>
            <div class="right">

            </div>
        </div>
        <div class="middle" ref="msgShow">
            <div class="msg" v-for="item in renderList" :key="item.id">
                <div class="timebar" v-if="item.isShowTime">{{item.time}}</div>
                <div class="local" v-if="item.flag===0">
                    <div class="content" >

                        <div class="wrapper">
                            <div class="nickname">群主</div>

                            <div class="text" v-if="item.type==='text'" v-html="item.content"></div>
                            <div class="file" v-if="item.type==='file'">
                                <FileMsgDisplay :msg-content="item.content" />
                            </div>
                            <div class="img" v-if="item.type==='img'">
                                <img :src="config.minioUrl+item.content">
                            </div>
                        </div>

                    </div>
                    <div class="avatar">
                        <img :src="config.minioUrl+item.avatar" alt="">
                    </div>
                </div>
                <div class="remote" v-if="item.flag===1">
                    <div class="avatar">
                        <img :src="config.minioUrl+item.avatar" alt="">
                    </div>
                    <div class="content">
                        <div class="text" v-if="item.type==='text'" v-html="item.content"></div>
                        <div class="file" v-if="item.type==='file'">
                            <FileMsgDisplay :msg-content="item.content" />
                        </div>
                        <div class="img" v-if="item.type==='img'">
                            <img :src="config.minioUrl+item.content">
                        </div>
                    </div>
                </div>

                <div class="system" v-if="item.flag===-1">
                    {{item.content}}
                </div>
            </div>
        </div>

        <MsgSend />
    </div>
</template>

<style scoped lang="less">
.group-chat-frame{
    height: 100%;
    display: flex;
    flex-direction: column;
    background-color: #F2F2F2;

    .top{
        padding: 40px 20px 20px;
        border-bottom: 1px solid #E9E9E9;
        font-size: 20px;

        display: flex;
        justify-content: space-between;
        align-items: center;

        .left{

        }
        .right{
            display: flex;
            svg{
                width: 30px;
                margin-left: 1rem;
            }
            svg:hover{
                fill: #0099FF;
            }
        }
    }
    .middle{
        flex-grow: 1;
        padding: 25px;
        overflow: auto;

        .msg{
            margin-bottom: 30px;

            .timebar{
                text-align: center;
                color: #9C9C9C;
                margin-bottom: 24px;
            }
            .local{
                display: flex;
                justify-content: flex-end;

                .content{

                    flex-grow: 1;

                    .wrapper{
                        width: 100%;
                        display: flex;
                        flex-direction: column;
                        align-items: flex-end;

                        .nickname{
                            margin-bottom: 5px;
                            font-size: 16px;
                            color: gray;
                        }
                        .text{
                            padding: 13px 10px;
                            background-color: #0099FF;
                            color: white;
                            border-radius: 5px;

                            //长文本单词换行必备设置
                            max-width: 60%;
                            overflow-wrap: anywhere;

                            white-space: pre-wrap;
                        }
                        .file{

                        }
                    }

                }
                .avatar{
                    margin-left: 10px;

                    img{
                        width:40px;
                        height: 40px;
                        border-radius: 50%;
                    }
                }
            }
            .remote{
                display: flex;

                .content{
                    display: flex;

                    .text{
                        padding: 13px 10px;
                        background-color: white;
                        border-radius: 5px;

                        //长文本单词换行必备设置
                        max-width: 60%;
                        overflow-wrap: anywhere;

                        white-space: pre-wrap;
                    }
                    .file{

                    }
                }
                .avatar{
                    margin-right: 10px;

                    img{
                        width:40px;
                        height: 40px;
                        border-radius: 50%;
                    }
                }
            }
            .system{
                text-align: center;
                color: #9C9C9C;
            }

        }
    }
}
</style>