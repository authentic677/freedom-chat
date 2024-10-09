<script>
import Plyr from "plyr";

export default {
    name: "ShipinhaoConsumerSide",

}
</script>

<template>
<div class="shipinhaoConsumerSide">
    <div class="top">
        <div class="item name">
            视频号
        </div>
        <div class="item search">
            搜索
        </div>
        <div class="item me" @click="$router.push('/shipinhao/producer')">
            <el-link>管理</el-link>
        </div>
    </div>
    <div class="bottom">
        <div class="left">
            <div class="nav">
                <div class="item" @click="$router.push('/shipinhao/consumer')">推荐</div>
                <div class="item">朋友</div>
                <div class="item" @click="$router.push('/shipinhao/consumer/profile/self')">我的</div>
            </div>
            <div class="divider"></div>
            <div class="follow">
                <div class="title">关注</div>
                <div class="item">方脸</div>
                <div class="item">方脸</div>
                <div class="item">方脸</div>
            </div>

        </div>
        <div class="right">

            <router-view></router-view>

        </div>
    </div>

</div>
</template>

<!--<style>-->
<!--@import 'plyr/dist/plyr.css';-->
<!--</style>-->
<style scoped lang="less">
.shipinhaoConsumerSide{
    width: 100%;
    height: 100%;

    //background-color: #161823;

    display: grid;
    grid-template-rows: auto 1fr;
    row-gap: 1rem;

    //color: white;

    .top{
        height: 90px;
        //background-color: #181818;

        display: grid;
        grid-template-columns: 200px 1fr auto;
        align-items: center;

        .item.name{
            text-align: center;
            font-size: 25px;
        }
        .item.me{
            padding: 0 1rem;
        }
    }
    .bottom{
        display: grid;
        grid-template-columns: 200px 1fr;

        padding-bottom: 1rem;
        overflow: hidden;

        .left{
            display: flex;
            flex-direction: column;
            align-items: center;

            padding: 0 1rem;

            .nav{

                .item{
                    padding: 0.5rem 1rem;
                    margin-bottom: 1rem;
                    cursor: pointer;
                    border-radius: 5px;
                }
                .item:hover{
                    background-color: #F2F2F2;
                }
            }
            .divider{
                width: 100%;
                height: 1px;
                background-color: #ccc;

                margin-bottom: 1rem;
            }
            .follow{
                width: 100%;

                .title{
                    padding-left: 0.5rem;
                    color: gray;
                    margin-bottom: 1rem;
                }
                .item{

                }
            }
        }
        .right{
            overflow: hidden;
        }
    }
}
</style>