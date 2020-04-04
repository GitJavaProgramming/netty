package org.pp.zookeeper.server.my;

import org.pp.zookeeper.server.my.msg.IMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QuorumCnxManagerX<S extends IMessage, T extends IMessage> extends RWBizCommunicationModel<S, T> {

    private final SocketManager socketManager; // 可以抽象为父类成员，不过已经不是解耦了

    public QuorumCnxManagerX(SocketManager socketManager) {
        // 连接管理器  根据需要可以抽象为通道
        this.socketManager = socketManager;

        // // 初始化通信通道  MainEntry
        BlockingQueue recvQueue = new LinkedBlockingQueue<>();
        communicationPipeLine = new CommunicationPipeLine(null/*zookeeper没用到*/, recvQueue);
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

    public void schedule(PipeLine pipeLine) {
        workerInit(new SenderTask(pipeLine), new RecvTask(pipeLine));
        super.schedule(pipeLine);
//        processConn(buildMsg(null, null)); // 底层链接
    }

//    /**
//     * build message  用于建立socket管理
//     */
//    public QuorumCnxManagerX.Message buildMsg(ToSend msg, Notification notification) {
//        // ToSend Notification  ->  Message
//        return new QuorumCnxManagerX.Message();
//    }

    /*********************************************** 与业务通信的模块 ***********************************************/
    class SenderTask extends SenderWorker {

        public SenderTask(PipeLine pipeLine) {
            super(pipeLine);
        }

        @Override
        public void run() {
//            communicationPipeLine
        }
    }

    class RecvTask extends RecvWorker {

        public RecvTask(PipeLine pipeLine) {
            super(pipeLine);
        }

        @Override
        public void run() {
//            sendqueueRelative.offer();
        }
    }
}
