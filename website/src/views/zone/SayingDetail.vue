<script setup>

import {useRouter} from "vue-router";
import SayingNode from "../../components/SayingNode.vue";

const router=useRouter()

const back=()=>{
    router.back()
}
</script>

<template>
    <div class="saying-detail" >
        <div class="back" @click="back">
            <svg t="1725349130776" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="9323" width="200" height="200"><path d="M700.371228 394.525472 174.028569 394.525472l255.952416-227.506551c12.389168-11.011798 13.505595-29.980825 2.492774-42.369993-11.011798-12.386098-29.977755-13.506619-42.367947-2.492774L76.425623 400.975371c-6.960529 5.496178-11.434423 14.003945-11.434423 23.561625 0 0.013303 0.001023 0.026606 0.001023 0.039909 0 0.01228-0.001023 0.025583-0.001023 0.037862 0 0.473791 0.01535 0.946558 0.037862 1.418302 0.001023 0.016373 0.001023 0.032746 0.001023 0.049119 0.39295 8.030907 3.992941 15.595186 10.034541 20.962427l315.040163 280.028764c5.717212 5.083785 12.83533 7.580652 19.925818 7.580652 8.274454 0 16.514115-3.403516 22.442128-10.07445 11.011798-12.387122 9.896394-31.357172-2.492774-42.367947l-256.128425-227.665163 526.518668 0c109.219517 0 198.075241 88.855724 198.075241 198.075241s-88.855724 198.075241-198.075241 198.075241L354.324888 850.696955c-16.57449 0-30.011524 13.437034-30.011524 30.011524s13.437034 30.011524 30.011524 30.011524l346.046341 0c142.31631 0 258.098289-115.783003 258.098289-258.098289S842.686515 394.525472 700.371228 394.525472z" fill="#272636" p-id="9324"></path></svg>
        </div>
        <div class="main">
            <div class="subject">
                <SayingNode />
            </div>
            <div class="comment">
                <div class="filter">
                    条件筛选
                </div>
                <div class="list">
                    <SayingNode v-for="item in 10" />
                </div>
            </div>
        </div>

    </div>
</template>

<style scoped lang="less">
.saying-detail{
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;

    overflow: auto;

    background-color: #E9E9E9;

    .back{
        padding: 1rem;
        svg{
            width: 20px;
            height: 20px;
        }
    }
    .main{
        max-width: 800px;
        margin: 0 auto;
        border-left: 1px solid #7F7F7F;
        border-right: 1px solid #7F7F7F;

        .subject{


        }
        .comment{
            border-top: 1px solid gray;
            padding-top: 1rem;
        }
    }

}
</style>