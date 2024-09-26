<script>
export default {
    name: "OfficialDetail"
}
</script>

<template>
<div class="official-detail">

    <div class="center">
        <div class="top">公众号</div>
        <div class="one">
            <div class="avatar">
                <img src="/641.webp" alt="">
            </div>
            <div class="name">
                顶顶顶顶
            </div>
        </div>
        <div class="two">
            传播贡献传播贡献传播贡献传播贡献传播贡献传播贡献传播贡献传播贡献传播贡献传播贡献
        </div>
        <div class="three">
            <div class="btn">取消关注</div>
        </div>
        <div class="four">
            <div class="item">消息</div>
        </div>
        <div class="five">
            <div
                class="item"
                v-for="item in 20" :key="item"
            >
                <div class="time">
                    9月10日
                </div>
                <div class="summary" @click="$router.push(`/official/article/${item}`)">
                    <div class="text">
                        <div class="title">标题i奥体标题的的士大夫但是</div>
                        <div class="info">999阅读 444评论 777赞</div>
                    </div>
                    <div class="cover">
                        <img src="/641.webp" alt="">
                    </div>
                </div>
                <div class="divide"></div>
                <div class="summary">
                    <div class="text">
                        <div class="title">标题i奥体标题的的士大夫但是</div>
                        <div class="info">999阅读 444评论 777赞</div>
                    </div>
                    <div class="cover">
                        <img src="/641.webp" alt="">
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
</template>

<style scoped lang="less">
.official-detail{
    height: 100%;
    display: flex;
    justify-content: center;

    background: linear-gradient(to top,#F3F3F3,rgba(0,0,0,0));
    overflow: auto;

    .center{
        max-width: 600px;
        padding:0 1rem 2rem;

        .top{
            padding: 1rem 0;
            text-align: center;
        }
        .one{
            display: flex;
            align-items: center;

            margin-bottom: 1rem;

            .avatar{
                width: 80px;
                height: 80px;
                border-radius: 50%;
                overflow: hidden;

                margin-right: 2rem;

                img{
                    width: 100%;
                    height: 100%;
                }
            }
            .name{
                font-size: 25px;
            }
        }
        .two{
            margin-bottom: 1rem;
        }
        .three{

            margin-bottom: 1rem;

            .btn{
                padding: 0.5rem;
                background-color: #F2F2F2;

                text-align: center;
            }
        }
        .four{

            margin-bottom: 1rem;
        }
        .five{
            padding-bottom: 2rem;

            .item{
                margin-bottom: 1rem;
                border-bottom: 1px solid #cccccc;

                background-color: white;
                border-radius: 10px;

                .time{
                    padding: 1rem;
                    color: gray;
                }
                .summary{
                    display: flex;
                    justify-content: space-between;

                    padding: 1rem;

                    .text{
                        flex-grow: 1;

                        display: flex;
                        flex-direction: column;
                        justify-content: space-between;

                        .title{
                            font-size: 18px;
                        }
                        .info{
                            font-size: 14px;
                            color: gray;
                        }
                    }
                    .cover{
                        width: 70px;
                        height: 70px;
                        border-radius: 10px;

                        margin-left: 1rem;

                        img{
                            width: 100%;
                            height: 100%;
                        }
                    }
                }
                .summary:hover{
                    background-color: #E5E5E5;
                }
                .divide{
                    width: calc(100% - 2rem - 70px);
                    height: 1px;
                    margin-left: 1rem;
                    background-color: #DCDFE6;
                }
            }
        }

    }
}
</style>