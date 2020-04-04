package org.pp.zookeeper.server.my;

import org.apache.zookeeper.server.quorum.Election;
import org.apache.zookeeper.server.quorum.Vote;
import org.pp.zookeeper.server.my.msg.IMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class LeaderElection<S extends IMessage, T extends IMessage> extends RWBizCommunicationModel<S, T> implements Election/*实现功能接口*/ {

    private final SocketManager socketManager; // 可以抽象为父类成员，不过已经不是解耦了

    public LeaderElection(SocketManager socketManager) {
        // 连接管理器  根据需要可以抽象为通道
        this.socketManager = socketManager;

        // 初始化通信通道  MainEntry
        BlockingQueue sendqueue = new LinkedBlockingQueue<>();
        BlockingQueue recvQueue = new LinkedBlockingQueue<>();
        communicationPipeLine = new CommunicationPipeLine(sendqueue, recvQueue);
    }


    /*********************************************** 逻辑业务模块 ***********************************************/
    @Override
    public Vote lookForLeader() throws InterruptedException {
        try {
            TimeUnit.SECONDS.sleep(5);
        } finally {
            shutdown();
        }
        return null;
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }

    public void schedule(PipeLine pipeLine) {
        workerInit(new SenderTask(pipeLine), new RecvTask(pipeLine));
        super.schedule(pipeLine);
//        buildMsg();
        try {
            // 在开始选举的准备工作完成之前需要阻塞
            lookForLeader(/*最小关联参数*/);  // 选举leader
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

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
