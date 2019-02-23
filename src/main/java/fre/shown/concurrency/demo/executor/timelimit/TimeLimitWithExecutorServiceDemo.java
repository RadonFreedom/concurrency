package fre.shown.concurrency.demo.executor.timelimit;

import fre.shown.concurrency.demo.Demo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 * 使用{@link java.util.concurrent.ExecutorService#invokeAll(Collection, long, TimeUnit)}来达到限时多个任务，超时取消任务的目的
 *
 * @author Radon Freedom
 * created at 2019.02.23 12:04
 */

@Demo
public class TimeLimitWithExecutorServiceDemo {
    private static final long TIMEOUT = 500;
    private static final Integer DEFAULT_RESULT = 0;
    private static final int MAX_WORK_QUEUE_LENGTH = 100;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ExecutorService executor = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(MAX_WORK_QUEUE_LENGTH));

    /**
     * 使用{@link ExecutorService#invokeAll(Collection, long, TimeUnit)}来对任务进行限时
     */
    @Test
    public void timeoutWithExecutorService() {

        List<Callable<Integer>> workList = new ArrayList<>(MAX_WORK_QUEUE_LENGTH);
        for (int i = 0; i < MAX_WORK_QUEUE_LENGTH; i++) {
            workList.add(new NeedTimeTask(i));
        }

        Integer[] results = new Integer[MAX_WORK_QUEUE_LENGTH];

        try {
            List<Future<Integer>> futureList = executor.invokeAll(workList, TIMEOUT, TimeUnit.MILLISECONDS);
            Future<Integer> currentFuture;
            for (int i = 0; i < futureList.size(); i++) {
                currentFuture = futureList.get(i);
                if (currentFuture.isCancelled()) {
                    results[i] = DEFAULT_RESULT;
                }
                else {
                    results[i] = currentFuture.get();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            logger.warn("任务出现异常：" + e.getCause());
        }

        logger.info("输出结果集：");
        for (Integer result : results) {
            System.out.print(result + " ");
        }
    }

    /**
     * 简单的返回整数值的{@link Callable}实现，可以构造方法传入参数来改变这个任务的耗时
     */
    private class NeedTimeTask implements Callable<Integer> {

        private final long timeNeeded;

        private NeedTimeTask(long timeNeeded) {
            this.timeNeeded = timeNeeded;
        }

        @Override
        public Integer call() throws Exception {
            Thread.sleep(timeNeeded);
            return 1;
        }
    }
}
