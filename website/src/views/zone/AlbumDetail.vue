<script>
import {Dashboard}  from "@uppy/vue";
import Uppy from '@uppy/core';
import Webcam from '@uppy/webcam';
import XHR from '@uppy/xhr-upload'

// Don't forget the CSS: core and UI components + plugins you are using
import '@uppy/core/dist/style.css';
import '@uppy/dashboard/dist/style.css';
import '@uppy/webcam/dist/style.css';
import config from "../../config/config.js";

export default {
    name: "AlbumDetail",
    computed: {
        config() {
            return config
        }
    },
    data(){
        return {
            album:{}, //相册数据
            photos:[], //相册下的照片数据

            uploadPhotoDialog:false,

            //uppy是一个处理文件上传的强大的前端库
            uppy:null,
        }
    },
    methods:{
        async getData(){
            {
                let res=await fetch(`/api/zone/album/${this.$route.params.id}`,{
                    headers:{
                        token:localStorage.getItem("token")
                    },
                })
                let json=await res.json()

                console.log('单个相册数据',json)
                this.album=json.data
            }
            {
                let res=await fetch('/api/zone/photos?albumId='+this.$route.params.id,{
                    headers:{
                        token:localStorage.getItem("token")
                    },
                })
                let json=await res.json()

                console.log('相册的照片数据',json)
                this.photos=json.data
            }
        },
        uploadPhotoDialogCloseHandle(){
            this.getData()
        },
        async photoOperationHandle(operation){
            if (operation.startsWith('delete')){
                let res=await fetch(`/api/zone/photo/${operation.split(':')[1]}`,{
                    method:'DELETE',
                    headers:{
                        token:localStorage.getItem("token")
                    }
                })
                let json=await res.json()

                console.log(json)

                if (json.code===1){
                    this.getData()
                }
            }
            if (operation.startsWith('becomeCover')){
                let res=await fetch(`/api/zone/album`,{
                    method:'PUT',
                    headers:{
                        token:localStorage.getItem("token"),
                        'content-type':'application/json;charset=utf-8',
                    },
                    body:JSON.stringify({
                        id:this.$route.params.id,
                        coverPhotoId:operation.split(':')[1]
                    })
                })
                let json=await res.json()

                console.log(json)

                if (json.code===1){
                    this.getData()
                }
            }
        }
    },
    created() {
        this.getData()

        //uppy的初始化工作，uppy是插件式的，也就说要使用它的功能需要安装各种对应的插件，类似vscode
        let uppy=new Uppy()

        uppy.use(Webcam)
        uppy.use(XHR,{
            endpoint:'/api/zone/photo',
            formData: true, // Set to true to use FormData
            fieldName: 'file', // The name of the field used for the file
            headers: {
                //鉴于后端使用的是sa-token，浏览器环境下它会用cookie来进行会话跟踪，所以可以不用这个
                //token: localStorage.getItem('token'), // If you need to add authorization headers
            },
        })
        uppy.on('file-added',(file)=>{
            // Add additional meta fields here
            uppy.setFileMeta(file.id, { albumId: this.$route.params.id});
        })

        this.uppy=uppy
    },
    components:{
        Dashboard,
    }
}
</script>

