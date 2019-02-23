package fre.shown.concurrency.demo.util;

import fre.shown.concurrency.demo.Demo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * {@link Semaphore} 控制窟一组固定数量的资源的访问。
 * 只有当有空闲的许可时才可以访问所需资源，否则调用{@link Semaphore#acquire()}将阻塞。
 * 必须在释放资源后调用{@link Semaphore#release()}。
 * <p/>
 * 使用Semaphore来维护一组资源。
 *
 * @author Radon Freedom
 * created at 2019.02.23 20:00
 */

@Demo
public class SemaphoreDemo {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final int PERMIT_NUM = 2;
    private static final int TASK_NUM = 5;

    @Test
    public void testSemaphore() throws InterruptedException {

        final Semaphore semaphore = new Semaphore(PERMIT_NUM);
        final ExecutorService executorService = new ThreadPoolExecutor(TASK_NUM, TASK_NUM, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(TASK_NUM));

        Callable<Integer> task = () -> {
            semaphore.acquire();
            logger.info(Thread.currentThread().getName() + "已获取资源");
            Thread.sleep(100);
            logger.info(Thread.currentThread().getName() + "释放资源");
            semaphore.release();
            return 1;
        };

        for (int i = 0; i < TASK_NUM; i++) {
            executorService.submit(task);
        }

        Thread.sleep(3000);
    }
}
