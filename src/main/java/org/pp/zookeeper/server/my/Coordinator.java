package org.pp.zookeeper.server.my;

import org.pp.zookeeper.server.my.msg.Notification;
import org.pp.zookeeper.server.my.msg.ToSend;

/**
 * 协调者 1.协调通信层与逻辑层 2.协调网络底层与通信层
 */
public class Coordinator extends BaseCoordinator<ToSend, Notification> {

    private final QuorumCnxManagerX<ToSend, Notification> qcm;
    private final LeaderElection<Notification, ToSend> election;

    public Coordinator(QuorumCnxManagerX<ToSend, Notification> qcm, LeaderElection<Notification, ToSend> election) {
        super(qcm, election); // 向父类传入数据，在父类可以做一些初始化工作
        this.qcm = qcm;
        this.election = election;
    }

    public void start() {
        // 模型调度
        qcm.schedule(election.communicationPipeLine/*, socketPipeLine*/);
        election.schedule(qcm.communicationPipeLine/*, socketPipeLine*/);
    }
}