<template>
<div class="album-detail">

    <div class="main">

        <div class="top">
            <div class="left">
                <img :src="album.coverPhoto?config.minioUrl+album.coverPhoto.src:'/default-album-cover.png'" alt="">
            </div>
            <div class="right">
                <div class="meta">
                    <div class="one">{{album.name}}</div>
                    <div class="two">{{album.photoCount}}张</div>
                    <div class="three">{{album.visibility}} - {{album.description}}</div>
                </div>
                <div class="control">
                    <el-button @click="uploadPhotoDialog=true" >上传</el-button>
                </div>
            </div>
        </div>
        <div class="content">

            <el-empty v-if="photos.length===0" />

            <div class="item" v-for="(item,i) in photos" :key="item.id">
                <div class="one">
                    <el-image
                        style="width: 100%;height: 100%"
                        :src="config.minioUrl+item.src"
                        :zoom-rate="1.2"
                        :max-scale="7"
                        :min-scale="0.2"
                        :preview-src-list="photos.map(e=>config.minioUrl+e.src)"
                        :initial-index="i"
                        fit="cover"
                    />
                </div>
                <div class="two">
                    {{item.name}}
                </div>

                <div class="operation">
                    <el-dropdown trigger="click" @command="photoOperationHandle">
                        <svg  class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1622" width="200" height="200"><path d="M80 512m-80 0a80 80 0 1 0 160 0 80 80 0 1 0-160 0Z" p-id="1623"></path><path d="M512 512m-80 0a80 80 0 1 0 160 0 80 80 0 1 0-160 0Z" p-id="1624"></path><path d="M944 512m-80 0a80 80 0 1 0 160 0 80 80 0 1 0-160 0Z" p-id="1625"></path></svg>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item :command="`delete:${item.id}`" >删除</el-dropdown-item>
                                <el-dropdown-item :command="`becomeCover:${item.id}`" >设为封面</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </div>
        </div>

        <div class="back">
            <el-link @click="$router.back()">返回</el-link>
        </div>
    </div>

    <el-dialog
        v-model="uploadPhotoDialog"
        title="上传照片"
        width="600"
        align-center

        @close="uploadPhotoDialogCloseHandle"
    >
        <Dashboard :uppy="uppy" />

        <template #footer>
            <div class="dialog-footer">
                <el-button @click="uploadPhotoDialog = false">取消</el-button>
                <el-button type="primary" @click="uploadPhotoDialog=false">
                    确定
                </el-button>
            </div>
        </template>
    </el-dialog>

</div>
</template>

<style scoped lang="less">
.album-detail {
    padding: 1rem 0 2rem;

    .main{
        padding: 1rem;
        background-color: white;

        position: relative;

        .top{
            display: flex;
            margin-bottom: 2rem;

            .left{
                width: 80px;
                height: 80px;
                border: 1px solid #D9D9D9;
                padding: 3px;

                margin-right: 1rem;

                img{
                    width: 100%;
                    height: 100%;

                    object-fit: cover;
                }
            }
            .right{

                .meta{
                    display: flex;
                    align-items: end;
                    margin-bottom: 10px;

                    .one{
                        font-size: 25px;

                        margin-right: 1rem;
                    }
                    .two{

                        margin-right: 5px;
                    }
                    .three{
                        color: gray;
                    }
                }
                .control{

                }
            }
        }
        .content{
            display: flex;
            flex-wrap: wrap;

            .item{
                width: 180px;
                height: 160px;
                border: 1px solid #E6E6E6;

                display: flex;
                flex-direction: column;

                position: relative;

                margin-right: 1rem;
                margin-bottom: 1rem;

                .one{
                    flex-grow: 1;

                    height: 80%;
                }
                .two{
                    height: 20%;
                    text-align: center;

                    display: flex;
                    justify-content: center;
                    align-items: center;
                }
                .operation{
                    position: absolute;
                    top: 10px;
                    right: 10px;

                    svg{
                        width: 18px;
                        height: 18px;
                    }
                }
            }
        }
    }

    .back{
        position: absolute;
        top: 1rem;
        right: 1rem;

    }

    .preparingToUpload{
        display: flex;
        flex-wrap: wrap;

        .item{
            width: 140px;
            height: 140px;

            margin-right: 1rem;
            margin-bottom: 1rem;

            img{
                width: 100%;
                height: 100%;
                object-fit: cover;
            }
        }
        .item.add{
            display: flex;
            justify-content: center;
            align-items: center;

            background-color: #F6F6F6;

            input{
                display: none;
            }
            svg{

                width: 50px;
                height: 50px;
            }
        }
    }
}
</style>