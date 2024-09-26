<script>
import Navigator from "../../components/Navigator.vue";
import SearchBox from "../../components/SearchBox.vue";
import MessageNoticeListItem from "../../components/MessageNoticeListItem.vue";
import OfficialDetail from "./OfficialDetail.vue";

export default {
    name: "Official",
    components: {OfficialDetail, MessageNoticeListItem, SearchBox, Navigator},

}
</script>

<template>
<div class="official">
    <div class="left">
        <navigator/>
    </div>
    <div class="right">
        <div class="bar">
            <div class="title">
                <div>已关注的公众号</div>
                <el-link @click="$router.push('/creativeSpace/official')">创作空间</el-link>
            </div>
            <div class="list">
                <div
                    class="item"
                    v-for="item in 20"
                    :key="item"

                    @click="$router.push(`/official/detail/${item}`)"
                >
                    <div class="avatar">
                        <img src="/641.webp" alt="">
                    </div>
                    <div class="name">
                        观星台
                    </div>
                </div>
            </div>
        </div>
        <div class="detail">
            <router-view></router-view>
        </div>

    </div>
</div>
</template>

<style scoped lang="less">
.official{
    height: 100vh;
    display: flex;

    .left{

    }
    .right {
        flex-grow: 1;
        display: flex;

        .bar{
            width: 400px;
            background-color: #E8E7E6;

            display: flex;
            flex-direction: column;

            .title{
                text-align: center;
                padding: 1rem 0;
            }
            .list{
                flex-grow: 1;
                background-color: white;

                overflow: auto;

                .item{
                    display: flex;
                    align-items: center;
                    padding: 0.5rem 1rem;

                    position: relative;

                    .avatar{
                        width: 60px;
                        height: 60px;
                        border-radius: 50%;
                        overflow: hidden;
                        margin-right: 1rem;

                        img{
                            width: 100%;
                            height: 100%;
                        }
                    }
                    .name{
                        font-size: 18px;
                    }
                }
                .item:hover{
                    background-color: #EBEBEB;
                }
                .item::after{
                    content: "";
                    display: block;
                    position: absolute;
                    //left: 50%;
                    bottom: 0;
                    //width: calc(100% - 2rem - 60px - 1rem);
                    //transform: translateX(calc(-50% + 60px + 1rem));
                    border-bottom: 1px solid #E7E7E7;
                    left: calc(60px + 2rem);
                    width: calc(100% - 60px - 3rem);
                }
            }
        }
        .detail{
            flex-grow: 1;

        }
    }
}
</style>