package org.pp.socket.netty.api.channel.impl;

public class Client {
    public static void main(String[] args) {
        new Initializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new HandlerAdapter() {
                });
            }
        };
    }
}
