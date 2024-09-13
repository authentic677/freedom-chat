<script setup>
import {nextTick, onMounted, ref, watch} from "vue";
import NewSaying from "../../components/NewSaying.vue";
import SayingNode from "../../components/SayingNode.vue";


let postsList=ref([])

const dialogVisible = ref(false)


let props=defineProps(['currentUser','uid'])


const getData=async ()=>{
    let res=await fetch('/api/zone/posts',{
        headers:{
            token:localStorage.getItem('token')
        }
    })
    let json=await res.json()

    console.log(json)

    postsList.value=json.data.map(e=>{
        e.content=JSON.parse(e.content)
        e.user={}
        return e
    })

    for(let i=0;i<postsList.value.length;i++){
        if(postsList.value[i].uid!==props.currentUser.uid){
            let res=await fetch(`/api/user/${postsList.value[i].uid}`,{
                headers:{
                    token:localStorage.getItem('token')
                }
            })
            let json=await res.json()

            postsList.value[i].user=json.data

            console.log(json.data)
        }else {
            postsList.value[i].user=props.currentUser
        }

    }
}

const dialogCloseHandler=(state)=>{
    dialogVisible.value = false

    if (state){
        getData()
    }
}

getData()
</script>

<script>
// 在 export default 中指定组件名称
export default {
    name: 'Saying'
}
</script>

<template>
    <div class="saying">
        <div class="item" v-for="(item,index) in postsList">
            <SayingNode :data="item" variant="secondary" @update="getData" />
        </div>

        <div class="newSaying">
            <div class="btn" @click="dialogVisible=true">
                <svg t="1726037290696" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4235" width="200" height="200"><path d="M512 0c-285.257143 0-512 226.742857-512 512s226.742857 512 512 512 512-226.742857 512-512-226.742857-512-512-512z m0 950.857143c-241.371429 0-438.857143-197.485714-438.857143-438.857143s197.485714-438.857143 438.857143-438.857143 438.857143 197.485714 438.857143 438.857143-197.485714 438.857143-438.857143 438.857143z" fill="#76A9E3" p-id="4236"></path><path d="M731.428571 475.428571h-182.857142v-182.857142c0-21.942857-14.628571-36.571429-36.571429-36.571429s-36.571429 14.628571-36.571429 36.571429v182.857142h-182.857142c-21.942857 0-36.571429 14.628571-36.571429 36.571429s14.628571 36.571429 36.571429 36.571429h182.857142v182.857142c0 21.942857 14.628571 36.571429 36.571429 36.571429s36.571429-14.628571 36.571429-36.571429v-182.857142h182.857142c21.942857 0 36.571429-14.628571 36.571429-36.571429s-14.628571-36.571429-36.571429-36.571429z" fill="#76A9E3" p-id="4237"></path></svg>
            </div>
            <NewSaying v-if="dialogVisible" @close="dialogCloseHandler" />
        </div>

    </div>
</template>

<style scoped lang="less">
.saying{
    margin-top: 2rem;

    .item{
        border: 1px solid white;
        width: 600px;
        background: rgba(255,255,255,0.6);
        border-radius: 15px;
        margin-bottom: 2rem;

        overflow: hidden;
    }

    .newSaying{
        position: fixed;
        right: 2rem;
        bottom: 2rem;

        .btn{
            display: flex;
            svg{
                width: 50px;
                height: 50px;
            }
        }
    }
}
</style>