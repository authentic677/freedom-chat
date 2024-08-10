<script>
import config from "../../config/config.js";
import axios from "axios";
import {ElMessage, ElMessageBox} from "element-plus";

export default {
    name: "GroupInfoShow",
    data(){
        return {
            //数据还未到达时的变量结构
            detail:{
                group: {}
            }
        }
    },
    computed: {
        config() {
            return config
        }
    },
    methods:{
        async getData(){
            let res=await axios.get(`/api/group/${this.$route.params.gid}`,{
                headers:{
                    token:localStorage.getItem('token')
                }
            });
            let json=res.data

            console.log(json)
            if(json.code===1){
                this.detail=json.data
            }

        },
        async setNote(){

            let newNote
            try {
                let {value}=await ElMessageBox.prompt('请输入备注', '修改备注', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                })
                console.log(value)
                newNote=value
            }catch(err){
                console.log(err)
                return
            }

            let res=await fetch('/api/group',{
                method:'PUT',
                headers:{
                    token:localStorage.getItem('token'),
                    'content-type':'application/json;charset=utf-8'
                },
                body:JSON.stringify({
                    gid:this.$route.params.gid,
                    note:newNote,
                })
            })
            let json=await res.json()

            console.log(json)
        },
        async setNickName(){

            let newNickname
            try {
                let {value}=await ElMessageBox.prompt('请输入在群内昵称', '修改群昵称', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                })
                console.log(value)
                newNickname=value
            }catch(err){
                console.log(err)
                return
            }

            let res=await fetch('/api/group',{
                method:'PUT',
                headers:{
                    token:localStorage.getItem('token'),
                    'content-type':'application/json;charset=utf-8'
                },
                body:JSON.stringify({
                    gid:this.$route.params.gid,
                    nickname:newNickname,
                })
            })
            let json=await res.json()

            console.log(json)
        },
        async setIntroduce(){
            //鉴权
            if(this.detail.role==='ordinary'){
                return
            }

            let newIntroduce
            try {
                let {value}=await ElMessageBox.prompt('请输入群介绍', '修改群介绍', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                })
                console.log(value)
                newIntroduce=value
            }catch(err){
                console.log(err)
                return
            }

            let res=await fetch('/api/groupAdmin',{
                method:'PUT',
                headers:{
                    token:localStorage.getItem('token'),
                    'content-type':'application/json;charset=utf-8'
                },
                body:JSON.stringify({
                    gid:this.$route.params.gid,
                    description:newIntroduce,
                })
            })
            let json=await res.json()

            console.log(json)
        }

    },
    created() {
        this.getData()
        this.$watch(()=>this.$route.params.gid,(newVal,oldVal)=>{
            this.getData()
        })
    },
}
</script>

