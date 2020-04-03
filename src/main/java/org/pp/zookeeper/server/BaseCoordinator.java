package org.pp.zookeeper.server;

import org.pp.zookeeper.server.my.RWBizCommunicationModel;
import org.pp.zookeeper.server.my.bizmsg.IMessage;

public abstract class BaseCoordinator<S extends IMessage, T extends IMessage> {

    /**
     * 这两个通信模型还可以继续抽象分离为两个不同接口模型至此就会完全解耦
     */
    private final RWBizCommunicationModel<S> qcm;
    private final RWBizCommunicationModel<T> election;

    public BaseCoordinator(RWBizCommunicationModel<S> qcm, RWBizCommunicationModel<T> election) {
        this.qcm = qcm;
        this.election = election;
    }
}
