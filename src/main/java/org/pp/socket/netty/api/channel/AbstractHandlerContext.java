package org.pp.socket.netty.api.channel;

public abstract class AbstractHandlerContext implements HandlerContext {
    private final Handler handler;


    protected AbstractHandlerContext(Handler handler) {
        this.handler = handler;
    }

    @Override
    public Handler handler() {
        return handler;
    }
}
