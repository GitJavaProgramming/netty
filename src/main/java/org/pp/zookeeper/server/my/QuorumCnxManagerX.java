package org.pp.zookeeper.server.my;

import org.pp.zookeeper.server.my.msg.IMessage;
import org.pp.zookeeper.server.my.msg.Notification;
import org.pp.zookeeper.server.my.msg.ToSend;

import java.util.concurrent.BlockingQueue;

public class QuorumCnxManagerX<T extends IMessage> extends RWBizCommunicationModel<T> {

    /**
     * 网络底层连接，这里还可以抽象，不过已经没有必要了，这里是一定和底层socket耦合的
     */
    private final SocketManager socketManager;

    public QuorumCnxManagerX() {
        workerInit(new SenderTask(), new RecvTask());
        socketManager = new SocketManager();
    }

    /**
     * 底层通信
     */
    protected void processConn(Message min/*最小关联*/) {
        socketManager.process(min);
    }

    static class Message implements IMessage {
        long sid;
        // 其他必须数据
    }

    public void schedule(BlockingQueue sendqueue, BlockingQueue recvQueue) {
        super.schedule(sendqueue, recvQueue);
//        processConn(buildMsg(null, null)); // 底层链接
    }

    /**
     * build message  用于建立socket管理
     */
    public QuorumCnxManagerX.Message buildMsg(ToSend msg, Notification notification) {
        // ToSend Notification  ->  Message
        return new QuorumCnxManagerX.Message();
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
