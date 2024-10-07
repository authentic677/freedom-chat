<script>
import {
    AccessibilityHelp,
    Alignment,
    Autoformat,
    AutoImage,
    Autosave,
    BalloonEditor,
    BalloonToolbar,
    BlockQuote,
    BlockToolbar,
    Bold,
    CloudServices,
    Code,
    CodeBlock,
    Essentials,
    FindAndReplace,
    FontBackgroundColor,
    FontColor,
    FontFamily,
    FontSize,
    GeneralHtmlSupport,
    Heading,
    Highlight,
    HorizontalLine,
    ImageBlock,
    ImageCaption,
    ImageInline,
    ImageInsertViaUrl,
    ImageResize,
    ImageStyle,
    ImageTextAlternative,
    ImageToolbar,
    ImageUpload,
    Indent,
    IndentBlock,
    Italic,
    Link,
    LinkImage,
    List,
    ListProperties,
    Paragraph,
    RemoveFormat,
    SelectAll,
    SimpleUploadAdapter,
    SpecialCharacters,
    SpecialCharactersArrows,
    SpecialCharactersCurrency,
    SpecialCharactersEssentials,
    SpecialCharactersLatin,
    SpecialCharactersMathematical,
    SpecialCharactersText,
    Strikethrough,
    Style,
    Subscript,
    Superscript,
    Table,
    TableCaption,
    TableCellProperties,
    TableColumnResize,
    TableProperties, TableToolbar, TextTransformation, TodoList, Underline, Undo
} from "ckeditor5";
import 'ckeditor5/ckeditor5.css';
import axios from "axios";

