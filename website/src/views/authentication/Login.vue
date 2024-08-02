<script>

import PasswordAuth from "../../components/PasswordAuth.vue";
import EmailAuth from "../../components/EmailAuth.vue";
import axios from "axios";

export default {
    name: "Login",
    components: {EmailAuth, PasswordAuth},

    data(){
        return {
            activeName:'password',
            remember:false
        }
    },
    methods:{
        async login(){ //点击登录按钮的回调
            let json=null
            if(this.activeName==='password'){ //密码登录
                let {account,password,verifyCode}=this.$store.state
                let res=await axios.post('/api/login',{
                    account,
                    password,
                    verifyCode,
                })
                json=res.data
                //密码登录结束
            }else{ //邮箱登录
                let {email,verifyCode}=this.$store.state

                let res=await axios.post('/api/email_login',{
                    email,
                    verifyCode,
                })
                json=res.data
                //邮箱登录结束
            }
            if(json.code===1){ //表示登录成功
                localStorage.setItem('token',json.data)

                //给用户的提示
                this.$message({
                    message:'欢迎回来！',
                    type:'success'
                })
                //如果不延迟，消息提示不出来
                await new Promise(resolve => setTimeout(resolve, 1000))
                //跳转
                this.$router.push('/message')
            }else{ //登录失败
                this.$message({
                    message:json.msg,
                    type:'error'
                })
            }
        }
    },
}
</script>

<template>
    <div class="login">
        <h2>自由聊天</h2>
        <div class="content">
            <el-tabs v-model="activeName" stretch>
                <el-tab-pane label="密码登录" name="password">
                    <password-auth/>
                </el-tab-pane>
                <el-tab-pane label="邮箱登录" name="email">
                    <email-auth/>
                </el-tab-pane>
            </el-tabs>

            <div class="side">
                <el-checkbox v-model="remember">记住账号</el-checkbox>
                <el-link type="primary" @click="$router.push('/authentication/resetPassword')">忘记密码</el-link>
            </div>

            <div class="row">
                <el-button type="primary" @click="login">登录</el-button>
            </div>
            <div class="row">
                <el-button @click="$router.push('/authentication/register')">立即注册</el-button>
            </div>
        </div>
    </div>
</template>

<style scoped lang="less">
.login{
    width: 436px;
    background-color: rgba(255,255,255,0.8);
    box-shadow: 0 4px 50px rgba(87,126,255,.4);
    border-radius: 8px;
    border: 2px solid #fff;

    h2{
        margin: 45px 0;
        text-align: center;
    }
    .content{
        padding: 0 42px 42px;

        .side{
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
        }
        .row{
            margin-bottom: 20px;

            button{
                width: 100%;
            }
        }
    }
}
</style>