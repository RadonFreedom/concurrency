package fre.shown.concurrency.demo.cas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 使用{@link AtomicReference}来构造原子操作的整数范围
 *
 * @author Radon Freedom
 * created at 2019.03.01 7:42
 */

public class CasIntegerRange {

    /**
     * 原子的不变型条件必须满足 lo <= hi
     */
    private static class IntegerRange {
        private final int lo;
        private final int hi;

        IntegerRange(int lo, int hi) {
            this.lo = lo;
            this.hi = hi;
        }
    }

    private final AtomicReference<IntegerRange> rangeRef = new AtomicReference<>(new IntegerRange(0, 0));
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public int getLo() {
        return rangeRef.get().lo;
    }

    public int getHi() {
        return rangeRef.get().hi;
    }

    public void setLo(int newLo) throws Exception {
        IntegerRange oldRange = rangeRef.get();
        if (newLo > oldRange.hi) {
            throw new IllegalArgumentException("无法设置下界 " + newLo + " > " + "上界 " + oldRange.hi);
        }
        IntegerRange newRange = new IntegerRange(newLo, oldRange.hi);
        if (rangeRef.compareAndSet(oldRange, newRange)) {
            logger.info("更新下界成功");
        } else {
            throw new Exception("数组边界值已被其他线程更新，请重新设置边界值！");
        }
    }

    public void setHi(int newHi) throws Exception {
        IntegerRange oldRange = rangeRef.get();
        if (newHi < oldRange.lo) {
            throw new IllegalArgumentException("无法设置上界 " + newHi + " < " + "下界 " + oldRange.hi);
        }
        IntegerRange newRange = new IntegerRange(oldRange.lo, newHi);
        if (rangeRef.compareAndSet(oldRange, newRange)) {
            logger.info("更新上界成功");
        } else {
            throw new Exception("数组边界值已被其他线程更新，请重新设置边界值！");
        }
    }
}
