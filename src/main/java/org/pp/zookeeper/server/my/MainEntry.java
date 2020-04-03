package org.pp.zookeeper.server.my;

import org.pp.zookeeper.server.my.bizmsg.Notification;
import org.pp.zookeeper.server.my.bizmsg.ToSend;

public class MainEntry {

    public static void main(String[] args) throws InterruptedException {
        // 一人一个发送线程、接收线程、发送队列、接收队列
        QuorumCnxManagerX<ToSend> qcm = createCnxnManager();
        LeaderElection<Notification> election = new LeaderElection(); // Notification 通信消息模型

        // 由于泛型限定的存在，消息通信需要进行消息接口转换
        Coordinator coordinator = new Coordinator(qcm, election);
        coordinator.start(); // 消息与通信协调

        qcm.processConn(); // 底层链接
        election.lookForLeader(); // 选举
    }

    public static QuorumCnxManagerX<ToSend> createCnxnManager() {
        return new QuorumCnxManagerX();
    }
}
