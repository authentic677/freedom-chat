import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import basicSsl from '@vitejs/plugin-basic-ssl'; //启用https的

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        // basicSsl()
    ],
    server:{
        host: '0.0.0.0',
        proxy:{
            '/api':{
                target:'https://127.0.0.1',
                changeOrigin:true,
                secure:false //不验证tls证书
            },
            '/websocket':{
                target:'wss://127.0.0.1',
                ws:true,
                secure:false //不验证tls证书
            }
        },
    }
})
