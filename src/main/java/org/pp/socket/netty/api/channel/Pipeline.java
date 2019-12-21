package org.pp.socket.netty.api.channel;

public interface Pipeline {
    Pipeline addLast(Handler... handlers);

    Handler first();
}
