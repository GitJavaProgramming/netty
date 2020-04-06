package org.pp.zookeeper.server.my;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketManager {
    private Socket socket;

    /**
     * 发送队列
     */
    final ConcurrentHashMap<Long, LowerLayerSendWorker> senderWorkerMap;
    final ExecutorService lowerLayerSendService; // 根据集群配置
    final ConcurrentHashMap<Long, BlockingQueue<ByteBuffer>> queueSendMap;
    /**
     * socket通信接收队列
     */
    final ConcurrentHashMap<Long, ByteBuffer> lastMessageSent;

    volatile boolean shutdown = false;

    public SocketManager() {
        this.senderWorkerMap = new ConcurrentHashMap<>();
        this.lowerLayerSendService = Executors.newSingleThreadExecutor(); // 根据集群配置
        this.queueSendMap = new ConcurrentHashMap<>();
        lastMessageSent = null;
    }

    public void process(QuorumCnxManagerX.Message min/*传入最小关联参数*/) {
        long sid = min.sid;
        // 查找queueMap
        // offer消息
        // build socket connect
        LowerLayerSendWorker lowerLayerSendWorker = senderWorkerMap.get(sid/*server id*/);
        if (lowerLayerSendWorker == null) {
            lowerLayerSendWorker = new LowerLayerSendWorker();
            senderWorkerMap.put(sid, lowerLayerSendWorker);
        }
//        BlockingQueue<ByteBuffer> bq = queueSendMap.computeIfAbsent(sid, serverId -> new CircularBlockingQueue<>(SEND_CAPACITY));
//        addToSendQueue(bq, b);
//        connectOne(sid);
    }

    class LowerLayerSendWorker implements Runnable {

        @Override
        public void run() {

        }
    }

    /* 选举服务器 */
    class LeaderElectionServer extends Thread {

        @Override
        public void run() {
            // ...
        }
    }

    /**
     * 监听，等待所有连接都建立，都是使用CountDownLatch来处理。例如：Leader.run()
     * -- ExecutorService executor = Executors.newFixedThreadPool(serverSockets.size());
     * -- CountDownLatch latch = new CountDownLatch(serverSockets.size());
     * -- serverSockets.forEach(serverSocket -> executor.submit(new LearnerCnxAcceptorHandler(serverSocket, latch)));
     * -- latch.await(); // 等待计数器为0
     */
    public void listen() {
        new LeaderElectionServer().start();
    }

}
