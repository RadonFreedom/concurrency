package fre.shown.concurrency.demo.util;

import fre.shown.concurrency.demo.Demo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 阻塞队列是典型的生产者消费者模式，而{@link ThreadPoolExecutor 线程池}的实现满足这个模式。<br/
 * 尝试自己使用{@link BlockingQueue}实现简化的{@link CompletionService}。
 *
 * @author Radon Freedom
 * created at 2019.02.23 17:26
 */

@Demo
public class PriorityQueueDemo {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * {@link CompletionService}的简单实现。
     */
    public class CompletionServiceExecutor <T> {

        private final BlockingQueue<Future<T>> completedTasks;
        private final ThreadPoolExecutor executor;
        private class CompletableFuture extends FutureTask<T> {
            CompletableFuture(Callable<T> callable) {
                super(callable);
            }

            @Override
            protected void done() {
                completedTasks.add(this);
            }
        }

        public CompletionServiceExecutor (int fixedPoolSize, int taskQueueSize) {
            if (fixedPoolSize <= 0) {
                throw new IllegalArgumentException("线程池大小应该大于0！");
            }
            if (taskQueueSize <= 0) {
                throw new IllegalArgumentException("任务队列大小应该大于0！");
            }

            executor = new ThreadPoolExecutor(fixedPoolSize, fixedPoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(taskQueueSize));
            completedTasks = new LinkedBlockingQueue<>();
        }

        /**
         * 提交后返回该任务对应的Future，但不保证任务在返回时已经完成。
         *
         * @param task 需要完成的任务
         * @return 任务对应的Future
         */
        public Future<T> submit(Callable<T> task) {
            Future<T> future = new CompletableFuture(task);
            executor.submit((Runnable) future);
            return future;
        }

        public Future<T> take() throws InterruptedException {
            return completedTasks.take();
        }

        public Future<T> poll() {
            return completedTasks.poll();
        }
    }

    private static int TASK_TIME = 1000;

    @Test
    public void testCompletionServiceExecutor () throws InterruptedException, ExecutionException {

        //创建任务
        final Callable<Integer> callable = () -> {
            Thread.sleep(TASK_TIME);
            return 1;
        };

        final CompletionServiceExecutor<Integer> executor = new CompletionServiceExecutor<>(3, 100);
        logger.info("任务提交");
        executor.submit(callable);
        Future<Integer> future = executor.take();
        logger.info("输出结果：" + future.get());
    }
}
