<script>
import {ElMessage} from "element-plus";

export default {
    name: "ShipinhaoPublishVideo",
    data(){
        return {
            coverUrl:'',
            coverBin:'',
            videoUrl:'',
            videoBin:'',

            title:'',
            summary:'',
            visibility:'public'
        }
    },
    methods:{
        videoChange(){
            this.videoBin=this.$refs.video.files[0]
            this.videoUrl=URL.createObjectURL(this.videoBin)
            //设置视频封面为首帧
            console.log(this.$refs.videoDom)
            this.getVideoFrame()
        },
        coverChange(){
            this.coverBin=this.$refs.cover.files[0]
            this.coverUrl=URL.createObjectURL(this.coverBin)

        },
        getVideoFrame(isUseCurrent){
            let video=this.$refs.videoDom
            let canvas=this.$refs.canvas

            if (isUseCurrent){
                // 设置 canvas 的宽高为视频的宽高
                canvas.width = video.videoWidth;
                canvas.height = video.videoHeight;

                // 在 canvas 上绘制视频的第一帧
                const ctx = canvas.getContext('2d');
                ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

                // 将 canvas 转为图片的 Base64 URL
                canvas.toBlob(blob=>{
                    this.coverBin=blob
                    this.coverUrl=URL.createObjectURL(this.coverBin)

                    console.log(this.coverBin)
                },'image/png');
            }else {
                // 当视频元数据加载完成时执行
                video.addEventListener('loadeddata', ()=> {
                    // 确保视频已经准备好可以播放第一帧
                    video.currentTime = video.duration/2;

                    // 当视频可以播放时
                    video.addEventListener('seeked', ()=> {
                        // 设置 canvas 的宽高为视频的宽高
                        canvas.width = video.videoWidth;
                        canvas.height = video.videoHeight;

                        // 在 canvas 上绘制视频的第一帧
                        const ctx = canvas.getContext('2d');
                        ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

                        // 将 canvas 转为图片的 Base64 URL
                        canvas.toBlob(blob=>{
                            this.coverBin=blob
                            this.coverUrl=URL.createObjectURL(this.coverBin)

                            console.log(this.coverBin)
                        },'image/png');
                    }, { once: true });
                });
            }

        },

        async publish(){

            let fd=new FormData()
            fd.append('title',this.title)
            fd.append('summary',this.summary)
            fd.append('visibility',this.visibility)
            fd.append('video',this.videoBin,Math.random()+'')
            fd.append('cover',this.coverBin,Math.random()+'')



            let res=await fetch('/api/content-platform/video',{
                method:'POST',
                body:fd
            })
            let json=await res.json()

            console.log('发布视频',json)

            if (json.code===1){
                ElMessage({
                    message: '发布成功',
                    type: 'success',
                })
                this.$router.back()
            }
        }
    },
    created() {

    }
}
</script>

<template>
<div class="shipinhaoPublishVideo">
    <h3>发布视频</h3>
    <div class="row">
        <p>上传视频</p>

        <div class="videoFrame"  @click="$refs.video.click()">
            <div class="show1" v-show="!videoUrl">
                点击上传
            </div>
            <div class="show2" v-show="videoUrl">
                <video :src="videoUrl" ref="videoDom" controls></video>
                <canvas ref="canvas" style="display: none" ></canvas>
            </div>
            <input type="file" ref="video" @change="videoChange" style="display: none">
        </div>
    </div>
    <div class="row">
        <p>设置封面</p>

        <div class="coverFrame" @click="$refs.cover.click()">
            <div class="show1" v-if="!coverUrl" >
                点击上传
            </div>
            <div class="show2" v-if="coverUrl">
                <img :src="coverUrl" alt="">
            </div>
            <input type="file" ref="cover" @change="coverChange" style="display: none">
        </div>
        <div class="control">

            <el-button @click="getVideoFrame(true)">使用当前帧作为封面</el-button>
        </div>
    </div>
    <div class="row">
        <p>视频标题</p>
        <el-input v-model="title" />
    </div>
    <div class="row">
        <p>视频概要</p>
        <el-input v-model="summary" type="textarea" />
    </div>
    <div class="row">
        <p>设置可见性</p>
        <el-select
            v-model="visibility"
            size="small"
            style="width: 240px"
        >
            <el-option
                v-for="item in [{label:'公开',value:'public'},{label: '仅好友',value: 'friend'},{label: '仅自己',value: 'private'}]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
        </el-select>
    </div>
    <div class="row">
        <el-button type="primary" @click="publish">发布</el-button>
    </div>

</div>
</template>

<style scoped lang="less">
.shipinhaoPublishVideo{
    height: 100%;
    padding: 0 1rem;

    .row{
        margin: 1rem 0;

        .videoFrame{
            height: 150px;
            border-radius: 5px;
            overflow: hidden;

            .show1{
                height: 100%;
                box-sizing: border-box;
                border: 1px dotted gray;

                display: flex;
                justify-content: center;
                align-items: center;
            }
            .show2{
                height: 100%;

                video{
                    width: 100%;
                    height: 100%;
                }
            }
        }
        .coverFrame{
            width: 100px;
            height: 100px;
            border-radius: 5px;
            overflow: hidden;

            .show1{
                width: 100%;
                height: 100%;
                border: 1px dotted gray;
                box-sizing: border-box;
                text-align: center;
                line-height: 100px;
            }
            .show2{
                width: 100%;
                height: 100%;

                img{
                    width: 100%;
                    height: 100%;
                    object-fit: cover;
                }
            }
        }
        .control{
            margin-top: 1rem;
        }
    }
}
</style>