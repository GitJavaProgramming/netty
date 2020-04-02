package org.pp.zookeeper.server;

public class ServerMainEntry {
    public static void main(String[] args) {
        MyQuorumCnxManager qcm = createCnxnManager();
        new MyLeaderElection(qcm).start();
    }

    public static MyQuorumCnxManager createCnxnManager() {
        return new MyQuorumCnxManager();
    }
}
