package ru.hse.spb.client;

import ru.hse.spb.common.benchmark.AverageTime;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientsBenchmarkHandler {
    private final int clientCount;
    final private AverageTime averageWorkTime = new AverageTime();
    final private AtomicInteger currentCount = new AtomicInteger();

    public ClientsBenchmarkHandler(int clientCount) {
        this.clientCount = clientCount;
    }

    public void addWorkTime(final long time) {
        averageWorkTime.addTime(time);
        if (currentCount.incrementAndGet() == clientCount) {
            //TODO Запись данных в файл
        }
    }

    public long getAverageWorkTime() {
        return averageWorkTime.getAverageTime();
    }
}
