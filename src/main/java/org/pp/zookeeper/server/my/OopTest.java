package org.pp.zookeeper.server.my;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 继承 测试属性继承
 */
public class OopTest {
    public static void main(String[] args) {
        // 多态，动态方法调用
        SuperCls<Integer> subA = new SubA();
        SuperCls<String> subB = new SubB();
        subA.test();
        subB.test();
        // 继承，非私有属性和方法继承，两个对象具有独立的数据空间
        System.out.println(subA.superQueue);
        System.out.println(subB.superQueue);
        System.out.println(subA.sendService == subB.sendService);
        System.out.println(SuperCls.aInt);
    }
}

abstract class SuperCls<T> {
    protected final BlockingQueue<T> superQueue;/*实例变量, 继承*/
    protected final ExecutorService sendService = Executors.newSingleThreadExecutor();
    protected static int aInt = 0; // 类变量--对象共享
    private static final int bInt = 0;  // 常量

    public SuperCls() {
        superQueue = new LinkedBlockingQueue<>();
    }

    abstract void test();
}

class SubA extends SuperCls<Integer> {
    public SubA() {
//        superQueue = new LinkedBlockingQueue<>();
    }

    public void test() {
        superQueue.offer(1);
        aInt++;
//        bInt++;
    }
}

class SubB extends SuperCls<String> {
    public SubB() {
//        superQueue = new LinkedBlockingQueue<>();
    }

    public void test() {
        aInt++;
        superQueue.offer("nihao");
        superQueue.offer("nihao");
    }
}