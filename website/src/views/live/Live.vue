<script>
import axios from "axios";

export default {
    name: "Live",
    data(){
        return {
            peerUser: {}
        }
    },
    methods:{

        async peerUserInfo(){
            let res=await axios.get(`/api/user/${this.$route.params.uid}`,{
                headers:{
                    token: localStorage.getItem("token")
                }
            })
            let json=res.data

            console.log(json)

            if(json.code===1){
                this.peerUser=json.data
            }
        }
    },
    async created() {
        await this.peerUserInfo(); //获取对方用户数据
        if(this.peerUser.isOnline===0){
            this.$alert('对方不在线','通话失败')
        }
    }
}
</script>

<template>
<div class="live">
    <router-view :peerUser="peerUser"></router-view>
</div>
</template>

<style scoped lang="less">
.live{
    height: 100vh;
}
</style>