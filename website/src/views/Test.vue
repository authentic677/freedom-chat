<script>
import PasswordAuth from "../components/PasswordAuth.vue";
import {ElLoading} from 'element-plus'
import MailVerifyCodeSender from "../components/MailVerifyCodeSender.vue";
import {mapState} from "vuex";
import SearchBox from "../components/SearchBox.vue";
import AgreeOrRefuseBtn from "../components/AgreeOrRefuseBtn.vue";
export default {
    name: "Test",
    components: {AgreeOrRefuseBtn, SearchBox, MailVerifyCodeSender, PasswordAuth},
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
    <agree-or-refuse-btn @agree="a" @refuse="r" @ignore="i"/>

    <div>
        <svg t="1710573236515" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4226" width="200" height="200"><path d="M966.4 323.2c-9.6-9.6-25.6-9.6-35.2 0l-416 416-425.6-416c-9.6-9.6-25.6-9.6-35.2 0-9.6 9.6-9.6 25.6 0 35.2l441.6 432c9.6 9.6 25.6 9.6 35.2 0l435.2-432C976 345.6 976 332.8 966.4 323.2z" p-id="4227"></path></svg>
    </div>
</div>
</template>

<style scoped>
.test{
    padding: 1rem;
}
svg{
    width: 16px;
    height: 16px;
}
</style>