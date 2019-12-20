package org.pp.socket.netty.simpleDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf resp = Unpooled.copiedBuffer("I am foo!!".getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {

        byte[] req = new byte[msg.readableBytes()];
        msg.readBytes(req);
        String body = new String(req, Charset.defaultCharset());
        System.out.println("Server echo : " + body);

    }

}
