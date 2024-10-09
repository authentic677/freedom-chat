<script>
import config from "../../config/config.js";

export default {
    name: "ShipinhaoProfile",
    computed: {
        config() {
            return config
        }
    },
    data(){
        return {
            channel: {},
            activeName:'video',

            videos:[]
        }
    },
    methods:{
        async getData(){
            {
                let res = await fetch('/api/content-platform/channel')
                let json = await res.json()

                console.log(json)

                this.channel = json.data
            }
            {
                let res=await fetch('/api/content-platform/videos')
                let json=await res.json()

                console.log(json)
                //处理url
                json.data.forEach(item=>{
                    item.cover=config.minioUrl+item.cover
                })
                this.videos=json.data
            }
        }
    },
    created() {
        this.getData()
    }
}
</script>

<template>
<div class="shipinhaoProfile">
    <div class="wrapper">
        <div class="banner">
            <img src="/641.webp" alt="">
        </div>
        <div class="profile">
            <div class="avatar">
                <img :src="config.minioUrl+channel.avatar" alt="">
            </div>
            <div class="text">
                <div class="name">
                    {{channel.name}}
                </div>
                <div class="info">
                    <div class="item follow">
                        关注 {{ channel.followCount }}
                    </div>
                    <div class="item fan">
                        粉丝 {{ channel.fanCount }}
                    </div>
                    <div class="item like">
                        获赞 {{ channel.likeCount }}
                    </div>
                    <div class="item videoCount">
                        视频 {{ channel.videoCount }}
                    </div>
                    <div class="item id">
                        UID: {{channel.uid}}
                    </div>
                </div>
                <div class="introduction">
                    {{ channel.description }}
                </div>
            </div>
        </div>
        <div class="content">
            <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
                <el-tab-pane label="视频" name="video">
                    <div class="video-list">
                        <div
                            v-for="(item,index) in videos"
                            class="item"
                        >
                            <div class="cover">
                                <img :src="item.cover" alt="">
                            </div>
                            <div class="text">
                                {{item.title}}
                            </div>
                            <div class="info">
                                {{item.time}}
                            </div>
                        </div>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="喜欢" name="like">

                </el-tab-pane>
                <el-tab-pane label="收藏" name="collection">

                </el-tab-pane>
                <el-tab-pane label="观看历史" name="watchHistory">
                    Task
                </el-tab-pane>
                <el-tab-pane label="稍后再看" name="watchLater">
                    Task
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</div>
</template>

<style scoped lang="less">
.shipinhaoProfile{
    width: 100%;
    height: 100%;
    padding-right: 1rem;
    box-sizing: border-box;
    overflow: auto;

    .wrapper{
        margin: 0 auto;
        width: 100%;
        max-width: 1200px;

        .banner{
            width: 100%;
            aspect-ratio: 806 / 130;
            margin-bottom: 2rem;

            img{
                width: 100%;
                height: 100%;
            }
        }
        .profile{
            display: flex;
            margin-bottom: 1rem;

            .avatar{
                width: 120px;
                height: 120px;
                border-radius: 50%;
                overflow: hidden;

                margin-right: 1rem;

                img{
                    width: 100%;
                    height: 100%;
                }
            }
            .text{

                .name{
                    font-size: 30px;
                    margin-bottom: 0.5rem;
                }
                .info{
                    display: flex;
                    margin-bottom: 0.5rem;

                    .item{
                        margin-right: 1rem;
                    }
                }
                .introduction{

                }
            }
        }
        .content{

            .video-list{
                display: grid;
                grid-template-columns: repeat(4,1fr);
                align-items: center;

                .item{
                    max-width: 200px;
                    margin: 0 auto;

                    .cover{
                        margin-bottom: 0.5rem;

                        img{
                            width: 100%;
                        }
                    }
                    .text{
                        margin-bottom: 0.5rem;
                    }
                    .info{
                        color: gray;
                    }
                }
            }
        }
    }
}
</style>