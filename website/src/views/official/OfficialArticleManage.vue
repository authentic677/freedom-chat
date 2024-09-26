<script>
export default {
    name: "OfficialArticleManage"
}
</script>

<template>
<div class="official-article-manage">
    <h3>文章管理</h3>
    <div class="control">
        <el-button type="primary" @click="$router.push('./article/editor')">创建</el-button>
    </div>
    <div class="content">
        <el-empty />
    </div>
</div>
</template>

<style scoped lang="less">
.official-article-manage {
    padding: 1rem;

    h3{
        margin: 0;
        padding: 0;
    }
    h3{
        margin-bottom: 1rem;
    }
}
</style>