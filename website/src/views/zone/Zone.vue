<script setup>



</script>

<template>
<div class="zone">
    <div class="main">
        <div class="middle">
            <div class="title">XXX的空间</div>

            <div class="header">
                <img src="https://qlogo2.store.qq.com/qzone/945491917/945491917/100?0" alt="">
                <div class="name">XXX</div>
            </div>

            <div class="nav">
                <div class="item">说说</div>
                <div class="item">日志</div>
                <div class="item">相册</div>
                <div class="item">留言板</div>
            </div>

            <router-view></router-view>

        </div>
    </div>
</div>
</template>

<style scoped lang="less">
.zone{

    background-image: url("/zone-bg.png");
    background-repeat: no-repeat;
    background-position-x: center;

    .main{
        min-height: 100vh;

        background: linear-gradient(to bottom,rgba(0,0,0,0) 50px,#E9E9E9 300px);
        //background: red;

        display: flex;
        justify-content: center;

        .middle{
            width: 100%;
            max-width: 1000px;
            height: 100%;
            padding: 0 1rem;
            box-sizing: border-box;

            .title{
                font-size: 30px;
                color: white;

                padding: 30px 0 60px;
            }
            .header{
                margin-top: 1rem;

                display: flex;
                align-items: center;

                img{
                    margin-right: 1rem;
                }
                .name{
                    font-size: 25px;
                    color: white;
                }
            }
            .nav{
                display: flex;
                margin-top: 1rem;

                padding-bottom: 1rem;
                border-bottom: 1px solid gray;

                .item{
                    margin-right: 1rem;
                    cursor: pointer;
                }
            }

        }
    }
}
</style>