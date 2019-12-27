package org.pp.socket.netty.api.channel.impl;

public interface Handler {

    void handlerAdded(HandlerContext ctx) throws Exception;

    void handlerRemoved(HandlerContext ctx) throws Exception;
}
