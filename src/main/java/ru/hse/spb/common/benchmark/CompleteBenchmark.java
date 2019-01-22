package ru.hse.spb.common.benchmark;

public class CompleteBenchmark {
    private final long averageSortingTime;
    private final long averageClientProcessingTime;
    private final long averageWorkTime;

    public CompleteBenchmark(long averageSortingTime, long averageClientProcessingTime, long averageWorkTime) {
        this.averageSortingTime = averageSortingTime;
        this.averageClientProcessingTime = averageClientProcessingTime;
        this.averageWorkTime = averageWorkTime;
    }

    public long getAverageSortingTime() {
        return averageSortingTime;
    }

    public long getAverageClientProcessingTime() {
        return averageClientProcessingTime;
    }

    public long getAverageWorkTime() {
        return averageWorkTime;
    }
}
