package fre.shown.concurrency.aqs;

import fre.shown.concurrency.Demo;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * 本Demo以{@link java.util.concurrent.locks.ReentrantLock}
 * 来调试{@link java.util.concurrent.locks.AbstractQueuedSynchronizer}的源码。
 *
 * @author Radon Freedom
 * created at 2019.03.13 18:40
 */

@Demo
public class AqsDemo {

    private static class Service {
        /**
         * 类级别的显式公平锁。
         * 假设业务场景下每个调用{@link #service()}方法的线程都必须获取这个锁。
         */
        private static ReentrantLock lock = new ReentrantLock(true);

        static void service() {

            lock.lock();
            try {
                System.out.println(Thread.currentThread() + "获得了锁！");
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        Service.lock.lock();
        new Thread(() -> Service.service()).start();
    }
}
