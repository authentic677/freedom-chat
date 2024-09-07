<script>
import PasswordAuth from "../components/PasswordAuth.vue";
import {ElLoading} from 'element-plus'
import MailVerifyCodeSender from "../components/MailVerifyCodeSender.vue";
import {mapState} from "vuex";
import SearchBox from "../components/SearchBox.vue";
import AgreeOrRefuseBtn from "../components/AgreeOrRefuseBtn.vue";
import {NButton} from 'naive-ui'
export default {
    name: "Test",
    components: {AgreeOrRefuseBtn, SearchBox, MailVerifyCodeSender, PasswordAuth,NButton},
    data(){
        return {
            active:1
        }
    },
    methods:{
        input(e){
            console.log('导出',e)
        },
        a(){
            console.log('同意')
        },
        r(){
            console.log('拒绝')
        },
        i(){
            console.log('忽略')
        }
    },
    computed:{
        ...mapState(['username','email'])
    }
}
</script>

<template>
<div class="test">

    <div class="img">
<!--        <img-->
<!--            src="http://127.0.0.1:9000/post/2024-09/image-jpeg/1f6e81ea-617a-424d-967c-ad1d26902c20/0000009599"-->
<!--        />-->
        <img src="http://127.0.0.1:9000/chat/2024-08/img/ecd13e66-bb37-40b7-ae75-2c13ae1e0f30/t.jpg" alt="">
    </div>

</div>
</template>

<style scoped>
.img{
    width: 100px;
    height: 100px;
    border: 1px solid black;

    overflow: hidden;


    img{
        width: 100%;
        height: 100%;
        object-fit: fill;
    }
}
</style>