package org.pp.zookeeper.server.my;

import org.pp.zookeeper.server.my.bizmsg.ToSend;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class QuorumCnxManagerX extends RWBizCommunicationModel<ToSend> {


    /*********************************************** 底层通信模块 ***********************************************/
    /**
     * 连接管理器
     */
    private final SocketManager socketManager;
    /**
     * 发送队列
     */
    final ConcurrentHashMap<Long, SenderWorker> senderWorkerMap;
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

    /**
     * 底层通信 空实现
     */
    protected void processConn(/*SocketManager socketManager*/) {
    }

    /*********************************************** 与业务通信的模块 ***********************************************/
    class SenderTask extends SenderWorker {

        @Override
        public void run() {

        }
    }
    class RecvTask extends RecvWorker {

        @Override
        public void run() {
        }
    }
}
