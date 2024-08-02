<template>
    <div class="searchBox" :class="{focus:isFocus}">
        <svg v-if="!icon" t="1709628408374" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3482" width="200" height="200"><path d="M889.6 838.4l-192-192C742.4 595.2 768 524.8 768 448c0-179.2-140.8-320-320-320S128 268.8 128 448s140.8 320 320 320c76.8 0 147.2-25.6 198.4-70.4l192 192c12.8 12.8 32 12.8 44.8 0C896 876.8 896 851.2 889.6 838.4zM192 448c0-140.8 115.2-256 256-256s256 115.2 256 256c0 140.8-115.2 256-256 256S192 588.8 192 448z" p-id="3483"></path></svg>
        <input type="text" :placeholder="placeholder" @focus="isFocus=true" @blur="isFocus=false" @keydown.enter="enter" @input="input">
        
    </div>
</template>

<script>
export default {
    name:'SearchBox',
    data(){
        return {
            isFocus:false
        }
    },
    props:['placeholder','icon'],
    methods:{
        enter(e){
            this.$emit('enter',e.target.value)
        },
        input(e){
            console.log(e.target.value)
            this.$emit('in',e.target.value)
        }
    }
}
</script>

<style scoped lang="less">
.searchBox{
    /* flex-grow: 1; */

    display: flex;
    align-items: center;
    background-color: #F5F5F5;
    border-radius: 5px;

    svg{
        width: 18px;
        height: 18px;
        margin-left: 10px;
    }
    input{

        outline: none;
        border: none;
        padding: 0px;
        margin: 0 10px;
        background-color: transparent;
        flex-grow: 1;
        height: 30px;
        font-size: 16px;
    }
}
.searchBox.focus{
    outline: 1px solid blue;
}
</style>