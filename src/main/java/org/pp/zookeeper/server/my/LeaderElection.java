package org.pp.zookeeper.server.my;

import org.apache.zookeeper.server.quorum.Election;
import org.apache.zookeeper.server.quorum.Vote;
import org.pp.zookeeper.server.my.msg.IMessage;

public class LeaderElection<T extends IMessage> extends RWBizCommunicationModel<T> implements Election/*实现功能接口*/ {

    private IMessage minMessageFromQuorumCnxManager;

    public LeaderElection() {
        workerInit(new SenderTask(), new RecvTask());
    }


    /*********************************************** 逻辑业务模块 ***********************************************/
    @Override
    public Vote lookForLeader() throws InterruptedException {
//        use minMessageFromQuorumCnxManager
        recvQueue.poll();
        return null;
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }

    public void schedule(T msg, IMessage minMessageFromQuorumCnxManager) {
        super.schedule(msg);
        this.minMessageFromQuorumCnxManager = minMessageFromQuorumCnxManager;

        try {
            // 在开始选举的准备工作完成之前需要阻塞
            lookForLeader(/*最小关联参数*/);  // 选举leader
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
