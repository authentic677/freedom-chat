# 自由聊天

## 注意：由于特殊原因，本项目暂停开源

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

## 各模块

### 聊天

单聊界面

![image-20241105152308173](README.assets/image-20241105152308173.png)

群聊界面

![image-20241105152428303](README.assets/image-20241105152428303.png)

### 联系人管理

好友

![image-20241105152533546](README.assets/image-20241105152533546.png)

群聊

![image-20241105152550060](README.assets/image-20241105152550060.png)

好友申请

![image-20241105152617514](README.assets/image-20241105152617514.png)

入群申请

![image-20241105152632445](README.assets/image-20241105152632445.png)

### 我的

![image-20241105152714542](README.assets/image-20241105152714542.png)

### 空间

说说

![image-20241105152741728](README.assets/image-20241105152741728.png)

日志

![image-20241105152832723](README.assets/image-20241105152832723.png)

相册

![image-20241105152853000](README.assets/image-20241105152853000.png)

留言板

![image-20241105152909303](README.assets/image-20241105152909303.png)

### 公众号

前台

![image-20241105152938933](README.assets/image-20241105152938933.png)

![image-20241105152951961](README.assets/image-20241105152951961.png)

![image-20241105154657606](README.assets/image-20241105154657606.png)

后台

![image-20241105153015252](README.assets/image-20241105153015252.png)

### 视频号

前台

![image-20241105153051986](README.assets/image-20241105153051986.png)

![image-20241105153118151](README.assets/image-20241105153118151.png)

后台

![image-20241105153137064](README.assets/image-20241105153137064.png)

### 搜一搜

![image-20241105153146543](README.assets/image-20241105153146543.png)

![image-20241105153254574](README.assets/image-20241105153254574.png)

## 项目结构

### 前端

![image-20241105153630374](README.assets/image-20241105153630374.png)

### 后端

![image-20241105153948965](README.assets/image-20241105153948965.png)

### 数据库

![image-20241105154042516](README.assets/image-20241105154042516.png)

## 提交历史

![image-20241105154351977](README.assets/image-20241105154351977.png)