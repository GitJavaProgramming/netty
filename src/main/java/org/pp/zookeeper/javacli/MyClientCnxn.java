package org.pp.zookeeper.javacli;


import org.apache.zookeeper.Watcher;

import java.util.concurrent.LinkedBlockingQueue;

public class MyClientCnxn {

    private final String connectConfig; // 外部传入，构造注入
    private final MySendThread sendThread;
    private final MyEventThread eventThread;

    // Watcher registed in Packet
//    private final LinkedBlockingDeque<ClientCnxn.Packet> outgoingQueue = new LinkedBlockingDeque<ClientCnxn.Packet>();

    private final Watcher watcher; // use zookeeper watcher interface

    public MyClientCnxn(String connectConfig, Watcher watcher) {
        this.connectConfig = connectConfig;
        this.watcher = watcher;
        sendThread = new MySendThread();
        eventThread = new MyEventThread();
    }

    class MySendThread extends MyZooKeeperThread {
        // nio or netty connect
        // ClientCnxnSocketNetty extends ClientCnxnSocket 异步ChannelFutureListener监听 构造Packet put into outgoingQueue
        // 创建nio和netty的SocketChannel参考本例
        // socket连接建立后KeeperState会变化
//        KeeperState eventState = (isRO) ? KeeperState.ConnectedReadOnly : KeeperState.SyncConnected;

//        private /*final*/ ClientCnxnSocket clientCnxnSocket;

        @Override
        public void run() {
            System.out.println("sendThread async notify.");
        }
    }

    class MyEventThread extends MyZooKeeperThread {
        // event queue
        private final LinkedBlockingQueue<Object> waitingEvents = new LinkedBlockingQueue(); // take() blocks
        private volatile Watcher.Event.KeeperState sessionState = Watcher.Event.KeeperState.Disconnected;

        @Override
        public void run() {
            System.out.println("collect event and consume.");
            // Watcher call process(WatchedEvent event);
        }
    }

    protected void start() {
        sendThread.start();
        eventThread.start();
    }

}
