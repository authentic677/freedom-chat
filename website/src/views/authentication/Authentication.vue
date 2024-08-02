<script>
export default {
    name: "Authentication"
}
</script>

<template>
    <div class="authentication">
<!--        组件动画-->
        <router-view v-slot="{Component}">
            <transition name="turn" mode="out-in">
                <component :is="Component"/>
            </transition>
        </router-view>
    </div>
</template>

<style scoped>
.authentication{
    height: 100vh;

    background-image: url('/authentication-bg.jpg');
    background-position: center;
    background-size: cover;

    display: flex;
    justify-content: center;
    align-items: center;

    perspective: 1000px;
}


.turn-enter-active,
.turn-leave-active {
    transition: 0.2s linear;
}
.turn-enter-from{
    transform: rotateY(90deg);
}
.turn-enter-to{
    transform: rotateY(0deg);
}
.turn-leave-from {
    transform: rotateY(0deg);
}
.turn-leave-to {
    transform: rotateY(-90deg);
}
</style>