package ru.hse.spb.common.benchmark;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AverageTime {
    private volatile AtomicInteger statCount = new AtomicInteger(0);
    private volatile AtomicLong sumTime = new AtomicLong(0);

    public AverageTime() {
    }

    public int getStatCount() {
        return statCount.get();
    }

    public synchronized void addTime(long time) {
        statCount.incrementAndGet();
        sumTime.addAndGet(time);
    }

    public synchronized double getAverageTime() {
        return statCount.get() == 0 ? -1 : ((double) sumTime.get()) / statCount.get();
    }
}
