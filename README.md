# netty
io、nio、网络编程到netty源码分析

io、nio、序列化、网络编程、rmi等等

## 分享一波图，代码在项目中，都是干货！  

    java
        └─org
            └─pp
                ├─exception
                ├─io        java io UML图
                ├─nio       java nio Buffer、Channel数据结构与api UML图
                └─socket
                    ├─aio       java异步api通信 异步回调机制
                    ├─bio       阻塞通信demo
                    ├─netty     
                    │  ├─api        netty api示例
                    │  │  ├─channel     Channel机制
                    │  │  └─eventloop       事件循环机制
                    │  ├─simpleDemo     一个最简单的netty通信demo
                    │  └─核心组件       核心组件UML
                    └─nio       java nio通信
    resources     netty相关资源整理 UML图、说明文档
        └─netty

##### ByteBuf体系图
![ByteBuf体系图](/src/main/resources/netty/AbstractByteBuf.png "这就是你想看的图")   

##### Channel各组件关系图
![Channel各组件关系图](/src/main/resources/netty/Channel组件关系图.png "这就是你想看的图")   
