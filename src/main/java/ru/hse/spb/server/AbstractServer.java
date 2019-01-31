package ru.hse.spb.server;

import ru.hse.spb.common.benchmark.AverageTime;

import java.net.InetAddress;

public abstract class AbstractServer implements Runnable {
    protected final int serverPort;
    protected final InetAddress serverAddress;

    protected AverageTime sortingTime;
    protected AverageTime processingTime;

    protected AbstractServer(InetAddress serverAddress,
                             int serverPort,
                             AverageTime sortingTime,
                             AverageTime processingTime) {
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
        this.sortingTime = sortingTime;
        this.processingTime = processingTime;
    }

    public final double getAverageSortingTime() {
        return sortingTime.getAverageTime();
    }

    public final double getSortingTimesCounter() {
        return sortingTime.getStatCount();
    }

    public final double getAverageProcessingTime() {
        return processingTime.getAverageTime();
    }

    public final double getProcessingTimesCounter() {
        return processingTime.getStatCount();
    }

    final void addSortingTime(long time) {
        sortingTime.addTime(time);
    }

    final void addProcessingTime(long time) {
        processingTime.addTime(time);
    }

    final void resetStats() {
        sortingTime.reset();
        processingTime.reset();
    }


}
