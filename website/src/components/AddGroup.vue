<template>
    <div class="addGroup">
        <div class="box">
            <div class="title">申请加入</div>
            <div class="body">

                <div class="applicant_note">
                    {{group.applicantNote}}
                </div>
                <SearchBox :icon="true" placeholder="请输入你的留言" @in="input" @enter="send" />

                <div class="control">
                    <div class="send" :class="{opacity:!canSend}" @click="send">发送</div>
                    <div class="cancel" @click="close">取消</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import SearchBox from "./SearchBox.vue";
export default {
    name:'AddGroup',
    data(){
        return {
            note:'',
            canSend:false
        }
    },
    props:['group'],
    methods:{
        close(){
            this.$emit('close')
        },
        send(){
            if(this.note===''){
                return
            }
            this.$emit('note',this.note)
        },
        input(e){
            this.note=e
        }
    },
    watch:{
        note(newValue){
            if(newValue&&newValue!==''){
                this.canSend=true
            }else{
                this.canSend=false
            }
        }
    },
    components:{
        SearchBox
    }
}
</script>

<style scoped lang="less">
.addGroup{
    position: fixed;
    left: 0px;
    top: 0px;
    width: 100vw;
    height: 100vh;
    background: rgba(0,0,0,0.6);
    
    display: flex;
    justify-content: center;
    align-items: center;

    .box{
        width: 400px;
        border: 1px solid #C0C0C0;
        background: #F2F2F2;
        border-radius: 5px;
        box-shadow: 0px 0px 10px rgba(0,0,0,0.3);

        .title{
            
            padding: 8px;
            border-bottom: 1px solid #C0C0C0;
            text-align: center;
        }
        .body{
            padding: 20px;

            .applicant_note{
                color: #999;
                padding: 10px;
            }
            .control{
                display: flex;
                justify-content: flex-end;
                margin-top: 20px;

                div{
                    padding: 8px 30px;
                    border-radius: 5px;
                }
                .send{
                    color: white;
                    background-color: #0099FF;
                }
                .send.opacity{
                    opacity: 0.3;
                }
                .send:hover{
                    background-color: #008DEB;
                }
                .cancel{
                    margin-left: 10px;
                    border: 1px solid #C2C2C2;
                }
                .cancel:hover{
                    background-color: #E9E9E9;
                }
            }
        }
    }
}
</style>