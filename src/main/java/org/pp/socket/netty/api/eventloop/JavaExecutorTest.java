package org.pp.socket.netty.api.eventloop;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class JavaExecutorTest {

    //    static ExecutorService service;
    private static int count = 0;

    public static void main(String[] args) {
        testExecutorService();
    }

    public static void testExecutorService() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
        ScheduledFuture future = null;
        LocalTime localTime = LocalTime.now();
        System.out.println("test start local time = " + localTime);
//        future = service.schedule(JavaExecutorTest::runTask, 3, TimeUnit.SECONDS); // 1

        /**
         * 线程在规定时间内执行完时1、4、7秒钟执行
         *
         * 10:33:31.299
         * 10:33:32.314
         * localTime2 = 10:33:36
         * 10:33:36
         * localTime2 = 10:33:40
         *
         * 加上count,执行任务时长超过间隔时间时 应该如何调度（阻塞）？？
         * test start local time = 11:16:46.375
         * count=1
         * runTask start local time : 11:16:47.391
         * runTask end local time : 11:16:51
         * count=2
         * runTask start local time : 11:16:51
         * runTask end local time : 11:16:55
         */
        future = service.scheduleAtFixedRate(JavaExecutorTest::runTask, 1/*延迟启动,初始延迟时间,首次执行*/, 3/*执行周期*/, TimeUnit.SECONDS);


        /**
         *  其中的一次执行结果,最后程序卡在那里不能退出了！！  另外localTime2小数点后面的数字不见了 参考toString() now()方法
         * 10:17:51.335
         * 10:17:52.347
         * localTime2 = 10:17:56
         * 10:17:59
         *
         * 第二次运行正常退出
         * 10:20:25.611
         * 10:20:26.634
         * localTime2 = 10:20:30
         * 10:20:33.001
         * localTime2 = 10:20:37
         */
//        service.scheduleWithFixedDelay(JavaExecutorTest::runTask, 1/*延迟启动,初始延迟时间,首次执行*/, 3/*每次任务（终止和开始）之间的间隔*/, TimeUnit.SECONDS);
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.shutdown();
    }

//    public static void runTask() {
//        System.out.println("runnable 3 seconds...");
//    }

//    public static <T> T runTask() {
//        T t = null;
//        System.out.println("callable 3 seconds...");
//        return t;
//    }

    public static String runTask() {
        System.out.println("count=" + (++count));

//        System.out.println(Thread.currentThread().getName() + " - callable 3 seconds..." /*+ System*/);

//        try {
//            Thread.sleep(4000); // 长耗时任务...
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // 长耗时任务2 程序在运行...
        LocalTime localTime = LocalTime.now();
        LocalTime localTime2 = LocalTime.now();
        System.out.println("runTask start local time : " + localTime);
        while (localTime2.getSecond() - localTime.getSecond() < 4) {
            localTime2 = LocalTime.now();
        }
        System.out.println("runTask end local time : " + localTime2);
        return "hello world";
    }
}
