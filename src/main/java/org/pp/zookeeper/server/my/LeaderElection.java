package org.pp.zookeeper.server.my;

import org.apache.zookeeper.server.quorum.Election;
import org.apache.zookeeper.server.quorum.Vote;
import org.pp.zookeeper.server.my.msg.IMessage;

import java.util.concurrent.BlockingQueue;

public class LeaderElection<S extends IMessage, T extends IMessage> extends RWBizCommunicationModel<S, T> implements Election/*实现功能接口*/ {

    public LeaderElection() {
    }


    /*********************************************** 逻辑业务模块 ***********************************************/
    @Override
    public Vote lookForLeader() throws InterruptedException {
        return null;
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }

    public void schedule(BlockingQueue<T> sendqueue, BlockingQueue<T> recvQueue) {
        workerInit(new SenderTask(recvQueue), new RecvTask(sendqueue));
        super.schedule(sendqueue, recvQueue);
//        buildMsg();
        try {
            // 在开始选举的准备工作完成之前需要阻塞
            lookForLeader(/*最小关联参数*/);  // 选举leader
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * build message  用于模型层间通信
//     */
//    public QuorumCnxManagerX.Message buildMsg(ToSend msg, Notification notification) {
//        // ToSend Notification  ->  Message
//        return new QuorumCnxManagerX.Message();
//    }


    /*********************************************** 与业务通信的模块 ***********************************************/
    class SenderTask extends SenderWorker {

        public SenderTask(BlockingQueue<T> recvQueue) {
            super(recvQueue);
        }

        @Override
        public void run() {
            recvQueueRelative.poll();
        }
    }

    class RecvTask extends RecvWorker {

        protected RecvTask(BlockingQueue<T> sendqueue) {
            super(sendqueue);
        }

        @Override
        public void run() {
//            sendqueueRelative.offer();
        }
    }
}
