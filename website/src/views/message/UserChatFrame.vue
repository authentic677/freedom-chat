<script>
import {displayTime, emitter, formatFileSize} from "../../utils/utils.js"
import FileList from "../../components/FileList.vue";
import FileMsgDisplay from "../../components/FileMsgDisplay.vue";
import config from "../../config/config.js";
import {NSplit} from 'naive-ui'
export default {
    name: "UserChatFrame",
    components: {FileMsgDisplay, FileList, NSplit},
    data(){
        return {
            messages:[],
            messages2:[], //监视属性用的

            peerUserInfo:{},
            peerUsername:'',//对方用户名
            peerUid:'',//对方用户id

            isShowExpr:false, //控制表情框是否显示
            exprList:[], //表情列表数据
            currentExprListItem:null,
            expression:[], //表情数据

            inputFlag:false, //文本输入相关的flag，影响是否应该发送

            //文件发送相关
            dialogVisible:false, //文件发送确认对话框是否出现
            fileList:[], //用户选择的要发送的文件

            config
        }
    },
    methods:{
        keydown(e){
            //约束：没有输入内容时按回车键不应该有反应
            if(this.$refs.msg.innerText===''){
                if(e.key==='Enter'){ //这里不用code属性的原因是，小键盘的enter和主键盘的enter键值不一样
                    e.preventDefault()
                }
            }else{
                if(e.key==='Enter'&&e.shiftKey===false){ //按shift+enter换行
                    this.inputFlag=true
                }
            }
        },
        input(e){
            if(this.inputFlag){
                if(!e.isComposing){ //这里是为了当输入中文时，如果拼音输到一半就按回车键，是不应该发送的
                    this.prepareSend()
                }

            }
        },
        prepareSend(){
            //替换img节点为文本节点
            let c=this.$refs.msg.children
            let f=true
            while(f){
                f=false
                for(let i=0;i<c.length;i++){
                    if(c[i].nodeName=='IMG'){
                        // console.log(c[i]);

                        //替换
                        this.$refs.msg.replaceChild(document.createTextNode(c[i].outerHTML),c[i])

                        f=true
                        break
                    }
                }
            }


            let r=this.$refs.msg.innerText.replace(/(\n)+$/,'') //去掉尾部的\n

            this.send(r)

            this.inputFlag=false
            this.$refs.msg.innerText='' //清空
        },
        async send(msg){

            let res=await fetch('/api/userChatRecord',{
                method:'POST',
                headers:{
                    token:localStorage.getItem('token'),
                    'Content-Type':'application/json;charset=utf-8'
                },
                body:JSON.stringify({
                    uid1:this.peerUserInfo.uid,
                    type:'text',
                    content:msg
                })
            })
            let json=await res.json()

            console.log(json);

        },
        async getData(){
            let uid=this.$route.params.uid //对方的uid
            console.log(uid);
            //获取对方的基本信息
            {
                let res=await fetch(`/api/user/${uid}`,{
                    headers:{
                        token:localStorage.getItem('token')
                    }
                })
                let json=await res.json()

                this.peerUserInfo=json.data
            }



            let res=await fetch('/api/userChatRecords?targetUid='+uid,{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            console.log('聊天数据',json);

            if(json.code===1){
                this.messages=json.data

                this.$nextTick( ()=> {
                    // DOM 更新完毕之后的回调操作
                    this.$refs.msgShow.scrollTop=this.$refs.msgShow.scrollHeight
                });
            }
        },
        //这个是获取表情列表的
        async getExprData(){
            let res=await fetch('/api/exprList',{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            console.log(json);

            if(json.code===1){
                this.exprList=json.data
                this.currentExprListItem=this.exprList[0]
            }
        },
        //这个是真正获取表情的
        async getExpressionData(id){
            let res=await fetch('/api/expression?expressionListId='+id,{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            console.log('表情数据',json);

            if(json.code===1){
                this.expression=json.data
            }
        },
        async expr(){ //控制表情框的出现与否
            this.isShowExpr=true

            window.addEventListener('mousedown',actiion)

            let t=this
            function actiion(e) {
                if(!t.$refs.expr.contains(e.target)){
                    t.isShowExpr=false //关闭框
                    window.removeEventListener('mousedown',actiion)
                }

            }


        },
        insertExpression(item){ //点击表情插入表情的
            console.log('插入',item);

            //手动设置选区(Selection)和所对应的范围(Range)确保表情插入位置的正确
            //先清除潜在选区，防止不生效
            getSelection().removeAllRanges()

            let child=this.$refs.msg.childNodes
            //创建范围
            let r=new Range()
            if(child.length===0){ //对于有没有内容分两种情况进行设置
                r.setStart(this.$refs.msg,0)
                r.setStart(this.$refs.msg,0)
            }else{
                r.setStartAfter(child[child.length-1])
                r.setEndAfter(child[child.length-1])
            }

            console.log(r);

            let n=document.createElement('img')
            n.src=item.path
            n.style.width='30px'
            n.style.verticalAlign='middle'
            r.insertNode(n)
            r.collapse() //这个方法是必要的，否则插入的图片会被选中

            getSelection().removeAllRanges()
            getSelection().addRange(r)
        },
        test(){

            this.getData()
        },


        //图片发送相关
        async imgInputChangeHandler(){
            let imgs = this.$refs.imgInput.files;

            console.log(imgs)

            for(let i=0;i<imgs.length;i++){
                let res=await fetch(`/api/message/${this.peerUid}`,{
                    method:'POST',
                    headers:{
                        token:localStorage.getItem('token'),
                        'x-amz-meta-name':imgs[i].name,
                        'x-amz-meta-type':'img',
                        'x-amz-meta-size':imgs[i].size
                    },
                    body:imgs[i]
                })
                let json=await res.json()
                console.log(json)
            }

        },

        //文件发送相关
        fileInputHandler(){
            this.$refs.fileInput.click()
        },
        fileChangeHandler(){
            console.log(this.$refs.fileInput.files)
            this.fileList=this.$refs.fileInput.files
            this.dialogVisible=true
        },
        async fileSendHandler(){
            console.log('发送文件')
            let fileList=[]
            for(let i=0;i<this.fileList.length;i++){
                fileList.push({
                    name:this.fileList[i].name,
                    size:this.fileList[i].size,
                    type:this.fileList[i].type,
                })
            }
            let res=await fetch('/api/userChatRecord',{
                method:'POST',
                headers:{
                    token:localStorage.getItem('token'),
                    'Content-Type':'application/json;charset=utf-8'
                },
                body:JSON.stringify({
                    uid1:this.peerUid,
                    type:'file',
                    content:JSON.stringify(fileList)
                })
            })
            let json=await res.json()

            console.log(json);

            if(json.code!==1){
                this.$message({
                    message:"发送文件出错",
                    type:'error'
                })
                return
            }
            //文件发送第一步完成
            this.$message({
                message:"正在发送",
                type:'success'
            })
            for(let i=0;i<json.data.length;i++){
                let res=await fetch(`/api/minio/upload/${json.data[i]}`,{
                    method:'POST',
                    headers:{
                        token:localStorage.getItem('token')
                    },
                    body:this.fileList[i]
                })
                let json2=await res.json();
                console.log(json2)
            }
            this.$message({
                message:"发送完成",
                type:'success'
            })
            this.dialogVisible=false

        }
    },
    watch:{
        currentExprListItem(newValue){
            this.getExpressionData(newValue.id)
        },
        messages(){ //这里为什么用监视属性，因为用计算属性会有问题，会出现莫名其妙触发计算，即当this.messages没变时它也计算，导致时间显示出现问题，在切换不同用户的聊天窗口时可以发现此情况
            console.log('userChatFrame message 监视属性执行')
            this.messages2= this.messages.map((e,index,arr)=>{
                //判断是否显示时间
                if(index!==0){

                    let diff=new Date(e.time)-new Date(arr[index-1].time)

                    if(diff>1000*60*5){
                        e.isShowTime=true
                    }
                }else{
                    e.isShowTime=true
                }
                return e
            }).map(e=>{ //美化时间一定要放在后面，否则，，，
                //美化时间显示
                e.time=displayTime(new Date(e.time))
                return e
            })
        }
    },
    created(){
        console.log('UserChatFrame created')
        this.getData()
        this.getExprData()
        //响应路由参数变化
        this.$watch(()=>this.$route.params.uid,(newVal,oldVal)=>{

            this.getData()
        })

        emitter.on('messageUpdate',this.getData)
    },
    beforeUnmount(){
        console.log('UserChatFrame will unmounted')
        emitter.off('messageUpdate',this.getData)
    }
}
</script>

<template>
    <div class="user-chat-frame">
        <div class="top">
            <div class="left">{{peerUserInfo.username}}</div>
            <div class="right">
                <svg @click="$router.push(`/live/audio/to/${peerUid}`)" t="1722082323399" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" ><path d="M726.326 916.746c-27.914 0-56.191-6.068-82.405-18.69-115.659-50.366-219.788-121.363-309.233-211.050-89.687-89.687-160.563-193.938-210.686-309.84-1.214-2.548-1.821-3.884-3.398-7.767l-1.942-4.491c-10.559-26.214-13.107-63.715-6.796-100.125 7.282-41.628 25.244-78.643 50.73-104.129 26.578-26.578 60.075-44.661 96.726-52.307 25.729-5.34 52.065 5.219 67.235 26.821l133.014 189.933c15.17 21.724 16.020 50.366 1.942 72.94-7.646 12.379-16.869 23.908-27.185 34.224-20.875 20.875-46.117 36.652-73.789 45.997 51.7 70.634 113.474 132.406 184.108 184.108 9.467-27.67 25.122-52.914 45.997-73.789 9.588-9.588 20.268-18.205 31.554-25.486 22.574-14.563 51.337-13.957 73.546 1.578l191.389 134.227c22.209 15.534 32.526 42.72 26.457 69.177-8.131 34.953-25.851 66.992-51.337 92.479-36.773 36.773-86.047 56.191-135.926 56.191zM172.547 342.941l1.578 3.762c1.578 3.762 1.821 4.369 2.791 6.311l0.364 0.729c95.513 220.759 269.668 395.279 490.306 491.156l0.971 0.485c51.094 24.515 112.503 14.078 152.675-26.093 17.718-17.718 30.098-40.050 35.803-64.322 0.729-3.277-0.485-6.432-3.155-8.374l-191.632-133.985c-2.548-1.821-5.947-1.942-8.496-0.364-7.888 5.098-15.292 11.044-21.967 17.84-18.326 18.326-30.947 41.385-36.287 66.75-3.035 14.078-12.743 25.607-26.093 30.947-13.471 5.34-28.521 3.641-40.414-4.611-89.687-62.138-166.753-139.203-229.012-229.012-8.253-11.893-9.952-26.943-4.611-40.414 5.34-13.349 16.869-23.18 30.947-26.093 25.244-5.34 48.423-17.962 66.75-36.287 7.282-7.282 13.593-15.292 19.054-23.908 1.699-2.67 1.578-6.068-0.242-8.617l-133.014-190.054c-1.821-2.548-4.854-3.884-7.767-3.277-25.486 5.34-48.788 17.84-67.357 36.409-16.991 16.991-29.249 42.962-34.467 72.818-4.369 25.364-3.035 52.065 3.277 68.206z" fill="" p-id="1513"></path></svg>
                <svg @click="$router.push(`/live/video/to/${peerUid}`)" t="1722082421162" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" ><path d="M912 302.3L784 376V224c0-35.3-28.7-64-64-64H128c-35.3 0-64 28.7-64 64v576c0 35.3 28.7 64 64 64h592c35.3 0 64-28.7 64-64V648l128 73.7c21.3 12.3 48-3.1 48-27.6V330c0-24.6-26.7-40-48-27.7zM712 792H136V232h576v560z m176-167l-104-59.8V458.9L888 399v226z" p-id="1528"></path><path d="M208 360h112c4.4 0 8-3.6 8-8v-48c0-4.4-3.6-8-8-8H208c-4.4 0-8 3.6-8 8v48c0 4.4 3.6 8 8 8z" p-id="1529"></path></svg>
                <svg  class="icon" viewBox="0 0 1024 1024"  xmlns="http://www.w3.org/2000/svg"  ><path d="M889.6 127.488H141.696c-38.656 0-70.08 31.488-70.08 70.144v467.392c0 38.656 31.488 70.144 70.08 70.144h342.976v99.456H368.384c-18.688 0-33.792 13.888-33.792 30.976s15.104 30.976 33.792 30.976h294.592c18.688 0 33.792-13.888 33.792-30.976s-15.104-30.976-33.792-30.976H546.688v-99.456H889.6c38.656 0 70.144-31.424 70.144-70.144V197.568c0-38.592-31.424-70.08-70.144-70.08z m4.864 526.592a22.272 22.272 0 0 1-22.272 22.336H159.168a22.272 22.272 0 0 1-22.272-22.336V208.512c0-12.352 10.048-22.272 22.272-22.272h713.024c12.288 0 22.272 9.92 22.272 22.272v445.568z"  p-id="11653"></path><path d="M566.976 313.984c-13.184-10.624-23.936-2.88-23.936 15.872v49.984h-2.176c-77.056 0-208.128 89.024-209.216 168.192 0 6.336 5.12 8.128 10.048 0 24.896-44.416 129.536-67.456 182.848-67.456h18.496v52.736c0 15.744 11.648 26.496 24.896 15.872l121.856-97.792c13.184-10.56 13.184-27.904 0-38.528L566.976 313.984z"  p-id="11654"></path></svg>
            </div>
        </div>

        <div class="b">
            <n-split direction="vertical" :default-size="0.65" >
                <template #1>
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
                </template>
                <template #2>
                    <div class="bottom">
                        <div class="bar">
                            <!-- 表情 -->
                            <div class="item" @click="expr" ref="expr">
                                <!-- 表情按钮图标 -->
                                <svg t="1711940262243" class="icon" viewBox="0 0 1044 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="8504" width="200" height="200"><path d="M515.204746 1024C377.571561 1024 248.168301 970.752 150.869338 874.116741 53.551412 777.367704-0.018958 648.798815 5.0e-06 512 5.0e-06 375.220148 53.608301 246.632296 150.94519 149.921185 248.244153 53.248 377.647412 0 515.299561 0 652.989635 0 782.373931 53.248 879.71082 149.921185 1080.623412 349.601185 1080.604449 674.417778 879.71082 874.021926 782.354968 970.714074 652.89482 1024 515.204746 1024 515.204746 1024 515.204746 1024 515.204746 1024 515.204746 1024 515.204746 1024 515.204746 1024 515.204746 1024 515.204746 1024 515.204746 1024ZM515.299561 66.730667C395.567412 66.730667 283.060153 113.019259 198.409487 197.101037 113.75882 281.163852 67.147857 393.02637 67.147857 512 67.128894 630.992593 113.720894 742.836148 198.333635 826.936889 282.965338 910.980741 395.510524 957.326222 515.204746 957.326222 634.955857 957.326222 747.557931 910.942815 832.227561 826.842074 1006.933338 653.255111 1006.933338 370.744889 832.227561 197.101037 747.576894 113.019259 635.031709 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667ZM332.174227 651.738074C332.174227 651.738074 392.552301 747.633778 515.185783 747.633778 637.819264 747.633778 718.24119 651.738074 718.24119 651.738074 718.24119 651.738074 763.828153 651.58637 763.80919 699.676444 763.80919 699.676444 684.771561 811.557926 515.185783 811.557926 345.600005 811.557926 290.39882 699.676444 290.39882 699.676444 290.39882 699.676444 288.730079 651.738074 332.174227 651.738074 332.174227 651.738074 332.174227 651.738074 332.174227 651.738074 332.174227 651.738074 332.174227 651.738074 332.174227 651.738074ZM365.454227 347.363556C330.714079 347.363556 302.345487 375.447704 302.345487 410.074074 302.345487 444.662519 330.695116 472.746667 365.454227 472.746667 400.365042 472.746667 428.562968 444.662519 428.562968 410.074074 428.562968 375.447704 400.365042 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556ZM688.791709 347.363556C653.994672 347.363556 625.720894 375.447704 625.720894 410.074074 625.720894 444.662519 653.994672 472.746667 688.791709 472.746667 723.664598 472.746667 751.900449 444.662519 751.900449 410.074074 751.900449 375.447704 723.664598 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556Z" p-id="8505"></path></svg>
                                <!-- 表情框 -->
                                <div class="expr" v-if="isShowExpr">
                                    <div class="content" >
                                        <div class="i" v-for="item in expression" :key="item.id" @click="insertExpression(item)">
                                            <img :src="config.minioUrl+item.path" :title="item.description" >
                                        </div>
                                    </div>
                                    <div class="lists">
                                        <div class="i" v-for="item in exprList" :key="item.id" @click="currentExprListItem=item" :class="{hightlight:currentExprListItem==item}">
                                            {{item.name}}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- 语音 -->
                            <div class="item">
                                <svg t="1711940159479" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5421" width="200" height="200"><path d="M469.632 808.064A341.376 341.376 0 0 1 170.666667 469.333333h85.333333a256 256 0 0 0 256 256h1.749333A256 256 0 0 0 768 469.333333h85.333333a341.376 341.376 0 0 1-298.368 338.645334l0.426667 130.56-85.333333 0.256-0.426667-130.730667zM512 128a85.333333 85.333333 0 0 0-85.333333 85.333333v256a85.333333 85.333333 0 1 0 170.666666 0V213.333333a85.333333 85.333333 0 0 0-85.333333-85.333333z m0-85.333333a170.666667 170.666667 0 0 1 170.666667 170.666666v256a170.666667 170.666667 0 1 1-341.333334 0V213.333333a170.666667 170.666667 0 0 1 170.666667-170.666666z"  p-id="5422"></path><path d="M341.333333 981.333333v-85.333333h341.333334v85.333333z"  p-id="5423"></path></svg>

                            </div>

                            <div class="item" @click="this.$refs.imgInput.click()">
                                <svg t="1711940193445" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6437" width="200" height="200"><path d="M938.666667 553.92V768c0 64.8-52.533333 117.333333-117.333334 117.333333H202.666667c-64.8 0-117.333333-52.533333-117.333334-117.333333V256c0-64.8 52.533333-117.333333 117.333334-117.333333h618.666666c64.8 0 117.333333 52.533333 117.333334 117.333333v297.92z m-64-74.624V256a53.333333 53.333333 0 0 0-53.333334-53.333333H202.666667a53.333333 53.333333 0 0 0-53.333334 53.333333v344.48A290.090667 290.090667 0 0 1 192 597.333333a286.88 286.88 0 0 1 183.296 65.845334C427.029333 528.384 556.906667 437.333333 704 437.333333c65.706667 0 126.997333 16.778667 170.666667 41.962667z m0 82.24c-5.333333-8.32-21.130667-21.653333-43.648-32.917333C796.768 511.488 753.045333 501.333333 704 501.333333c-121.770667 0-229.130667 76.266667-270.432 188.693334-2.730667 7.445333-7.402667 20.32-13.994667 38.581333-7.68 21.301333-34.453333 28.106667-51.370666 13.056-16.437333-14.634667-28.554667-25.066667-36.138667-31.146667A222.890667 222.890667 0 0 0 192 661.333333c-14.464 0-28.725333 1.365333-42.666667 4.053334V768a53.333333 53.333333 0 0 0 53.333334 53.333333h618.666666a53.333333 53.333333 0 0 0 53.333334-53.333333V561.525333zM320 480a96 96 0 1 1 0-192 96 96 0 0 1 0 192z m0-64a32 32 0 1 0 0-64 32 32 0 0 0 0 64z" p-id="6438"></path></svg>
                                <div class="img-input">
                                    <input type="file" accept="image/*" multiple ref="imgInput" @change="imgInputChangeHandler">
                                </div>
                            </div>
                            <!--文件-->
                            <div class="item" @click="fileInputHandler">
                                <svg t="1711940219559" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="7457" width="200" height="200"><path d="M912 208H427.872l-50.368-94.176A63.936 63.936 0 0 0 321.056 80H112c-35.296 0-64 28.704-64 64v736c0 35.296 28.704 64 64 64h800c35.296 0 64-28.704 64-64v-608c0-35.296-28.704-64-64-64z m-800-64h209.056l68.448 128H912v97.984c-0.416 0-0.8-0.128-1.216-0.128H113.248c-0.416 0-0.8 0.128-1.248 0.128V144z m0 736v-96l1.248-350.144 798.752 1.216V784h0.064v96H112z"  p-id="7458"></path></svg>
                                <div class="file-input" @click.stop>
                                    <input type="file" ref="fileInput" @change="fileChangeHandler" multiple>
                                    <el-dialog
                                        v-model="dialogVisible"
                                        title="确认发送内容"
                                        width="500"
                                    >
                                        <FileList :file-list="fileList" />
                                        <template #footer>
                                            <div class="dialog-footer">
                                                <el-button @click="dialogVisible = false">取消</el-button>
                                                <el-button type="primary" @click="fileSendHandler">
                                                    发送({{fileList.length}})
                                                </el-button>
                                            </div>
                                        </template>
                                    </el-dialog>
                                </div>
                            </div>
                        </div>
                        <div class="textInput">
                            <div class="textarea" contenteditable ref="msg" @keydown="keydown" @input="input"></div>
                        </div>
                        <div class="control">
                            <div class="send" @click="prepareSend">发送</div>
                        </div>
                    </div>
                </template>
            </n-split>
        </div>



    </div>
</template>

<style scoped lang="less">
.user-chat-frame{
    height: 100%;
    display: flex;
    flex-direction: column;
    background-color: #F2F2F2;
    overflow: hidden;

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
    .b{
        flex-grow: 1;
        overflow: hidden;

        .middle{
            height: 100%;
            box-sizing: border-box;
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

                        flex-grow: 1;

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
                            background-color: white;
                            color: black;
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
        .bottom{
            height: 100%;
            box-sizing: border-box;
            padding: 0px 25px 25px;

            display: flex;
            flex-direction: column;

            .bar{
                padding-top: 10px;
                padding-bottom: 5px;

                display: flex;

                .item{
                    padding-right: 10px;
                    position: relative;

                    svg{
                        width: 26px;
                        height: 26px;
                    }
                    svg:hover{
                        fill: #0099FF;
                    }

                    .expr{
                        position: absolute;
                        left: -25%;
                        bottom: 150%;
                        display: flex;
                        flex-direction: column;

                        width: 560px;
                        height: 415px;
                        box-shadow: 0px 0px 20px 5px rgba(0,0,0,0.3);
                        border-radius: 5px;
                        background-color: white;

                        .content{
                            flex-grow: 1;
                            overflow: auto;
                            padding: 20px;
                            display: flex;
                            flex-wrap: wrap;

                            .i{
                                padding: 5px;

                                img{
                                    width: 45px;
                                    height: 45px;
                                }
                            }
                            .i:hover{
                                background-color: #F1F2F1;
                                border-radius: 5px;
                            }
                        }
                        .lists{
                            display: flex;
                            padding: 5px;
                            border-top: 1px solid #ddd;

                            .i{
                                padding: 5px;
                                border-radius: 5px;
                            }
                            .i:hover{
                                background-color: #F3F3F3;
                            }
                            .i.hightlight{
                                background-color: #0099FF;
                                color: white;
                            }
                        }
                    }

                    .img-input{

                        input{
                            display: none;
                        }
                    }
                    .file-input{

                        input{
                            display: none;
                        }
                    }
                }
            }
            .textInput{
                flex-grow: 1;

                .textarea{
                    height: 100%;

                    border: none;
                    outline: none;
                    font-size: 18px;
                    font-family: "微软雅黑";
                    background-color: transparent;

                }
            }
            .control{
                margin-top: 20px;
                display: flex;
                justify-content: flex-end;

                .send{
                    padding: 10px 24px;
                    background-color: #0099FF;
                    color: white;
                    border-radius: 5px;
                    cursor: pointer;
                }
            }
        }
    }
}
</style>