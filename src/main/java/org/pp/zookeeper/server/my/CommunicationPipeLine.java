package org.pp.zookeeper.server.my;

import java.util.concurrent.BlockingQueue;

public class CommunicationPipeLine<S> implements PipeLine<S> {

    protected final BlockingQueue<S> sendqueue;
    protected final BlockingQueue<S> recvQueue;

    public CommunicationPipeLine(BlockingQueue<S> sendqueue, BlockingQueue<S> recvQueue) {
        this.sendqueue = sendqueue;
        this.recvQueue = recvQueue;
    }
}
