<script setup>

import config from "../../config/config.js";
import {useRoute} from "vue-router";
import {ref} from "vue";

const route=useRoute()

const currentUser=ref({})


const getData=async ()=>{
    let res=await fetch(`/api/user/${route.params.uid}`,{
        headers:{
            token:localStorage.getItem('token')
        }
    })
    let json=await res.json()

    currentUser.value=json.data
}

getData()
</script>

<script>
// 在 export default 中指定组件名称
export default {
    name: 'Zone'
}
</script>

<template>
<div class="zone">

    <div class="layout-head">
        <div class="title">{{ currentUser.username }}的空间</div>
    </div>
    <div class="layout-nav">
        <div class="inner">
            <div class="avatar">
                <img :src="config.minioUrl+currentUser.avatar" alt="">
                <div class="username">{{ currentUser.username }}</div>
            </div>
            <ul>
                <li @click="$router.push(`/zone/${$route.params.uid}/saying`)" >说说</li>
                <li @click="$router.push(`/zone/${$route.params.uid}/journal`)" >日志</li>
                <li>相册</li>
                <li>留言板</li>
            </ul>
        </div>
    </div>
    <div class="layout-main">
        <div class="box">
            <router-view v-slot="{ Component }" :currentUser="currentUser">
                <!--                只让Saying组件缓存不销毁-->
                <keep-alive include="Saying" >
                    <component :is="Component" />
                </keep-alive>
            </router-view>
        </div>
    </div>

</div>
</template>

<style scoped lang="less">
.zone{

    .layout-head{
        height: 250px;

        background-image: url("/zone-bg.png");
        background-repeat: no-repeat;
        background-position-x: center;

        .title{
            font-size: 30px;
            color: white;

            padding: 30px;
        }
    }
    .layout-nav{
        display: flex;
        justify-content: center;
        height: 50px;

        .inner{
            display: flex;
            width: 900px;

            .avatar{
                width: 128px;
                height: 128px;
                box-sizing: border-box;
                padding: 3px;
                background-color: white;
                border: 1px solid #D9D9D9;
                border-radius: 5px;

                position: relative;
                top: -200%;

                margin-right: 1rem;

                img{
                    width: 100%;
                    height: 100%;
                }
                .username{
                    position: absolute;
                    left: 100%;
                    top: 50%;
                    margin-left: 1rem;

                    font-size: 25px;
                    color: white;
                }
            }
            ul{
                display: flex;
                align-items: center;

                li{
                    margin: 0 1rem;
                    cursor: pointer;
                }
            }
        }
    }
    .layout-main{
        display: flex;
        justify-content: center;

        background-color: #E9E9E9;

        .box{
            width: 900px;
        }
    }
}
</style>