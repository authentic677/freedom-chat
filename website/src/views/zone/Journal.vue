<script>

import axios from "axios";

export default {
    name: "Journal",
    data(){
        return {
            activeName:'my',
            category:[ //这个是内置分类，不可更改
                {
                    id:-1,
                    name: '全部日志',
                    journalCount:0,
                },
                {
                    id:0,
                    name: '默认分类',
                    journalCount:0,
                }
            ],
            userCreateCategory:[], //这个是用户创建的分类
            currentSort:'publishTime',
            sortBy:[
                {
                    value: 'publishTime',
                    label: '发表时间',
                },
                {
                    value: 'commentTime',
                    label: '评论时间',
                },
                {
                    value: 'recentlyUpdate',
                    label: '最新修改',
                },
                {
                    value: 'commentCount',
                    label: '评论数目',
                }
            ],

            categoryManageDialogShow:false,
            categoryCreateDialogShow:false,
            categoryUpdateDialogShow:false,
            newCategory:'', //创建和修改分类时用到
            currentIndex:-1, //修改分类时用到

            //日志数据
            journals:[],
            journalQueryParams:{ //日志数据的查询参数对象，监视了此对象以响应此对象的更改
                one:{},
                two:{}
            },

            //日志操作选项
            journalOperationOptions:[
                {
                    label:'删除日志',
                    value:'delete'
                },
                {
                    label:'设置权限',
                    value:'updateVisibility'
                },
                {
                    label:'修改分类',
                    value:'updateCategory'
                },
                {
                    label:'置顶',
                    value:'topping'
                },
                {
                    label:'转为私密',
                    value:'toPrivate'
                },
            ],

            //这下面是用来实现修改日志所属分类相关的变量
            updateJournalCategoryDialogShow:false,
            categoryValue:null,
            updateJournalCategoryCallback:null

        }
    },
    methods:{
        //获取"日志"和"分类"数据的
        async getCategoryData(){
            {
                let res=await fetch('/api/zone/journalCategory/count',{
                    headers:{
                        token:localStorage.getItem('token')
                    }
                })
                let json=await res.json()

                console.log(json)

                this.category[0].journalCount=json.data.total
                this.category[1].journalCount=json.data.default

            }
            {
                let res=await fetch('/api/zone/journalCategorys',{
                    headers:{
                        token:localStorage.getItem('token')
                    }
                })
                let json=await res.json()

                console.log(json)

                this.userCreateCategory=json.data
            }
        },
        async getJournalData(args){

            let defaultValue={
                status:'published',
                isPrivate:0,
                isDeleted:0,
                categoryId:-1
            }
            if (!args){
                args=defaultValue
            }else {
                //后者对象覆盖同名的前对象属性的值
                // args={...defaultValue,...args}
            }

            let res=await axios.get('/api/zone/journals',{
                params:args,
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=res.data

            console.log('获取日志数据',json.data)

            this.journals=json.data
        },
        handleChange(){
            switch (this.activeName){
                case 'my':
                    this.journalQueryParams.one={
                        status: 'published',
                        isPrivate: 0,
                        isDeleted: 0
                    }
                    break;
                case 'private':
                    this.journalQueryParams.one={
                        isPrivate: 1,
                        isDeleted:0
                    }
                    break;
                case 'drafts':
                    this.journalQueryParams.one={
                        status: 'draft',
                        isDeleted:0
                    }
                    break;
                case 'recycle':
                    this.journalQueryParams.one={
                        isDeleted: 1
                    }
                    break
            }
        },
        //创建新的日志分类
        async createCategoryHandle(){

            let res=await fetch('/api/zone/journalCategory',{
                method:'POST',
                headers:{
                    token:localStorage.getItem("token"),
                    'content-type':'application/x-www-form-urlencoded'
                },
                body:`name=${encodeURIComponent(this.newCategory)}`
            })
            let json=await res.json()

            if(json.code===1){

                console.log(json)
                this.newCategory=''
                this.categoryCreateDialogShow=false

                this.getCategoryData()
            }

        },
        async deleteCategoryHandle(index){
            let res=await fetch(`/api/zone/journalCategory/${this.userCreateCategory[index].id}`,{
                method:'DELETE',
                headers:{
                    token:localStorage.getItem("token")
                }
            })
            let json=await res.json()

            if(json.code===1){

                console.log(json)
                this.getCategoryData()
            }
        },
        async updateCategoryHandle(){

            //发请求保存
            let res=await fetch('/api/zone/journalCategory',{
                method:'PUT',
                headers:{
                    token:localStorage.getItem("token"),
                    'content-type':"application/json;charset=utf-8"
                },
                body:JSON.stringify({
                    id:this.userCreateCategory[this.currentIndex].id,
                    name:this.newCategory
                })
            })
            let json=await res.json()

            if(json.code===1){

                console.log(json)
                this.newCategory=''
                this.categoryUpdateDialogShow=false

                this.getCategoryData()
            }
        },

        newJournalOperationOptionsData(id){ //每条数据的选项对象都是不一样的哦，id是它们各自的id
            return this.journalOperationOptions.map(e=>{
                return {
                    label:e.label,
                    value:e.value+':'+id
                }
            })
        },
        journalListRowClickHandle(item){
            this.$router.push(`/zone/${this.$route.params.uid}/journalViewer/${item.id}`)
        },
        //对日志的操作句柄
        async journalsOperationHandle(operation){
            //删除日志
            if (operation.startsWith('delete')){
                let res=await fetch(`/api/zone/journal/${operation.split(':')[1]}`,{
                    method:'DELETE',
                    headers:{
                        token:localStorage.getItem("token")
                    }
                })
                let json=await res.json()

                console.log('删除日志',json)

                if(json.code===1){
                    this.getJournalData()
                }
            }
            //修改所属分类
            if (operation.startsWith('updateCategory')){

                this.updateJournalCategoryDialogShow=true
                this.categoryValue=this.journals.find(e=>e.id==operation.split(':')[1]).categoryId //切记这里不能用三等号哦
                console.log(this.categoryValue)

                this.updateJournalCategoryCallback=async ()=>{
                    //this.categoryValue是分类的id
                    let res=await fetch(`/api/zone/journal`,{
                        method:'PUT',
                        headers:{
                            token:localStorage.getItem("token"),
                            'content-type':'application/json;charset=utf-8'
                        },
                        body:JSON.stringify({
                            id:parseInt(operation.split(':')[1]),
                            categoryId:this.categoryValue
                        })
                    })
                    let json=await res.json()

                    console.log('修改日志',json)

                    if(json.code===1){
                        this.getJournalData()
                        this.getCategoryData()
                    }

                    //关闭对话框
                    this.updateJournalCategoryDialogShow=false
                }

            }
        },

        //分类表的行点击回调
        categoryTableRowClickHandle(item){
            this.journalQueryParams.two={
                categoryId:item.id
            }
        }
    },
    computed:{
        allCategory(){
            return this.category.concat(this.userCreateCategory)
        }
    },
    watch:{
        journalQueryParams:{
            handler(newValue){
                console.log(newValue)
                this.getJournalData({
                    ...newValue.one,...newValue.two
                })
            },
            deep:true,
        }
    },
    async created() {
        await this.getJournalData()
        await this.getCategoryData()
    },
    mounted() {

    }
}
</script>

<template>
<div class="journal">

    <div class="box">
        <div class="left">
            <el-tabs v-model="activeName" @tab-change="handleChange">
                <el-tab-pane label="我的日志" name="my">

                </el-tab-pane>
                <el-tab-pane label="私密日志" name="private">

                </el-tab-pane>
                <el-tab-pane label="草稿箱" name="drafts">

                </el-tab-pane>
                <el-tab-pane label="回收站" name="recycle">

                </el-tab-pane>
            </el-tabs>
            <div class="exposed">
                <div class="control">
                    <div class="add">
                        <el-button type="primary" @click="$router.push(`/zone/${$route.params.uid}/journalEditor`)">写日志</el-button>
                    </div>
                    <div class="sort">
                        <el-select v-model="currentSort" placeholder="选择排序方式">
                            <el-option
                                v-for="item in sortBy"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                            />
                        </el-select>
                    </div>
                </div>

                <div class="list">
                    <el-empty v-if="journals.length===0" description="无任何日志，快去写日志吧" />

                    <el-table
                        :data="journals"
                        :show-header="false"
                        @row-click="journalListRowClickHandle"
                    >
                        <el-table-column prop="title" label="标题" />
                        <el-table-column prop="createdAt" label="时间" />
                        <el-table-column fixed="right" label="操作" width="90" >
                            <template #default="scope">
                                <el-button
                                    link
                                    type="primary"
                                    size="small"
                                    @click.stop
                                >
                                    编辑
                                </el-button>
                                <!--                                        选择框-->
                                <el-select
                                    placeholder="Select"
                                    size="small"
                                    style="width: 30px"
                                    @change="journalsOperationHandle"
                                >
                                    <el-option
                                        v-for="item in newJournalOperationOptionsData(scope.row.id)"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    />
                                </el-select>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </div>
        <div class="right">
            <el-table
                :data="allCategory"
                highlight-current-row

                :header-cell-style="{'color': 'black','font-weight':'blod','border':'none'}"
                :cell-style="{'border':'none'}"

                @row-click="categoryTableRowClickHandle"
            >
                <el-table-column prop="name" label="日志分类"/>
                <el-table-column align="right">
                    <template #header>
                        <el-link @click="categoryManageDialogShow=true">管理</el-link>
                    </template>
                    <template #default="scope">
                        ({{ scope.row.journalCount }})
                    </template>
                </el-table-column>
            </el-table>
        </div>

    </div>

<!--    分类管理-->
    <el-dialog
        v-model="categoryManageDialogShow"
        title="日志分类管理"
        width="600"
        align-center
    >
        <el-empty v-if="userCreateCategory.length===0" description="当前还没有任何分类，创建一个吧" />

        <el-table
            :data="userCreateCategory"
            :show-header="false"
            :cell-style="{'border':'none'}"
        >
            <el-table-column prop="name" label="分类名称" />
            <el-table-column fixed="right" label="操作" width="120">
                <template #default="scope">
                    <el-button link type="primary" size="small" @click="categoryUpdateDialogShow=true,currentIndex=scope.$index,newCategory=userCreateCategory[currentIndex].name">
                        编辑
                    </el-button>
                    <el-button link type="danger" size="small" @click="deleteCategoryHandle(scope.$index)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-divider />

        <el-button style="width: 100%" @click="categoryCreateDialogShow=true">
            创建
        </el-button>

        <template #footer>
            <div class="dialog-footer">
                <el-button @click="categoryManageDialogShow = false">取消</el-button>
                <el-button type="primary" @click="categoryManageDialogShow = false">
                    确定
                </el-button>
            </div>
        </template>
    </el-dialog>

<!--    创建分类-->
    <el-dialog
        v-model="categoryCreateDialogShow"
        title="创建分类"
        align-center
    >
        <el-input v-model="newCategory" placeholder="请输入分类名称"></el-input>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="categoryCreateDialogShow = false">取消</el-button>
                <el-button type="primary" @click="createCategoryHandle">
                    确定
                </el-button>
            </div>
        </template>
    </el-dialog>
<!--    编辑分类-->
    <el-dialog
        v-model="categoryUpdateDialogShow"
        title="编辑分类"
        align-center
    >
        <el-input v-model="newCategory" placeholder="请输入分类名称"></el-input>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="categoryUpdateDialogShow = false">取消</el-button>
                <el-button type="primary" @click="updateCategoryHandle">
                    确定
                </el-button>
            </div>
        </template>
    </el-dialog>

<!--    修改日志分类时要选择分类的对话框-->
    <el-dialog
        v-model="updateJournalCategoryDialogShow"
        title="选择新分类"
        width="500"
        align-center
    >

        <el-select
            v-model="categoryValue"
            placeholder="选择分类"
            size="small"
        >
            <el-option
                v-for="item in userCreateCategory"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
        </el-select>

        <template #footer>
            <div class="dialog-footer">
                <el-button @click="updateJournalCategoryDialogShow = false">取消</el-button>
                <el-button type="primary" @click="updateJournalCategoryCallback">确定</el-button>
            </div>
        </template>
    </el-dialog>

</div>
</template>

<style scoped lang="less">
.journal{
    padding: 1rem 0;

    .box{
        padding: 1rem 1.5rem;
        background: white;

        display: flex;

        .left{
            flex-grow: 1;

            .exposed{

                .control{
                    display: flex;

                    justify-content: space-between;

                    .sort{
                        width: 100px;
                    }
                }
                .list{
                    margin-top: 1rem;
                }
            }
        }
        .right{
            width: 200px;
            margin-left: 1rem;
        }

    }
}
</style>