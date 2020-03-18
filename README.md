# netty
io、nio、网络编程到netty源码分析

io、nio、序列化、网络编程、rmi等等

    netty的应用参考我的其他库，包括：心跳，防火墙，消息处理器的注册，protobuf协议、消息的加密解密，封包、拆包和数据缓存等
    https://github.com/GitJavaProgramming/nettyProject
    https://github.com/GitJavaProgramming/ys
    
    输入输出I/O--外设通信
    操作系统管理软硬件、内核与I/O交互、用户进程陷入内核使用系统资源
    OS内核经由总线系统与设备（抽象字符设备、块设备和网络接口等）通信，交互方式包括：I/O端口、I/O内存映射、轮询和中断
    （微）内核进行资源调度并且提供设备访问接口，用户进程（使用系统调用或中断）陷入内核使用系统资源。

    nio与netty对比：
    
    abstract Buffer 和 abstract ByteBuf
    
    nio Channel SelectionKey  Selector
    netty 
        Channel系列（AbstractNioChannel、AbstractChannel） --- regist key
        EventLoop体系       --- manage Selector
        
        ChannelHandler ChannelPipeline 事件驱动 监听器模式 Listener Event fireAction
        
    Future Promise 异步IO实现

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
![ByteBuf体系图](/src/main/java/org/pp/socket/netty/api/bytebuf/ByteBuf继承体系图.png "这就是你想看的图")  
##### Channel各组件关系图
![Channel各组件关系图](/src/main/java/org/pp/socket/netty/api/channel/maps/association/Channel组件关系图.png "这就是你想看的图")  
##### Channel继承体系图
![Channel继承体系图](/src/main/java/org/pp/socket/netty/api/channel/maps/self/Channel继承体系图.png "这就是你想看的图")  
##### EventLoop继承体系图
![EventLoop继承体系图](/src/main/java/org/pp/socket/netty/核心组件/EventExecutorGroup.png "这就是你想看的图")   

# 参考资料
* Netty权威指南 第2版
* Netty实战
* 深入linux内核架构
* LINUX设备驱动程序 第3版
* unix环境高级编程 第三版
* UNIX网络编程卷1：套接字联网API（第3版）
* UNIX网络编程 卷2：进程间通信（第2版）
* 计算机网络 谢希仁
