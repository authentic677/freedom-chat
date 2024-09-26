<script>
import Quill from "quill";
// import "quill/dist/quill.snow.css";

export default {
    name: "JournalEditor",
    data(){
        return {
            getEditor:null,

            title:'',

            category:[{id:0, name:"默认分类"}],
            userCreateCategory:[],
            categoryValue: 0,

            allowComment:true,
            isTopping:false,

            visibility:[
                {id:0,label:'公开',value:'public'},
                {id:1,label:'好友',value:'friends'},
                {id:2,label:'指定好友',value:'custom'},
            ],
            visibilityValue:'public'
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
                placeholder:"记录一下",
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
            window.q=editor
        },
        //发表日志的回调
        async publishJournal(){

            let content=this.getEditor().getContents()

            let res=await fetch('/api/zone/journal',{
                method:'POST',
                headers:{
                    token:localStorage.getItem('token'),
                    'content-type':'application/json;charset=utf-8'
                },
                body:JSON.stringify({
                    title:this.title,
                    content:JSON.stringify(content),
                    status:'published',
                    letterPaper:'',
                    visibility:this.visibilityValue,
                    allowComments:this.allowComment?1:0,
                    categoryId:this.categoryValue,
                    isPrivate:0,
                    isPinned:this.isTopping?1:0,
                })
            })
            let json=await res.json()

            console.log('发表日志',json)

            if (json.code===1){
                this.$router.back()
            }
        }
    },
    computed:{
        allCategory(){
            return this.category.concat(this.userCreateCategory)
        }
    },
    watch:{
        title(){
            // let quill=this.getEditor()
            // let text=this.title
            //
            // // 删除当前编辑器中的第一行内容
            // quill.deleteText(0, quill.getText(0, 1).length);
            //
            // // 在第一行插入新的文本作为一级标题
            // quill.insertText(0, text + '\n', { header: 1 });
            //
            // // 将插入的第一行居中对齐
            // quill.formatLine(0, 1, { align: 'center' });
        }
    },
    async created() {
        this.getCategoryData()
        //加载quill的snow主题css
        let style=document.createElement('style')
        style.setAttribute("type","text/css")
        style.setAttribute("id","quill.snow.css")
        let res=await fetch('/css/quill.snow.css')
        let text=await res.text()
        style.innerHTML=text
        document.head.appendChild(style)
    },
    mounted() {
        this.initQuillEditor()
    },
    beforeUnmount() {
        //卸载css
        let snow_css=document.getElementById('quill.snow.css')
        document.head.removeChild(snow_css)
    }
}
</script>

<template>
<div class="journal-editor">

    <div class="box">
        <div class="top">
            <h3>写日志</h3>
        </div>
        <div class="main">

            <el-input v-model="title" placeholder="在此输入日志标题" />

            <div class="quill-editor-container" ref="editor"></div>

            <div class="divide"></div>
            <div class="meta">
                <div class="left">
                    分类：
                    <el-select
                        v-model="categoryValue"
                        size="small"
                        style="width: 120px"
                    >
                        <el-option
                            v-for="item in allCategory"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                    &nbsp;
                    权限：
                    <el-select
                        v-model="visibilityValue"
                        size="small"
                        style="width: 120px"
                    >
                        <el-option
                            v-for="item in visibility"
                            :key="item.id"
                            :label="item.label"
                            :value="item.value"
                        />
                    </el-select>
                </div>

                <div class="right">
                    <el-checkbox v-model="allowComment" label="允许评论" size="small" />

                    <el-checkbox v-model="isTopping" label="是否置顶" size="small" />
                </div>

            </div>
        </div>
        <div class="bottom">
            <el-button type="primary" @click="publishJournal">发表</el-button>
            <el-button @click="this.$router.back()">取消</el-button>
            <el-button>预览</el-button>
            <el-button>保存草稿</el-button>
        </div>

    </div>

</div>
</template>

<style scoped lang="less">
@import url("quill/dist/quill.snow.css");

.journal-editor{
    padding: 1rem 0 2rem;

    .box{
        border: 1px solid #DDDDDD;
        background-color: white;

        .top{
            background-color: #F2F2F2;

            h3{
                padding: 1rem;
            }
        }
        .main{
            margin: 1rem;
            background-color: white;
            border: 1px solid #DDDDDD;

            min-height: 600px;

            display: flex;
            flex-direction: column;

            /*去掉quill容器的边框*/
            .ql-container{
                border: none;
            }
            .divide{
                flex-grow: 1;
            }
            .meta{
                padding: 1rem;

                display: flex;
                justify-content: space-between;
            }
        }
        .bottom{
            background-color: #F2F2F2;
            padding: 1rem ;
        }
    }
}
</style>