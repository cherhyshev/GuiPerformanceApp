package ru.hse.spb.common.benchmark;

public class AverageTime {
    public AverageTime() {
    }

    private volatile int statCount = 0;
    private volatile long sumTime = 0;

    public synchronized void addTime(long time) {
        statCount++;
        sumTime += time;
    }

    public synchronized long getAverageTime() {
        return statCount == 0 ? -1 : sumTime / statCount;
    }
}
