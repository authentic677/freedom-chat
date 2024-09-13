<script>

import {displayTime} from "../utils/utils.js";
import config from "../config/config.js";

export default {
    name: 'SayingNode',
    computed: {
        config() {
            return config
        }
    },
    data(){
        return {
            isActive:false
        }
    },
    props:{
        data:{
            type:Object,
            default:{}
        },
        variant:{
            type:String,
            default:"secondary"
        }
    },
    methods:{
        displayTime,
        mouseupHandler(){
            this.isActive=false

            setTimeout(()=>{
                this.$router.push(`/zone/${props.uid}/sayingDetail/${item.id}`)
            },50)
        },
        toDetail(){
            console.log('点击了')
        },
        doLike(){
            console.log('点赞')
        },
        doComment(){
            console.log('评论')
        }
    },
    created() {
        console.log(this.variant)
    },
    mounted() {

    }
}
</script>

<template>
<div class="saying-node">

    <div v-if="variant==='primary'" class="primary-variant">
        <div class="writer">
            <img :src="config.minioUrl+data.user.avatar" alt="">
            <div class="text">
                <div class="name">{{ data.user.username }}</div>
                <div class="time">{{ displayTime(new Date(data.time)) }}</div>
            </div>
        </div>
        <div class="content">
            {{ data.content.text }}
        </div>
        <div class="footer">

        </div>
        <div class="ops">
            <div class="ops_item">
                <svg t="1725346671253" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2409" width="200" height="200"><path d="M510.340709 900.892171c-23.173792 0-247.254314-180.180685-332.157006-289.301965-58.338686-53.264111-105.562221-152.650679-105.562221-222.732827 0-148.765185 108.952434-269.77626 242.874563-269.77626 81.781608 0 151.191444 67.679425 194.013739 137.605008 42.822294-69.925582 112.207571-137.605008 194.013739-137.605008 133.922129 0 242.874563 121.011074 242.874563 269.77626 0 70.239737-47.445592 169.805384-106.055454 223.182059C754.630505 722.553437 534.862195 900.892171 510.340709 900.892171zM315.497069 165.0685c-108.570741 0-196.887182 100.397594-196.887182 223.788879 0 58.382689 42.059931 145.353482 91.864244 189.903118 1.033539 0.943488 2.022053 1.977028 2.874467 3.098571 83.937714 109.042485 251.138784 234.421031 297.17119 267.923052 48.054459-34.715663 213.05542-161.193239 294.611901-267.451308 0.899486-1.12359 1.888-2.178619 2.964518-3.142573 50.03251-44.663222 92.312452-131.813095 92.312452-190.33086 0-123.391285-88.316441-223.788879-196.885136-223.788879-70.487378 0-137.808646 75.135234-173.241646 149.597133-7.634888 16.033162-33.908274 16.033162-41.543162 0C453.303668 240.203734 385.983423 165.0685 315.497069 165.0685z" fill="#2c2c2c" p-id="2410"></path></svg>
                <div class="value">{{ data.likeCount }}</div>
            </div>
            <div class="ops_item">
                <svg t="1725346601763" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4341" width="200" height="200"><path d="M157.568 751.296c-11.008-18.688-18.219-31.221-21.803-37.91A424.885 424.885 0 0 1 85.333 512c0-235.637 191.03-426.667 426.667-426.667S938.667 276.363 938.667 512 747.637 938.667 512 938.667a424.779 424.779 0 0 1-219.125-60.502A2786.56 2786.56 0 0 0 272.82 866.4l-104.405 28.48c-23.893 6.507-45.803-15.413-39.285-39.296l28.437-104.288z m65.301 3.787l-17.258 63.306 63.306-17.258a32 32 0 0 1 24.523 3.21 4515.84 4515.84 0 0 1 32.352 18.944A360.79 360.79 0 0 0 512 874.667c200.299 0 362.667-162.368 362.667-362.667S712.299 149.333 512 149.333 149.333 311.701 149.333 512c0 60.587 14.848 118.955 42.827 171.136 3.712 6.912 12.928 22.827 27.37 47.232a32 32 0 0 1 3.34 24.715z m145.995-70.774a32 32 0 1 1 40.917-49.205A159.19 159.19 0 0 0 512 672c37.888 0 73.675-13.173 102.187-36.885a32 32 0 0 1 40.917 49.216A223.179 223.179 0 0 1 512 736a223.179 223.179 0 0 1-143.136-51.69z" p-id="4342" fill="#2c2c2c"></path></svg>
                <div class="value">{{ data.commentCount }}</div>
            </div>
            <div class="ops_item">
                <svg t="1725346750571" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="8312" width="200" height="200"><path d="M512 619.38c29.23 0 59.78-45.83 59.78-107.6s-30.56-107.6-59.78-107.6c-29.22 0-59.78 45.83-59.78 107.6s30.55 107.6 59.78 107.6z m0 71.72c-76.42 0-131.5-82.63-131.5-179.33S435.58 332.45 512 332.45s131.5 82.63 131.5 179.33S588.42 691.1 512 691.1z m0 71.74c169.63 0 346.7-150.95 346.7-251.06S681.63 260.72 512 260.72c-164.37 0-346.7 160.27-346.7 251.06S347.63 762.84 512 762.84z m0 71.73c-202.08 0-418.44-190.18-418.44-322.79S309.92 188.99 512 188.99c206.94 0 418.44 180.3 418.44 322.79S718.94 834.57 512 834.57z" fill="#2c2c2c" p-id="8313"></path></svg>
                <div class="value">{{ data.view }}</div>
            </div>
        </div>
    </div>

    <div v-if="variant==='secondary'" class="secondary-variant">
        <div class="item" @click="toDetail">
            <div class="left">
                <img :src="config.minioUrl+data.user.avatar" alt="">
            </div>
            <div class="right">
                <div class="name">{{ data.user.username }}</div>
                <div class="time">{{displayTime(new Date(data.time))}}</div>
                <div class="content">
                    <div class="text">
                        {{data.content.text}}
                    </div>
                    <div class="attachments">

                        <div
                            v-for="(item2,index) in data.content.attachments"
                            class="attachment"

                            @mousedown.stop
                            @mouseup.stop
                        >
                            <el-image
                                style="width: 100%;height: 100%;"
                                :src="config.minioUrl+item2"
                                :fit="'cover'"

                                :preview-src-list="data.content.attachments.map(e=>e=config.minioUrl+e)"
                                :initial-index="index"
                            />
                        </div>
                    </div>
                </div>
                <div class="ops">
                    <div class="ops_item" @mouseup.stop @mousedown.stop @click="doLike(item)">
                        <svg t="1725346671253" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2409" width="200" height="200"><path d="M510.340709 900.892171c-23.173792 0-247.254314-180.180685-332.157006-289.301965-58.338686-53.264111-105.562221-152.650679-105.562221-222.732827 0-148.765185 108.952434-269.77626 242.874563-269.77626 81.781608 0 151.191444 67.679425 194.013739 137.605008 42.822294-69.925582 112.207571-137.605008 194.013739-137.605008 133.922129 0 242.874563 121.011074 242.874563 269.77626 0 70.239737-47.445592 169.805384-106.055454 223.182059C754.630505 722.553437 534.862195 900.892171 510.340709 900.892171zM315.497069 165.0685c-108.570741 0-196.887182 100.397594-196.887182 223.788879 0 58.382689 42.059931 145.353482 91.864244 189.903118 1.033539 0.943488 2.022053 1.977028 2.874467 3.098571 83.937714 109.042485 251.138784 234.421031 297.17119 267.923052 48.054459-34.715663 213.05542-161.193239 294.611901-267.451308 0.899486-1.12359 1.888-2.178619 2.964518-3.142573 50.03251-44.663222 92.312452-131.813095 92.312452-190.33086 0-123.391285-88.316441-223.788879-196.885136-223.788879-70.487378 0-137.808646 75.135234-173.241646 149.597133-7.634888 16.033162-33.908274 16.033162-41.543162 0C453.303668 240.203734 385.983423 165.0685 315.497069 165.0685z" fill="#2c2c2c" p-id="2410"></path></svg>
                        <div class="value">{{ data.likeCount }}</div>
                    </div>
                    <div class="ops_item" @mouseup.stop @mousedown.stop @click="doComment(item)" >
                        <svg t="1725346601763" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4341" width="200" height="200"><path d="M157.568 751.296c-11.008-18.688-18.219-31.221-21.803-37.91A424.885 424.885 0 0 1 85.333 512c0-235.637 191.03-426.667 426.667-426.667S938.667 276.363 938.667 512 747.637 938.667 512 938.667a424.779 424.779 0 0 1-219.125-60.502A2786.56 2786.56 0 0 0 272.82 866.4l-104.405 28.48c-23.893 6.507-45.803-15.413-39.285-39.296l28.437-104.288z m65.301 3.787l-17.258 63.306 63.306-17.258a32 32 0 0 1 24.523 3.21 4515.84 4515.84 0 0 1 32.352 18.944A360.79 360.79 0 0 0 512 874.667c200.299 0 362.667-162.368 362.667-362.667S712.299 149.333 512 149.333 149.333 311.701 149.333 512c0 60.587 14.848 118.955 42.827 171.136 3.712 6.912 12.928 22.827 27.37 47.232a32 32 0 0 1 3.34 24.715z m145.995-70.774a32 32 0 1 1 40.917-49.205A159.19 159.19 0 0 0 512 672c37.888 0 73.675-13.173 102.187-36.885a32 32 0 0 1 40.917 49.216A223.179 223.179 0 0 1 512 736a223.179 223.179 0 0 1-143.136-51.69z" p-id="4342" fill="#2c2c2c"></path></svg>
                        <div class="value">{{ data.commentCount }}</div>
                    </div>
                    <div class="ops_item">
                        <svg t="1725346750571" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="8312" width="200" height="200"><path d="M512 619.38c29.23 0 59.78-45.83 59.78-107.6s-30.56-107.6-59.78-107.6c-29.22 0-59.78 45.83-59.78 107.6s30.55 107.6 59.78 107.6z m0 71.72c-76.42 0-131.5-82.63-131.5-179.33S435.58 332.45 512 332.45s131.5 82.63 131.5 179.33S588.42 691.1 512 691.1z m0 71.74c169.63 0 346.7-150.95 346.7-251.06S681.63 260.72 512 260.72c-164.37 0-346.7 160.27-346.7 251.06S347.63 762.84 512 762.84z m0 71.73c-202.08 0-418.44-190.18-418.44-322.79S309.92 188.99 512 188.99c206.94 0 418.44 180.3 418.44 322.79S718.94 834.57 512 834.57z" fill="#2c2c2c" p-id="8313"></path></svg>
                        <div class="value">{{ data.view }}</div>
                    </div>
                </div>

            </div>
        </div>
    </div>

