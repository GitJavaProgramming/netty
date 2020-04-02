package org.pp.zookeeper.server;

import org.apache.zookeeper.server.quorum.Election;
import org.apache.zookeeper.server.quorum.Vote;

/**
 * FastLeaderElection模型与QuorumCnxManager模型类似，sender receive 两个线程读写分离
 */
public class MyLeaderElection implements Election/*zookeeper Election interface*/ {

    private final MyQuorumCnxManager manager;
//    Messenger messenger;

    public MyLeaderElection(MyQuorumCnxManager manager) {
        this.manager = manager; // QuorumPeer::createCnxnManager()
    }

    @Override
    public Vote lookForLeader() throws InterruptedException {
         // election logic
        return null;
    }

    @Override
    public void shutdown() {
//        stop = true;
//        proposedLeader = -1;
//        proposedZxid = -1;
//        leadingVoteSet = null;
//        LOG.debug("Shutting down connection manager");
//        manager.halt();
//        LOG.debug("Shutting down messenger");
//        messenger.halt();
//        LOG.debug("FLE is down");
    }

    public void start() {
//        this.messenger.start();

        // send.start()
        // recv.start()
    }
}
