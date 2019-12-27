package org.pp.socket.netty.api.channel.impl;

public interface Pipeline {
    Pipeline addLast(Handler... handlers);

    Handler first();
}
