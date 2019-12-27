package org.pp.socket.netty.api.channel.impl;

public abstract class Initializer<C extends Channel> {
    protected abstract void initChannel(C ch) throws Exception;
}
