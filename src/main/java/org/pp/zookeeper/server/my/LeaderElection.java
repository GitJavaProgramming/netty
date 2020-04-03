package org.pp.zookeeper.server.my;

import org.apache.zookeeper.server.quorum.Election;
import org.apache.zookeeper.server.quorum.Vote;
import org.pp.zookeeper.server.my.bizmsg.IMessage;

import java.util.concurrent.LinkedBlockingQueue;

public class LeaderElection<T extends IMessage> extends RWBizCommunicationModel<T> implements Election {

    /**
     * 发送队列
     */
    private final LinkedBlockingQueue<T> sendqueue;

    public LeaderElection() {
        sendqueue = null;
        workerInit(new SenderTask(), new RecvTask());
    }


    /*********************************************** 逻辑通信模块 ***********************************************/
    @Override
    public Vote lookForLeader() throws InterruptedException {
        recvQueue.poll();
        return null;
    }
    @Override
    public void shutdown() {
        // ...
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
