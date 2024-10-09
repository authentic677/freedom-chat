<script>
import config from "../../config/config.js";

export default {
    name: "ShipinhaoCustomization",
    computed: {
        config() {
            return config
        }
    },
    data(){
        return {
            bannerBin:null,
            avatarBin:null,
            bannerUrl:'',
            avatarUrl:'',
            channel:{},
        }
    },
    methods:{
        async getData(){
            let res=await fetch('/api/content-platform/channel')
            let json=await res.json()

            this.channel=json.data

            this.avatarUrl=config.minioUrl+this.channel.avatar
            this.bannerUrl=config.minioUrl+this.channel.banner
        },
        bannerChange(){
            this.bannerBin=this.$refs.banner.files[0]
            this.bannerUrl=URL.createObjectURL(this.bannerBin)
        },
        avatarChange(){
            this.avatarBin=this.$refs.avatar.files[0]
            this.avatarUrl=URL.createObjectURL(this.avatarBin)
        },
        async save(){
            let fd=new FormData()
            fd.append('name',this.channel.name)
            fd.append('description',this.channel.description)
            //如果没有更改它们，则不上传
            if(this.bannerBin){
                fd.append('banner',this.bannerBin,Math.random()+'')
            }
            if(this.avatarBin){
                fd.append('avatar',this.avatarBin,Math.random()+'')
            }
            let res=await fetch('/api/content-platform/channel',{
                method:'PUT',
                body:fd
            })
            let json=await res.json()

            console.log('修改频道',json)

            if(json.code===1){
                this.getData()
            }
        }
    },
    async created() {
        this.getData()
    }
}
</script>

<template>
<div class="shipinhaoCustomization">
    <h3>频道设置</h3>
    <div class="item">
        <p>横幅图像</p>
        <div class="row">
            <el-avatar :size="100" shape="square" :src="bannerUrl" />
            <div class="op">
                <el-button round @click="$refs.banner.click()">更改</el-button>
                <input type="file" ref="banner" @change="bannerChange" style="display: none">
            </div>
        </div>
    </div>
    <div class="item">
        <p>头像</p>
        <div class="row">
            <el-avatar :size="100" :src="avatarUrl" />
            <div class="op">
                <el-button round @click="$refs.avatar.click()">更改</el-button>
                <input type="file" ref="avatar" @change="avatarChange" style="display: none">
            </div>
        </div>
    </div>
    <div class="item">
        <p>名称</p>
        <el-input v-model="channel.name" />
    </div>
    <div class="item">
        <p>描述</p>
        <el-input v-model="channel.description" type="textarea" />
    </div>

    <div class="save">
        <el-button type="primary" @click="save">保存</el-button>
    </div>

</div>
</template>

<style scoped lang="less">
.shipinhaoCustomization{
    padding: 1rem;

    .item{

        .row{
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
    }
    .save{
        margin-top: 1rem;
    }
}
</style>