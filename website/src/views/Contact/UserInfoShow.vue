<template>
    <div class="userInfoShow">
        <div class="container">
            <div class="part1">
                <img :src="config.minioUrl+detail.avatar" alt="">
                <div class="info">
                    <div class="name">
                        {{detail.username}}
                    </div>
                    <div class="uid">
                        UID {{detail.uid2}}
                    </div>
                    <div class="status">
                        <span :class="{offline:detail.status===0}"></span>
                        {{detail.status===0?'离线':'在线'}}
                    </div>
                </div>
            </div>
            <div class="line"></div>
            <div class="part2">
                <div class="item">
                    <div class="key">
                        <svg t="1709632449750" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="9959" width="200" height="200"><path d="M576 448h192a64 64 0 0 1 0 128H512a64 64 0 0 1-64-64V256a64 64 0 1 1 128 0v192z m-64 576A512 512 0 1 1 512 0a512 512 0 0 1 0 1024z m0-128A384 384 0 1 0 512 128a384 384 0 0 0 0 768z" fill="#999999" p-id="9960"></path></svg>
                        添加时间
                    </div>
                    <div class="value">{{new Date(detail.time).toLocaleString()}}</div>
                </div>
                <div class="item">
                    <div class="key">
                        <svg t="1709632376142" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="7267" width="200" height="200"><path d="M748.885333 935.253333H246.442667a170.666667 170.666667 0 0 1-170.666667-170.666666V261.802667a170.666667 170.666667 0 0 1 170.666667-170.666667h285.354666a34.133333 34.133333 0 0 1 0 68.266667H246.442667a102.4 102.4 0 0 0-72.362667 30.037333 102.4 102.4 0 0 0-30.037333 72.362667V764.586667a102.4 102.4 0 0 0 30.037333 72.362666 102.4 102.4 0 0 0 72.362667 30.037334h502.442666a102.4 102.4 0 0 0 102.4-102.4V477.866667a34.133333 34.133333 0 0 1 68.266667 0v286.72a170.666667 170.666667 0 0 1-170.666667 170.666666z" fill="#282424" p-id="7268"></path><path d="M497.664 546.133333a34.133333 34.133333 0 0 1-23.893333-10.24 34.133333 34.133333 0 0 1 0-48.128L844.117333 118.784a34.133333 34.133333 0 1 1 48.128 48.128L521.898667 537.258667a34.133333 34.133333 0 0 1-24.234667 8.874666zM682.666667 697.685333H313.344a34.133333 34.133333 0 0 1 0-68.266666H682.666667a34.133333 34.133333 0 0 1 0 68.266666z" fill="#282424" p-id="7269"></path></svg>
                        备注
                    </div>
                    <div class="value">{{detail.note}}</div>
                </div>
                <div class="item">
                    <div class="key">
                        <svg t="1709632263993" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4488" width="200" height="200"><path d="M914.262 907.418H109.741c-19.406 0-35.15 15.745-35.15 35.148 0 19.406 15.744 35.151 35.15 35.151h804.521c19.404 0 35.148-15.745 35.148-35.151 0-19.403-15.744-35.148-35.148-35.148M111.686 801.01H261.07a35.09 35.09 0 0 0 24.852-10.322l534.082-534.059a35.153 35.153 0 0 0 10.297-24.829 35.22 35.22 0 0 0-10.297-24.874L669.635 56.558a35.14 35.14 0 0 0-24.852-10.275h-0.023a35.196 35.196 0 0 0-24.898 10.321L86.765 591.646a35.278 35.278 0 0 0-10.229 24.829v149.386c0 19.38 15.744 35.149 35.15 35.149m533.142-669.829L745.449 231.8l-57.713 57.714-100.504-100.53 57.596-57.803zM146.833 630.983L537.62 238.779l100.412 100.414L246.516 730.71h-99.684v-99.727z" p-id="4489"></path></svg>
                        签名
                    </div>
                    <div class="value">{{detail.personalSignature}}</div>
                </div>
            </div>
            <div class="line"></div>
            <div class="part3">
                <div class="delete">删除</div>
                <div class="chat" @click="chat">发消息</div>
            </div>
        </div>
    </div>
    
</template>

<script>
import axios from "axios";
import config from "../../config/config.js";

export default {
    name:'UserInfoShow',
    computed: {
        config() {
            return config
        }
    },
    data(){
        return {
            detail: {}
        }
    },
    methods:{
        async getData(){
            let res=await axios.get("/api/contacts2?uid2="+this.$route.params.uid,{
                headers:{
                    token:localStorage.getItem('token')
                }
            });
            let json=res.data

            console.log(json)
            if(json.code===1){
                this.detail=json.data[0]
            }

        },
        async chat(){
            let res=await fetch('/api/messageNotices?targetUid='+this.$route.query.uid,{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            console.log(json);

            if(json.code===1){
                //编程路由导航
                this.$router.push({
                    path:'/one/chatwindow',
                    query:json.data.find(e=>e.uid2===this.$route.query.uid)
                })
            }
                
        },
    },
    created() {
        this.getData()
        this.$watch(()=>this.$route.params.uid,(newVal,oldVal)=>{
            this.getData()
        })
    },
    beforeRouteEnter(to,from,next){
        // console.log('进入');

        next()
    },
    beforeRouteLeave(to,from,next){
        // console.log('离开');

        next()
    }
}
</script>

<style scoped lang="less">
.userInfoShow{
    height: 100%;
    background: white;
    display: flex;
    justify-content: center;

    .container{
        width: 600px;
        padding-top: 70px;

        .line{
            height: 2px;
            background-color: #F5F5F5;
        }
        .part1{
            display: flex;
            margin-bottom: 18px;

            img{
                width: 120px;
                height: 120px;
                margin-right: 15px;
                border-radius: 50%;
            }
            .info{
                display: flex;
                flex-direction: column;
                justify-content: center;

                .name{
                    font-size: 23px;
                }
                .uid{
                    font-size: 14px;
                    color: #7F7F7F;
                    margin: 6px 0;
                }
                .status{
                    font-size: 18px;

                    span{
                        display: inline-block;
                        width: 18px;
                        height: 18px;
                        background: radial-gradient(#15DE86, #43F196);
                        border-radius: 50%;

                        margin-bottom: -2px;
                    }
                    span.offline{
                        background: radial-gradient(#B1B4C0, #C8CBD3);
                    }
                }
            }
        }
        .part2{

            .item{
                padding: 16px 0;
                display: flex;
                justify-content: space-between;
                font-size: 18px;

                .key{
                    svg{
                        width: 16px;
                        height: 16px;
                    }
                }
            }
        }
        .part3{
            display: flex;
            justify-content: center;

            .chat{
                width: 110px;
                box-sizing: border-box;
                padding: 10px 30px;
                color: white;
                background-color: #0099FF;
                margin-top: 32px;
                border-radius: 5px;
                text-align: center;
            }
            .chat:hover{
                cursor: pointer;
                background-color: #008DEB;
            }
            .delete{
                width: 110px;
                box-sizing: border-box;
                padding: 10px 30px;
                border: 1px solid #CCCCCC;
                margin-top: 32px;
                color: red;
                border-radius: 5px;

                margin-right: 10px;
                text-align: center;
            }
            .delete:hover{
                background-color: #F5F5F5;
                cursor: pointer;
            }
        }
    }
}
</style>