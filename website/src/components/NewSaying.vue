<script>
import Quill from "quill";
import "quill/dist/quill.core.css";

export default {
    name: "NewSaying",
    data(){
        return {
            attachList:[],
            getEditor:null
        }
    },
    methods:{
        initQuillEditor(){
            //这个对象最好不要被vue所直接管理，也就是不要写在data里面，会有问题
            const editor = new Quill(this.$refs.editor,{
                placeholder:"说点什么吧"
            });

            // 监听文本改变事件
            editor.on('text-change', () => {
                console.log(editor.root.innerHTML);
            });

            this.getEditor=function () {
                return editor
            }
        },
        attachIn(){
            this.$refs.attachInput.click()
        },
        attachChangeHandler(){
            let {attachInput}=this.$refs
            for(let i=0;i<attachInput.files.length;i++){
                this.attachList.push(attachInput.files[i]);
            }
        },
        toURL(o){
            return URL.createObjectURL(o)
        },
        async submit(){

            let text=this.getEditor().getText()

            console.log(text)
            console.log(this.attachList)

            let fd=new FormData()
            fd.append('text',text)
            for(let i=0;i<this.attachList.length;i++){
                fd.append('attachments',this.attachList[i],this.attachList[i].name)
            }

            let res=await fetch('/api/zone/post',{
                method:'POST',
                body:fd
            })
            let json=await res.json()

            console.log(json)

            this.$emit('close',true)
        }
    },
    watch:{
        attachList(){

        }
    },
    created() {

    },
    mounted() {
        this.initQuillEditor()
    }
}
</script>

<template>
<div class="new-saying">

    <div class="body">
        <h3>发布说说</h3>

        <div class="content">
            <div class="editor-wrapper">
                <div class="editor" ref="editor"></div>
            </div>
            <div class="attachments">
                <div class="item" v-for="(item,index) in attachList" :key="item.name">
                    <img :src="toURL(item)" alt="">
                </div>
                <div class="item add" @click="attachIn">
                    <svg t="1726046688448" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4383" width="200" height="200"><path d="M902.343 570.936h-331.78v331.833c0 32.337-26.226 58.537-58.564 58.537-32.337 0-58.563-26.2-58.563-58.537V570.936H121.654c-32.364 0-58.564-26.2-58.564-58.538 0-32.325 26.203-58.537 58.564-58.537h331.78V122.028c0-32.325 26.226-58.537 58.563-58.537 32.338 0 58.564 26.213 58.564 58.537v331.834h331.78c32.364 0 58.565 26.211 58.565 58.535-0.001 32.337-26.2 58.536-58.565 58.536z" fill="#5C5C5C" p-id="4384"></path></svg>
                    <input type="file" accept="image/*" multiple="multiple" ref="attachInput" @change="attachChangeHandler" />
                </div>
            </div>
        </div>

        <div class="buttons">
            <el-button @click="$emit('close')" >取消</el-button>
            <el-button type="primary"  @click="submit">
                确定
            </el-button>
        </div>
    </div>
</div>
</template>

<style scoped lang="less">
.new-saying{
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;

    display: flex;
    justify-content: center;
    align-items: center;

    background-color: rgba(0, 0, 0, 0.5);

    .body{
        width: 800px;
        height: 600px;

        display: flex;
        flex-direction: column;

        background-color: white;
        padding: 1.5rem;
        box-sizing: border-box;

        h3{
            text-align: center;
            margin-bottom: 1rem;
        }

        .content{
            flex-grow: 1;

            overflow: auto;

            .editor-wrapper{
                height: 50%;
                box-sizing: border-box;

                margin-bottom: 1rem;

                .editor{
                    padding: 0;
                }
            }
            .attachments{
                display: grid;
                grid-template-columns: repeat(5, 1fr);
                gap: 10px;

                .item{
                    width: 100%;
                    aspect-ratio: 1;

                    border: 2px dotted gray;
                    box-sizing: border-box;

                    img{
                        width: 100%;
                        height: 100%;
                        object-fit: cover;

                        vertical-align: top;
                    }
                }
                .item.add{
                    display: flex;
                    justify-content: center;
                    align-items: center;

                    cursor: pointer;

                    svg{
                        width: 30px;
                        height: 30px;
                    }
                    input{
                        display: none;
                    }
                }
            }
        }
        .buttons{
            display: flex;
            justify-content: space-between;
        }
    }
}
</style>