<script>
export default {
    name: "ShipinhaoPlayer",
    data(){
        return {
            base64:'', //视频首帧的base64编码的图像数据
        }
    },
    computed: {
        firstFrame(){
            return `url("${this.base64}") center center / cover no-repeat`
        }
    },
    mounted() {

        const video = this.$refs.video;
        const canvas = this.$refs.canvas

        // 当视频元数据加载完成时执行
        video.addEventListener('loadeddata', ()=> {
            // 确保视频已经准备好可以播放第一帧
            video.currentTime = 0;

            // 当视频可以播放时
            video.addEventListener('seeked', ()=> {
                // 设置 canvas 的宽高为视频的宽高
                canvas.width = video.videoWidth;
                canvas.height = video.videoHeight;

                // 在 canvas 上绘制视频的第一帧
                const ctx = canvas.getContext('2d');
                ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

                // 将 canvas 转为图片的 Base64 URL
                const dataURL = canvas.toDataURL('image/png');

                this.base64 = dataURL;
            }, { once: true });
        });

        // 加载视频
        video.load();
    },
}
</script>

<template>
<div class="shipinhaoPlayer">
    <div class="v">
        <video ref="video" controls>
            <source src="/xz.mp4" type="video/mp4" />
        </video>
        <canvas ref="canvas" style="display:none;"></canvas>
    </div>
</div>
</template>

<style scoped lang="less">
.shipinhaoPlayer{
    width: 100%;
    height: 100%;
    padding-right: 60px;
    box-sizing: border-box;

    .v{
        border-radius: 20px;

        position: relative;
        width: 100%;
        height: 100%;
        overflow: hidden; /* 隐藏溢出的部分 */
        background-color: #000; /* 背景颜色防止加载前出现白色空白 */

        video{
            position: relative;
            width: 100%;
            height: 100%;
            object-fit: contain; /* 保持视频内容按比例缩放 */
            z-index: 2; /* 视频层 */
            background: transparent; /* 背景透明，避免影响显示 */
        }
    }
    .v:before{
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: v-bind(firstFrame);
        filter: blur(20px); /* 应用模糊效果 */
        z-index: 1; /* 背景层 */

        transform: scale(1.2); /* 放大模糊背景，避免边缘空白 */
    }
}
</style>