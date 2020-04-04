package org.pp.zookeeper.server.my;

import org.pp.zookeeper.server.my.msg.Notification;
import org.pp.zookeeper.server.my.msg.ToSend;

/**
 * 极简进程入口
 * 三大顶层接口：
 * IMessage 消息模型
 * CommunicationModel 通信模型
 * BaseCoordinator 模型协调者（通过消息接口转换）
 */
public class MainEntry {

    private static SocketManager socketManager;

    public static void main(String[] args) {
        // 一人一个发送线程、接收线程、发送队列、接收队列
        // 由于泛型限定的存在，消息通信需要进行消息接口转换
        Coordinator coordinator = new Coordinator(createCnxnManager(), createLeaderElection());
        coordinator.start(); // 消息与通信协调

    }

    private static QuorumCnxManagerX<ToSend, Notification> createCnxnManager() {
        return new QuorumCnxManagerX<ToSend, Notification>(getSingleton());
    }

    private static LeaderElection<Notification, ToSend> createLeaderElection() {
        return new LeaderElection<>(getSingleton());
    }

    private static synchronized SocketManager getSingleton() {
        if (socketManager == null) {
            socketManager = new SocketManager();
        }
        return socketManager;
    }
}
