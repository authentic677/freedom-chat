<script>
import config from "../../config/config.js";

export default {
    name: "ShipinhaoPlayer",
    data(){
        return {
            base64:'', //视频首帧的base64编码的图像数据

            //播放列表，一开始是0个
            playList:[],
            current:-1, //当前播放的视频缩索引
            currentVideo:{
                channel:{}
            },//通过监视属性变化其值
        }
    },
    methods:{
        async getNewVideo(){
            let res=await fetch('/api/content-platform/video?mode=rand')
            let json=await res.json()

            //处理url
            json.data.video=config.minioUrl+json.data.video
            console.log('新视频',json)

            this.playList.push(json.data)
        },
        //切换视频
        previous(){
            if (this.current>0){
                this.current--;
            }
        },
        async next(){
            if (this.current===-1||this.current===this.playList.length-1){
                await this.getNewVideo()
                this.current++
            }else{
                this.current++
            }
        }
    },
    computed: {
        config() {
            return config
        },
        firstFrame(){
            return `url("${this.base64}") center center / cover no-repeat`
        }
    },
    watch:{
        current(newValue){
            this.currentVideo=this.playList[newValue]
        }
    },
    created(){
        this.next()
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
        <video ref="video" :src="currentVideo.video" controls autoplay>
<!--            <source :src="currentVideo.video" type='video/*' />-->
        </video>
        <canvas ref="canvas" style="display:none;"></canvas>

        <div class="bottomInfo">
            <div class="title">
                {{currentVideo.title}}
            </div>
            <div class="summary">
                {{currentVideo.summary?currentVideo.summary:"暂无视频摘要"}}
            </div>
        </div>
        <div class="leftInfo">
            <div class="avatar">
                <img :src="config.minioUrl+currentVideo.channel.avatar" alt="">
            </div>
            <div class="item like">
                <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 90 94"  preserveAspectRatio="xMidYMid meet" ><defs><clipPath id="__lottie_element_2"><rect width="90" height="94" x="0" y="0"></rect></clipPath><clipPath id="__lottie_element_4"><path d="M0,0 L1680,0 L1680,1680 L0,1680z"></path></clipPath><clipPath id="__lottie_element_8"><path d="M0,0 L1680,0 L1680,1680 L0,1680z"></path></clipPath><clipPath id="__lottie_element_12"><path d="M0,0 L1000,0 L1000,1000 L0,1000z"></path></clipPath><clipPath id="__lottie_element_19"><path d="M0,0 L1000,0 L1000,1000 L0,1000z"></path></clipPath><clipPath id="__lottie_element_26"><path d="M0,0 L1000,0 L1000,1000 L0,1000z"></path></clipPath><clipPath id="__lottie_element_36"><path d="M0,0 L168,0 L168,168 L0,168z"></path></clipPath><clipPath id="__lottie_element_40"><path d="M0,0 L1680,0 L1680,1680 L0,1680z"></path></clipPath><clipPath id="__lottie_element_44"><path d="M0,0 L1000,0 L1000,1000 L0,1000z"></path></clipPath><clipPath id="__lottie_element_51"><path d="M0,0 L1200,0 L1200,1000 L0,1000z"></path></clipPath><clipPath id="__lottie_element_58"><path d="M0,0 L1200,0 L1200,1000 L0,1000z"></path></clipPath><clipPath id="__lottie_element_68"><path d="M0,0 L120,0 L120,120 L0,120z"></path></clipPath></defs><g clip-path="url(#__lottie_element_2)"><g clip-path="url(#__lottie_element_36)" transform="matrix(0.5249999761581421,0,0,0.5249999761581421,1.0250015258789062,8.822105407714844)" opacity="1" style="display: block;"><g clip-path="url(#__lottie_element_40)" transform="matrix(0.10000000149011612,0,0,0.10000000149011612,0,0)" opacity="1" style="display: block;"><g style="display: none;"><g><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4"></path></g></g><g clip-path="url(#__lottie_element_68)" style="display: none;"><g style="display: none;"><g><path></path><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4"></path></g></g><g style="display: none;"><g><path></path><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4"></path></g></g><g style="display: none;"><g><path></path><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4"></path></g></g><g style="display: none;"><g><path></path><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4"></path></g></g><g style="display: none;"><g><path></path><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4"></path></g></g><g style="display: none;"><g><path></path><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4"></path></g></g></g><g class="png" clip-path="url(#__lottie_element_58)" transform="matrix(1,0,0,1,240,340)" opacity="1" style="display: block;"><g transform="matrix(1,0,0,1,154.5,92)" opacity="1" style="display: block;"><g opacity="1" transform="matrix(1,0,0,1,0,0)"><path fill="rgb(255,255,255)" fill-opacity="1" d=" M453.0360107421875,88.71199798583984 C493.77398681640625,30.663999557495117 560.6599731445312,0 634.2509765625,0 C774.4030151367188,0 890.9719848632812,121.58999633789062 890.9719848632812,266.1369934082031 C890.9719848632812,266.1549987792969 890.9719848632812,266.1730041503906 890.9719848632812,266.1910095214844 C890.9810180664062,266.1709899902344 890.9910278320312,266.1510009765625 891,266.1310119628906 C891,270.25201416015625 890.9299926757812,273.37200927734375 890.8779907226562,275.6679992675781 C890.8060302734375,278.8190002441406 890.77001953125,280.4159851074219 891,280.9159851074219 C890.468994140625,311.3190002441406 885.2219848632812,336.43798828125 875.8989868164062,369.62799072265625 C870.6090087890625,375.5880126953125 865.6939697265625,386.81201171875 860.7979736328125,399.1990051269531 C853.073974609375,411.1969909667969 850.1510009765625,417.0090026855469 845.697021484375,428.7699890136719 C841.1519775390625,436.39898681640625 836.323974609375,444.06298828125 831.2449951171875,451.7439880371094 C793.7960205078125,508.54400634765625 743.7080078125,565.073974609375 693.6669921875,615.2520141601562 C615.3359985351562,694.2769775390625 535.5440063476562,760.0579833984375 500.8320007324219,788.6749877929688 C491.24700927734375,796.5759887695312 485.1000061035156,801.6439819335938 483.3680114746094,803.375 C471.0669860839844,815.677978515625 458.7650146484375,815.9860229492188 446.4630126953125,815.9940185546875 C446.1390075683594,815.9979858398438 445.8139953613281,816 445.4859924316406,816 C420.25,816 407.6319885253906,803.3809814453125 395.0140075683594,790.7630004882812 C394.0509948730469,789.7999877929688 391.60101318359375,787.7789916992188 387.8580017089844,784.7830200195312 C349.625,756.5999755859375 263.58599853515625,687.7860107421875 182.74200439453125,604.7830200195312 C121.06600189208984,542.02001953125 61.62200164794922,470.0069885253906 29.091999053955078,399.5880126953125 C16.474000930786133,374.35101318359375 0.7310000061988831,314.2640075683594 0,280.9219970703125 C0.26899999380111694,280.6549987792969 0.22699999809265137,279.04901123046875 0.14399999380111694,275.843994140625 C0.08299999684095383,273.49798583984375 0,270.2969970703125 0,266.1369934082031 C0,121.52400207519531 116.50199890136719,0 256.72100830078125,0 C330.1789855957031,0 397.1310119628906,30.663999557495117 453.0360107421875,88.71199798583984z"></path></g></g></g><g class="png" clip-path="url(#__lottie_element_51)" style="display: none;"><g style="display: none;"><g><path></path></g></g></g><g class="png" clip-path="url(#__lottie_element_44)" style="display: none;"><g style="display: none;"><g><path></path></g></g></g></g></g><g clip-path="url(#__lottie_element_4)" style="display: none;"><g clip-path="url(#__lottie_element_8)" style="display: none;"><g style="display: none;"><g><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4"></path></g></g><g class="png" clip-path="url(#__lottie_element_26)" style="display: none;"><g style="display: none;"><g><path></path></g></g></g><g class="png" clip-path="url(#__lottie_element_19)" style="display: none;"><g style="display: none;"><g><path></path></g></g></g><g class="png" clip-path="url(#__lottie_element_12)" style="display: none;"><g style="display: none;"><g><path></path></g></g></g></g></g></g></svg>
                <div class="text">1.0万</div>
            </div>
            <div class="item comment">
                <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 99 99"  preserveAspectRatio="xMidYMid meet" ><defs><clipPath id="__lottie_element_90"><rect width="99" height="99" x="0" y="0"></rect></clipPath></defs><g clip-path="url(#__lottie_element_90)"><g transform="matrix(0.6616887450218201,0,0,0.6616887450218201,76.31509399414062,52.85038757324219)" opacity="1" style="display: block;"><g opacity="1" transform="matrix(3,0,0,3,0,0)"><path fill="rgb(255,255,255)" fill-opacity="1" d=" M-4.644999980926514,4.482999801635742 C-7.25,7.818999767303467 -13.482000350952148,8.300000190734863 -13.482000350952148,8.300000190734863 C-13.482000350952148,8.300000190734863 -14.413999557495117,11.48799991607666 -12.135000228881836,10.821000099182129 C-9.855999946594238,10.154000282287598 -7.25,8.152000427246094 -4.644999980926514,4.482999801635742z M-17.36,-1.04 C-17.36,-0.16 -18.07,0.56 -18.96,0.56 C-18.96,0.56 -18.96,0.56 -18.96,0.56 C-19.85,0.56 -20.57,-0.16 -20.57,-1.04 C-20.57,-1.92 -19.85,-2.64 -18.96,-2.64 C-18.07,-2.64 -17.36,-1.92 -17.36,-1.04z M-11.89,-1.04 C-11.89,-0.16 -12.61,0.56 -13.5,0.56 C-13.5,0.56 -13.5,0.56 -13.5,0.56 C-14.39,0.56 -15.11,-0.16 -15.11,-1.04 C-15.11,-1.92 -14.39,-2.64 -13.5,-2.64 C-12.61,-2.64 -11.89,-1.92 -11.89,-1.04z M-6.43,-1.04 C-6.43,-0.16 -7.15,0.56 -8.04,0.56 C-8.04,0.56 -8.04,0.56 -8.04,0.56 C-8.92,0.56 -9.64,-0.16 -9.64,-1.04 C-9.64,-1.92 -8.92,-2.64 -8.04,-2.64 C-7.15,-2.64 -6.43,-1.92 -6.43,-1.04z M-5.79,5.98 C-3.56,3.75 -2.25,1.42 -2.25,-1.29 C-2.25,-6.79 -7.29,-11.25 -13.5,-11.25 C-19.71,-11.25 -24.75,-6.79 -24.75,-1.29 C-24.75,4.21 -19.55,7.99 -13.34,7.99 C-13.34,7.99 -13.34,11.06 -13.34,11.06 C-13.34,11.06 -8.51,8.72 -5.79,5.98z"></path><g opacity="1" transform="matrix(1,0,0,1,-6.75,-7.456999778747559)"><g opacity="1" transform="matrix(-1,0,0,1,0,0)"></g><g opacity="1" transform="matrix(-1,0,0,1,0,0)"></g><g opacity="1" transform="matrix(-1,0,0,1,0,0)"></g><g opacity="1" transform="matrix(-1,0,0,1,0,0)"></g></g></g></g></g></svg>
                <div class="text">3226</div>
            </div>
            <div class="item collect">
                <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 100 100"  preserveAspectRatio="xMidYMid meet" ><defs><clipPath id="__lottie_element_95"><rect width="100" height="100" x="0" y="0"></rect></clipPath><clipPath id="__lottie_element_100"><path d="M0,0 L1000,0 L1000,1000 L0,1000z"></path></clipPath><clipPath id="__lottie_element_122"><path d="M0,0 L120,0 L120,120 L0,120z"></path></clipPath><clipPath id="__lottie_element_129"><path d="M0,0 L120,0 L120,120 L0,120z"></path></clipPath><clipPath id="__lottie_element_136"><path d="M0,0 L120,0 L120,120 L0,120z"></path></clipPath><clipPath id="__lottie_element_143"><path d="M0,0 L120,0 L120,120 L0,120z"></path></clipPath><clipPath id="__lottie_element_156"><path d="M0,0 L120,0 L120,120 L0,120z"></path></clipPath><clipPath id="__lottie_element_169"><path d="M0,0 L120,0 L120,120 L0,120z"></path></clipPath></defs><g clip-path="url(#__lottie_element_95)"><g clip-path="url(#__lottie_element_169)" style="display: none;"><g style="display: none;"><g><path></path></g></g><g style="display: none;"><g><path></path></g></g><g style="display: none;"></g></g><g clip-path="url(#__lottie_element_156)" style="display: none;" transform="matrix(0.4658204913139343,0,0,0.4658204913139343,23.050769805908203,26.447376251220703)" opacity="1"><g style="display: block;" transform="matrix(1,0,0,1,10,12)" opacity="1"><g opacity="1" transform="matrix(1,0,0,1,0,0)"><path fill="rgb(255,255,255)" fill-opacity="1" d=" M43.79199981689453,4.316999912261963 C45.93899917602539,-1.4390000104904175 54.06100082397461,-1.4390000104904175 56.20800018310547,4.316999912261963 C56.20800018310547,4.316999912261963 65.927001953125,30.375 65.927001953125,30.375 C65.927001953125,30.375 93.6510009765625,31.59000015258789 93.6510009765625,31.59000015258789 C99.7750015258789,31.857999801635742 102.28500366210938,39.60200119018555 97.48799896240234,43.428001403808594 C97.48799896240234,43.428001403808594 75.77100372314453,60.74700164794922 75.77100372314453,60.74700164794922 C75.77100372314453,60.74700164794922 83.18599700927734,87.55599975585938 83.18599700927734,87.55599975585938 C84.8239974975586,93.47799682617188 78.25299835205078,98.26399993896484 73.14099884033203,94.87200164794922 C73.14099884033203,94.87200164794922 50,79.51799774169922 50,79.51799774169922 C50,79.51799774169922 26.858999252319336,94.87200164794922 26.858999252319336,94.87200164794922 C21.746999740600586,98.26399993896484 15.175999641418457,93.47799682617188 16.81399917602539,87.55599975585938 C16.81399917602539,87.55599975585938 24.229000091552734,60.74700164794922 24.229000091552734,60.74700164794922 C24.229000091552734,60.74700164794922 2.51200008392334,43.428001403808594 2.51200008392334,43.428001403808594 C-2.2850000858306885,39.60200119018555 0.22499999403953552,31.857999801635742 6.348999977111816,31.59000015258789 C6.348999977111816,31.59000015258789 34.073001861572266,30.375 34.073001861572266,30.375 C34.073001861572266,30.375 43.79199981689453,4.316999912261963 43.79199981689453,4.316999912261963z"></path></g></g><g style="display: block;" transform="matrix(1,0,0,1,10,45.720001220703125)" opacity="1"><g opacity="1" transform="matrix(1,0,0,1,0,0)"><path fill="rgb(255,255,255)" fill-opacity="1" d=" M77.80999755859375,25.400999069213867 C77.80999755859375,25.400999069213867 97.48799896240234,9.708000183105469 97.48799896240234,9.708000183105469 C100.77100372314453,7.090000152587891 100.63200378417969,2.635999917984009 98.25399780273438,0 C98.02300262451172,0.24799999594688416 97.76799774169922,0.48500001430511475 97.48799896240234,0.7080000042915344 C97.48799896240234,0.7080000042915344 75.77100372314453,18.027000427246094 75.77100372314453,18.027000427246094 C75.77100372314453,18.027000427246094 77.80999755859375,25.400999069213867 77.80999755859375,25.400999069213867z M17.757999420166016,50.42300033569336 C17.757999420166016,50.42300033569336 16.81399917602539,53.83599853515625 16.81399917602539,53.83599853515625 C15.175999641418457,59.757999420166016 21.746999740600586,64.54399871826172 26.858999252319336,61.152000427246094 C26.858999252319336,61.152000427246094 50,45.79800033569336 50,45.79800033569336 C50,45.79800033569336 73.14099884033203,61.152000427246094 73.14099884033203,61.152000427246094 C78.25299835205078,64.54399871826172 84.8239974975586,59.757999420166016 83.18599700927734,53.83599853515625 C83.18599700927734,53.83599853515625 82.24199676513672,50.42300033569336 82.24199676513672,50.42300033569336 C80.31900024414062,53.132999420166016 76.4280014038086,54.33300018310547 73.14099884033203,52.152000427246094 C73.14099884033203,52.152000427246094 50,36.79800033569336 50,36.79800033569336 C50,36.79800033569336 26.858999252319336,52.152000427246094 26.858999252319336,52.152000427246094 C23.57200050354004,54.33300018310547 19.680999755859375,53.132999420166016 17.757999420166016,50.42300033569336z M1.746000051498413,0 C-0.6320000290870667,2.635999917984009 -0.7710000276565552,7.090000152587891 2.51200008392334,9.708000183105469 C2.51200008392334,9.708000183105469 22.190000534057617,25.400999069213867 22.190000534057617,25.400999069213867 C22.190000534057617,25.400999069213867 24.229000091552734,18.027000427246094 24.229000091552734,18.027000427246094 C24.229000091552734,18.027000427246094 2.51200008392334,0.7080000042915344 2.51200008392334,0.7080000042915344 C2.2320001125335693,0.48500001430511475 1.9769999980926514,0.24799999594688416 1.746000051498413,0z"></path></g></g><g style="display: block;" transform="matrix(1,0,0,1,60,60)" opacity="1"></g></g><g clip-path="url(#__lottie_element_143)" transform="matrix(0.4646500051021576,0,0,0.4646500051021576,23.121000289916992,26.373146057128906)" opacity="1" style="display: block;"><g transform="matrix(1,0,0,1,10,12)" opacity="1" style="display: block;"><g opacity="1" transform="matrix(1,0,0,1,0,0)"><path fill="rgb(255,255,255)" fill-opacity="1" d=" M43.79199981689453,4.316999912261963 C45.93899917602539,-1.4390000104904175 54.06100082397461,-1.4390000104904175 56.20800018310547,4.316999912261963 C56.20800018310547,4.316999912261963 65.927001953125,30.375 65.927001953125,30.375 C65.927001953125,30.375 93.6510009765625,31.59000015258789 93.6510009765625,31.59000015258789 C99.7750015258789,31.857999801635742 102.28500366210938,39.60200119018555 97.48799896240234,43.428001403808594 C97.48799896240234,43.428001403808594 75.77100372314453,60.74700164794922 75.77100372314453,60.74700164794922 C75.77100372314453,60.74700164794922 83.18599700927734,87.55599975585938 83.18599700927734,87.55599975585938 C84.8239974975586,93.47799682617188 78.25299835205078,98.26399993896484 73.14099884033203,94.87200164794922 C73.14099884033203,94.87200164794922 50,79.51799774169922 50,79.51799774169922 C50,79.51799774169922 26.858999252319336,94.87200164794922 26.858999252319336,94.87200164794922 C21.746999740600586,98.26399993896484 15.175999641418457,93.47799682617188 16.81399917602539,87.55599975585938 C16.81399917602539,87.55599975585938 24.229000091552734,60.74700164794922 24.229000091552734,60.74700164794922 C24.229000091552734,60.74700164794922 2.51200008392334,43.428001403808594 2.51200008392334,43.428001403808594 C-2.2850000858306885,39.60200119018555 0.22499999403953552,31.857999801635742 6.348999977111816,31.59000015258789 C6.348999977111816,31.59000015258789 34.073001861572266,30.375 34.073001861572266,30.375 C34.073001861572266,30.375 43.79199981689453,4.316999912261963 43.79199981689453,4.316999912261963z"></path></g></g><g transform="matrix(1,0,0,1,10,45.720001220703125)" opacity="1" style="display: block;"><g opacity="1" transform="matrix(1,0,0,1,0,0)"><path fill="rgb(255,255,255)" fill-opacity="1" d=" M77.80999755859375,25.400999069213867 C77.80999755859375,25.400999069213867 97.48799896240234,9.708000183105469 97.48799896240234,9.708000183105469 C100.77100372314453,7.090000152587891 100.63200378417969,2.635999917984009 98.25399780273438,0 C98.02300262451172,0.24799999594688416 97.76799774169922,0.48500001430511475 97.48799896240234,0.7080000042915344 C97.48799896240234,0.7080000042915344 75.77100372314453,18.027000427246094 75.77100372314453,18.027000427246094 C75.77100372314453,18.027000427246094 77.80999755859375,25.400999069213867 77.80999755859375,25.400999069213867z M17.757999420166016,50.42300033569336 C17.757999420166016,50.42300033569336 16.81399917602539,53.83599853515625 16.81399917602539,53.83599853515625 C15.175999641418457,59.757999420166016 21.746999740600586,64.54399871826172 26.858999252319336,61.152000427246094 C26.858999252319336,61.152000427246094 50,45.79800033569336 50,45.79800033569336 C50,45.79800033569336 73.14099884033203,61.152000427246094 73.14099884033203,61.152000427246094 C78.25299835205078,64.54399871826172 84.8239974975586,59.757999420166016 83.18599700927734,53.83599853515625 C83.18599700927734,53.83599853515625 82.24199676513672,50.42300033569336 82.24199676513672,50.42300033569336 C80.31900024414062,53.132999420166016 76.4280014038086,54.33300018310547 73.14099884033203,52.152000427246094 C73.14099884033203,52.152000427246094 50,36.79800033569336 50,36.79800033569336 C50,36.79800033569336 26.858999252319336,52.152000427246094 26.858999252319336,52.152000427246094 C23.57200050354004,54.33300018310547 19.680999755859375,53.132999420166016 17.757999420166016,50.42300033569336z M1.746000051498413,0 C-0.6320000290870667,2.635999917984009 -0.7710000276565552,7.090000152587891 2.51200008392334,9.708000183105469 C2.51200008392334,9.708000183105469 22.190000534057617,25.400999069213867 22.190000534057617,25.400999069213867 C22.190000534057617,25.400999069213867 24.229000091552734,18.027000427246094 24.229000091552734,18.027000427246094 C24.229000091552734,18.027000427246094 2.51200008392334,0.7080000042915344 2.51200008392334,0.7080000042915344 C2.2320001125335693,0.48500001430511475 1.9769999980926514,0.24799999594688416 1.746000051498413,0z"></path></g></g><g transform="matrix(1,0,0,1,60,60)" opacity="1" style="display: block;"></g></g><g clip-path="url(#__lottie_element_136)" style="display: none;"><g style="display: none;"><g><path></path></g></g></g><g clip-path="url(#__lottie_element_129)" style="display: none;" transform="matrix(0,0,0,0,51,51)" opacity="0"><g style="display: block;" transform="matrix(1,0,0,1,10,12)" opacity="1"><g opacity="1" transform="matrix(1,0,0,1,0,0)"><path fill="rgb(255,184,2)" fill-opacity="1" d=" M43.79199981689453,4.316999912261963 C45.93899917602539,-1.4390000104904175 54.06100082397461,-1.4390000104904175 56.20800018310547,4.316999912261963 C56.20800018310547,4.316999912261963 65.927001953125,30.375 65.927001953125,30.375 C65.927001953125,30.375 93.6510009765625,31.59000015258789 93.6510009765625,31.59000015258789 C99.7750015258789,31.857999801635742 102.28500366210938,39.60200119018555 97.48799896240234,43.428001403808594 C97.48799896240234,43.428001403808594 75.77100372314453,60.74700164794922 75.77100372314453,60.74700164794922 C75.77100372314453,60.74700164794922 83.18599700927734,87.55599975585938 83.18599700927734,87.55599975585938 C84.8239974975586,93.47799682617188 78.25299835205078,98.26399993896484 73.14099884033203,94.87200164794922 C73.14099884033203,94.87200164794922 50,79.51799774169922 50,79.51799774169922 C50,79.51799774169922 26.858999252319336,94.87200164794922 26.858999252319336,94.87200164794922 C21.746999740600586,98.26399993896484 15.175999641418457,93.47799682617188 16.81399917602539,87.55599975585938 C16.81399917602539,87.55599975585938 24.229000091552734,60.74700164794922 24.229000091552734,60.74700164794922 C24.229000091552734,60.74700164794922 2.51200008392334,43.428001403808594 2.51200008392334,43.428001403808594 C-2.2850000858306885,39.60200119018555 0.22499999403953552,31.857999801635742 6.348999977111816,31.59000015258789 C6.348999977111816,31.59000015258789 34.073001861572266,30.375 34.073001861572266,30.375 C34.073001861572266,30.375 43.79199981689453,4.316999912261963 43.79199981689453,4.316999912261963z"></path></g></g></g><g clip-path="url(#__lottie_element_122)" style="display: none;"><g style="display: none;"><g><path></path></g></g></g><g style="display: none;"><g><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4"></path></g></g><g clip-path="url(#__lottie_element_100)" style="display: none;" transform="matrix(0.1301020085811615,0,0,0.1301020085811615,-14.050996780395508,-14.051002502441406)" opacity="1"><g style="display: block;" transform="matrix(1,0,0,1,500,500)" opacity="1"><g opacity="1" transform="matrix(1,0,0,1,0.5,-324.5)"><path fill="rgb(250,206,21)" fill-opacity="1" d=" M0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0z"></path><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4" stroke="rgb(255,255,255)" stroke-opacity="1" stroke-width="0" d=" M0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0z"></path></g></g><g style="display: block;" transform="matrix(0.5,0.8660253882408142,-0.8660253882408142,0.5,500,500)" opacity="1"><g opacity="1" transform="matrix(1,0,0,1,0.5,-324.5)"><path fill="rgb(255,184,2)" fill-opacity="1" d=" M0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0z"></path><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4" stroke="rgb(255,255,255)" stroke-opacity="1" stroke-width="0" d=" M0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0z"></path></g></g><g style="display: block;" transform="matrix(-0.5,0.8660253882408142,-0.8660253882408142,-0.5,500,500)" opacity="1"><g opacity="1" transform="matrix(1,0,0,1,0.5,-324.5)"><path fill="rgb(250,206,21)" fill-opacity="1" d=" M0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0z"></path><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4" stroke="rgb(255,255,255)" stroke-opacity="1" stroke-width="0" d=" M0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0z"></path></g></g><g style="display: block;" transform="matrix(-1,0,0,-1,500,500)" opacity="1"><g opacity="1" transform="matrix(1,0,0,1,0.5,-324.5)"><path fill="rgb(255,184,2)" fill-opacity="1" d=" M0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0z"></path><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4" stroke="rgb(255,255,255)" stroke-opacity="1" stroke-width="0" d=" M0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0z"></path></g></g><g style="display: block;" transform="matrix(-0.5,-0.8660253882408142,0.8660253882408142,-0.5,500,500)" opacity="1"><g opacity="1" transform="matrix(1,0,0,1,0.5,-324.5)"><path fill="rgb(250,206,21)" fill-opacity="1" d=" M0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0z"></path><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4" stroke="rgb(255,255,255)" stroke-opacity="1" stroke-width="0" d=" M0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0z"></path></g></g><g style="display: block;" transform="matrix(0.5,-0.8660253882408142,0.8660253882408142,0.5,500,500)" opacity="1"><g opacity="1" transform="matrix(1,0,0,1,0.5,-324.5)"><path fill="rgb(255,184,2)" fill-opacity="1" d=" M0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0z"></path><path stroke-linecap="butt" stroke-linejoin="miter" fill-opacity="0" stroke-miterlimit="4" stroke="rgb(255,255,255)" stroke-opacity="1" stroke-width="0" d=" M0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0 C0,0 0,0 0,0z"></path></g></g></g></g></svg>
                <div class="text">10万</div>
            </div>
        </div>
    </div>
    <div class="change">
        <div class="previous" @click="previous">
            <svg t="1728438202094" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4393" width="200" height="200"><path d="M900.8 959.6c-16.8 0-33.7-5.6-44.9-16.9L548.5 581.1c-11.2-5.6-16.9-11.2-33.7-11.2-5.6 0-22.5 5.6-28.1 11.2L160.3 941.6c-22.5 22.5-56.2 22.5-78.6 5.6-22.5-22.5-22.5-56.2-5.6-78.6l326.5-360.5c28.1-33.7 73-50.5 117.9-50.5 44.9 0 84.2 22.5 117.9 56.2l307.3 356c22.5 22.5 16.8 56.2-5.6 78.6-11.3 5.5-28.1 11.2-39.3 11.2z m0 0" fill="#333333" p-id="4394"></path></svg>
        </div>
        <div class="next" @click="next">
            <svg t="1728438184747" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4250" width="200" height="200"><path d="M900.8 63.7c11.2 0 28.1 5.6 39.3 11.2 22.5 22.5 28.1 56.2 5.6 78.6l-307.3 356c-33.7 33.7-73 56.2-117.9 56.2-44.9 0-89.9-16.8-117.9-50.5L76 154.7c-16.8-22.5-16.8-56.2 5.6-78.6 22.5-16.9 56.2-16.9 78.6 5.6l326.5 360.5c5.6 5.6 22.5 11.2 28.1 11.2 16.8 0 22.5-5.6 33.7-11.2L855.8 80.6c11.3-11.3 28.1-16.9 45-16.9z m0 0" fill="#333333" p-id="4251"></path></svg>
        </div>
    </div>
</div>
</template>

<style scoped lang="less">
.shipinhaoPlayer{
    width: 100%;
    height: 100%;
    padding-right: 60px;
    box-sizing: border-box;

    position: relative;

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

        .bottomInfo{
            position: absolute;
            left: 0;
            bottom: 0;
            color: white;
            z-index: 2;

            margin-bottom: 4rem;
            margin-left: 1rem;

            .title{
                font-size: 18px;
            }
        }
        .leftInfo{
            position: absolute;
            right: 1rem;
            bottom: 4rem;
            color: white;
            z-index: 2;

            .avatar{
                width: 60px;
                height: 60px;
                border-radius: 50%;
                overflow: hidden;
                border: 2px solid white;

                margin-bottom: 1rem;

                img{
                    width: 100%;
                    height: 100%;
                }
            }
            .item{
                text-align: center;

                svg{
                    width: 60px;
                    height: 60px;
                }
            }
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

    .change{
        position: absolute;
        top: 50%;
        right: 0;
        transform: translateY(-50%);

        display: flex;
        flex-direction: column;

        .previous{
            padding: 1rem 1rem 0.5rem;

            svg{
                width: 25px;
                height: 25px;
            }
        }
        .next{
            padding: 1rem 1rem 0.5rem;

            svg{
                width: 25px;
                height: 25px;
            }
        }
    }
}
</style>