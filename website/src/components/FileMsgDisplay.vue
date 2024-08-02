<script>
import {formatFileSize} from "../utils/utils.js";
import config from "../config/config.js";

export default {
    name: "FileMsgDisplay",
    methods: {
        formatFileSize,
        async download(){
            let res=await fetch(`${config.minioUrl}${this.msgContent}`,)
            let blob=await res.blob();

        }
    },
    data(){
        return {
            fileInfo:{name:'文件信息正在加载',size:1024},
            url:'' //此文件的下载地址
        }
    },
    props:['msgContent'],
    created() {
        //获取fileInfo
        //注意这里采用head请求方法，只获取请求头数据
        this.url=`${config.minioUrl}${this.msgContent}`
        fetch(this.url,{method:'HEAD'}).then(res=>{
            let headers = res.headers;

            this.fileInfo= {name:headers.get("x-amz-meta-name"),size:parseInt(headers.get("x-amz-meta-size"))}
        })

    }
}
</script>

<template>
<div class="file-msg-display">
    <div class="left">
        <div class="name">{{fileInfo.name}}</div>
        <div class="size">{{formatFileSize(fileInfo.size)}}</div>
    </div>
    <div class="right">
        <svg t="1722473016742" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1525" width="50" height="50"><path d="M923.32032 662.646784V331.948032h-0.28672a27.140096 27.140096 0 0 0-7.962624-19.152896l0.196608-0.200704-275.542016-275.542016-0.2048 0.200704a26.988544 26.988544 0 0 0-17.494016-7.880704c0.04096-0.196608 0.053248-0.401408 0.094208-0.598016H567.33696v0.561152H178.950144c-42.25024 0-76.627968 34.373632-76.627968 76.61568V907.34592c0 42.25024 34.369536 76.623872 76.627968 76.623872h667.742208c42.25024 0 76.627968-34.373632 76.627968-76.623872v-190.21824h0.057344v-54.4768h-0.057344z m-78.0288-342.986752h-168.370176c-30.98624 0-56.098816-25.116672-56.098816-56.102912v-168.38656l224.468992 224.489472z m24.834048 55.7056v543.330304a10.960896 10.960896 0 0 1-10.952704 10.944512H167.600128a10.960896 10.960896 0 0 1-10.948608-10.944512V94.609408a10.952704 10.952704 0 0 1 10.948608-10.940416h391.294976v-0.045056h8.441856V299.6224c0 41.013248 33.23904 74.252288 74.252288 74.252288h228.552704v1.490944h-0.016384z" fill="#040000" p-id="1526"></path><path d="M395.788288 726.032384l-172.875776-70.811648v-44.31872l172.875776-74.825728v47.525888l-109.981696 48.693248v0.9216l109.981696 45.314048v47.501312zM629.911552 678.526976l109.9776-45.314048v-0.9216l-109.9776-48.693248v-47.525888l172.875776 74.825728v44.31872l-172.875776 70.811648v-47.501312zM591.015936 490.029056l-115.662848 277.430272H430.96064l115.2-277.430272h44.855296z" p-id="1527"></path></svg>
    </div>


    <a :href="url" class="download" download >
        <el-button>下载</el-button>
    </a>
</div>
</template>

<style scoped lang="less">
.file-msg-display{
    width: 270px;
    height: 100px;
    border-radius: 10px;
    background-color: white;
    padding: 10px;

    display: flex;
    justify-content: space-between;

    position: relative;

    .left{

        .name{
            font-size: 18px;
            margin-bottom: 3px;
        }
        .size{
            color: gray;
        }
    }
    .right{
        display: flex;
    }

    .download{
        position: absolute;
        left: 50%;
        bottom: 10px;
        transform: translateX(-50%);

        display: none;
    }
}
.file-msg-display:hover .download{
    display: block;
}
</style>