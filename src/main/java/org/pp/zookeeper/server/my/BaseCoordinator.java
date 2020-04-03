package org.pp.zookeeper.server.my;

import org.pp.zookeeper.server.my.msg.IMessage;

public abstract class BaseCoordinator<S extends IMessage, T extends IMessage> {

    /**
     * 这两个通信模型还可以继续抽象分离为两个不同接口模型至此就会完全解耦
     */
    private final CommunicationModel<S, T> qcm;
    private final CommunicationModel<T, S> election;

    public BaseCoordinator(CommunicationModel<S, T> qcm, CommunicationModel<T, S> election) {
        this.qcm = qcm;
        this.election = election;
    }

    protected abstract void start();
}
