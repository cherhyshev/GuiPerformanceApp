package ru.hse.spb.common.benchmark;

public class SingleIterationBenchmark {
    private final int valueParam;
    private final double averageClientTime;
    private final double averageSortingTime;
    private final double averageRequestTime;

    public SingleIterationBenchmark(int valueParam, double averageClientTime, double averageSortingTime, double averageRequestTime) {
        this.valueParam = valueParam;
        this.averageClientTime = averageClientTime;
        this.averageSortingTime = averageSortingTime;
        this.averageRequestTime = averageRequestTime;
    }


    public int getValueParam() {
        return valueParam;
    }

    public double getAverageClientTime() {
        return averageClientTime;
    }

    public double getAverageRequestTime() {
        return averageRequestTime;
    }

    public double getAverageSortingTime() {
        return averageSortingTime;
    }
}
