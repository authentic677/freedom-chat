<script>
import Peer from 'peerjs'
import {emitter} from "../../utils/utils.js";

export default {
    name: "VideoLiveCaller",
    data(){
        return {
            mode:1,//视频显示模式

            peer:null,
            localVideo :null,
            remoteVideo :null,

            videoStyle:['large','small'],


            status:0, //0表示还在呼叫，1表示正在进行
        }
    },
    methods:{
        startCall(id){

            const call = this.peer.call(id, this.localVideo.srcObject);
            call.on('stream', remoteStream => {
                this.remoteVideo.srcObject = remoteStream;
            });
        }
    },
    created() {

        this.peer = new Peer(undefined, {
            // host: '677123.xyz',
            // port: 9000,
            // path: '/app',
            secure: true, // Set to true if using HTTPS,
            config:{
                iceServers:[
                    {
                        url:'turn:45.77.178.74:3478',
                        username: 'f', credential: 'f' //这个用户名和密码是随机指定的，但是如果不随机指定它不正常工作
                    }
                ],
                sdpSemantics: 'unified-plan'

            }
        });

        this.peer.on('open', id => {
            // document.getElementById('id').innerText=id
            console.log('My peer ID is: ' + id);
        });

        this.peer.on('call', call => {
            navigator.mediaDevices.getUserMedia({ video: true }).then(stream => {
                this.localVideo.srcObject = stream;
                call.answer(stream);
                call.on('stream', remoteStream => {
                    this.remoteVideo.srcObject = remoteStream;
                });
            }).catch(err => {
                console.error('Failed to get local stream', err);
            });
        });

        //通知对方
        this.$store.state.ws.send(JSON.stringify({
            command: 'video-request',
            callee:this.$route.params.uid
        }))
        //监听对方的回复
        emitter.on('peerAgree',id=>{
            this.startCall(id)
            this.status=1 //通话进行
        })

    },
    async mounted() {
        this.localVideo = this.$refs.localVideo
        this.remoteVideo = this.$refs.remoteVideo

        let stream=await navigator.mediaDevices.getUserMedia({ video: true,audio:true })
        this.localVideo.srcObject = stream;
    },
    beforeUnmount() {
        this.peer.destroy()
        //关闭本地媒体流
        this.localVideo.srcObject.getTracks().forEach(track=>{
            track.stop()
        })

        emitter.off('peerAgree')
    }
}
</script>

