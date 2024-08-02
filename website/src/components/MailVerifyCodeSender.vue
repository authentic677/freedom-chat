<template>
    <div class="mail-verify-code-sender">
        <div class="myCol1">
            <el-input
                clearable
                v-model="verifyCode"
                placeholder="请输入验证码" >
            
                <template #prefix >
                    <svg  viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path d="M9.5 12.4L10.9286 14L14.5 10" stroke="#1C274C" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path> <path d="M3 10.4167C3 7.21907 3 5.62028 3.37752 5.08241C3.75503 4.54454 5.25832 4.02996 8.26491 3.00079L8.83772 2.80472C10.405 2.26824 11.1886 2 12 2C12.8114 2 13.595 2.26824 15.1623 2.80472L15.7351 3.00079C18.7417 4.02996 20.245 4.54454 20.6225 5.08241C21 5.62028 21 7.21907 21 10.4167C21 10.8996 21 11.4234 21 11.9914C21 14.4963 20.1632 16.4284 19 17.9041M3.19284 14C4.05026 18.2984 7.57641 20.5129 9.89856 21.5273C10.62 21.8424 10.9807 22 12 22C13.0193 22 13.38 21.8424 14.1014 21.5273C14.6796 21.2747 15.3324 20.9478 16 20.5328" stroke="#1C274C" stroke-width="1.5" stroke-linecap="round"></path> </g></svg>
                </template>
                
            </el-input>
        </div>
        <div class="myCol2">
            <el-button :loading="isloading" :disabled="isDisabled" @click="emailVerifyCode">{{verifyCodeBtn[verifyCodeBtn.length-1]}}</el-button>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    name:'MailVerifyCodeSender',
    data(){
        return {
            verifyCode:'',
            count:null,
            verifyCodeBtn:['发送验证码'],
            isloading:false,
            isDisabled:false
        }
    },
    props:['email'],
    methods:{
        async emailVerifyCode(){
            this.isloading=true
            this.verifyCodeBtn.push('正在发送')

            let res=await axios.get('/api/mailVerifyCode?email='+this.email)
            let json=res.data;
            
            this.isloading=false

            if(json.code===0){
                this.$message({
                    message:json.msg,
                    type:'error'
                })
                this.verifyCodeBtn.pop()
            }else{
                this.$message({
                    message:'发送成功，请注意查收',
                    type:'success'
                })
                this.countDown()
            }
            
        },
        countDown(){
            this.count=10
            this.isDisabled=true
            this.verifyCodeBtn.pop()
            this.verifyCodeBtn.push(this.count+' '+this.verifyCodeBtn[0])
            let handle=setInterval(() => {
                this.count--
                this.verifyCodeBtn.pop()
                this.verifyCodeBtn.push(this.count+' '+this.verifyCodeBtn[0])
                if(this.count===0){
                    this.verifyCodeBtn.pop()
                    this.isDisabled=false
                    clearInterval(handle)
                }
            }, 1000);
        },
    },
    watch:{
        verifyCode(newValue){
            this.$emit('newValue',newValue)
        }
    }
}
</script>

<style scoped lang="less">
.mail-verify-code-sender{
    display: flex;
    
    .myCol1{
        flex-grow: 1;

        svg{
            width: 18px;
        }
    }

}
</style>