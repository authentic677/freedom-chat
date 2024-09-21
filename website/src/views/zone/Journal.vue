<script>
import Quill from "quill";
import "quill/dist/quill.core.css";


export default {
    name: "Journal",
    data(){
        return {
            activeName:'my',
            category:[ //这个是内置分类，不可更改
                {
                    name: '全部日志',
                },
                {
                    name: '默认分类',
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
            getEditor:null,

            categoryManageDialogShow:false,
            categoryCreateDialogShow:false,
            categoryUpdateDialogShow:false,
            newCategory:'', //创建和修改分类时用到
            currentIndex:-1 //修改分类时用到
        }
    },
    methods:{
        async getCategoryData(){
            let res=await fetch('/api/zone/journalCategorys',{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            console.log(json)

            this.userCreateCategory=json.data
        },
        initQuillEditor(){
            //这个对象最好不要被vue所直接管理，也就是不要写在data里面，会有问题
            const editor = new Quill(this.$refs.editor,{
                placeholder:"说点什么吧",
                theme:'snow',
                modules: {
                    toolbar: [
                        [{ 'font': [] }],             // 字体选择
                        [{ 'size': ['small', false, 'large', 'huge'] }],  // 字号
                        [{ 'header': [1, 2, 3, 4, 5, 6, false] }],  // 标题等级
                        ['bold', 'italic', 'underline', 'strike'],   // 加粗、斜体、下划线、删除线
                        [{ 'color': [] }, { 'background': [] }],    // 字体颜色和背景颜色
                        [{ 'script': 'sub'}, { 'script': 'super' }], // 上标/下标
                        [{ 'list': 'ordered'}, { 'list': 'bullet' }],  // 有序/无序列表
                        [{ 'indent': '-1'}, { 'indent': '+1' }],    // 缩进
                        [{ 'align': [] }],                         // 对齐方式
                        ['blockquote', 'code-block'],              // 引用和代码块
                        ['link', 'image', 'video'],                // 插入链接、图片、视频
                        ['clean']                                  // 清除格式
                    ]
                }
            });



            this.getEditor=function () {
                return editor
            }
        },
        handleChange(){
            console.log(this.activeName)
        },
        get(){

            this.getEditor().setContents({
                "ops": [
                    { "insert": "Here is a blog post with an image and a link.\n\n" },
                    { "insert": "Check out this image:\n" },
                    { "insert": { "image": "https://example.com/image.jpg" } },
                    { "insert": "\n\nAnd here is a useful link: " },
                    { "insert": "OpenAI", "attributes": { "link": "https://www.openai.com" } },
                    { "insert": ".\n\nThat's all for now!\n" }
                ]
            })
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
        // async updateCategoryHandle(index) {
        //     this.categoryUpdateDialogShow=true
        //     this.newCategory=this.userCreateCategory[index].name
        // },
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
        }
    },
    computed:{
        allCategory(){
            return this.category.concat(this.userCreateCategory)
        }
    },
    created() {
        this.getCategoryData()
    },
    mounted() {
        // this.initQuillEditor()
    }
}
</script>

<template>
<div class="journal">

    <div class="box">
        <el-tabs v-model="activeName" @tab-change="handleChange">
            <el-tab-pane label="我的日志" name="my">
                <div class="exposed">
                    <div class="left">
                        <div class="control">
                            <div class="add">
                                <el-button type="primary">写日志</el-button>
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

<!--                        <el-divider />-->

                        <el-empty description="无任何日志，快去写日志吧" />
                    </div>
                    <div class="right">
                        <el-table
                            :data="allCategory"
                            highlight-current-row

                            :header-cell-style="{'color': 'black','font-weight':'blod','border':'none'}"
                            :cell-style="{'border':'none'}"
                        >
                            <el-table-column prop="name" label="日志分类"/>
                            <el-table-column align="right">
                                <template #header>
                                    <el-link @click="categoryManageDialogShow=true">管理</el-link>
                                </template>
                                <template #default="scope">
                                    (44)
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
            </el-tab-pane>
            <el-tab-pane label="私密日志" name="private">
                <div class="sealed">
                    私密的
                </div>
            </el-tab-pane>
        </el-tabs>
    </div>

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

</div>
</template>

<style scoped lang="less">
.journal{
    padding: 1rem 0;

    .box{
        padding: 1rem 1.5rem;
        background: white;

        .exposed{
            display: flex;

            .left{
                flex-grow: 1;

                .control{
                    display: flex;

                    justify-content: space-between;

                    .sort{
                        width: 100px;
                    }
                }
            }
            .right{
                margin-left: 1rem;
            }
        }
    }
}
</style>