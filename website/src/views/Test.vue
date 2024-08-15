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

    <n-button>按钮</n-button>

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