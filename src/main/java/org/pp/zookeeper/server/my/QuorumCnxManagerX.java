package org.pp.zookeeper.server.my;

import org.pp.zookeeper.server.my.bizmsg.ToSend;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuorumCnxManagerX extends RWBizCommunicationModel<ToSend> {


    /*********************************************** 底层通信模块 ***********************************************/
    /**
     * 连接管理器 组合 别用继承 非共同领域
     */
    private final SocketManager socketManager;
    /**
     * 发送队列
     */
    final ConcurrentHashMap<Long, LowerLayerSendWorker> senderWorkerMap;
    final ExecutorService lowerLayerSendService = Executors.newSingleThreadExecutor(); // 根据集群配置
    final ConcurrentHashMap<Long, BlockingQueue<ByteBuffer>> queueSendMap;
    /**
     * socket通信接收队列
     */
    final ConcurrentHashMap<Long, ByteBuffer> lastMessageSent;

    public QuorumCnxManagerX() {
        this.socketManager = new SocketManager();
        this.senderWorkerMap = new ConcurrentHashMap<>();
        this.queueSendMap = new ConcurrentHashMap<>();
        lastMessageSent = null;

        workerInit(new SenderTask(), new RecvTask());
    }

    class LowerLayerSendWorker implements Runnable {

        @Override
        public void run() {

        }
    }

    /**
     * 底层通信 空实现
     */
    protected void processConn(/*SocketManager socketManager*/) {
        LowerLayerSendWorker lowerLayerSendWorker = senderWorkerMap.get(1/*server id*/);
        lowerLayerSendService.execute(lowerLayerSendWorker);
    }

    /*********************************************** 与业务通信的模块 ***********************************************/
    class SenderTask implements Runnable {

        @Override
        public void run() {

        }
    }

    class RecvTask implements Runnable {

        @Override
        public void run() {
        }
    }
}
