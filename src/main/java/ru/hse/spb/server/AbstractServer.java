package ru.hse.spb.server;

import ru.hse.spb.common.benchmark.AverageTime;

public abstract class AbstractServer implements Runnable {
    protected final AverageTime sortingTime = new AverageTime();
    protected final AverageTime processingTime = new AverageTime();

    final double getAverageSortingTime() {
        return sortingTime.getAverageTime();
    }

    final double getAverageProcessingTime() {
        return processingTime.getAverageTime();
    }

    final void addSortingTime(long time) {
        sortingTime.addTime(time);
    }

    final void addProcessingTime(long time) {
        processingTime.addTime(time);
    }


}
