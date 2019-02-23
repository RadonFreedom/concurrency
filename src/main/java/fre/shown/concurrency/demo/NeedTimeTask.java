package fre.shown.concurrency.demo;

import java.util.concurrent.Callable;

/**
 * 简单的返回整数值的{@link Callable}实现，可以构造方法传入参数来改变这个任务的耗时
 * @author Radon Freedom
 * created at 2019.02.23 20:38
 */

public class NeedTimeTask implements Callable<Integer> {

    private final long timeNeeded;

    public NeedTimeTask(long timeNeeded) {
        this.timeNeeded = timeNeeded;
    }

    @Override
    public Integer call() throws Exception {
        Thread.sleep(timeNeeded);
        return 1;
    }
}