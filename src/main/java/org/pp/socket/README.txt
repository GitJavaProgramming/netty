场景：
    V0 服务端用线程池处理连接
    V1 客户端第一次连接时，打印"welcome" 客户端发送任何字符，服务端返回
    V2 从控制台输入字符串发送给服务器，当发送SHUTDOWN时关闭连接

    V3 实现场景，使用NIO非阻塞的通信

api参考javadoc，认真看哦~