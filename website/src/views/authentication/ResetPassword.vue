<script>
import MailVerifyCodeSender from "../../components/MailVerifyCodeSender.vue";
import axios from "axios";
import {Close} from "@element-plus/icons-vue";

export default {
    name: "ResetPassword",
    components: {Close, MailVerifyCodeSender},
    data(){
        return {
            account:'',
            verifyCode:'',
            step:1,
            email:'',
            newPassword:''
        }
    },
    methods:{
        async next(){ //下一步
            if(this.step===1&&this.account===''){
                this.$message({
                    message:'请填写账户',
                    type:'warning'
                })
                return
            }
            if(this.step===1){
                let res=await axios.get('/api/checkAccount?account='+this.account)
                let json=res.data
                if(json.code===0){
                    this.$message({
                        message:'账户不存在',
                        type:'error'
                    })
                    return
                }else{
                    this.email=json.data
                    this.step++
                    return
                }
            }
            if(this.step===2&&this.verifyCode===''){
                this.$message({
                    message:'请填写验证码',
                    type:'warning'
                })
                return
            }
            if(this.step===2){
                let res=await axios.get('/api/checkEmailVerifyCode?code='+this.verifyCode+'&email='+this.email)
                let json=res.data
                if (json.code===0){
                    this.$message({
                        message:json.msg,
                        type:'warning'
                    })
                    return
                }else{
                    this.step++
                    return
                }
            }
            if(this.step===3){
                //提交密码
                let res=await axios.post('/api/resetPwd',{
                    email:this.email,
                    password:this.newPassword,
                    verifyCode:this.verifyCode
                })
                let json=res.data
                if (json.code===0){
                    this.$message({
                        message:json.msg,
                        type:'error'
                    })
                    return
                }else{
                    this.$message({
                        message:'重置成功',
                        type:'success'
                    })
                    this.$router.push('/authentication/login')
                    return
                }
            }
        },
        prev(){
            if(this.step===1){
                return
            }

            this.step--
        }
    }
}
</script>

<template>
    <div class="reset-password">
        <h2>自由聊天 - reset</h2>

        <el-steps  :active="step" align-center>
            <el-step title="指定账户" />
            <el-step title="验证账户" />
            <el-step title="重置密码" />
        </el-steps>

        <div class="content">

            <div class="row">
<!--                //这里用v-if会有问题哦-->
                <el-input
                    v-model="account"
                    placeholder="请输入要重置的账户 UID/邮箱"
                    clearable
                    v-show="step===1"
                ></el-input>

                <div v-show="step===2">
                    <div style="margin-bottom: 1rem">验证：{{email}}</div>
                    <mail-verify-code-sender :email="email" @newValue="e=>verifyCode=e"/>
                </div>

                <div v-show="step===3">
                    <el-input v-model="newPassword" placeholder="请输入新密码" clearable show-password></el-input>
                </div>

            </div>
            <div class="row row2">
                <el-button style="margin-top: 12px" @click="prev" :disabled="step===1">上一步</el-button>

                <el-button style="margin-top: 12px" @click="next" >{{step===3?'确定': '下一步' }}</el-button>
            </div>

        </div>

        <div class="close" @click="$router.push('/authentication/login')">
            <el-icon><Close /></el-icon>
        </div>
    </div>
</template>

<style scoped lang="less">
.reset-password {
    width: 436px;
    background-color: rgba(255,255,255,0.8);
    box-shadow: 0 4px 50px rgba(87,126,255,.4);
    border-radius: 8px;
    border: 2px solid #fff;

    position: relative;

    h2{
        margin: 45px 0;
        text-align: center;
    }
    .content{
        padding: 20px 42px 42px;

        .row{
            margin-bottom: 20px;
        }
        .row2{
            display: flex;
            justify-content: space-between;
        }
    }
    .close{
        position: absolute;
        top: 1rem;
        right: 1rem;
        cursor: pointer;
    }
}
</style>