<template>
<div class="video-live-caller">
    <div class="top">
        {{status===0? '等待对方接听':'正在通话中' }}
    </div>
    <div class="middle">
        <div :class="{mode1:mode===1,mode2:mode===2}">
            <div :class="videoStyle[0]" @click="videoStyle.reverse()">
                <video ref="localVideo" autoplay></video>
            </div>
            <div :class="videoStyle[1]" @click="videoStyle.reverse()">
                <video ref="remoteVideo" autoplay></video>
            </div>
        </div>
    </div>
    <div class="bottom">
        <div class="item">
            <div class="mode" @click="mode===1?mode=2:mode=1">
                <svg v-if="mode===1" t="1722092662889" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="9821" width="200" height="200"><path d="M960 149.333333h-896v725.333334h896v-725.333334z m-42.666667 42.666667v640h-810.666666v-640h810.666666z" fill="#ffffff" p-id="9822"></path><path d="M490.666667 256H170.666667v512h320V256z m-42.666667 42.666667v426.666666H213.333333V298.666667h234.666667zM853.333333 256h-320v512H853.333333V256z m-42.666666 42.666667v426.666666h-234.666667V298.666667H810.666667z" fill="#ffffff" p-id="9823"></path></svg>
                <svg v-if="mode===2" t="1722130310458" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="12645" width="200" height="200"><path d="M768 512h-341.333333c-25.6 0-42.666667 17.066667-42.666667 42.666667v128c0 25.6 17.066667 42.666667 42.666667 42.666666h341.333333c25.6 0 42.666667-17.066667 42.666667-42.666666v-128c0-25.6-17.066667-42.666667-42.666667-42.666667z" p-id="12646" fill="#ffffff"></path><path d="M853.333333 170.666667H170.666667c-46.933333 0-85.333333 38.4-85.333334 85.333333v512c0 46.933333 38.4 85.333333 85.333334 85.333333h682.666666c46.933333 0 85.333333-38.4 85.333334-85.333333V256c0-46.933333-38.4-85.333333-85.333334-85.333333z m42.666667 597.333333c0 25.6-17.066667 42.666667-42.666667 42.666667H170.666667c-25.6 0-42.666667-17.066667-42.666667-42.666667V256c0-25.6 17.066667-42.666667 42.666667-42.666667h682.666666c25.6 0 42.666667 17.066667 42.666667 42.666667v512z" p-id="12647" fill="#ffffff"></path></svg>
            </div>
            <div class="text">{{mode===1? '宫格模式':'画中画' }}</div>
        </div>
        <div class="item">
            <div class="hang" @click="$router.push('/message')">
                <svg t="1722092585393" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6117" width="200" height="200"><path d="M905.2 447.2c-46-55.1-119.2-92.2-217.7-110.1-75.8-13.8-144.6-12.2-170.6-11.5-3.2 0.1-5.9 0.1-7.3 0.1s-4.2-0.1-7.3-0.1c-25.9-0.6-94.8-2.3-170.6 11.5C233.3 355 160 392.1 114 447.2c-41.9 50.2-53.9 96.5-51.3 123.1 1.5 15.2 7.8 42.6 18 66.8 15.8 37.5 36.8 58.2 62.4 61.6 2.8 0.4 5.8 0.6 8.9 0.6 30.4 0 71.4-16.9 88.7-24.8 29.8-13.5 67.5-34.3 78.1-57.1 10.7-23 11-41.1 11.2-54.4 0.2-10.5 0.4-14.6 3.4-19.6 1.3-1.3 15.2-13.4 78.4-23.4 40.3-6.3 81.3-8.7 97.7-8.7s57.3 2.4 97.7 8.7c63.2 9.9 77 22.1 78.4 23.4 3 5 3.2 9 3.4 19.6 0.2 13.2 0.5 31.3 11.2 54.4 10.6 22.8 48.3 43.7 78.1 57.1 17.4 7.8 58.4 24.8 88.7 24.8 3.1 0 6.1-0.2 8.9-0.6 25.6-3.4 46.6-24.1 62.4-61.6 10.2-24.3 16.5-51.7 18-66.8 2.9-26.6-9.2-72.9-51.1-123.1z" fill="#ffffff" p-id="6118"></path></svg>
            </div>
            <div class="text">挂断</div>
        </div>
    </div>

</div>
</template>

<style scoped lang="less">
.video-live-caller{
    height: 100%;
    background-color: #1A1A1A;

    display: flex;
    flex-direction: column;

    .top{
        padding: 0.5rem 0;
        color: white;
        text-align: center;
    }
    .middle{
        flex-grow: 1;

        .mode1{
            height: 100%;
            padding: 0.5rem;
            position: relative;

            display: flex;
            justify-content: center;

            .large{
                height: 100%;
                border: 1px solid gray;
                border-radius: 10px;

                video{
                    height: 100%;
                    max-width: 100%;
                    border-radius: 10px;
                }
            }
            .small{
                position: absolute;
                right: 2rem;
                bottom: 2rem;
                width: 300px;
                height: 150px;

                border: 1px solid gray;
                border-radius: 10px;
                overflow: hidden;
                background-color: black;

                display: flex;
                justify-content: center;

                video{
                    height: 100%;
                    max-width: 100%;
                }
            }
        }
        .mode2{
            height: 100%;

            display: flex;
            justify-content: space-around;
            align-items: center;

            .large{
                height: 100%;
                border: 1px solid gray;
                border-radius: 10px;

                video{
                    height: 100%;
                    max-width: 100%;
                    border-radius: 10px;
                }
            }
            .small{
                height: 100%;
                border: 1px solid gray;
                border-radius: 10px;

                video{
                    height: 100%;
                    max-width: 100%;
                    border-radius: 10px;
                }
            }
        }
    }
    .bottom{
        padding: 2rem 0;

        display: flex;
        justify-content: center;

        .item{
            margin: 0 1rem;
            display: flex;
            flex-direction: column;
            align-items: center;

            .text{
                margin-top: 0.5rem;
                color: white;
            }
            .hang{
                width: 50px;
                height: 50px;
                border-radius: 10px;
                background-color: #FF3352;

                display: flex;
                justify-content: center;
                align-items: center;

                svg{
                    width: 30px;
                    height: 30px;
                }
            }
            .mode{
                width: 50px;
                height: 50px;
                border-radius: 10px;
                background-color: #484848;

                display: flex;
                justify-content: center;
                align-items: center;

                svg{
                    width: 30px;
                    height: 30px;
                }
            }
        }
    }
}
</style>