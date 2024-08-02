<script>
import PasswordAuth from "../../components/PasswordAuth.vue";
import EmailAuth from "../../components/EmailAuth.vue";
import MailVerifyCodeSender from "../../components/MailVerifyCodeSender.vue";
import axios from "axios";

export default {
    name: "Register",
    components: {MailVerifyCodeSender, EmailAuth, PasswordAuth},

    data(){
        return {
        }
    },
    methods: {
        async register() {
            let {username,password,email,verifyCode}=this.$store.state
            if (username === '' || password === '' || email === '' || verifyCode === '') {
                this.$message({
                    message: '请确保表单填写完整',
                    type: 'warning'
                })
                return
            }
            let res=await axios.post('/api/register',{
                username,
                password,
                email,
                verifyCode
            })
            let json=res.data

            if (json.code === 0) {
                this.$message({
                    message: json.msg,
                    type: 'error'
                })
            } else {
                this.$message({
                    message: '注册成功',
                    type: 'success'
                })
                this.$alert('您分配的UID是：' + json.data.uid, '注册成功', {
                    confirmButtonText: '确定',
                    callback: action => {
                        this.$router.push('/authentication/login')
                    }
                });
            }
        },
    }
}
</script>

<template>
    <div class="register">
        <h2>自由聊天 - sign up</h2>
        <div class="content">
            <div class="row">
                <el-input v-model="$store.state.username" placeholder="请输入用户名" clearable>
                    <template #prefix>
                        <svg fill="none" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg" class="svg-icon input-icon" data-v-f97f88d6=""><rect x="6.39395" y="1.47451" width="7.21356" height="8.40786" rx="3.60678" stroke="currentColor" stroke-width="1.2"></rect> <path d="M2.1874 16.7663C2.1874 12.9673 5.26711 9.8876 9.06612 9.8876H10.9358C14.7348 9.8876 17.8145 12.9673 17.8145 16.7663C17.8145 17.7257 17.0368 18.5034 16.0774 18.5034H3.9245C2.96513 18.5034 2.1874 17.7257 2.1874 16.7663Z" stroke="currentColor" stroke-width="1.2"></path></svg>
                    </template>
                </el-input>
            </div>

            <div class="row">
                <el-input v-model="$store.state.password" placeholder="请输入密码" clearable show-password>
                    <template #prefix >
                        <svg viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg" class="svg-icon input-icon" data-v-f97f88d6=""><rect x="3.1" y="8.1" width="13.8" height="9.63333" rx="1.4" stroke="currentColor" stroke-width="1.2"></rect> <path fill-rule="evenodd" clip-rule="evenodd" d="M13.0859 5.71448V7.85736C13.0859 7.85749 13.0859 7.85762 13.0859 7.85775H14.2859C14.2859 7.85762 14.2859 7.85749 14.2859 7.85736V5.71448C14.2859 3.34751 12.3671 1.42871 10.0001 1.42871C7.63316 1.42871 5.71436 3.34751 5.71436 5.71447V7.85736C5.71436 7.85749 5.71436 7.85762 5.71436 7.85775H6.91436C6.91436 7.85762 6.91436 7.85749 6.91436 7.85736V5.71447C6.91436 4.01025 8.2959 2.62871 10.0001 2.62871C11.7043 2.62871 13.0859 4.01025 13.0859 5.71448Z" fill="currentColor"></path></svg>
                    </template>
                </el-input>
            </div>

            <div class="row">
                <el-input v-model="$store.state.email" placeholder="请输入邮箱" clearable>
                    <template #prefix>
                        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path fill-rule="evenodd" clip-rule="evenodd" d="M3.75 5.25L3 6V18L3.75 18.75H20.25L21 18V6L20.25 5.25H3.75ZM4.5 7.6955V17.25H19.5V7.69525L11.9999 14.5136L4.5 7.6955ZM18.3099 6.75H5.68986L11.9999 12.4864L18.3099 6.75Z" fill="#080341"></path> </g></svg>
                    </template>
                </el-input>
            </div>

            <div class="row">
                <mail-verify-code-sender :email="$store.state.email" @newValue="e=>this.$store.state.verifyCode=e" />
            </div>

            <div class="row">
                <el-button type="primary" @click="register">注册</el-button>
            </div>

            <div class="row">
                <el-button @click="$router.push('/authentication/login')">返回登录</el-button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.register{
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

        .row{
            margin-bottom: 20px;

            svg{
                width: 18px;
            }
            button{
                width: 100%;
            }
        }
    }

}
</style>