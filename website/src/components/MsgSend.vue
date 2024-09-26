<script>
import FileList from "./FileList.vue";
import config from "../config/config.js";
import {displayTime} from "../utils/utils.js";
import Quill from "quill";

//quill是一个富文本编辑框架
//它不能放在data中被vue所管理，不然调用focus方法会报错
let quill

export default {
    name: "MsgSend",
    computed: {
        config() {
            return config
        }
    },
    data(){
        return {
            //quill是一个富文本编辑框架
            //为了插入表情时插入到当前光标所在位置
            currentIndex:0,

            //表情发送相关
            isShowExpr:false, //控制表情框是否显示
            exprList:[], //表情列表数据
            currentExprListItem:null,
            expression:[], //表情数据

            //文件发送相关
            dialogVisible:false, //文件发送确认对话框是否出现
            fileList:[], //用户选择的要发送的文件
        }
    },
    methods:{
        //文字发送相关
        keydown(e){

        },
        //文本消息发送
        async send(){
            let childNodes = quill.root.childNodes;
            let content=[]
            childNodes.forEach(item=>{
                let line=''
                item.childNodes.forEach(item2=>{
                    if(item2.nodeName==='IMG'){
                        line+=`<img src="${new URL(item2.src).pathname}" />`
                    }else{
                        line+=item2.textContent
                    }
                })
                content.push(line)
                console.log([line])
            })

            content=content.join('\n')

            if(content===''){
                return
            }

            console.log('发送')
            let res=await fetch('/api/groupMessage',{
                method: 'POST',
                headers:{
                    token:localStorage.getItem('token'),
                    'content-type':'application/json;charset=utf-8'
                },
                body:JSON.stringify({
                    gid:this.$route.params.gid,
                    type:'text',
                    content
                })
            })
            let json=await res.json()

            console.log('群消息发送',json)
            if (json.code===1){
                //清空
                quill.setText('')
            }
        },

        //表情相关的数据获取
        //获取表情列表数据的
        async getExprListData(){
            let res=await fetch('/api/exprList',{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            console.log('表情列表',json);

            if(json.code===1){
                this.exprList=json.data
                this.currentExprListItem=this.exprList[0]
            }
        },
        //获取某个具体列表的数据的
        async getExprData(id){
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

        //表情相关的点击句柄
        async expr(){ //控制表情框的出现与否
            this.isShowExpr=true

            window.addEventListener('mousedown',action)

            let t=this
            function action(e) {
                if(!t.$refs.expr.contains(e.target)){
                    t.isShowExpr=false //关闭框
                    window.removeEventListener('mousedown',action)
                }

            }
        },
        insertExpression(item){ //点击表情插入表情的
            console.log('插入',item);

            // Insert the image at the current cursor position
            quill.insertEmbed(this.currentIndex, 'image', config.minioUrl+item.path);
        },

        //图片发送相关
        async imgInputChangeHandler(){
            let imgs = this.$refs.imgInput.files;

            console.log(imgs)

            return

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

        },

        editorBlurHandler(){
            this.currentIndex=quill.getSelection().index
            console.log(this.currentIndex)
        },
    },
    watch:{
        //表情相关的
        currentExprListItem(newValue){
            this.getExprData(newValue.id)
        }
    },
    created() {
        //表情相关
        this.getExprListData()
    },
    mounted() {
        quill=new Quill(this.$refs.editor,{
            placeholder:'键入消息'
        })
        quill.root.addEventListener('blur',this.editorBlurHandler)
    },
    beforeUnmount() {
        quill.root.removeEventListener('blur',this.editorBlurHandler)
    },
    components: {FileList}
}
</script>

<template>
<div class="msgSend">
    <div class="bar">
        <!-- 表情 -->
        <div class="item" @click="expr" ref="expr">
            <!-- 表情按钮图标 -->
            <svg t="1711940262243" class="icon" viewBox="0 0 1044 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="8504" width="200" height="200"><path d="M515.204746 1024C377.571561 1024 248.168301 970.752 150.869338 874.116741 53.551412 777.367704-0.018958 648.798815 5.0e-06 512 5.0e-06 375.220148 53.608301 246.632296 150.94519 149.921185 248.244153 53.248 377.647412 0 515.299561 0 652.989635 0 782.373931 53.248 879.71082 149.921185 1080.623412 349.601185 1080.604449 674.417778 879.71082 874.021926 782.354968 970.714074 652.89482 1024 515.204746 1024 515.204746 1024 515.204746 1024 515.204746 1024 515.204746 1024 515.204746 1024 515.204746 1024 515.204746 1024 515.204746 1024 515.204746 1024ZM515.299561 66.730667C395.567412 66.730667 283.060153 113.019259 198.409487 197.101037 113.75882 281.163852 67.147857 393.02637 67.147857 512 67.128894 630.992593 113.720894 742.836148 198.333635 826.936889 282.965338 910.980741 395.510524 957.326222 515.204746 957.326222 634.955857 957.326222 747.557931 910.942815 832.227561 826.842074 1006.933338 653.255111 1006.933338 370.744889 832.227561 197.101037 747.576894 113.019259 635.031709 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667 515.299561 66.730667ZM332.174227 651.738074C332.174227 651.738074 392.552301 747.633778 515.185783 747.633778 637.819264 747.633778 718.24119 651.738074 718.24119 651.738074 718.24119 651.738074 763.828153 651.58637 763.80919 699.676444 763.80919 699.676444 684.771561 811.557926 515.185783 811.557926 345.600005 811.557926 290.39882 699.676444 290.39882 699.676444 290.39882 699.676444 288.730079 651.738074 332.174227 651.738074 332.174227 651.738074 332.174227 651.738074 332.174227 651.738074 332.174227 651.738074 332.174227 651.738074 332.174227 651.738074ZM365.454227 347.363556C330.714079 347.363556 302.345487 375.447704 302.345487 410.074074 302.345487 444.662519 330.695116 472.746667 365.454227 472.746667 400.365042 472.746667 428.562968 444.662519 428.562968 410.074074 428.562968 375.447704 400.365042 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556 365.454227 347.363556ZM688.791709 347.363556C653.994672 347.363556 625.720894 375.447704 625.720894 410.074074 625.720894 444.662519 653.994672 472.746667 688.791709 472.746667 723.664598 472.746667 751.900449 444.662519 751.900449 410.074074 751.900449 375.447704 723.664598 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556 688.791709 347.363556Z" p-id="8505"></path></svg>
            <!-- 表情框 绝对定位 -->
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
        <!--图片-->
        <div class="item" @click="this.$refs.imgInput.click()">
            <svg t="1711940193445" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6437" width="200" height="200"><path d="M938.666667 553.92V768c0 64.8-52.533333 117.333333-117.333334 117.333333H202.666667c-64.8 0-117.333333-52.533333-117.333334-117.333333V256c0-64.8 52.533333-117.333333 117.333334-117.333333h618.666666c64.8 0 117.333333 52.533333 117.333334 117.333333v297.92z m-64-74.624V256a53.333333 53.333333 0 0 0-53.333334-53.333333H202.666667a53.333333 53.333333 0 0 0-53.333334 53.333333v344.48A290.090667 290.090667 0 0 1 192 597.333333a286.88 286.88 0 0 1 183.296 65.845334C427.029333 528.384 556.906667 437.333333 704 437.333333c65.706667 0 126.997333 16.778667 170.666667 41.962667z m0 82.24c-5.333333-8.32-21.130667-21.653333-43.648-32.917333C796.768 511.488 753.045333 501.333333 704 501.333333c-121.770667 0-229.130667 76.266667-270.432 188.693334-2.730667 7.445333-7.402667 20.32-13.994667 38.581333-7.68 21.301333-34.453333 28.106667-51.370666 13.056-16.437333-14.634667-28.554667-25.066667-36.138667-31.146667A222.890667 222.890667 0 0 0 192 661.333333c-14.464 0-28.725333 1.365333-42.666667 4.053334V768a53.333333 53.333333 0 0 0 53.333334 53.333333h618.666666a53.333333 53.333333 0 0 0 53.333334-53.333333V561.525333zM320 480a96 96 0 1 1 0-192 96 96 0 0 1 0 192z m0-64a32 32 0 1 0 0-64 32 32 0 0 0 0 64z" p-id="6438"></path></svg>
            <div class="img-input">
                <input type="file" accept="image/*" multiple ref="imgInput" @change="imgInputChangeHandler">
            </div>
        </div>
        <!--文件-->
        <div class="item" @click="this.$refs.fileInput.click()">
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
<!--    <div class="textarea" contenteditable ref="msg" @keydown="keydown" @input="input"></div>-->
    <!--这里使用了quill这个富文本编辑框架-->
    <div MsgSend class="editor" ref="editor"></div>
    <div class="control">
        <div class="send" @click="send">发送</div>
    </div>
</div>
</template>

<style>
/*@import "quill/dist/quill.core.css";*/

/*MsgSend是样式冲突解决方法，用组件名作为命名空间*/
.editor[MsgSend]{
    img{
        width: 30px;
        vertical-align: middle;
    }
}
</style>
<style scoped lang="less">
.msgSend{
    border-top: 1px solid #E9E9E9;
    padding: 0 25px 25px;

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
    .textarea{
        width: 100%;
        height:150px;

        border: none;
        outline: none;
        font-size: 18px;
        font-family: "微软雅黑";
        background-color: transparent;

    }
    .editor{
        font-size: 18px;
        height: 150px;

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
</style>