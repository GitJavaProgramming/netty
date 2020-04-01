package org.pp.zookeeper.server;

public class QuorumPeerMainTest {
    public static void main(String[] args) throws InterruptedException {
        ZooKeeperServerMainTest.main(args); // 静态方法调用
//        TimeUnit.SECONDS.sleep(1);
        System.out.println("QuorumPeerMainTest");
    }
}
