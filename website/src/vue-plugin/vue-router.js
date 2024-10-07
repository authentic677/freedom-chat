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
import Journal from "../views/zone/Journal.vue";
import JournalEditor from "../views/zone/JournalEditor.vue";
import JournalViewer from "../views/zone/JournalViewer.vue";
import GuestBook from "../views/zone/GuestBook.vue";
import Album from "../views/zone/Album.vue";
import AlbumDetail from "../views/zone/AlbumDetail.vue";
import Official from "../views/official/Official.vue";
import OfficialDetail from "../views/official/OfficialDetail.vue";
import OfficialArticle from "../views/official/OfficialArticle.vue";
import OfficialCreativeSpace from "../views/official/OfficialCreativeSpace.vue";
import OfficialArticleManage from "../views/official/OfficialArticleManage.vue";
import OfficialArticleEditor from "../views/official/OfficialArticleEditor.vue";
import OfficialGongzhonghaoManage from "../views/official/OfficialGongzhonghaoManage.vue";
import Shipinhao from "../views/shipinhao/Shipinhao.vue";
import ShipinhaoConsumerSide from "../views/shipinhao/ShipinhaoConsumerSide.vue";
import ShipinhaoProducerSide from "../views/shipinhao/ShipinhaoProducerSide.vue";
import ShipinhaoProfile from "../views/shipinhao/ShipinhaoProfile.vue";
import ShipinhaoPlayer from "../views/shipinhao/ShipinhaoPlayer.vue";

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
                },
                {
                    path:'journal',component: Journal
                },
                {
                    path:'journalEditor',component: JournalEditor
                },
                {
                    path:'journalViewer/:id',component: JournalViewer
                },
                {
                    path:'guestBook',component: GuestBook
                },
                {
                    path:'album',component: Album
                },
                {
                    path: 'albumDetail/:id',component: AlbumDetail
                }
            ]
        },
        {
            path:'/official',component:Official,
            children:[
                {
                    path: 'detail/:id',component:OfficialDetail
                },
                {
                    path: 'article/:id',component:OfficialArticle
                },
            ]
        },
        {
            path: '/creativeSpace/official',component:OfficialCreativeSpace,
            children:[
                {
                    path:'manage/article',component:OfficialArticleManage
                },
                {
                    path: 'manage/article/editor',component: OfficialArticleEditor
                },
                {
                    path: 'manage/gongzhonghao',component: OfficialGongzhonghaoManage
                }
            ]
        },
        {
            path:'/test',component:Test
        },
        {
            path:'/shipinhao',component:Shipinhao,
            children:[
                {
                    path: 'consumer',component: ShipinhaoConsumerSide,
                    children:[
                        {
                            path:'',component:ShipinhaoPlayer
                        },
                        {
                            path:'profile/:id',component:ShipinhaoProfile
                        }
                    ]
                },
                {
                    path: 'producer',component: ShipinhaoProducerSide
                }
            ]
        }
    ]
})

//路由前置守卫
router.beforeEach(async (to, from) => {
    //使用条件，避免登录后每次切换路由都请求一次很脓肿
    if(from.path==='/'||from.path.startsWith('/authentication')&&!to.path.startsWith('/test')){ //刷新时会触发，而普通路由切换不会触发
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