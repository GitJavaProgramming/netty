package org.pp.socket.netty.api.channel;

public abstract class Initializer<C extends Channel> {
    protected abstract void initChannel(C ch) throws Exception;
}
