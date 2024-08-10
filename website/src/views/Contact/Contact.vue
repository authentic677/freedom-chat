<script>
import Navigator from "../../components/Navigator.vue";
import SearchBox from "../../components/SearchBox.vue";
import ContactSearch from "../../components/ContactSearch.vue";
import {emitter} from "../../utils/utils.js";
import config from "../../config/config.js";
import CreateGroup from "../../components/CreateGroup.vue";

export default {
    name: "Contact",
    components: {CreateGroup, ContactSearch, SearchBox, Navigator},
    data(){
        return {
            isContact:true, //标识显示的是好友还是群聊
            contacts:[], //数据

            isSearch:false, //标识是否打开联系人搜索框
            isCreateGroup:false //标识创建群组件是否显示
        }
    },
    methods:{
        //获取联系人数据
        async getData(){
            //先清空一下数据
            this.contacts=[]
            let res=await fetch('/api/contacts',{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            if(json.code===1){
                //键名转换和日期格式处理
                json.data.forEach(e => {
                    e.time=new Date(e.time).toLocaleString()
                    e.uid=e.uid2
                    e.name=e.username
                    e.signature=e.personalSignature
                });
                this.contacts=json.data
            }

            console.log(json);
        },
        //获取群聊数据
        async getData2(){
            //先清空一下数据
            this.contacts=[]
            let res=await fetch('/api/group',{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            if(json.code===1){
                this.contacts=json.data
            }

            console.log(json);
        }
    },
    computed:{
        config() {
            return config
        },
        contacts2(){
            if(this.isContact){
                return this.contacts.sort((a,b)=>{ //在线的显示在离线的前面
                    return b.status-a.status
                })
            }else{
                return this.contacts
            }

        }
    },
    watch:{
        isContact(){
            this.isContact?this.getData():this.getData2()
        }
    },
    created(){
        this.getData()

        emitter.on('contactUpdate',this.getData)
    },
    beforeUnmount() {
        emitter.off('contactUpdate',this.getData)
    }
}
</script>

<template>
    <div class="contact">
        <div class="left">
            <navigator/>
        </div>
        <div class="right">
            <div class="listBar">
                <div class="top">

                    <div class="search">
                        <SearchBox placeholder="在联系人中搜索" class="s" />

                        <div class="add" >
                            <svg t="1709628183096" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2415" width="200" height="200"><path d="M863.328262 481.340895l-317.344013 0.099772L545.984249 162.816826c0-17.664722-14.336138-32.00086-32.00086-32.00086s-31.99914 14.336138-31.99914 32.00086l0 318.400215-322.368714-0.17718c-0.032684 0-0.063647 0-0.096331 0-17.632039 0-31.935493 14.239806-32.00086 31.904529-0.096331 17.664722 14.208843 32.031824 31.871845 32.095471l322.59234 0.17718 0 319.167424c0 17.695686 14.336138 32.00086 31.99914 32.00086s32.00086-14.303454 32.00086-32.00086L545.982529 545.440667l317.087703-0.099772c0.063647 0 0.096331 0 0.127295 0 17.632039 0 31.935493-14.239806 32.00086-31.904529S880.960301 481.404542 863.328262 481.340895z" fill="#575B66" p-id="2416"></path></svg>
                            <div class="addFrame">
                                <div class="item" @click="isCreateGroup=true">创建群聊</div>
                                <div class="item" @click="isSearch=true">添加好友/群</div>
                            </div>
                        </div>
                    </div>

                    <router-link to="/contact/friendNotification">
                        <div class="notification">
                            <span>好友通知</span>
                            <svg t="1709627298123" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1447" width="200" height="200"><path d="M307.6 104.6c-14.2 14.2-14.2 37.2 0 51.4L655 503.4c2.8 2.9 2.8 7.5 0 10.3L307.6 861.2c-14.2 14.2-14.2 37.2 0 51.4 14.2 14.2 37.2 14.2 51.4 0l347.4-347.4c15.6-15.6 23.4-36 23.4-56.5s-7.8-41-23.4-56.5L359 104.6c-14.2-14.2-37.2-14.2-51.4 0z" p-id="1448"></path></svg>
                        </div>
                    </router-link>
                    <router-link to="/contact/groupNotification">
                        <div class="notification">
                            <span>群通知</span>
                            <svg t="1709627298123" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1447" width="200" height="200"><path d="M307.6 104.6c-14.2 14.2-14.2 37.2 0 51.4L655 503.4c2.8 2.9 2.8 7.5 0 10.3L307.6 861.2c-14.2 14.2-14.2 37.2 0 51.4 14.2 14.2 37.2 14.2 51.4 0l347.4-347.4c15.6-15.6 23.4-36 23.4-56.5s-7.8-41-23.4-56.5L359 104.6c-14.2-14.2-37.2-14.2-51.4 0z" p-id="1448"></path></svg>
                        </div>
                    </router-link>

                    <div class="line"></div>
                    <div class="switch">
                        <div class="item" :class="isContact?'hightLight':''" @click="isContact=true">好友</div>
                        <div class="item" :class="!isContact?'hightLight':''" @click="isContact=false">群聊</div>
                    </div>
                </div>
                <div class="list">

                    <div class="friend" v-if="isContact">
                        <router-link
                            v-for="item in contacts2" :key="item.id"
                            :to="`/contact/user/${item.uid2}`"
                        >

                            <div class="item">
                                <div class="avatar" :class="{offline:item.status===0}">
                                    <img :src="config.minioUrl+item.avatar" alt="">
                                </div>

                                <div class="info">
                                    <div class="name">{{item.name}}</div>
                                    <div class="status">
                                        [
                                        <span :class="{offline:item.status===0}"></span>
                                        {{item.status===0?'离线':'在线'}}
                                        ]
                                        {{item.signature}}
                                    </div>
                                </div>
                            </div>
                        </router-link>
                    </div>
                    <div class="group" v-if="!isContact">
                        <router-link
                            v-for="item in contacts" :key="item.id"
                            :to="`/contact/group/${item.gid}`"
                        >
                            <div class="item">
                                <div class="avatar">
                                    <img :src="config.minioUrl+item.group.avatar" alt="">
                                </div>

                                <div class="info">
                                    <div class="name">{{item.group.name}}</div>
                                </div>
                            </div>
                        </router-link>
                    </div>

                </div>
            </div>
            <div class="detail">
                <router-view></router-view>
            </div>
        </div>

        <ContactSearch v-if="isSearch" @close="isSearch=false" />
        <CreateGroup v-if="isCreateGroup" @close="isCreateGroup=false"  />
    </div>
</template>

<style scoped lang="less">
.contact{
    height: 100vh;
    display: flex;

    .left{

    }
    .right{
        flex-grow: 1;

        display: flex;

        .listBar{
            display: flex;
            width: 400px;
            flex-direction: column;
            background-color: white;
            border-right: 1px solid #E9E9E9;

            .top{
                .search{
                    display: flex;
                    padding: 45px 20px 20px;

                    .s{
                        flex-grow: 1;
                    }
                    .add{
                        width: 35px;
                        height: 35px;
                        border-radius: 5px;
                        background-color: #F5F5F5;
                        margin-left: 10px;

                        display: flex;
                        justify-content: center;
                        align-items: center;

                        position: relative;

                        svg{
                            width: 14px;
                            height: 14px;
                        }
                        .addFrame{
                            display: none;

                            position: absolute;
                            top: 100%;
                            left: 0;

                            width: 150px;
                            background-color: white;
                            border-radius: 10px;
                            box-shadow: 0px 0px 10px rgba(0,0,0,0.4);
                            padding: 5px;

                            .item{
                                padding: 5px;
                                cursor: pointer;
                            }
                            .item:hover{
                                background-color: #F5F5F5;
                            }
                        }
                    }
                    .add:hover{
                        background-color: #EBEBEB;
                    }
                    .add:hover .addFrame{
                        display: block;
                    }
                }

                a{
                    text-decoration: none;
                    color: black;
                    cursor: default;

                    .notification{
                        display: flex;
                        justify-content: space-between;
                        padding: 15px 20px;

                        svg{
                            width: 15px;
                            height: 15px;
                            vertical-align: bottom;
                        }
                    }
                    .notification:hover{
                        background-color: #F5F5F5;
                    }
                }

                .line{
                    height: 1px;
                    background-color: #F5F5F5;
                }
                .switch{
                    display: flex;
                    justify-content: space-between;
                    padding: 5px;
                    background-color: #F5F5F5;
                    margin: 20px;
                    border-radius: 5px;

                    .item{
                        padding: 8px 66px;
                        border-radius: 5px;
                    }
                    .item:hover{
                        cursor: pointer;
                    }
                    .item:first-child{
                        margin-right: 5px;
                    }
                    .item.hightLight{
                        color: #0099FF;
                        background-color: #FFFFFF;
                    }
                }
            }

            .list{
                flex-grow: 1;
                overflow: auto;

                .friend{
                    a {
                        color: inherit;
                        text-decoration: none;
                        display: block;
                    }
                    a:hover{
                        cursor: default;
                        background-color: #EBEBEB;
                    }
                    a.router-link-active{
                        background-color: #0099FF;
                        color: white;

                        .item{
                            .status{
                                color: white;
                            }
                        }
                    }
                    .item{
                        padding: 20px;
                        display: flex;

                        .avatar{
                            margin-right: 10px;

                            img{
                                width: 50px;
                                height: 50px;
                                border-radius: 50%;
                            }
                        }
                        .avatar.offline{
                            opacity: 0.3;
                        }
                        .info{
                            flex-grow: 1;

                            .name{
                                font-size: 18px;
                                margin-bottom: 2px;
                            }
                            .status{
                                color: #999999;

                                span{
                                    display: inline-block;
                                    width: 15px;
                                    height: 15px;
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
                }
                .group{
                    a {
                        color: inherit;
                        text-decoration: none;
                        display: block;
                    }
                    a:hover{
                        cursor: default;
                        background-color: #EBEBEB;
                    }
                    a.router-link-active{
                        background-color: #0099FF;
                        color: white;

                        .item{
                            .status{
                                color: white;
                            }
                        }
                    }
                    .item{
                        padding: 20px;
                        display: flex;

                        align-items: center;

                        .avatar{
                            display: flex;
                            margin-right: 10px;

                            img{
                                width: 50px;
                                height: 50px;
                                border-radius: 50%;
                            }
                        }
                        .info{
                            flex-grow: 1;

                            .name{
                                font-size: 18px;
                            }
                        }
                    }
                }


            }
        }
        .detail{
            flex-grow: 1;
            background-color: #F2F2F2;
            background-image: url('/logo3.png');
            background-repeat: no-repeat;
            background-position: center;
            background-size: 150px;
        }
    }
}
</style>