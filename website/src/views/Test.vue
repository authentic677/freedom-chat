<script setup>



</script>

<template>
<div class="test">

    <div class="grid">
        <div class="item" v-for="item in 2">
            aaa
        </div>
        <div class="item">+</div>
    </div>

</div>
</template>

<style lang="less" scoped>
.test{
    .grid{
        display: grid;
        grid-template-columns: repeat(3,1fr);

        .item{
            aspect-ratio: 1;
        }
    }
}
</style>