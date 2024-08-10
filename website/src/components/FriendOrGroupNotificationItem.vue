<script>
import AgreeOrRefuseBtn from "./AgreeOrRefuseBtn.vue";
import config from "../config/config.js";
import {displayTime} from "../utils/utils.js";

export default {
    name: "FriendOrGroupNotificationItem",
    data(){
        return {
            rightMenuShow:false,//控制右键菜单是否显示
            x:0,
            y:0,
        }
    },
    methods: {
        displayTime,

        async del(){
            console.log('删除')
            this.$emit('del')
            this.rightMenuShow=false
        },
        mousedown(e){
            //只处理本组件范围内的鼠标按下事件
            if(this.$refs.item.contains(e.target)){
                if(e.button===2){
                    this.rightMenuShow=true
                    this.x=e.x+'px'
                    this.y=e.y+'px'
                    console.log(this.x,this.y);
                }else{
                    this.rightMenuShow=false
                }
            }else{ //一旦别的地方有鼠标按下事件，赶紧隐藏
                this.rightMenuShow=false
            }
        },
    },
    computed: {
        config() {
            return config
        }
    },
    props:['item'],
    created() {
        window.addEventListener('mousedown',this.mousedown)
    },
    beforeUnmount() {
        window.removeEventListener('mousedown',this.mousedown)
    },
    components: {AgreeOrRefuseBtn}
}
</script>

<template>
<div class="friendOrGroupNotificationItem"
    @contextmenu.prevent
    ref="item"
>
    <div class="info">
        <img :src="item.avatar" alt="">
        <div class="detail">
            <div class="name">
                <span class="user">{{item.name}}</span>
                &nbsp;
                <span class="text"> {{item.text}} </span>
                &nbsp;
                <span class="time">{{displayTime(new Date(item.time))}}</span>
            </div>
            <div class="description">留言：{{item.message}}</div>
        </div>
    </div>
    <div class="status">
        <AgreeOrRefuseBtn v-if="item.statusShowType===0" @agree="this.$emit('agree')" @refuse="this.$emit('refuse')" />
        <div v-if="item.statusShowType===1">已同意</div>
        <div v-if="item.statusShowType===2">已拒绝</div>
        <div v-if="item.statusShowType===3">等待验证</div>
    </div>

    <div class="rightMenu" v-show="rightMenuShow" :style="{left:x,top:y}">
        <div class="del" @mousedown.stop="del">删除</div>
    </div>
</div>
</template>

<style scoped lang="less">
.friendOrGroupNotificationItem {
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
</style>