<template>
<div class="group-info-show">
    <div class="container">
        <div class="part1">
            <img :src="config.minioUrl+detail.group.avatar" alt="">
            <div class="info">
                <div class="name">
                    {{detail.group.name}}
                </div>
                <div class="uid">
                    GID: {{detail.gid}}
                </div>
            </div>
        </div>
        <div class="line"></div>
        <div class="part2">
            <div class="item">
                <div class="key">
                    <svg t="1723190913640" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4293" width="200" height="200"><path d="M695.450267 0.031999l-534.127309 0c-36.670854 0-66.493922 29.75907-66.493922 66.333927l0 891.268148c0 36.606856 29.823068 66.333927 66.461923 66.333927l701.418081 0c36.638855 0 66.461923-29.75907 66.461923-66.397925l0-713.001719-233.720696-244.568357zM682.650667 68.989844l169.658698 177.530452-160.122996 0c-5.247836 0-9.535702-4.287866-9.535702-9.535702l0-167.99475zM862.70904 967.105778l-701.418081 0c-5.279835 0-9.567701-4.255867-9.567701-9.471704l0-891.268148c0-5.215837 4.319865-9.471704 9.5997-9.471704l464.433486 0 0 180.090372c0 36.638855 29.791069 66.429924 66.429924 66.429924l180.090372 0 0 654.187557c0 5.247836-4.319865 9.503703-9.567701 9.503703z" fill="#424251" p-id="4294"></path><path d="M730.073185 474.097184l-436.14637 0c-15.711509 0-28.447111 12.735602-28.447111 28.447111s12.735602 28.447111 28.447111 28.447111l436.14637 0c15.711509 0 28.447111-12.735602 28.447111-28.447111s-12.735602-28.447111-28.447111-28.447111z" fill="#424251" p-id="4295"></path><path d="M578.365926 644.747852l-284.439111 0c-15.711509 0-28.447111 12.735602-28.447111 28.447111s12.735602 28.447111 28.447111 28.447111l284.439111 0c15.711509 0 28.447111-12.735602 28.447111-28.447111s-12.735602-28.447111-28.447111-28.447111z" fill="#000000" p-id="4296"></path></svg>
                    群介绍
                </div>
                <div class="value"  @click="setIntroduce">
                    <div v-if="detail.group.description">{{detail.group.description}}</div>
                    <div v-else v-if="detail.role!=='ordinary'">点击设置群介绍</div>
                </div>
            </div>
            <div class="item">
                <div class="key">
                    <svg t="1723191011425" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6183" width="200" height="200"><path d="M494.4 171.2l-155.2 97.6C318.4 281.6 296 288 272 288h-76.8C131.2 288 80 339.2 80 401.6v225.6C80 696 136 752 204.8 752h89.6c24 0 46.4 6.4 67.2 19.2L496 852.8c9.6 6.4 20.8 9.6 33.6 9.6 35.2 0 64-28.8 64-64V225.6c0-11.2-3.2-24-9.6-33.6-20.8-30.4-59.2-40-89.6-20.8zM528 798.4l-134.4-81.6c-30.4-17.6-64-28.8-99.2-28.8h-89.6c-33.6 0-60.8-27.2-60.8-60.8V401.6c0-27.2 22.4-49.6 49.6-49.6H272c36.8 0 70.4-9.6 102.4-28.8l155.2-97.6v572.8zM912 480H720c-17.6 0-32 14.4-32 32s14.4 32 32 32h192c17.6 0 32-14.4 32-32s-14.4-32-32-32z m-12.8 272l-166.4-96c-16-9.6-35.2-3.2-43.2 11.2-9.6 16-3.2 35.2 11.2 43.2l166.4 96c16 9.6 35.2 3.2 43.2-11.2 9.6-14.4 4.8-33.6-11.2-43.2z m-168-384l166.4-96c16-9.6 20.8-28.8 11.2-43.2-9.6-16-28.8-20.8-43.2-11.2l-166.4 96c-16 9.6-20.8 28.8-11.2 43.2s28.8 20.8 43.2 11.2z" fill="#333333" p-id="6184"></path></svg>
                    群公告
                </div>
                <div class="value">{{detail.personalSignature}}</div>
            </div>
            <div class="item">
                <div class="key">
                    <svg t="1709632449750" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="9959" width="200" height="200"><path d="M576 448h192a64 64 0 0 1 0 128H512a64 64 0 0 1-64-64V256a64 64 0 1 1 128 0v192z m-64 576A512 512 0 1 1 512 0a512 512 0 0 1 0 1024z m0-128A384 384 0 1 0 512 128a384 384 0 0 0 0 768z" fill="#999999" p-id="9960"></path></svg>
                    添加时间
                </div>
                <div class="value">{{new Date(detail.joinTime).toLocaleString()}}</div>
            </div>
            <div class="item">
                <div class="key">
                    <svg t="1709632376142" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="7267" width="200" height="200"><path d="M748.885333 935.253333H246.442667a170.666667 170.666667 0 0 1-170.666667-170.666666V261.802667a170.666667 170.666667 0 0 1 170.666667-170.666667h285.354666a34.133333 34.133333 0 0 1 0 68.266667H246.442667a102.4 102.4 0 0 0-72.362667 30.037333 102.4 102.4 0 0 0-30.037333 72.362667V764.586667a102.4 102.4 0 0 0 30.037333 72.362666 102.4 102.4 0 0 0 72.362667 30.037334h502.442666a102.4 102.4 0 0 0 102.4-102.4V477.866667a34.133333 34.133333 0 0 1 68.266667 0v286.72a170.666667 170.666667 0 0 1-170.666667 170.666666z" fill="#282424" p-id="7268"></path><path d="M497.664 546.133333a34.133333 34.133333 0 0 1-23.893333-10.24 34.133333 34.133333 0 0 1 0-48.128L844.117333 118.784a34.133333 34.133333 0 1 1 48.128 48.128L521.898667 537.258667a34.133333 34.133333 0 0 1-24.234667 8.874666zM682.666667 697.685333H313.344a34.133333 34.133333 0 0 1 0-68.266666H682.666667a34.133333 34.133333 0 0 1 0 68.266666z" fill="#282424" p-id="7269"></path></svg>
                    备注
                </div>
                <div class="value" @click="setNote">
                    <div v-if="detail.note">{{detail.note}}</div>
                    <div v-else >点击设置备注</div>
                </div>
            </div>
            <div class="item">
                <div class="key">
                    <svg t="1723275744756" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4258" width="200" height="200"><path d="M511.913993 941.605241c-255.612968 0-385.311608-57.452713-385.311608-170.810012 0-80.846632 133.654964-133.998992 266.621871-151.88846L393.224257 602.049387c-79.986561-55.904586-118.86175-153.436587-118.86175-297.240383 0-139.33143 87.211154-222.586259 233.423148-222.586259l7.912649 0c146.211994 0 233.423148 83.254829 233.423148 222.586259 0 54.184445 0 214.67361-117.829666 297.412397l-0.344028 16.685369c132.966907 18.061482 266.105829 71.041828 266.105829 151.716445C897.225601 884.152528 767.526961 941.605241 511.913993 941.605241zM507.957668 141.567613c-79.470519 0-174.250294 28.382328-174.250294 163.241391 0 129.698639 34.230808 213.469511 104.584579 255.784982 8.944734 5.332437 14.277171 14.965228 14.277171 25.286074l0 59.344868c0 15.309256-11.524945 28.0383-26.662187 29.414413-144.319839 14.449185-239.959684 67.429531-239.959684 95.983874 0 92.199563 177.346548 111.637158 325.966739 111.637158 148.792206 0 325.966739-19.26558 325.966739-111.637158 0-28.726356-95.639845-81.534688-239.959684-95.983874-15.48127-1.548127-27.006215-14.621199-26.662187-30.102469l1.376113-59.344868c0.172014-10.148833 5.676466-19.437594 14.277171-24.770032 70.525785-42.487485 103.208466-123.678145 103.208466-255.784982 0-135.031077-94.779775-163.241391-174.250294-163.241391L507.957668 141.567613 507.957668 141.567613z" fill="#2c2c2c" p-id="4259"></path></svg>
                    在本群昵称
                </div>
                <div class="value" @click="setNickName">
                    <div v-if="detail.nickname">{{detail.nickname}}</div>
                    <div v-else >点击设置群昵称</div>
                </div>
            </div>

            <div class="members">
                <div class="description">
                    <svg t="1723275987369" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5304" width="200" height="200"><path d="M402.432 565.76c-129.024 0-233.984-104.96-233.984-233.984S272.896 97.28 402.432 97.28s233.984 104.96 233.984 233.984-104.96 234.496-233.984 234.496z m0-400.384c-91.648 0-165.888 74.24-165.888 165.888s74.24 165.888 165.888 165.888S568.32 422.912 568.32 331.264 493.568 165.376 402.432 165.376z m342.016 762.88H60.416c-18.944 0-34.304-15.36-34.304-34.304 0-165.376 168.96-299.52 376.32-299.52s376.32 134.144 376.32 299.52c-0.512 18.944-15.36 34.304-34.304 34.304zM97.792 859.648h609.28c-22.016-111.616-150.016-197.12-304.64-197.12s-282.624 86.016-304.64 197.12z m896-22.528c0-81.92-36.352-166.4-96.768-226.304-26.624-26.112-56.32-46.592-88.576-61.44 46.592-36.864 76.288-93.696 76.288-157.184 0-110.08-89.6-200.192-200.192-200.192a34.304 34.304 0 0 0 0 68.608c72.704 0 131.584 58.88 131.584 131.584 0 72.192-58.368 131.072-130.56 131.584h-1.024c-18.944 0-33.792 15.36-33.792 34.304s15.36 33.792 34.304 33.792h1.536c78.336 0 131.584 36.352 162.304 67.072 47.104 47.104 76.288 115.2 76.288 178.176a34.304 34.304 0 0 0 68.608 0z" p-id="5305" fill="#2c2c2c"></path></svg>
                    群成员 {{detail.group.number}} 人
                </div>
                <div class="list">
                    <img v-for="item in detail.group.members" :src="config.minioUrl+item.avatar" alt="">
                </div>
            </div>
        </div>
        <div class="line"></div>
        <div class="part3">
            <div class="exit" v-show="detail.role==='ordinary'">退出群聊</div>
            <div class="dissolution" v-show="detail.role==='owner'">解散群聊</div>
            <div class="chat" @click="chat">发消息</div>
        </div>
    </div>
</div>
</template>

<style scoped lang="less">
.group-info-show{
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
            .members{
                padding: 16px 0;

                .description{
                    font-size: 18px;

                    svg{
                        width: 16px;
                        height: 16px;
                    }
                }
                .list{
                    padding: 16px 0;

                    img{
                        width: 40px;

                        margin-right: 1rem;
                        border-radius: 50%;
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
                padding: 10px 0;
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
            .exit{
                width: 110px;
                box-sizing: border-box;
                padding: 10px 0;
                border: 1px solid #CCCCCC;
                margin-top: 32px;
                color: red;
                border-radius: 5px;

                margin-right: 10px;
                text-align: center;
            }
            .exit:hover{
                background-color: #F5F5F5;
                cursor: pointer;
            }
            .dissolution{
                width: 110px;
                box-sizing: border-box;
                padding: 10px 0;
                border: 1px solid #CCCCCC;
                margin-top: 32px;
                color: red;
                border-radius: 5px;

                margin-right: 10px;
                text-align: center;
            }
            .dissolution:hover{
                background-color: #F5F5F5;
                cursor: pointer;
            }
        }
    }
}
</style>