export default {
    name: "OfficialArticleEditor",
    data(){
        return {
            isLayoutReady: false,
            config: null, // CKEditor needs the DOM tree before calculating the configuration.
            editor: BalloonEditor,
            editorData: '',

            title:'',

            gongzhonghaos:[],//公众号列表
            gongzhonghao:null,//所属的公众号
            coverUrl:'',//文章封面
            cover:null //封面File对象
        }
    },
    methods:{
        async publish(){
            let id=this.$route.query.id
            if (id){
                //此次修改
            }else{
                let article
                //此次是新增
                {
                    let res = await fetch('/api/content-platform/article', {
                        method: 'POST',
                        headers: {
                            'content-type': 'application/json;charset=utf-8'
                        },
                        body: JSON.stringify({
                            title: this.title,
                            content: this.editorData,
                            gongzhonghaoId: this.gongzhonghao
                        })
                    })
                    let json = await res.json()

                    console.log('新增文章', json)
                    article=json.data
                }

                //上传封面
                {
                    let fd=new FormData()
                    fd.append('file',this.cover,''+Math.random())
                    let res = await fetch(`/api/content-platform/article/${article.id}/cover`, {
                        method: 'PUT',
                        body: fd
                    })
                    let json = await res.json()

                    console.log('封面',json)
                }

                this.$router.back()

            }
        },
        coverChange(){
            this.cover=this.$refs.upload.files[0]
            this.coverUrl=URL.createObjectURL(this.cover)
        }
    },
    async created() {
        let res=await axios.get('/api/content-platform/gongzhonghaos')
        let json=res.data

        this.gongzhonghaos=json.data
    },
    mounted() {
        this.config = {
            toolbar: {
                items: [
                    'undo',
                    'redo',
                    '|',
                    'findAndReplace',
                    '|',
                    'uploadImage',
                    '|',
                    'heading',
                    'style',
                    '|',
                    'fontSize',
                    'fontFamily',
                    'fontColor',
                    'fontBackgroundColor',
                    '|',
                    'bold',
                    'italic',
                    'underline',
                    'strikethrough',
                    'subscript',
                    'superscript',
                    'code',
                    'removeFormat',
                    '|',
                    'specialCharacters',
                    'horizontalLine',
                    'link',
                    'insertTable',
                    'highlight',
                    'blockQuote',
                    'codeBlock',
                    '|',
                    'alignment',
                    '|',
                    'bulletedList',
                    'numberedList',
                    'todoList',
                    'outdent',
                    'indent'
                ],
                shouldNotGroupWhenFull: false
            },
            plugins: [
                SimpleUploadAdapter,
                AccessibilityHelp,
                Alignment,
                Autoformat,
                AutoImage,
                Autosave,
                BalloonToolbar,
                BlockQuote,
                BlockToolbar,
                Bold,
                CloudServices,
                Code,
                CodeBlock,
                Essentials,
                FindAndReplace,
                FontBackgroundColor,
                FontColor,
                FontFamily,
                FontSize,
                GeneralHtmlSupport,
                Heading,
                Highlight,
                HorizontalLine,
                ImageBlock,
                ImageCaption,
                ImageInline,
                ImageInsertViaUrl,
                ImageResize,
                ImageStyle,
                ImageTextAlternative,
                ImageToolbar,
                ImageUpload,
                Indent,
                IndentBlock,
                Italic,
                Link,
                LinkImage,
                List,
                ListProperties,
                Paragraph,
                RemoveFormat,
                SelectAll,
                SpecialCharacters,
                SpecialCharactersArrows,
                SpecialCharactersCurrency,
                SpecialCharactersEssentials,
                SpecialCharactersLatin,
                SpecialCharactersMathematical,
                SpecialCharactersText,
                Strikethrough,
                Style,
                Subscript,
                Superscript,
                Table,
                TableCaption,
                TableCellProperties,
                TableColumnResize,
                TableProperties,
                TableToolbar,
                TextTransformation,
                TodoList,
                Underline,
                Undo
            ],
            balloonToolbar: ['bold', 'bold', '|', 'link', '|', 'bulletedList', 'numberedList'],
            blockToolbar: [
                'bold',
                'fontColor',
                'fontBackgroundColor',
                '|',
                'bold',
                'italic',
                '|',
                'link',
                'insertTable',
                '|',
                'bulletedList',
                'numberedList',
                'outdent',
                'indent'
            ],
            fontFamily: {
                supportAllValues: true
            },
            fontSize: {
                options: [10, 12, 14, 'default', 18, 20, 22],
                supportAllValues: true
            },
            heading: {
                options: [
                    {
                        model: 'paragraph',
                        title: 'Paragraph',
                        class: 'ck-heading_paragraph'
                    },
                    {
                        model: 'heading1',
                        view: 'h1',
                        title: 'Heading 1',
                        class: 'ck-heading_heading1'
                    },
                    {
                        model: 'heading2',
                        view: 'h2',
                        title: 'Heading 2',
                        class: 'ck-heading_heading2'
                    },
                    {
                        model: 'heading3',
                        view: 'h3',
                        title: 'Heading 3',
                        class: 'ck-heading_heading3'
                    },
                    {
                        model: 'heading4',
                        view: 'h4',
                        title: 'Heading 4',
                        class: 'ck-heading_heading4'
                    },
                    {
                        model: 'heading5',
                        view: 'h5',
                        title: 'Heading 5',
                        class: 'ck-heading_heading5'
                    },
                    {
                        model: 'heading6',
                        view: 'h6',
                        title: 'Heading 6',
                        class: 'ck-heading_heading6'
                    }
                ]
            },
            htmlSupport: {
                allow: [
                    {
                        name: /^.*$/,
                        styles: true,
                        attributes: true,
                        classes: true
                    }
                ]
            },
            image: {
                toolbar: [
                    'toggleImageCaption',
                    'imageTextAlternative',
                    '|',
                    'imageStyle:inline',
                    'imageStyle:wrapText',
                    'imageStyle:breakText',
                    '|',
                    'resizeImage'
                ]
            },
            link: {
                addTargetToExternalLinks: true,
                defaultProtocol: 'https://',
                decorators: {
                    toggleDownloadable: {
                        mode: 'manual',
                        label: 'Downloadable',
                        attributes: {
                            download: 'file'
                        }
                    }
                }
            },
            list: {
                properties: {
                    styles: true,
                    startIndex: true,
                    reversed: true
                }
            },
            placeholder: '请在这里输入你的正文内容',
            style: {
                definitions: [
                    {
                        name: 'Article category',
                        element: 'h3',
                        classes: ['category']
                    },
                    {
                        name: 'Title',
                        element: 'h2',
                        classes: ['document-title']
                    },
                    {
                        name: 'Subtitle',
                        element: 'h3',
                        classes: ['document-subtitle']
                    },
                    {
                        name: 'Info box',
                        element: 'p',
                        classes: ['info-box']
                    },
                    {
                        name: 'Side quote',
                        element: 'blockquote',
                        classes: ['side-quote']
                    },
                    {
                        name: 'Marker',
                        element: 'span',
                        classes: ['marker']
                    },
                    {
                        name: 'Spoiler',
                        element: 'span',
                        classes: ['spoiler']
                    },
                    {
                        name: 'Code (dark)',
                        element: 'pre',
                        classes: ['fancy-code', 'fancy-code-dark']
                    },
                    {
                        name: 'Code (bright)',
                        element: 'pre',
                        classes: ['fancy-code', 'fancy-code-bright']
                    }
                ]
            },
            table: {
                contentToolbar: ['tableColumn', 'tableRow', 'mergeTableCells', 'tableProperties', 'tableCellProperties']
            },
            simpleUpload: {
                // The URL that the images are uploaded to.
                uploadUrl: '/api/zone/test/t',

                // Enable the XMLHttpRequest.withCredentials property.
                withCredentials: true,

                // Headers sent along with the XMLHttpRequest to the upload server.
                headers: {
                    'X-CSRF-TOKEN': 'CSRF-Token',
                    Authorization: 'Bearer <JSON Web Token>'
                }
            }
        };

        this.isLayoutReady = true;
    }
}
</script>

