package ru.hse.spb.server;

import ru.hse.spb.common.benchmark.AverageTime;

import java.net.InetAddress;

public abstract class AbstractServer implements Runnable {
    protected final int serverPort;
    protected final InetAddress serverAddress;

    protected final AverageTime sortingTime = new AverageTime();
    protected final AverageTime processingTime = new AverageTime();

    protected AbstractServer(int serverPort, InetAddress serverAddress) {
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
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


}
