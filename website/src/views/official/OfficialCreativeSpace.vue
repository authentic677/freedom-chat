<script>
export default {
    name: "OfficialCreativeSpace",
    data(){
        return {

        }
    },
    methods:{
        select(e){
            switch (e){
                case 'article':
                    this.$router.push('/creativeSpace/official/manage/article')
                    break
                case 'official':
                    this.$router.push('/creativeSpace/official/manage/gongzhonghao')
                    break
            }
        }
    }
}
</script>

<template>
    <div class="official-creative-space">
        <div class="header">
            <div class="left">创作空间</div>
        </div>
        <div class="bottom">
            <div class="navBar">
                <div class="name">菜单</div>
                <el-menu
                    default-active="msg"
                    @select="select"
                >
                    <el-menu-item index="msg">
<!--                        <el-icon><icon-menu /></el-icon>-->
                        <span>消息</span>
                    </el-menu-item>
                    <el-sub-menu index="manage">
                        <template #title>
<!--                            <el-icon><location /></el-icon>-->
                            <span>管理</span>
                        </template>
                        <el-menu-item index="article">文章管理</el-menu-item>
                        <el-menu-item index="comment">评论管理</el-menu-item>
                        <el-menu-item index="official">公众号管理</el-menu-item>
                    </el-sub-menu>
                    <el-sub-menu index="data">
                        <template #title>
<!--                            <el-icon><location /></el-icon>-->
                            <span>数据</span>
                        </template>
                        <el-menu-item index="product">作品数据</el-menu-item>
                        <el-menu-item index="fan">粉丝数据</el-menu-item>
                    </el-sub-menu>
                </el-menu>
            </div>
            <div class="content">

                <router-view ></router-view>

            </div>
        </div>
    </div>
</template>

<style scoped lang="less">
.official-creative-space{
    height: 100vh;
    background-color: #F4F6F9;

    display: flex;
    flex-direction: column;

    .header{
        background-color: white;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);

        .left{
            padding: 1rem;
            font-size: 18px;
            color: #1772F6;
        }
    }
    .bottom{
        flex-grow: 1;

        display: flex;

        .navBar{
            width: 320px;
            margin: 1rem;
            margin-right: 0;
            background-color: white;
            border-radius: 15px;

            overflow: hidden;

            flex-shrink: 0;

            .name{
                padding: 1rem;
                text-align: center;
            }
        }
        .content{
            flex-grow: 1;
            margin: 1rem;
            background-color: white;
            border-radius: 15px;

            overflow: hidden;
        }
    }
}
</style>