</div>
</template>

<style scoped lang="less">
.saying-node{

    .primary-variant{
        padding: 1rem;

        .writer{
            display: flex;
            align-items: center;
            margin-bottom: 1rem;

            img{
                width: 50px;
                height: 50px;
                border-radius: 50%;
                margin-right: 10px;
            }
            .text{

                .name{
                    font-size: 18px;
                }
                .time{
                    font-size: 14px;
                    color: gray;
                }
            }
        }
        .content{

        }
        .footer{

        }
        .ops{
            padding-top: 1rem;
            display: flex;

            .ops_item{
                flex-grow: 1;
                display: flex;
                align-items: center;
                justify-content: center;

                svg{
                    width: 15px;
                    height: 15px;
                }
                .value{
                    margin-left: 3px;
                }
            }
        }
    }
    .secondary-variant{

        .item{
            padding: 1rem;

            display: flex;

            .left{

                img{
                    width: 50px;
                    height: 50px;
                    border-radius: 50%;
                    margin-right: 10px;
                }
            }
            .right{
                flex-grow: 1;

                .name{
                    font-size: 18px;
                    font-weight: bold;
                    margin-bottom: 3px;
                }
                .time{
                    font-size: 14px;
                    color: gray;

                    margin-bottom: 10px;
                }
                .content{

                    .text{
                        margin-bottom: 1rem;
                        white-space: pre;
                    }
                    .attachments{
                        overflow: hidden;
                        display: grid;
                        gap: 10px;
                        grid-template-columns: repeat(3,1fr);

                        .attachment{
                            aspect-ratio: 1;
                            background-color: red;

                            /*这个是为了应对沙雕的elementui它的el-image组件把包裹img的div变成了行内块级元素，导致对图片的vertical-align不生效*/
                            div{
                                display: block;
                            }
                        }
                    }
                }
                .ops{
                    padding-top: 1rem;
                    display: flex;

                    .ops_item{
                        flex-grow: 1;
                        display: flex;
                        align-items: center;
                        justify-content: center;

                        svg{
                            width: 15px;
                            height: 15px;
                        }
                        .value{
                            margin-left: 3px;
                        }
                    }
                }
            }
        }
        .item:active{
            background-color: rgba(0,0,0,0.1);
        }
    }

}
</style>