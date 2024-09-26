<script>
export default {
    name: "OfficialArticle"
}
</script>

<template>
<div class="official-article">
    <div class="top">
        <div class="left" @click="this.$router.back()">
            <svg t="1727159343710" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3394" width="200" height="200"><path d="M916.336 917.44a15.952 15.952 0 0 1-11.312-4.688L96.368 104.08a15.984 15.984 0 1 1 22.624-22.624l808.656 808.672a15.984 15.984 0 0 1-11.312 27.312z" fill="" p-id="3395"></path><path d="M107.68 917.44a15.984 15.984 0 0 1-11.312-27.312L905.024 81.456a15.984 15.984 0 1 1 22.624 22.624L118.992 912.752a15.92 15.92 0 0 1-11.312 4.688z" fill="" p-id="3396"></path></svg>
        </div>
        <div class="right">

        </div>
    </div>
    <div class="content">
        <div class="box">
            <div class="title">
                阿阿不
            </div>
            <div class="info">
                <div class="author">阿川同学</div>
                <div class="time">2024年09月23日</div>
            </div>
            <div class="body">
                <div v-for="i in 40">adfffadfd</div>
            </div>
        </div>
    </div>
    <div class="bottom">
        <div class="wrapper">
            <div class="left">
                <div class="avatar">
                    <img src="/641.webp" alt="">
                </div>
                <div class="name">
                    观星台
                </div>
            </div>
            <div class="right">
                <div>点赞</div>
                <div>评论</div>
                <div>收藏</div>
            </div>
        </div>
    </div>
</div>
</template>

<style scoped lang="less">
.official-article{
    height: 100%;
    display: flex;
    flex-direction: column;

    .top{
        display: flex;
        justify-content: space-between;

        .left{
            padding: 1rem;

            display: flex;

            svg{
                width: 18px;
                height: 18px;
            }
        }
    }
    .content{
        flex-grow: 1;

        display: flex;
        justify-content: center;
        overflow: auto;

        .box{
            width: 100%;
            max-width: 850px;
            padding: 1rem;

            .title{
                padding: 1rem 0;
                font-size: 30px;
            }
            .info{
                display: flex;
                color: gray;
                margin-bottom: 1rem;

                .author{
                    margin-right: 1rem;
                }
            }
        }
    }
    .bottom{
        display: flex;
        justify-content: center;

        .wrapper{
            width: 100%;
            max-width: 850px;

            display: flex;
            justify-content: space-between;

            border-top: 1px solid #cccccc;
            padding: 1rem;

            .left{
                display: flex;
                align-items: center;

                .avatar{
                    width: 30px;
                    height: 30px;
                    border-radius: 50%;
                    overflow: hidden;

                    margin-right: 1rem;

                    img{
                        width: 100%;
                        height: 100%;
                    }
                }
                .name{
                    //font-size: 18px;
                }
            }
            .right{
                display: flex;

            }
        }
    }
}
</style>