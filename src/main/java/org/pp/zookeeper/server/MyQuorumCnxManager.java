package org.pp.zookeeper.server;

import org.apache.zookeeper.util.CircularBlockingQueue;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class MyQuorumCnxManager {

    final ConcurrentHashMap<Long, SendWorker> senderWorkerMap;
    final ConcurrentHashMap<Long, BlockingQueue<ByteBuffer>> queueSendMap; // 发送给每个服务器的缓冲队列
    public final BlockingQueue<Message> recvQueue;  // 本地接收队列

    /*
     * Maximum capacity of thread queues
     */
    static final int RECV_CAPACITY = 100;

    public MyQuorumCnxManager() {
        this.senderWorkerMap = new ConcurrentHashMap<>();
        this.queueSendMap = new ConcurrentHashMap<>();
        this.recvQueue = new CircularBlockingQueue<>(RECV_CAPACITY); // use zookeeper class
    }

    /**
     * 消息
     * 唯一构造调用： addToRecvQueue(new Message(ByteBuffer.wrap(msgArray), sid));
     * 在两处：send recv
     *     QuorumCnxManager.toSend()
     *     QuorumCnxManager.RecvWorker.run()
     */
    public static class Message/*系统间通信--消息传递，封装server ID*/ {
        Message(ByteBuffer buffer, long sid) {
            this.buffer = buffer;
            this.sid = sid;
        }
        ByteBuffer buffer;
        long sid;
    }

    public static class SendWorker/* 发送线程 use queueSendMap */ extends Thread{} // BlockingQueue<ByteBuffer> bq = queueSendMap.get(sid);
    public static class RecvWorker/* 接收线程 use recvQueue */ extends Thread{} // addToRecvQueue(new Message(ByteBuffer.wrap(msgArray), sid));

    public void connectOne() {}
    public void connectAll() {}
    public void processConnection() {} // use sendWorker recvWorker
    public void closeSocket() {}
    // ...
}
