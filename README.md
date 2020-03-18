# netty
io、nio、网络编程到netty源码分析

io、nio、序列化、网络编程、rmi等等

    netty的应用参考我的其他库，主要包括：心跳，防火墙，消息处理器的注册，protobuf协议、消息的加密解密，封包、拆包，数据缓存等等
    https://github.com/GitJavaProgramming/nettyProject
    https://github.com/GitJavaProgramming/ys

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
![ByteBuf体系图](/org/pp/socket/netty/api/bytebuf/ByteBuf继承体系图.png "这就是你想看的图")  
##### Channel各组件关系图
![Channel各组件关系图](/org/pp/socket/netty/api/channel/maps/association/Channel组件关系图.png "这就是你想看的图")  
##### Channel继承体系图
![Channel继承体系图](/org/pp/socket/netty/api/channel/maps/self/Channel继承体系图.png "这就是你想看的图")  
##### EventLoop继承体系图
![EventLoop继承体系图](/src/main/java/org/pp/socket/netty/核心组件/EventExecutorGroup.png "这就是你想看的图")   

    nio与netty对比：
    
    Buffer ByteBuf
    
    nio Channel SelectionKey  Selector
    netty 
        Channel系列（AbstractNioChannel、AbstractChannel） --- regist key
        EventLoop体系       --- manage Selector
        
        ChannelHandler ChannelPipeline 事件驱动 监听器模式 Listener Event fireAction
        
    Future Promise 异步IO实现