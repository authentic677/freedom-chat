<script>
import axios from "axios";
import config from "../../config/config.js";

export default {
    name: "OfficialArticleManage",
    computed: {
        config() {
            return config
        }
    },
    data(){
        return {
            articles:[]
        }
    },
    methods:{
        async getData(){
            let res=await axios.get('/api/content-platform/articles')
            let json=res.data

            console.log('获取文章',json)

            this.articles=json.data
        },
        editHandle(){

        },
        async handleDelete(item){
            let res = await fetch(`/api/content-platform/article/${item.id}`, {
                method: 'DELETE',
            })
            let json = await res.json()

            console.log('删除文章',json)

            if (json.code===1){
                this.getData()
            }
        }
    },
    created() {
        this.getData()
    }
}
</script>

<template>
<div class="official-article-manage">
    <h3>文章管理</h3>
    <div class="control">
        <el-button type="primary" @click="$router.push('./article/editor')">创建</el-button>
    </div>
    <div class="content">
        <el-empty v-if="articles.length===0" />

        <el-table v-else :data="articles" style="width: 100%">
            <el-table-column label="封面" >
                <template #default="scope">
                    <img style="width: 50px;height: 50px" :src="config.minioUrl+scope.row.cover" alt="">
                </template>
            </el-table-column>
            <el-table-column label="名称" >
                <template #default="scope">
                    {{scope.row.title}}
                </template>
            </el-table-column>
            <el-table-column label="创建时间" >
                <template #default="scope">
                    {{ new Date(scope.row.createdAt).toLocaleString() }}
                </template>
            </el-table-column>
            <el-table-column label="更新时间" >
                <template #default="scope">
                    {{ new Date(scope.row.updateAt).toLocaleString() }}
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template #default="scope">
                    <el-button size="small" @click="editHandle(scope.row)">
                        编辑
                    </el-button>
                    <el-button
                        size="small"
                        type="danger"
                        @click="handleDelete(scope.row)"
                    >
                        删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
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