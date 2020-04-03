package org.pp.zookeeper.server.my;

import org.pp.zookeeper.server.my.bizmsg.Notification;
import org.pp.zookeeper.server.my.bizmsg.ToSend;

/**
 * 协调者 协调两个模型层
 */
public class Coordinator {

    private final LeaderElection<Notification> election;
    private final QuorumCnxManagerX<ToSend> qcm;

    public Coordinator(QuorumCnxManagerX<ToSend> qcm, LeaderElection<Notification> election) {
        this.qcm = qcm;
        this.election = election;
    }

    public void start() {
        ToSend msg = new ToSend();
        qcm.sendqueue.offer(msg);
        ToSend send = qcm.recvQueue.poll();

        Notification notification = election.recvQueue.poll(); // 需要消息转换
    }
}
