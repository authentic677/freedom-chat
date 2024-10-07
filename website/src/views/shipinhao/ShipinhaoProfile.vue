<script>
export default {
    name: "ShipinhaoProfile",
    data(){
        return {
            activeName:'video'
        }
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
                <img src="/641.webp" alt="">
            </div>
            <div class="text">
                <div class="name">
                    老周
                </div>
                <div class="info">
                    <div class="item follow">
                        关注 8
                    </div>
                    <div class="item fan">
                        粉丝 100
                    </div>
                    <div class="item like">
                        获赞 322
                    </div>
                    <div class="item videoCount">
                        视频 120
                    </div>
                </div>
                <div class="introduction">
                    这个人很懒，没有写任何描述
                </div>
            </div>
        </div>
        <div class="content">
            <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
                <el-tab-pane label="视频" name="video">
                    <div v-for="i in 100">aaa</div>
                </el-tab-pane>
                <el-tab-pane label="喜欢" name="like">

                </el-tab-pane>
                <el-tab-pane label="收藏" name="collection">

                </el-tab-pane>
                <el-tab-pane label="观看历史" name="watchLater">
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
    }
}
</style>