<template>
<div class="official-article-editor">
    <div class="topBar">
        <h3>编辑文章</h3>
    </div>

    <div class="editArea">
        <div class="article">
            <div class="title">
                <input type="text" placeholder="请输入标题" v-model="title">
                <div class="line"></div>
            </div>
            <div class="body">
                <!--                    <div class="editor"></div>-->
                <ckeditor
                    v-if="isLayoutReady"
                    v-model="editorData"
                    :editor="editor"
                    :config="config"
                />
            </div>
        </div>

        <div class="more">
            <div class="grid">
                <div class="item">所属公众号：</div>
                <div class="item">
                    <el-select
                        v-model="gongzhonghao"
                        placeholder="选择所属公众号"
                        size="small"
                        style="width: 240px"
                    >
                        <el-option
                            v-for="item in gongzhonghaos"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </div>
                <div class="item">文章封面：</div>
                <div class="item">
                    <div class="upload-frame" @click="$refs.upload.click()">
                        <div class="mode1" v-if="!coverUrl">
                            <img src="/upload.png" alt="">
                        </div>
                        <div class="mode2" v-if="coverUrl">
                            <img :src="coverUrl" alt="">
                        </div>
                        <input type="file" ref="upload" accept="image/*" style="display: none" @change="coverChange" >
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="bottomBar">
        <el-button @click="publish">发布</el-button>
    </div>
</div>
</template>

<style scoped lang="less">
.official-article-editor{
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;

    background-color: #F5F6F7;

    display: grid;
    grid-template-rows: auto 1fr auto; /* 第一行和第三行高度由内容决定,中间行撑满剩余空间 */

    .topBar{
        padding: 1rem;
        background-color: white;
        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .05);

        h3{
            margin: 0;
        }
    }
    .editArea{

        padding: 2rem 0;


        overflow: auto;

        .article{
            margin: 0 auto;
            width: 820px;
            //min-height: 100%;
            background-color: white;

            .title{
                padding: 32px 64px;

                input{
                    width: 100%;
                    font-weight: 600;
                    border: none;               /* 去掉边框 */
                    outline: none;              /* 去掉聚焦时的轮廓 */
                    font-size: 23px;           /* 设置文字尺寸 */
                    /* 可选：设置背景色和内边距以保持视觉一致性 */
                    background: transparent;    /* 可选：去掉背景色 */
                    padding: 5px;              /* 可选：调整内边距 */
                }
                .line{
                    margin-top: 1rem;
                    height: 1px;
                    background-color: #E8E7E8;
                }
            }
            .body{
                margin-top: 24px;
                padding: 0 64px 64px;

                .editor{

                }
            }
        }
        .more{
            width: 820px;
            margin: 24px auto 0;
            padding: 64px;
            background-color: white;
            box-sizing: border-box;

            .grid{
                display: grid;
                grid-template-columns: 150px 1fr;
                row-gap: 1rem;

                .item{

                    .upload-frame{
                        width: 80px;
                        height: 80px;
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
            }
        }
    }
    .bottomBar{
        border-top: 1px solid #E8E8E8;
        padding: 1rem;
    }
}
</style>