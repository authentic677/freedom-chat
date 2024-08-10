<template>
    <div class="agreeOrRefuseBtn">
        <span @click="agree">同意</span>
        <div class="wrap" ref="other">
            <div class="wrap2" >
                <svg t="1710573236515" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4226" width="200" height="200"><path d="M966.4 323.2c-9.6-9.6-25.6-9.6-35.2 0l-416 416-425.6-416c-9.6-9.6-25.6-9.6-35.2 0-9.6 9.6-9.6 25.6 0 35.2l441.6 432c9.6 9.6 25.6 9.6 35.2 0l435.2-432C976 345.6 976 332.8 966.4 323.2z" p-id="4227"></path></svg>
                <div class="other" v-show="otherShow">
                    <div class="item" ref="refuse">拒绝</div>
                    <div class="item" ref="ignore">忽略</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name:'AgreeOrRefuseBtn',
    data(){
        return {
            otherShow:false,
        }
    },
    methods:{
        agree(){
            console.log("同意");
            this.$emit('agree')
        },
        btnClick(e){
            if(this.$refs.other.contains(e.target)){
                console.log(e.target);
                if(e.target===this.$refs.refuse){
                    console.log("拒绝");
                    this.otherShow=false
                    this.$emit('refuse')
                }else if(e.target===this.$refs.ignore){
                    console.log("忽视");
                    this.otherShow=false
                    this.$emit('ignore')
                }else{
                    this.otherShow=true
                }
                
            }else{
                this.otherShow=false
            }
        }
    },
    created(){
        window.addEventListener('click',this.btnClick)
    },
    beforeUnmount(){
        window.removeEventListener('click',this.btnClick)
    }
}
</script>

<style scoped lang="less">
.agreeOrRefuseBtn{
    display: flex;
    border: 1px solid #CDCDCD;
    border-radius: 5px;

    span{
        margin: 5px 0;
        padding: 0px 10px 0px 15px;
        border-right: 1px solid #CDCDCD;
    }
    .wrap{
        display: flex;
        align-items: center;

        .wrap2{
            position: relative;
            display: flex; /*关于这里为什么用弹性，如果不用的话，wrap2的高度会大于 svg的高度*/
            svg{
                padding: 0 10px;

                width: 16px;
                height: 16px;
                vertical-align: middle;
            }
            .other{
                white-space: nowrap;
                position: absolute;
                top:100%;
                left: 10px;
                padding: 5px;
                background-color: white;
                border: 1px solid #CDCDCD;
                border-radius: 5px;

                .item{
                    padding: 5px;
                }
                .item:hover{
                    background-color: #F5F5F5;
                }
            }
        }  
    }
        
}
.agreeOrRefuseBtn:hover{
    background-color: #F5F5F5;
    cursor: pointer;
}
</style>