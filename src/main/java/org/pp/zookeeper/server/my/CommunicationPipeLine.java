package org.pp.zookeeper.server.my;

import java.util.concurrent.BlockingQueue;

public class CommunicationPipeLine<S> implements PipeLine<S> {

    protected final BlockingQueue<S> sendQueue;
    protected final BlockingQueue<S> recvQueue;

    public CommunicationPipeLine(BlockingQueue<S> sendQueue, BlockingQueue<S> recvQueue) {
        this.sendQueue = sendQueue;
        this.recvQueue = recvQueue;
    }

    public BlockingQueue<S> getSendQueue() {
        return sendQueue;
    }

    public BlockingQueue<S> getRecvQueue() {
        return recvQueue;
    }
}
