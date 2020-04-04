package org.pp.zookeeper.server.my;

public class SocketPipeLine<S> implements PipeLine<S> {

    private SocketManager socketManager;

    public SocketPipeLine(SocketManager socketManager) {
        this.socketManager = socketManager;
    }
}
