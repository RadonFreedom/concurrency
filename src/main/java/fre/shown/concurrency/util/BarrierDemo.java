package fre.shown.concurrency.util;

import fre.shown.concurrency.Demo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 *
 * {@link CyclicBarrier}控制一组线程，在初始化时指定线程数量。
 * 当所有线程都到达指定代码点{@link CyclicBarrier#await()}处时释放所有线程。
 *
 * @author Radon Freedom
 * created at 2019.02.23 20:28
 */

@Demo
public class BarrierDemo {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final int TASK_NUM = 5;


    @Test
    public void testBarrier() throws InterruptedException {
        final CyclicBarrier barrier = new CyclicBarrier(TASK_NUM);
        Thread[] threads = new Thread[TASK_NUM];
        for (int i = 0; i < TASK_NUM; i++) {
            threads[i] = createThread(i * 100, barrier);
        }
        for (int i = 0; i < TASK_NUM; i++) {
            threads[i].start();
        }

        Thread.sleep(3000);
    }

    private Thread createThread(final int time, final CyclicBarrier barrier) {
        return new Thread(() -> {
            try {
                Thread.sleep(time);
                logger.info(Thread.currentThread().getName() + "执行完毕");
                barrier.await();
                logger.info(Thread.currentThread().getName() + "线程已被释放");
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } catch (BrokenBarrierException e) {
                logger.info("栅栏已破坏！");
                e.printStackTrace();
            }
        });
    }
}
