<script>
import Navigator from "../../components/Navigator.vue";

export default {
    name: "Self",
    components: {Navigator}
}
</script>

<template>
<div class="self">
    <div class="left">
        <Navigator />
    </div>
    <div class="right">
        aaa
    </div>
</div>
</template>

<style scoped lang="less">
.self{
    height: 100vh;

    display: flex;

    .left{

    }
    .right{
        flex-grow: 1;


    }
}
</style>