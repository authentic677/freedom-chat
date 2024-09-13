import { createWebHashHistory, createRouter } from 'vue-router'
import Home from "../views/Home.vue";
import Loader from "../views/Loader.vue";
import Login from "../views/authentication/Login.vue";
import Register from "../views/authentication/Register.vue";
import ResetPassword from "../views/authentication/ResetPassword.vue";
import Authentication from "../views/authentication/Authentication.vue";
import Test from "../views/Test.vue";
import Message from "../views/message/Message.vue";
import Contact from "../views/Contact/Contact.vue";
import UserChatFrame from "../views/message/UserChatFrame.vue";
import GroupChatFrame from "../views/message/GroupChatFrame.vue";
import UserInfoShow from "../views/Contact/UserInfoShow.vue";
import FriendNotification from "../views/Contact/FriendNotification.vue";
import Self from "../views/self/Self.vue";
import store from "./vuex.js";
import GroupNotification from "../views/Contact/GroupNotification.vue";
import Live from "../views/live/Live.vue";
import AudioLiveCaller from "../views/live/AudioLiveCaller.vue";
import VideoLiveCaller from "../views/live/VideoLiveCaller.vue";
import VideoLiveCallee from "../views/live/VideoLiveCallee.vue";
import GroupInfoShow from "../views/Contact/GroupInfoShow.vue";
import * as path from "node:path";

const router= createRouter({
    history: createWebHashHistory(),
    routes:[
        {
            path:'/',component:Loader
        },
        {
            path:'/authentication',component:Authentication,
            children:[
                {
                    path:'login',component:Login
                },
                {
                    path:'register',component:Register
                },
                {
                    path:'resetPassword',component:ResetPassword
                },
            ]
        },
        {
            path:'/message',component:Message,
            children:[
                {
                    path:'user/:uid',component:UserChatFrame,
                },
                {
                    path:'group/:gid',component:GroupChatFrame,
                }
            ]
        },
        {
            path:'/contact',component:Contact,
            children:[
                {
                    path:'user/:uid',component:UserInfoShow,
                },
                {
                    path:'group/:gid',component:GroupInfoShow,
                },
                {
                    path:'friendNotification',component: FriendNotification,
                },
                {
                    path:'groupNotification',component: GroupNotification,
                }
            ]
        },
        {
            path:'/self',component:Self,
        },
        {
            path:'/live',component:Live,
            children:[
                {
                    path: 'audio/to/:uid',component: AudioLiveCaller
                },
                {
                    path: 'video/to/:uid',component: VideoLiveCaller
                },
                {
                    path: 'video/from/:uid',component: VideoLiveCallee
                }
            ]
        },
        {
            path:'/zone/:uid',component:()=>import('../views/zone/Zone.vue'),
            children:[
                {
                    path:'saying',component: ()=>import('../views/zone/Saying.vue'),
                    props:true, //此属性为真时，路由参数(params)当作props传递给路由子组件
                },
                {
                    path:'sayingDetail/:id',component:()=>import('../views/zone/SayingDetail.vue'),
                }
            ]
        },
        {
            path:'/test',component:Test
        }
    ]
})

//路由前置守卫
router.beforeEach(async (to, from) => {
    //使用条件，避免登录后每次切换路由都请求一次很脓肿
    if(from.path==='/'||from.path.startsWith('/authentication')){ //刷新时会触发，而普通路由切换不会触发
        console.log('身份验证')
        console.log(to,from)
        let token=localStorage.getItem("token")
        //核实token是否有效
        let res = await fetch('/api/checkToken', {
            headers: {token}
        })
        let json = await res.json()

        if (json.code === 1) { //验证成功
            store.dispatch('init',router)
        }else if (!to.path.startsWith('/authentication')) { //token无效
            //去登录
            router.push('/authentication/login')
        }
    }

})

export default router