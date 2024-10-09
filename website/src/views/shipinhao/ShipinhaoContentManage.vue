<script>
import config from "../../config/config.js";

export default {
    name: "ShipinhaoContentManage",
    data(){
        return {
            videos:[]
        }
    },
    methods:{
        async getData(){
            let res=await fetch('/api/content-platform/videos')
            let json=await res.json()

            console.log(json)

            //处理url
            json.data.forEach((item)=>{
                item.cover=config.minioUrl+item.cover
            })
            this.videos=json.data
        }
    },
    created() {
        this.getData()
    }
}
</script>

<template>
<div class="shipinhaoContentManage">
    <h3>内容管理</h3>
    <div class="control">
        <el-button type="primary" @click="$router.push('/shipinhao/producer/publishVideo')">发布</el-button>
    </div>
    <el-table :data="videos" >
        <el-table-column label="封面" prop="cover">
            <template #default="scope">
                <el-avatar :size="80" shape="square" :src="scope.row.cover" />
            </template>
        </el-table-column>
        <el-table-column label="标题" prop="title" />
        <el-table-column label="概要" prop="summary" />
        <el-table-column label="创建时间" prop="time">
            <template #default="scope">
                <div>{{new Date(scope.row.time).toLocaleString()}}</div>
            </template>
        </el-table-column>
        <el-table-column label="可见性" prop="visibility">
            <template #default="scope">
                <div v-if="scope.row.visibility==='public'">公开</div>
                <div v-if="scope.row.visibility==='friend'">仅好友</div>
                <div v-if="scope.row.visibility==='private'">仅自己</div>
            </template>
        </el-table-column>
        <el-table-column label="操作">
            <template #default="scope">
                <el-button size="small" @click="handleEdit(scope.$index, scope.row)">
                    编辑
                </el-button>
                <el-button
                    size="small"
                    type="danger"
                    @click="handleDelete(scope.$index, scope.row)"
                >
                    删除
                </el-button>
            </template>
        </el-table-column>
    </el-table>
</div>
</template>

<style scoped lang="less">
.shipinhaoContentManage{
    height: 100%;
    padding: 0 1rem 1rem;
    box-sizing: border-box;

    overflow: auto;

    .control{
        margin-bottom: 1rem;
    }
}
</style>