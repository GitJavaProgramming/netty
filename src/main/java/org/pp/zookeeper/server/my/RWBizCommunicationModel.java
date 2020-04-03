package org.pp.zookeeper.server.my;

import org.pp.zookeeper.server.my.msg.IMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 读写分离通信模型
 *
 * 抽象公共的业务通信层
 */
public abstract class RWBizCommunicationModel<S extends IMessage, T extends IMessage> implements CommunicationModel<S, T> {

    /***************************** 业务通信模块 与业务通信相关的接收队列 子类共用 ****************************/
    /** 发送线程 */
    protected final ExecutorService sendService = Executors.newSingleThreadExecutor();
    /** 接收线程 */
    protected final ExecutorService recvService = Executors.newSingleThreadExecutor();
    /** 发送队列 */
    protected final BlockingQueue<S> sendqueue;
    /** 接收队列 */
    protected final BlockingQueue<S> recvQueue;
    /** 发送队列 */
    protected BlockingQueue<T> sendqueueRelative;
    /** 接收队列 */
    protected BlockingQueue<T> recvQueueRelative;

    protected/*只被继承*/ RWBizCommunicationModel() {
        sendqueue = new LinkedBlockingQueue<>();
        recvQueue = new LinkedBlockingQueue<>();
    }

    private SenderWorker senderWorker;
    private RecvWorker recvWorker;
    public void schedule(BlockingQueue<T> sendqueue, BlockingQueue<T> recvQueue) {
        this.sendqueueRelative = sendqueue;
        this.recvQueueRelative = recvQueue;
        sendService.execute(senderWorker); // 任务交给子类实例化 setter
        recvService.execute(recvWorker);
    }
    /** 以下setter子类必须实现，注入属性 */
    protected void workerInit(SenderWorker senderWorker, RecvWorker recvWorker) {
        this.senderWorker = senderWorker;
        this.recvWorker = recvWorker;
    }

    protected abstract class SenderWorker implements Runnable {
        public SenderWorker(BlockingQueue<T> recvQueue) {
            recvQueueRelative = recvQueue;
        }
    }

    protected abstract class RecvWorker implements Runnable {
        protected RecvWorker(BlockingQueue<T> sendqueue) {
            sendqueueRelative = sendqueue;
        }
    }

    /** 资源回收 */
    public void shutdown() {
        this.sendService.shutdown();
        this.recvService.shutdown();
        this.sendqueue.clear();
        this.recvQueue.clear();
        this.sendqueueRelative.clear();
        this.recvQueueRelative.clear();
    }
}
