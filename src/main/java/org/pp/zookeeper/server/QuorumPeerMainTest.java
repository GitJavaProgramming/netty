package org.pp.zookeeper.server;

import java.util.concurrent.TimeUnit;

public class QuorumPeerMainTest {
    public static void main(String[] args) throws InterruptedException {
//        test01(args);
        test02();
    }

    public static void test02() {
        Thread thread = new StartThread();
        System.out.println("1 - " + thread.getState());

        // 子类重写start() 注释 super.start();
        thread.start(); // 同步方法 lock thread
        System.out.println("2 - " + thread.getState()); // NEW
    }

    public static void test01(String[] args) throws InterruptedException {
        ZooKeeperServerMainTest.main(args); // 静态方法调用
//        TimeUnit.SECONDS.sleep(1);
        System.out.println("QuorumPeerMainTest");
    }
}

class StartThread extends Thread {

    @Override
    public synchronized void start() {
        System.out.println("this state: " + getState());
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        super.start(); // 注释掉 调用后，线程状态为NEW run() 没有运行
    }

    @Override
    public void run() {
        System.out.println("run called.");
    }
}