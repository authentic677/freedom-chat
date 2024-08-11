import {createStore} from "vuex";
import axios from "axios";
import {emitter} from "../utils/utils.js";
import router from "./vue-router.js";

export default createStore({
    state(){
        return {
            //登录注册相关的共享数据
            account:'',
            password:'',
            verifyCode:'',
            //邮箱登录相关数据
            email:'',
            username:'',

            ws:null
        }
    },
    mutations:{ //这里只能放同步操作
        setVerifyCode(state,v){
            state.verifyCode=v
        },
        setWs(state,v){
            state.ws=v
        }
    },
    actions:{ //这里可以放异步操作
        init(context){ //此处放身份认证之后的初始化逻辑
            console.log('身份认证后初始化')
            let token = localStorage.getItem('token');
            //获取当前用户信息
            fetch('/api/user/self',{
                headers:{
                    token:localStorage.getItem('token')
                }
            }).then(res=>res.json()).then(json=>{
                localStorage.setItem('user',JSON.stringify(json.data))
            })


            websocket()
            function websocket() {
                //建立websocket连接
                let ws=new WebSocket(`${location.protocol==='http:'?'ws':'wss'}://${location.hostname}:${location.port}/websocket`);
                ws.onopen=function () {
                    console.log('websocket open')
                    ws.send(JSON.stringify({
                        command:'auth',
                        token:localStorage.getItem('token')
                    }))
                }
                ws.onclose=function () {
                    console.log('websocket close')

                    setTimeout(websocket,3000)
                }
                ws.onmessage = function (event) {
                    let json=JSON.parse(event.data)
                    switch (json.command) {
                        case "auth":
                            console.log("auth result: "+json.result);
                            break;
                        case "dataUpdate":
                            //数据更新
                            json.dataPoints.forEach(e => {
                                switch (e) {
                                    case 'contactNotice':
                                        emitter.emit('contactNoticeUpdate')
                                        break;
                                    case 'contact':
                                        emitter.emit('contactUpdate')
                                        break
                                    case 'message':
                                        emitter.emit('messageUpdate')
                                        break
                                    case 'messageNotice':
                                        emitter.emit('messageNoticeUpdate')
                                        break
                                    default:
                                        break;
                                }
                            });
                            break
                        case 'video-request':
                            router.push(`/live/video/from/${json.caller}`)
                            break
                        case 'video-response':
                            if(json.status==='agree'){
                                emitter.emit('peerAgree',json.id)
                            }
                            break
                        default:
                            break;
                    }
                }
                context.commit('setWs',ws)
            }

        }
    }
})