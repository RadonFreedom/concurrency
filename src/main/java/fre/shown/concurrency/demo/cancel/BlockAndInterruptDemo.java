package fre.shown.concurrency.demo.cancel;

import fre.shown.concurrency.demo.Demo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 示例线程的阻塞(block)和中断(interrupt)。
 * <p/>
 * 线程当获取其他资源时无法获取便阻塞，例如尝试获取一个锁。
 * 一个线程被阻塞通常意味着被挂起，并被设置为某个状态。
 * 只有能抛出{@link InterruptedException}的方法才会被阻塞。<tr/>
 * 线程A可以被线程B通过引用线程A的引用调用{@link Thread#interrupt() A.interrupt()}来中断线程A。
 *
 * @author Radon Freedom
 * created at 2019.02.21 12:27
 */

@Demo
public class BlockAndInterruptDemo {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 用{@link Thread#sleep(long)}方法来测试阻塞与中断
     */
    @Test
    public void testBlockAndInterruptWithSleep() throws InterruptedException {

        Thread t1 = new Thread(() -> {
            try {
                logger.debug("开始阻塞");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.debug("线程" + Thread.currentThread().getName() + "已经被中断");
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }, "t1");

        t1.start();
        logger.debug("等待" + t1.getName() + "启动");
        Thread.sleep(10);
        logger.debug("即将中断线程" + t1.getName());
        t1.interrupt();
    }
}
