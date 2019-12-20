package org.pp.socket.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class ChildChannelInitializer extends ChannelInitializer {

    @Override
    protected void initChannel(Channel channel) {
        channel.pipeline().addLast(new ServerHandler());
    }
}
