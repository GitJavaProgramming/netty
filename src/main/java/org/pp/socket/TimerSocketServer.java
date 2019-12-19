package org.pp.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;

public class TimerSocketServer {

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(/*8000*/);
            // SO_REUSEADDR、SO_RCVBUF跟具体平台有关
            System.out.println("Default ReuseAddress Value : " + serverSocket.getReuseAddress());
            System.out.println("Default ReceiveBufferSize Value : " + serverSocket.getReceiveBufferSize());

            option(serverSocket);

            serverSocket.bind(new InetSocketAddress(8000));

            serverSocket.accept(); // 阻塞等待客户端连接，连接请求队列为空则会一直等待，当有客户端连接时才返回。

//            hook(); // 虚拟机钩子未执行  main方法没有执行到这里 accept异常处理

        } catch (IOException e) {
            try {
                serverSocket.close();  // 检测到异常时首先关闭socket
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();  // 打印异常栈
        }
    }

    /**
     * 配置ServerSocket （TCP Connection） 选项
     */
    public static void option(ServerSocket serverSocket) throws SocketException {

        // 启用或禁用SocketOptions#SO_TIMEOUT SO_TIMEOUT
        // 当等待客户端连接超时将会抛出java.net.SocketTimeoutException
        serverSocket.setSoTimeout(100); // 等待客户端连接超时时间

        // TCP 三次握手、四次挥手
        // SocketOptions#SO_REUSEADDR
        // MSL是Maximum Segment Lifetime英文的缩写，中文可以译为“报文最大生存时间”
        // 服务器端关闭一个tcp连接时，Server端连接会保持一段时间处于超时状态，通常被认为是TIME_WAIT状态或者2MSL等待状态
        // 对于使用已知socket地址和端口的应用程序来说，如果（Server端）有一个连接正在超时状态可能无法绑定所需的SocketAddress
        serverSocket.setReuseAddress(true); // 设置允许重用服务器所绑定的地址
        // SocketOptions#SO_RCVBUF
        serverSocket.setReceiveBufferSize(1024); // 设置接收数据缓冲区大小

//        serverSocket.setPerformancePreferences();
    }

    public static void hook() {
        System.out.println("register hook...");
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("服务器超时关闭...");
            }
        }));
    }
}
