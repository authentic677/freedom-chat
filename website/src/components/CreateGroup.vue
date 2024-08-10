<script>
export default {
    name: 'CreateGroup',
    data(){
        return {
            groupName:'',
            groupDescription:'',
            groupApplicantNote:'',
            groupAvatar:'',
        }
    },
    methods:{
        async submit(){
            console.log(this.groupName)
            console.log(this.groupDescription)
            console.log(this.groupApplicantNote)

            let res=await fetch('/api/group',{
                method:'POST',
                headers:{
                    token:localStorage.getItem('token'),
                    'Content-Type':'application/json;charset=utf-8'
                },
                body:JSON.stringify({
                    name:this.groupName,
                    description:this.groupDescription,
                    applicantNote:this.groupApplicantNote,
                })
            })
            let json=await res.json()

            console.log(json)

            if(json.code===1){
                this.$message({
                    type: 'success',
                    message:"创建成功"
                })
            }else{
                this.$message({
                    type: 'error',
                    message:json.msg
                })
            }

            this.$emit('close')
        }
    }
}
</script>

<template>
<div class="create-group">
    <div class="box">

        <div class="title">创建群聊</div>

        <div class="row">
            <el-input
                clearable
                v-model="groupName"
                placeholder="群聊名称"
            />
        </div>

        <div class="row">
            <el-input
                clearable
                v-model="groupDescription"
                placeholder="群介绍"
            />
        </div>

        <div class="row">
            <el-input
                clearable
                v-model="groupApplicantNote"
                placeholder="申请者备注"
            />
        </div>

        <div class="row">
            <el-button @click="$emit('close')">取消</el-button>
            <el-button type="primary" @click="submit">确认</el-button>
        </div>

    </div>
</div>
</template>

<style scoped lang="less">
.create-group{
    position: fixed;
    left: 0px;
    top: 0px;
    width: 100vw;
    height: 100vh;
    background: rgba(0,0,0,0.6);

    display: flex;
    justify-content: center;
    align-items: center;

    .box {
        width: 500px;
        background: white;
        border-radius: 5px;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.3);

        display: flex;
        flex-direction: column;

        .title{
            padding: 1rem;
            text-align: center;
        }
        .row{
            padding: 0 1rem 1rem;

            text-align: center;
        }
    }
}
</style>