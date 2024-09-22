<script>
import Quill from "quill";
import "quill/dist/quill.core.css";
import config from "../../config/config.js";

export default {
    name: "GuestBook",
    computed: {
        config() {
            return config
        }
    },
    data(){
        return {
            getQuill:null,
            addCommentDialog:false,

            comments:[],

            comment:'', //用户待发布的留言
            visibility:false, //假表示不勾选仅彼此可见
        }
    },
    methods:{
        async getData(){
            let res=await fetch('/api/zone/comments',{
                headers:{
                    token:localStorage.getItem("token")
                },
            })
            let json=await res.json()

            console.log('留言数据',json)
            this.comments=json.data
        },
        initQuillEditor(){
            //这个对象最好不要被vue所直接管理，也就是不要写在data里面，会有问题
            const editor = new Quill(this.$refs.editor,{

                readOnly:true
            });

            this.getQuill=function () {
                return editor
            }
        },
        async addComment(){ //对话框关闭回调

            let res=await fetch('/api/zone/comment',{
                method:'POST',
                headers:{
                    token:localStorage.getItem("token"),
                    'content-type':'application/json;charset=utf-8'
                },
                body:JSON.stringify({
                    uid1:this.$route.params.uid,
                    content:this.comment,
                    visibility:this.visibility?'private':'public',
                })
            })
            let json=await res.json()

            console.log(json)

            if (json.code===1){
                this.addCommentDialog = false
                this.comment=''
                this.getData()
            }
        },
        async deleteCommentHandle(item){
            let res=await fetch(`/api/zone/comment/${item.id}`,{
                method:'DELETE',
                headers:{
                    token:localStorage.getItem("token")
                }
            })
            let json=await res.json()

            console.log(json)

            if (json.code===1){
                this.getData()
            }
        }
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
    <div class="guest-book">
        <div class="main">

            <div class="title">留言板</div>
            <el-divider />

            <div class="hostMessage">
                <div class="text">主人寄语</div>
                <div class="quill-container" ref="editor">欢迎光临我的空间</div>
            </div>
            <el-divider />

            <div class="sendLeaveMsg">
                <el-button type="primary" @click="addCommentDialog=true">发表留言</el-button>
            </div>


            <div>留言 (0)</div>
            <el-divider />

            <div class="leaveMessage">
                <el-empty v-if="comments.length===0" />
                <div class="item" v-for="item in comments" :key="item.id">
                    <div class="top">
                        <div class="left">
                            <img :src="config.minioUrl+item.user2.avatar" alt="">
                        </div>
                        <div class="right">
                            <div class="name">{{item.user2.username}}</div>
                            <div class="content">{{item.content}}</div>
                            <div class="bottom">
                                <div class="time">{{item.createdAt}}</div>
                                <div class="operation">
                                    <div class="o">
                                        <el-link type="primary">回复</el-link>
                                    </div>
                                    <div class="o">
                                        <el-link type="danger" @click="deleteCommentHandle(item)">删除</el-link>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <el-divider />
                </div>
            </div>

        </div>

        <el-dialog
            v-model="addCommentDialog"
            title="留言"
            width="500"
            align-center
        >
            <el-input
                v-model="comment"
                :rows="5"
                type="textarea"
                placeholder="请输入你的留言"
            />
            <el-checkbox v-model="visibility" label="仅彼此可见" size="small" />

            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="addCommentDialog = false">取消</el-button>
                    <el-button type="primary" @click="addComment">
                        确定
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<style scoped lang="less">
.guest-book {
    padding: 1rem 0 2rem;

    .main{
        border: 1px solid #DDDDDD;
        background-color: white;
        padding: 1rem;

        .hostMessage{

            .text{
                margin-bottom: 1rem;
            }
        }
        .sendLeaveMsg{
            margin: 1rem 0;
        }
        .leaveMessage{

            .item{

                .top{
                    display: flex;

                    .left{

                        img{
                            width: 50px;
                            height: 50px;
                            border-radius: 10px;
                            margin-right: 10px;
                        }
                    }
                    .right{
                        flex-grow: 1;

                        .name{
                            font-size: 20px;
                            font-weight: bold;
                        }
                        .content{
                            margin: 5px 0;
                        }
                        .bottom{
                            display: flex;
                            align-items: end;
                            justify-content: space-between;

                            .time{
                                font-size: 14px;
                                color: gray;
                            }
                            .operation{
                                display: flex;

                                .o{
                                    margin-left: 1rem;
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}
</style>