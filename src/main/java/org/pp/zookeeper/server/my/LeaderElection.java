package org.pp.zookeeper.server.my;

import org.apache.zookeeper.server.quorum.Election;
import org.apache.zookeeper.server.quorum.Vote;
import org.pp.zookeeper.server.my.bizmsg.IMessage;

public class LeaderElection<T extends IMessage> extends RWBizCommunicationModel<T> implements Election/*实现功能接口*/ {


    public LeaderElection() {
        workerInit(new SenderTask(), new RecvTask());
    }


    /*********************************************** 逻辑通信模块 ***********************************************/
    @Override
    public Vote lookForLeader() throws InterruptedException {
//        sendqueue.offer();
        recvQueue.poll();
        return null;
    }
    @Override
    public void shutdown() {
        // ...
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
