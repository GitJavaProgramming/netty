package org.pp.zookeeper.server.my;

import org.pp.zookeeper.server.my.bizmsg.IMessage;
import org.pp.zookeeper.server.my.bizmsg.ToSend;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 读写分离通信模型
 *   底层通信
 *   业务通信
 *
 * 抽象公共的业务通信层
 */
public abstract class RWBizCommunicationModel<T extends IMessage> implements CommunicationModel<T> {

    /***************************** 业务通信模块 与业务通信相关的接收队列 子类共用 ****************************/
    /**
     * 连接管理器
     */
//    private final SocketManager socketManager;

    /** 发送线程 */
    protected final ExecutorService sendService = Executors.newSingleThreadExecutor();
    /** 接收线程 */
    protected final ExecutorService recvService = Executors.newSingleThreadExecutor();
    /** 发送队列 */
    protected final LinkedBlockingQueue<T> sendqueue;
    /** 接收队列 */
    protected final BlockingQueue<T> recvQueue;

    protected/*只被继承*/ RWBizCommunicationModel() {
        sendqueue = new LinkedBlockingQueue<>();
        recvQueue = new LinkedBlockingQueue<>();
//        socketManager = null;
    }

//    protected abstract class SenderWorker implements Runnable {}
//    protected abstract class  RecvWorker implements Runnable {}

    /**
     * 与业务模型通信
     */
//    protected abstract void schedule(T msg);
//    /** 底层通信 空实现 */
//    protected void processConn(/*SocketManager socketManager*/) {}

    private Runnable senderWorker;
    private Runnable recvWorker;
    protected void schedule(ToSend msg) {
        sendService.execute(senderWorker); // 任务交给子类实例化 setter
        recvService.execute(recvWorker);
    }
    /** 以下setter子类必须实现，注入属性 */
    public void workerInit(Runnable senderWorker, Runnable recvWorker) {
        this.senderWorker = senderWorker;
        this.recvWorker = recvWorker;
    }

    /** 资源回收 */
    public void shutdown() {
        this.sendService.shutdown();
        this.recvService.shutdown();
        this.sendqueue.clear();
        this.recvQueue.clear();
    }
}
