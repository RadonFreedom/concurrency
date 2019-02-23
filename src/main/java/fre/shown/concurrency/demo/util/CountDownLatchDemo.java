package fre.shown.concurrency.demo.util;

import fre.shown.concurrency.demo.Demo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * {@link java.util.concurrent.CountDownLatch} 的 Demo。<p/>
 * 闭锁的用法是直到所有所需任务完成之后，才放行调用闭锁阻塞方法的所有线程。
 *
 * @author Radon Freedom
 * created at 2019.02.23 19:19
 */

@Demo
public class CountDownLatchDemo {

    private static int TASK_NUM = 3;
    private static int TASK_TIME = 1000;

    @Test
    public void testCountDownLatch() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(TASK_NUM);
        final Thread[] workers = new Thread[TASK_NUM];
        final Thread[] proceedingWorkers = new Thread[TASK_NUM];
        Logger logger = LoggerFactory.getLogger(this.getClass());

        //第一批工作线程创建
        for (int i = 0; i < TASK_NUM; i++) {
            workers[i] = new Thread(() -> {
                try {
                    logger.info("第一批任务开始");
                    Thread.sleep(TASK_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                } finally {
                    //无论任务如何结束，都要递减闭锁
                    logger.info("任务结束");
                    latch.countDown();
                }
            });
        }

        //第二批工作线程创建
        for (int i = 0; i < TASK_NUM; i++) {
            proceedingWorkers[i] = new Thread(() -> {
                try {
                    logger.info("等待第一批任务完成");
                    latch.await();
                    logger.info("第二批任务开始");
                    Thread.sleep(TASK_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            });
        }

        for (int i = 0; i < TASK_NUM; i++) {
            workers[i].start();
        }
        for (int i = 0; i < TASK_NUM; i++) {
            proceedingWorkers[i].start();
        }

        //等待线程创建，开始和任务完成
        Thread.sleep(4000);
    }
}
