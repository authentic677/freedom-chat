import { createApp } from 'vue'
import './style.css'
import App from './App.vue'

//Vue Router
import VueRouter from './vue-plugin/vue-router.js'

//Element Plus
import ElementPlus from './vue-plugin/element-plus.js'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'  //图标啊

//Vuex
import Vuex from "./vue-plugin/vuex.js";

//狗东西啊，vuetify和element-plus会出现css样式冲突
//Vuetify
// import Vuetify from './vue-plugin/vuetify.js'

const app= createApp(App)


//element icons
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app
    .use(VueRouter)
    .use(ElementPlus)
    // .use(Vuetify)
    .use(Vuex)
    .mount('#app')
