package fre.shown.concurrency;

/**
 * @author Radon Freedom
 * created at 2019.02.23 20:40
 */

public class NeedTimeRunnable implements Runnable {

    private final long timeNeeded;

    public NeedTimeRunnable(long timeNeeded) {
        this.timeNeeded = timeNeeded;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timeNeeded);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
