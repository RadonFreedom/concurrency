package fre.shown.concurrency.util;

import fre.shown.concurrency.Demo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@link ReentrantLock}使用DEMO
 * <p/>
 * 一定要在finally中释放锁：{@link Lock#unlock()}
 *
 * @author Radon Freedom
 * created at 2019.02.25 9:35
 */


@Demo
public class ReentrantLockDemo {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Lock lock;
    private final Object internalLock = new Object();

    /**
     * 使用{@link Lock#tryLock()}来轮询锁
     */
    @Test
    public void iterateLock() {
        lock = new ReentrantLock();
        while (true) {
            logger.info("尝试获取锁");
            if (lock.tryLock()) {
                try {
                    logger.info("任务已完成");
                    break;
                } finally {
                    logger.info("释放锁");
                    lock.unlock();
                }
            }
        }
    }

    /**
     * 在{@link #iterateLock()}的基础上增加了时限
     */
    @Test
    public void timeLimitLock() {
        lock = new ReentrantLock();
        final long timeLimitMillis = 100;
        long endTime = System.currentTimeMillis() + timeLimitMillis;
        while (true) {
            logger.info("尝试获取锁");
            if (lock.tryLock()) {
                try {
                    logger.info("任务已完成");
                    break;
                } finally {
                    logger.info("释放锁");
                    lock.unlock();
                }
            }
            if (endTime < System.currentTimeMillis()) {
                logger.warn("获取锁超时！");
                break;
            }
        }
    }

    /**
     * 调用{@link Lock#lockInterruptibly()}获取锁在阻塞时可以被中断。
     *
     * @throws InterruptedException 如果当前线程在获取锁时被中断
     */
    @Test
    public void interruptLock() throws InterruptedException {

        lock = new ReentrantLock();

        Thread t = new Thread(() -> {
            logger.info(Thread.currentThread().getName() + "可中断地获取锁");
            try {
                lock.lockInterruptibly();
                try {
                    logger.info(Thread.currentThread().getName() + "已获取可中断锁，线程未被中断，继续工作");
                } finally {
                    logger.info("释放锁");
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }, "t");

        logger.debug(Thread.currentThread().getName() + "获取锁");
        lock.lock();
        logger.debug("启动线程" + t.getName());
        t.start();
        //等待线程t启动
        Thread.sleep(100);
        logger.debug("尝试在线程阻塞时中断线程" + t.getName());
        t.interrupt();
        Thread.sleep(500);
    }

    /**
     * 同{@link #interruptLock()}方法对比就可以得出，内置锁阻塞时无法被中断，
     * 只有能抛出{@link InterruptedException}的方法才能够被中断。
     * 在执行到这些方法时，会检查当前线程的中断标志，如果为true，
     * 将抛出{@link InterruptedException}，并将当前线程的中断标志设置为false。
     * <p/>
     * 在这个demo中，被内置锁阻塞的线程t将使用{@link Logger#info(String)}来打日志。<TR/>
     * 尝试中断处于内置锁阻塞中的线程main将使用{@link Logger#debug(String)}来打日志。
     */
    @Test
    public void nonInterruptLock() throws InterruptedException {

        Thread t = new Thread(() -> {
            logger.info(Thread.currentThread().getName() + "尝试获取内置锁，即将阻塞");
            synchronized (internalLock) {
                logger.info(Thread.currentThread().getName() + "已获取内置锁，线程未被中断，继续工作");
                if (Thread.currentThread().isInterrupted()) {
                    logger.info(Thread.currentThread().getName() + "线程阻塞状态已经被设置为true");
                }
            }
        }, "t");

        logger.debug(Thread.currentThread().getName() + "获取内置锁");
        synchronized (internalLock) {
            logger.debug("启动线程" + t.getName());
            t.start();

            //等待线程t启动
            Thread.sleep(500);
            logger.debug("尝试在线程阻塞时中断线程" + t.getName());
            t.interrupt();
            logger.debug("继续持有锁 500 ms");
            Thread.sleep(500);
        }
        //等待控制台输出
        Thread.sleep(1000);
    }
}
