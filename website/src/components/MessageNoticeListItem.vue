<script>
import config from "../config/config.js";

export default {
    name: "MessageNoticeListItem",
    computed: {
        config() {
            return config
        }
    },
    props:['item']
}
</script>

<template>
<div class="messageNoticeListItem">
    <div class="avatar">
        <img :src="config.minioUrl+item.avatar" alt="">
    </div>

    <div class="info">
        <div class="name">{{item.name}}</div>
        <div class="msg">{{item.content}}</div>
    </div>

    <div class="right2">
        <div class="time">{{item.time==null?'':item.time}}</div>
        <div class="count" :class="{opacity:item.count===0}">{{item.count}}</div>
    </div>
</div>
</template>

<style scoped lang="less">
.messageNoticeListItem{
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
</style>