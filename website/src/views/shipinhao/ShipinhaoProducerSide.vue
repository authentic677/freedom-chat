<script>
import config from "../../config/config.js";

export default {
    name: "ShipinhaoProducerSide",
    computed: {
        config() {
            return config
        }
    },
    data(){
        return {
            channel:{},
        }
    },
    async created() {
        let res=await fetch('/api/content-platform/channel')
        let json=await res.json()

        this.channel=json.data
    }
}
</script>

<template>
<div class="shipinhaoProducerSide">
    <div class="left">
        <div class="avatar">
            <img :src="config.minioUrl+channel.avatar" alt="">
            <div class="name">{{channel.name}}</div>
        </div>
        <div class="menu">
            <div class="item" @click="$router.push('/shipinhao/producer/contentManage')">内容管理</div>
            <div class="item" @click="$router.push('/shipinhao/producer/commentManage')">评论管理</div>
            <div class="item" @click="$router.push('/shipinhao/producer/customization')">主页设置</div>
        </div>
    </div>
    <div class="right">
        <router-view></router-view>
    </div>
</div>
</template>

<style scoped lang="less">
.shipinhaoProducerSide{
    height: 100%;

    display: grid;
    grid-template-columns: 255px 1fr;

    .left{
        border-right: 1px solid #E5E5E5;

        .avatar{
            display: flex;
            flex-direction: column;

            align-items: center;

            margin-top: 2rem;

            img{
                width: 112px;
                height: 112px;
                border-radius: 50%;
            }
            .name{
                margin: 1rem 0 2rem;
            }
        }
        .menu{

            .item{
                padding: 1rem 0;
                text-align: center;
            }
        }
    }
    .right{
        height: 100%;
        overflow: hidden;
    }
}
</style>