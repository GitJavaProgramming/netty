package org.pp.zookeeper.server.my;

import org.pp.zookeeper.server.my.bizmsg.Notification;
import org.pp.zookeeper.server.my.bizmsg.ToSend;

public class MainEntry {

    public static void main(String[] args) throws InterruptedException {
        // 一人一个发送线程、接收线程、发送队列、接收队列
        // 由于泛型限定的存在，消息通信需要进行消息接口转换
        QuorumCnxManagerX qcm = createCnxnManager();
        ToSend msg = new ToSend();
        qcm.sendqueue.offer(msg);
//        qcm.recvQueue.poll();

        LeaderElection<Notification> election = new LeaderElection(); // Notification 通信消息模型
        Notification notification = election.recvQueue.poll(); // 需要消息转换
        election.lookForLeader();

        System.out.println(qcm.sendqueue.size());
        System.out.println(qcm.recvQueue.size());

        System.out.println(election.sendqueue.size());
        System.out.println(election.recvQueue.size());
    }

    public static QuorumCnxManagerX createCnxnManager() {
        return new QuorumCnxManagerX();
    }
}
