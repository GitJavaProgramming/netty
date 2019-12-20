package org.pp.socket.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import static org.pp.socket.CommandConstant.SERVER_HOST;
import static org.pp.socket.CommandConstant.SERVER_PORT;

public class TcpClient {

    public static void main(String[] args) {
        connect(SERVER_HOST, SERVER_PORT);
    }

    public static void connect(String host, int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(bossGroup)
                .channel(NioSocketChannel.class)/*.remoteAddress(new InetSocketAddress(SERVER_HOST, SERVER_PORT))*/
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ClientHandler());
                    }
                });

        try {
            ChannelFuture future = b.connect(host, port).sync();
            System.out.println("connected");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
        }
    }
}
