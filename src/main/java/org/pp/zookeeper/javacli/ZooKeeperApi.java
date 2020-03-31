package org.pp.zookeeper.javacli;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 底层socket连接
 * 客户端
 * 启动服务器，测试连接
 * <p>
 * [root@localhost ~]# jps // 查看java进程
 * 1973 QuorumPeerMain  // 服务器端进程
 * 2028 Jps
 */
public class ZooKeeperApi {
    private static final int N = 1;
    private static final CountDownLatch latch = new CountDownLatch(N);

    public static void main(String[] args) throws IOException {
        /**
         * 测试：如果服务器连不上 该进程一直处于阻塞中？？
         * ClientCnxn字段构造时，初始化后台线程 sendThread、eventThread 它们的公共父类 ZooKeeperThread
         *
         * public void start() {
         *     sendThread.start();
         *     eventThread.start();
         * }
         */
        ZooKeeper zooKeeper = new ZooKeeper("192.168.0.132:2181", 5000, new WatcherThread(latch));
        System.out.println("zooKeeper client State: " + zooKeeper.getState());
        try {
            latch.await(); // 等待latch计数递减
        } catch (InterruptedException e) {
//            e.printStackTrace();
            System.out.println("zooKeeper 中断...");
        }
        System.out.println("zooKeeper 连接建立.");
    }
}

/**
 * zookeeper状态更改或者节点事件变化时通知的监视程序
 * KeeperState
 * EventType
 */
class WatcherThread /*extends Thread */ implements Watcher {

    private final CountDownLatch latch;

    WatcherThread(CountDownLatch latch) {
        this.latch = latch;
    }

    public void process(WatchedEvent event) {
        System.out.println("WatchedEvent : " + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            latch.countDown();
        } else {
            System.out.println(event.getState());
        }
    }
}
