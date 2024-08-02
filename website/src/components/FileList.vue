<script>
import {formatFileSize} from "../utils/utils.js";

export default {
    name: "FileList",
    methods: {formatFileSize},
    props:['fileList']
}
</script>

<template>
<div class="file-list">
    <div
        class="item"
        v-for="(item,index) in fileList"
    >
        <div class="left">
            <svg t="1722264908473" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6111" width="200" height="200"><path d="M763.782552 959.121741H258.844745c-61.988359 0-112.16425-50.248139-112.16425-112.16425V173.719124c0-61.988359 50.175892-112.272621 112.16425-112.272622h336.601122c37.532578-0.541856 52.487812 0.794723 56.136311 0.794723v-0.794723l224.364624 224.436872v561.074117c0.036124 61.916111-50.139768 112.16425-112.16425 112.16425zM651.618302 117.582813V229.747063c0 31.030303 25.069884 56.136311 56.027939 56.136311h112.272622l-168.300561-168.300561z m168.300561 224.436872h-112.272622c-61.916111 0-112.16425-50.248139-112.16425-112.272622 0 0-1.336579-45.010195-0.794723-112.16425H258.844745c-30.958056 0-56.136311 25.069884-56.13631 56.136311v673.238367c0 30.958056 25.178255 56.136311 56.13631 56.136311h504.937807c30.958056 0 56.136311-25.178255 56.136311-56.136311V342.019685z" fill="#231815" p-id="6112"></path></svg>
        </div>
        <div class="right">
            <div class="top">
                {{item.name}}
            </div>
            <div class="bottom">
                {{formatFileSize(item.size)}}
            </div>
        </div>
    </div>
</div>
</template>

<style scoped lang="less">
.file-list{

    .item{
        display: flex;
        align-items: center;
        margin-bottom: 0.5rem;

        .left{
            margin-right: 0.5rem;
            display: flex;

            svg{
                width: 50px;
                height: 50px;
                border-radius: 10px;
            }
        }
        .right{

            .top{
                font-size: 18px;
                margin-bottom: 3px;
                color: black;
            }
            .bottom{
                color: gray;
            }
        }
    }
}
</style>