package ru.hse.spb.server;

import ru.hse.spb.common.benchmark.AverageTime;

public abstract class AbstractServer {
    private final AverageTime averageSortingTime = new AverageTime();
    private final AverageTime averageClientProcessing = new AverageTime();

    abstract void start(int port);

    abstract void stop();

    void addClientProcessingTime(final long time) {
        averageClientProcessing.addTime(time);
    }

    final long getAverageClientProcessingTime() {
        return averageClientProcessing.getAverageTime();
    }

    void addSortingTime(final long time) {
        averageSortingTime.addTime(time);
    }

    final long getAverageSortingTime() {
        return averageSortingTime.getAverageTime();
    }
}
