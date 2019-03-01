package fre.shown.concurrency.demo.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 使用CAS操作开发的原子计数器
 *
 * @author Radon Freedom
 * created at 2019.03.01 6:03
 */

public class CasCounter {

    private final AtomicInteger cnt = new AtomicInteger(0);;


    public CasCounter() {
    }

    public int getCnt() {
        return cnt.get();
    }

    public int incrementAndGet() {
        return cnt.incrementAndGet();
    }
}
