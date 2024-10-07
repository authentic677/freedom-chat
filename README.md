# 自由聊天

## 概述

基于Web的聊天程序

前端框架：Vue3、Element-Plus、Less、Quill

后端框架：SpringBoot3、MyBatis、MyBatisPlus、Sa-Token

项目管理工具：Vite，Maven

依赖的软件：MySQL、Redis、MinIO、Nginx、Nacos、Coturn

正在施工当中...

## 功能

功能一览：
- 注册登录
- 好友/群组管理
- 异步通讯：单聊，群聊
- 同步通讯：视频聊天，语音聊天
- 用户空间：说说、日志、相册、留言板
- 视频号
- 公众号

## 架构

目前仅提供一个浏览器网址访问本程序，后续考虑提供安卓APP、桌面程序访问

典型的前后端分离架构，使用Nginx连结前后端，为前端提供静态资源服务，为后端提供反向代理以访问后端接口服务，同时也是整个系统的SSL/TLS终止

后端程序被按照功能模块拆分成了几个子程序，分别提供不同模块的Web后端API接口

MySQL负责系统数据的持久功能，不同的后端子程序拥有独立的数据库

MinIO服务提供对象存储功能，Redis提供缓存和会话存储，Nacos提供服务注册与发现，Coturn提供WebRTC的信令服务与中继服务