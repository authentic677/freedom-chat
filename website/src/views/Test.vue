<script>
import {Dashboard}  from "@uppy/vue";
import Uppy from '@uppy/core';
import Webcam from '@uppy/webcam';
import XHR from '@uppy/xhr-upload'

// Don't forget the CSS: core and UI components + plugins you are using
import '@uppy/core/dist/style.css';
import '@uppy/dashboard/dist/style.css';
import '@uppy/webcam/dist/style.css';

export default {

    data(){
        return {
            uppy:null
        }
    },
    methods:{
        t(){

        }
    },
    created() {
        this.uppy = new Uppy().use(Webcam);
        this.uppy.use(XHR,{
            endpoint:'/api/zone/test/post'
        })
        console.log("???")
        console.log(this.uppy)
    },
    mounted() {

    },
    components:{
        Dashboard
    }
}
</script>

<template>
<div class="test">
    草泥马
    <Dashboard :uppy="uppy" />
    啊啊啊
</div>
</template>

<style lang="less" scoped>
.container{
    width: 400px;
    height: 600px;
    border: 1px solid black;
}
</style>