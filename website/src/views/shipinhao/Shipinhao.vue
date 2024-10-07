<script>
import Navigator from "../../components/Navigator.vue";

export default {
    name: "Shipinhao",

    components: {Navigator}
}
</script>

<template>
<div class="shipinhao">
    <div class="left">
        <Navigator />
    </div>
    <div class="right">
        <router-view></router-view>
    </div>
</div>
</template>


<style scoped lang="less">
.shipinhao{
    height: 100vh;
    display: flex;

    .left{

    }
    .right {
        flex-grow: 1;

    }
}
</style>