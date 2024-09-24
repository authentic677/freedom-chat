<script>
import config from "../../config/config.js";

export default {
    name: "Album",
    computed: {
        config() {
            return config
        }
    },
    data(){
        return {
            createAlbumDialog:false,

            //创建相册时需要的变量
            name:'',
            description:'',
            visibilityValue:null,
            visibilityOptions:[
                {
                    value:'public',
                    label:'所有人'
                },
                {
                    value:'private',
                    label:'仅自己'
                },
                {
                    value:'friends',
                    label:'仅好友'
                },
                {
                    value:'custom',
                    label:'指定人可见'
                },
            ],

            albums:[],
        }
    },
    methods:{
        async getData(){
            let res=await fetch('/api/zone/albums',{
                headers:{
                    token:localStorage.getItem("token")
                },
            })
            let json=await res.json()

            console.log('相册数据',json)
            this.albums=json.data
        },
        async createAlbum(){

            let res=await fetch('/api/zone/album',{
                method:'POST',
                headers:{
                    token:localStorage.getItem("token"),
                    'content-type':'application/json;charset=utf-8'
                },
                body:JSON.stringify({
                    name:this.name,
                    description:this.description,
                    visibility:this.visibilityValue,
                })
            })
            let json=await res.json()

            console.log(json)

            if (json.code===1){
                this.createAlbumDialog=false //关闭对话框
                this.getData() //重新获取相册数据
                //清空输入
                this.name=''
                this.description=''
                this.visibilityValue=''
            }
        },

        async albumOperationHandle(operation){
            if (operation.startsWith('delete')){
                let res=await fetch(`/api/zone/album/${operation.split(':')[1]}`,{
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
        }
    },
    created() {
        this.getData()
    }
}
</script>

<template>
<div class="album">

    <div class="main">
        <div class="head">
            <div>相册</div>
            <el-divider />
        </div>
        <div class="control">
            <el-button type="primary" @click="createAlbumDialog=true">创建相册</el-button>
        </div>
        <div class="content">
            <el-empty v-if="albums.length===0" />

            <div class="item" v-for="(item,i) in albums" @click="$router.push(`./albumDetail/${item.id}`)">
                <div class="cover">
                    <img :src="item.coverPhoto?config.minioUrl+item.coverPhoto.src:'/default-album-cover.png'" alt="">
                    <div class="count">{{ item.photoCount }}</div>
                </div>
                <div class="bottom">
                    <div class="name">{{item.name}}</div>
                    <div class="more" @click.stop>
                        <el-dropdown trigger="click" @command="albumOperationHandle">
                            <svg class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4267" width="200" height="200"><path d="M512 341.333334C558.933334 341.333334 597.333334 302.933334 597.333334 256 597.333334 209.066666 558.933334 170.666666 512 170.666666 465.066666 170.666666 426.666666 209.066666 426.666666 256 426.666666 302.933334 465.066666 341.333334 512 341.333334L512 341.333334ZM512 426.666666C465.066666 426.666666 426.666666 465.066666 426.666666 512 426.666666 558.933334 465.066666 597.333334 512 597.333334 558.933334 597.333334 597.333334 558.933334 597.333334 512 597.333334 465.066666 558.933334 426.666666 512 426.666666L512 426.666666ZM512 682.666666C465.066666 682.666666 426.666666 721.066666 426.666666 768 426.666666 814.933336 465.066666 853.333336 512 853.333336 558.933334 853.333336 597.333334 814.933336 597.333334 768 597.333334 721.066666 558.933334 682.666666 512 682.666666L512 682.666666Z" fill="#666666" p-id="4268"></path></svg>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item :command="`delete:${item.id}`">删除</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                </div>
            </div>
        </div>

    </div>

    <el-dialog
        v-model="createAlbumDialog"
        title="创建相册"
        width="500"
        align-center
    >
        <div class="row">
            <el-input placeholder="相册名称" v-model="name"></el-input>
        </div>
        <div class="row">
            <el-input placeholder="相册描述" type="textarea" v-model="description"></el-input>
        </div>
        <div class="row">
            <el-select
                v-model="visibilityValue"
                placeholder="选择可见性"
            >
                <el-option
                    v-for="item in visibilityOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
            </el-select>
        </div>

        <template #footer>
            <div class="dialog-footer">
                <el-button @click="createAlbumDialog = false">取消</el-button>
                <el-button type="primary" @click="createAlbum">
                    确定
                </el-button>
            </div>
        </template>
    </el-dialog>

</div>
</template>

<style scoped lang="less">
.album{
    padding: 1rem 0 2rem;

    .row{
        margin-bottom: 1rem;
    }
    .main{
        padding: 1rem;
        background-color: white;

        .head{

        }
        .control{
            margin-bottom: 1rem;
        }
        .content{
            display: flex;
            flex-wrap: wrap;

            .item{
                width: 170px;
                height: 200px;
                padding: 6px;
                box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.5);

                margin-right: 1rem;
                margin-bottom: 1rem;

                display: flex;
                flex-direction: column;

                .cover{
                    flex-grow: 1;

                    position: relative;

                    img{
                        width: 100%;
                        height: 100%;
                        vertical-align: bottom;

                        object-fit: contain;
                    }
                    .count{
                        position: absolute;
                        bottom: 0;
                        left: 0;
                        width: 100%;
                        height: 50px;

                        background-image: linear-gradient(rgba(255,255,255,0),rgba(26,26,26,.4));

                        line-height: 50px;
                        text-align: right;
                        padding-right: 1rem;
                        box-sizing: border-box;
                        color: white;
                        font-size: 20px;
                    }
                }
                .bottom{
                    margin: 10px 0;

                    display: flex;
                    justify-content: space-between;
                    align-items: center;

                    .name{

                    }
                    .more{
                        display: flex;

                        svg{
                            width: 20px;
                            height: 20px;
                        }
                    }
                }
            }
        }
    }
}
</style>