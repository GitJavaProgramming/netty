package org.pp.socket.netty.api.channel;

public interface HandlerContext {


    HandlerContext fireChannelRegistered();

    HandlerContext fireChannelUnregistered();

    HandlerContext fireChannelActive();

    HandlerContext fireChannelInactive();

    HandlerContext fireExceptionCaught(Throwable cause);

    HandlerContext fireUserEventTriggered(Object evt);

    HandlerContext fireChannelRead(Object msg);

    HandlerContext fireChannelReadComplete();

    HandlerContext fireChannelWritabilityChanged();

    HandlerContext read();

    HandlerContext flush();

    /**
     * 获取所在的pipeline
     *
     * @return
     */
    Pipeline pipeline();

    Handler handler();
}
