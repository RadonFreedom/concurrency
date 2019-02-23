package fre.shown.concurrency.demo.executor.timelimit;

import fre.shown.concurrency.demo.Demo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 使用{@link java.util.concurrent.Future}来达到限时单个任务，超时取消任务的目的
 *
 * @author Radon Freedom
 * created at 2019.02.23 9:56
 */

@Demo
public class TimeLimitWithFutureDemo {

    private static final long TIMEOUT = 50;
    private static final Integer DEFAULT_RESULT = 0;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ExecutorService executor = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100));

    /**
     * 使用{@link Future#get(long, TimeUnit)}来对任务限时
     */
    @Test
    public void timeoutWithFuture() {

        long endTime = System.currentTimeMillis() + TIMEOUT;
        //提交任务
        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(100);
            return 1;
        });

        Integer result = DEFAULT_RESULT;
        long timeLeft = endTime - System.currentTimeMillis();
        try {
            result = future.get(timeLeft, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            logger.warn("任务出现异常：" + e.getCause());
        } catch (TimeoutException e) {
            logger.warn("任务超时！");
            e.printStackTrace();
            //取消任务
            future.cancel(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        logger.info("result: " + result);
    }
}
