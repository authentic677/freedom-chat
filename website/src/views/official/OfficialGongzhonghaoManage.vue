<script>
import config from "../../config/config.js";

export default {
    name: "OfficialGongzhonghaoManage",
    computed: {
        config() {
            return config
        }
    },
    data(){
        return {
            createGongzhonghaoDialog:false,
            editGongzhonghaoDialog:false,

            //当前正在编辑的公众号
            currentEditGongzhonghao:null,

            gongzhonghaos:[],

            name:'',
            description:'',
            avatar:null,
            avatarDataUrl:null
        }
    },
    methods:{
        async getData(){
            let res=await fetch('/api/content-platform/gongzhonghaos')
            let json=await res.json()

            this.gongzhonghaos=json.data
        },
        avatarChange(){
            this.avatar=this.$refs.upload.files[0]

            this.avatarDataUrl=URL.createObjectURL(this.avatar)
        },
        async createGongzhonghao(){

            let fd=new FormData()
            fd.append('name', this.name)
            fd.append('description', this.description)
            fd.append('avatar', this.avatar,Math.random()+'')

            let res=await fetch('/api/content-platform/gongzhonghao',{
                method:'POST',
                headers:{
                    token:localStorage.getItem("token"),
                },
                body:fd
            })
            let json=await res.json()

            console.log(json)

            if(json.code===1){
                this.getData()
                this.createGongzhonghaoDialog=false
            }
        },
        //清理输入残留
        createGongzhonghaoDialogClose(){
            this.name=''
            this.description=''
            this.avatar=null
            this.avatarDataUrl=null
        },
        editGongzhonghaoDialogClose(){
            this.name=''
            this.description=''
            this.avatar=null
            this.avatarDataUrl=null
        },

        //公众号的编辑和删除操作
        async handleDelete(row){
            let res=await fetch(`/api/content-platform/gongzhonghao/${row.id}`,{
                method:'DELETE',
                headers:{
                    token:localStorage.getItem("token"),
                }
            })
            let json=await res.json()

            console.log('删除',json)
        },
        editHandle(row){
            //设置公众号的基本信息
            this.name=row.name
            this.description=row.description
            this.avatarDataUrl=config.minioUrl+row.avatar

            this.currentEditGongzhonghao=row
            this.editGongzhonghaoDialog=true
        },
        async editGongzhonghao(){

            let fd=new FormData()
            fd.append('id',this.currentEditGongzhonghao.id)
            fd.append('name', this.name)
            fd.append('description', this.description)
            if(this.avatar!=null){
                fd.append('avatar', this.avatar,Math.random()+'')
            }

            let res=await fetch('/api/content-platform/gongzhonghao',{
                method:'PUT',
                headers:{
                    token:localStorage.getItem("token"),
                },
                body:fd
            })
            let json=await res.json()

            console.log(json)

            if(json.code===1){
                this.getData()
                this.editGongzhonghaoDialog=false
            }

            console.log('修改公众号',json)
        }
    },
    created() {
        this.getData()
    }
}
</script>

<template>
<div class="official-gongzhonghao-manage">
    <h3>公众号管理</h3>
    <div class="control">
        <el-button type="primary" @click="createGongzhonghaoDialog=true">创建</el-button>
    </div>
    <div class="content">
        <el-empty v-if="gongzhonghaos.length===0" />

        <el-table v-else :data="gongzhonghaos" style="width: 100%">
            <el-table-column label="头像" >
                <template #default="scope">
                    <img style="width: 50px;height: 50px" :src="config.minioUrl+scope.row.avatar" alt="">
                </template>
            </el-table-column>
            <el-table-column label="名称" >
                <template #default="scope">
                    {{scope.row.name}}
                </template>
            </el-table-column>
            <el-table-column label="描述" >
                <template #default="scope">
                    {{scope.row.description}}
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

    <el-dialog
        v-model="createGongzhonghaoDialog"
        title="创建公众号"
        width="500"
        @close="createGongzhonghaoDialogClose"
        align-center
    >
        <div class="row">
            <el-input placeholder="公众号名称" v-model="name" />
        </div>
        <div class="row">
            <el-input placeholder="公众号描述" type="textarea" v-model="description" />
        </div>
        <div class="row">
            <div class="upload-frame" @click="$refs.upload.click()">
                <div class="mode1" v-if="!avatarDataUrl">
                    <img src="/upload.png" alt="">
                    公众号头像
                </div>
                <div class="mode2" v-if="avatarDataUrl">
                    <img :src="avatarDataUrl" alt="">
                </div>
                <input type="file" ref="upload" accept="image/*" style="display: none" @change="avatarChange" >
            </div>
        </div>

        <template #footer>
            <div class="dialog-footer">
                <el-button @click="createGongzhonghaoDialog = false">取消</el-button>
                <el-button type="primary" @click="createGongzhonghao">
                    确定
                </el-button>
            </div>
        </template>
    </el-dialog>

    <el-dialog
        v-model="editGongzhonghaoDialog"
        title="创建公众号"
        width="500"
        @close="editGongzhonghaoDialogClose"
        align-center
    >
        <div class="row">
            <el-input placeholder="公众号名称" v-model="name" />
        </div>
        <div class="row">
            <el-input placeholder="公众号描述" type="textarea" v-model="description" />
        </div>
        <div class="row">
            <div class="upload-frame" @click="$refs.upload.click()">
                <div class="mode1" v-if="!avatarDataUrl">
                    <img src="/upload.png" alt="">
                    公众号头像
                </div>
                <div class="mode2" v-if="avatarDataUrl">
                    <img :src="avatarDataUrl" alt="">
                </div>
                <input type="file" ref="upload" accept="image/*" style="display: none" @change="avatarChange" >
            </div>
        </div>

        <template #footer>
            <div class="dialog-footer">
                <el-button @click="editGongzhonghaoDialog = false">取消</el-button>
                <el-button type="primary" @click="editGongzhonghao">
                    确定
                </el-button>
            </div>
        </template>
    </el-dialog>

</div>
</template>

<style scoped lang="less">
.official-gongzhonghao-manage{
    padding: 1rem;

    .row{
        margin-bottom: 1rem;

        .upload-frame{
            width: 120px;
            height: 120px;
            border: 1px dashed #d9d9d9;
            background: #f7f7f7;
            border-radius: 6px;
            cursor: pointer;

            display: flex;
            flex-direction: column;
            justify-content: center;

            .mode1{
                display: flex;
                flex-direction: column;
                align-items: center;

                img{
                    width: 24px;
                    height: 24px;
                    margin-bottom: 1rem;
                };
            }
            .mode2{
                width: 100%;
                height: 100%;

                img{
                    width: 100%;
                    height: 100%;
                    object-fit: cover;
                }
            }
        }
    }
    h3{
        margin: 0;
        padding: 0;
    }
    h3{
        margin-bottom: 1rem;
    }
    .control{
        margin-bottom: 1rem;
    }

}
</style>