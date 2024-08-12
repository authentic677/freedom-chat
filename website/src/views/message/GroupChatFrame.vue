<script>
import FileMsgDisplay from "../../components/FileMsgDisplay.vue";

export default {
    name: "GroupChatFrame",
    components: {FileMsgDisplay}
}
</script>

<template>
    <div class="group-chat-frame">
        <div class="top">
            <div class="left">哒哒哒</div>
            <div class="right">

            </div>
        </div>
        <div class="middle" ref="msgShow">
            <div class="msg" v-for="item in messages2" :key="item.id">
                <div class="timebar" v-if="item.isShowTime">{{item.time}}</div>
                <div class="local" v-if="item.flag===0">
                    <div class="content" >
                        <div class="text" v-if="item.type==='text'" v-html="item.content"></div>
                        <div class="file" v-if="item.type==='file'">
                            <FileMsgDisplay :msg-content="item.content" />
                        </div>
                        <div class="img" v-if="item.type==='img'">
                            <img :src="config.minioUrl+item.content">
                        </div>
                    </div>
                    <div class="avatar">
                        <img :src="config.minioUrl+item.avatar1" alt="">
                    </div>
                </div>
                <div class="remote" v-if="item.flag===1">
                    <div class="avatar">
                        <img :src="config.minioUrl+item.avatar2" alt="">
                    </div>
                    <div class="content">
                        <div class="text" v-if="item.type==='text'" v-html="item.content"></div>
                        <div class="file" v-if="item.type==='file'">
                            <FileMsgDisplay :msg-content="item.content" />
                        </div>
                        <div class="img" v-if="item.type==='img'">
                            <img :src="config.minioUrl+item.content">
                        </div>
                    </div>
                </div>

                <div class="system" v-if="item.flag===-1">
                    {{item.content}}
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped lang="less">
.group-chat-frame{
    height: 100%;
    display: flex;
    flex-direction: column;
    background-color: #F2F2F2;

    .top{
        padding: 40px 20px 20px;
        border-bottom: 1px solid #E9E9E9;
        font-size: 20px;

        display: flex;
        justify-content: space-between;
        align-items: center;

        .left{

        }
        .right{
            display: flex;
            svg{
                width: 30px;
                margin-left: 1rem;
            }
            svg:hover{
                fill: #0099FF;
            }
        }
    }
    .middle{
        flex-grow: 1;
        padding: 25px;
        overflow: auto;

        .msg{
            margin-bottom: 30px;

            .timebar{
                text-align: center;
                color: #9C9C9C;
                margin-bottom: 24px;
            }
            .local{
                display: flex;
                justify-content: flex-end;

                .content{
                    display: flex;
                    justify-content: flex-end;

                    .text{
                        padding: 13px 10px;
                        background-color: #0099FF;
                        color: white;
                        border-radius: 5px;

                        //长文本单词换行必备设置
                        max-width: 60%;
                        overflow-wrap: anywhere;

                        white-space: pre-wrap;
                    }
                    .file{

                    }
                }
                .avatar{
                    margin-left: 10px;

                    img{
                        width:40px;
                        height: 40px;
                        border-radius: 50%;
                    }
                }
            }
            .remote{
                display: flex;

                .content{
                    display: flex;

                    .text{
                        padding: 13px 10px;
                        background-color: #0099FF;
                        color: white;
                        border-radius: 5px;

                        //长文本单词换行必备设置
                        max-width: 60%;
                        overflow-wrap: anywhere;

                        white-space: pre-wrap;
                    }
                    .file{

                    }
                }
                .avatar{
                    margin-right: 10px;

                    img{
                        width:40px;
                        height: 40px;
                        border-radius: 50%;
                    }
                }
            }
            .system{
                text-align: center;
                color: #9C9C9C;
            }

        }
    }
}
</style>