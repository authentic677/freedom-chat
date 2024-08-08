<script>
export default {
    name: "PasswordAuth",
    data(){
        return {
            // account:'',
            // password:'',
            // verifyCode:'',

            captchaUrl:null
        }
    },
    methods:{
        async getCaptcha(){ //获取图片验证码

            let loading

            await new Promise((resolve,reject)=>{
                this.captchaUrl='/api/captcha?rand='+Math.random()+'&account='+this.$store.state.account

                loading = this.$loading({
                    target:'.col2',
                    lock: true,
                    // text: '正在加载验证码',
                    background: 'rgba(0, 0, 0, 0.7)',
                    customClass:'loading'
                });

                this.$refs.img.onload=resolve
            })

            loading.close();
        }
    },
    mounted() {
        this.getCaptcha()
    }
}
</script>

<template>
    <div class="password-auth">
        <div class="row">
            <el-input
                clearable
                v-model="$store.state.account"
                placeholder="UID / 邮箱" >

                <template #prefix>
                    <svg fill="none" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg" class="svg-icon input-icon" data-v-f97f88d6=""><rect x="6.39395" y="1.47451" width="7.21356" height="8.40786" rx="3.60678" stroke="currentColor" stroke-width="1.2"></rect> <path d="M2.1874 16.7663C2.1874 12.9673 5.26711 9.8876 9.06612 9.8876H10.9358C14.7348 9.8876 17.8145 12.9673 17.8145 16.7663C17.8145 17.7257 17.0368 18.5034 16.0774 18.5034H3.9245C2.96513 18.5034 2.1874 17.7257 2.1874 16.7663Z" stroke="currentColor" stroke-width="1.2"></path></svg>
                </template>
            </el-input>
        </div>
        <div class="row">
            <el-input
                clearable
                v-model="$store.state.password"
                placeholder="请输入账号密码"
                show-password >

                <template #prefix>
                    <svg viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg" class="svg-icon input-icon" data-v-f97f88d6=""><rect x="3.1" y="8.1" width="13.8" height="9.63333" rx="1.4" stroke="currentColor" stroke-width="1.2"></rect> <path fill-rule="evenodd" clip-rule="evenodd" d="M13.0859 5.71448V7.85736C13.0859 7.85749 13.0859 7.85762 13.0859 7.85775H14.2859C14.2859 7.85762 14.2859 7.85749 14.2859 7.85736V5.71448C14.2859 3.34751 12.3671 1.42871 10.0001 1.42871C7.63316 1.42871 5.71436 3.34751 5.71436 5.71447V7.85736C5.71436 7.85749 5.71436 7.85762 5.71436 7.85775H6.91436C6.91436 7.85762 6.91436 7.85749 6.91436 7.85736V5.71447C6.91436 4.01025 8.2959 2.62871 10.0001 2.62871C11.7043 2.62871 13.0859 4.01025 13.0859 5.71448Z" fill="currentColor"></path></svg>
                </template>
            </el-input>
        </div>
        <div class="row row2">
            <div class="col1">

                <el-input
                    clearable
                    v-model="$store.state.verifyCode"
                    placeholder="请输入验证码" >

                    <template #prefix >
                        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path d="M9.5 12.4L10.9286 14L14.5 10" stroke="#1C274C" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path> <path d="M3 10.4167C3 7.21907 3 5.62028 3.37752 5.08241C3.75503 4.54454 5.25832 4.02996 8.26491 3.00079L8.83772 2.80472C10.405 2.26824 11.1886 2 12 2C12.8114 2 13.595 2.26824 15.1623 2.80472L15.7351 3.00079C18.7417 4.02996 20.245 4.54454 20.6225 5.08241C21 5.62028 21 7.21907 21 10.4167C21 10.8996 21 11.4234 21 11.9914C21 14.4963 20.1632 16.4284 19 17.9041M3.19284 14C4.05026 18.2984 7.57641 20.5129 9.89856 21.5273C10.62 21.8424 10.9807 22 12 22C13.0193 22 13.38 21.8424 14.1014 21.5273C14.6796 21.2747 15.3324 20.9478 16 20.5328" stroke="#1C274C" stroke-width="1.5" stroke-linecap="round"></path> </g></svg>
                    </template>
                </el-input>
            </div>
            <div class="col2" title="点击刷新验证码">
                <img :src="captchaUrl" ref="img" alt="" @click="getCaptcha">
            </div>
        </div>
    </div>
</template>

<style scoped lang="less">
.password-auth{
    .row{
        margin-bottom: 20px;

        svg{
            width: 18px;
        }
    }
    .row2{
        display: flex;
        align-items: center;

        .col1{
            flex: 1;
        }
        .col2{
            width: 150px;
            cursor: pointer;

            text-align: center;

            img{
                width: 140px;
                height: 40px;
            }
        }
    }
}
</style>