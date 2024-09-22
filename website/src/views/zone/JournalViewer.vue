<script>
import Quill from "quill";
import "quill/dist/quill.core.css";

export default {
    name: "JournalViewer",
    data(){
        return {
            journal:{},
            getQuill:null,
        }
    },
    methods:{
        async getData(){
            let res=await fetch(`/api/zone/journal/${this.$route.params.id}`,{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            this.journal=json.data

            console.log(this.journal)

            let t=this
            wait()
            function wait() {
                if(t.getQuill){
                    t.getQuill().setContents(JSON.parse(t.journal.content))
                }else {
                    setTimeout(wait,10)
                }
            }
        },
        initQuillEditor(){
            //这个对象最好不要被vue所直接管理，也就是不要写在data里面，会有问题
            const quill = new Quill(this.$refs.quillDom,{
                readOnly:true
            });

            this.getQuill=function () {
                return quill
            }
        },
    },
    created() {
        this.getData()
    },
    mounted() {
        this.initQuillEditor()
    }
}
</script>

<template>
<div class="journal-viewer">
    <div class="top">
        <div class="left">
            日志
        </div>
        <div class="right">
            <el-link @click="$router.back()">返回日志列表</el-link>
        </div>
    </div>
    <div class="main">
        <div class="head">
            <div class="title">
                {{journal.title}}
            </div>
            <div class="meta">
                编辑于 {{journal.updateAt}}
            </div>
        </div>
        <div class="content">
            <div class="quillDom" ref="quillDom"></div>
        </div>
        <div class="control">
            <div class="item like">点赞</div>
            <div class="item comment">评论</div>
            <div class="item copyAddr">复制地址</div>
        </div>
    </div>
</div>
</template>

<style scoped lang="less">
.journal-viewer{
    padding: 1rem 0;

    .top{
        display: flex;
        justify-content: space-between;

        margin-bottom: 1rem;
    }
    .main{
        background-color: white;
        padding: 1rem;

        .head{
            display: flex;
            align-items: end;
            margin-bottom: 1rem;

            .title{
                font-size: 25px;
                font-weight: bold;
            }
            .meta{
                margin-left: 2rem;
                font-size: 14px;
                color: gray;
            }
        }
        .content{
            background-color: #F2F4F7;
        }
        .control{
            display: flex;

            margin-top: 1rem;

            .item{
                margin-right: 1rem;
            }
        }
    }
}
</style>