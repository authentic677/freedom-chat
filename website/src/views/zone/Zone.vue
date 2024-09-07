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

<template>
<div class="zone">
    <div class="main">
        <div class="middle">
            <div class="title">{{ currentUser.username }}的空间</div>

            <div class="header">
                <div class="img-wrapper">
                    <img :src="config.minioUrl+currentUser.avatar" alt="">
                </div>
                <div class="name">{{ currentUser.username }}</div>
            </div>

            <div class="nav">
                <div class="item">说说</div>
                <div class="item">日志</div>
                <div class="item">相册</div>
                <div class="item">留言板</div>
            </div>

            <router-view :currentUser="currentUser"></router-view>

        </div>
    </div>
</div>
</template>

<style scoped lang="less">
.zone{

    background-image: url("/zone-bg.png");
    background-repeat: no-repeat;
    background-position-x: center;

    .main{
        min-height: 100vh;

        background: linear-gradient(to bottom,rgba(0,0,0,0) 50px,#E9E9E9 300px);
        //background: red;

        display: flex;
        justify-content: center;

        .middle{
            width: 100%;
            max-width: 1000px;
            height: 100%;
            padding: 0 1rem;
            box-sizing: border-box;

            .title{
                font-size: 30px;
                color: white;

                padding: 30px 0 60px;
            }
            .header{
                margin-top: 1rem;

                display: flex;
                align-items: center;

                .img-wrapper{
                    padding: 3px;
                    border: 1px solid #D9D9D9;
                    background-color: white;
                    margin-right: 1rem;

                    img{
                        width: 120px;
                        height: 120px;
                        vertical-align: top;
                    }
                }
                .name{
                    font-size: 25px;
                    color: white;
                }
            }
            .nav{
                display: flex;
                margin-top: 1rem;

                padding-bottom: 1rem;
                border-bottom: 1px solid gray;

                .item{
                    margin-right: 1rem;
                    cursor: pointer;
                }
            }

        }
    }
}
</style>