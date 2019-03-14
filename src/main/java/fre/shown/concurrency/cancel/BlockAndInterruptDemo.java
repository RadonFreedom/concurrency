package fre.shown.concurrency.cancel;

import fre.shown.concurrency.Demo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 示例线程的阻塞(block)和中断(interrupt)。
 * <p/>
 * 线程当获取其他资源时无法获取便阻塞，按照是否可以被中断，阻塞分为可中断阻塞与不可中断阻塞。<TR/>
 * 只有能抛出{@link InterruptedException}的阻塞方法才可以被中断，
 * 这些方法会在被执行时不断检查当前线程的中断标志，
 * 如果为true，将抛出{@link InterruptedException}并将中断标志设置为false。<tr/>
 * 线程A可以被线程B调用{@link Thread#interrupt() A.interrupt()}来设置线程A的中断标志为true，
 * A是否真正意义上被“中断”，还需要看线程A栈顶的方法能否抛出{@link InterruptedException}。
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
                //重新设置当前线程的中断标志
                Thread.currentThread().interrupt();
            }
        }, "t1");

        t1.start();
        logger.debug("等待" + t1.getName() + "启动");
        Thread.sleep(10);
        logger.debug("即将中断线程" + t1.getName());
        //单纯的设置t1线程的中断标志
        t1.interrupt();
    }
}
