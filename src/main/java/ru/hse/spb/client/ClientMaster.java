package ru.hse.spb.client;

import java.net.InetAddress;
import java.util.concurrent.ScheduledExecutorService;

public class ClientMaster {
    private final int clientCount;
    private final int elementsInArray;
    private final int deltaInMs;
    private final int requestNum;
    private final int serverPort;
    private final InetAddress serverAddress;
    private final ScheduledExecutorService executorService;
    private final ClientsBenchmarkHandler clientsBenchmarkHandler;

    public ClientMaster(int clientCount, int elementsInArray, int deltaInMs,
                        int requestNum, int serverPort, InetAddress serverAddress,
                        ScheduledExecutorService executorService,
                        ClientsBenchmarkHandler clientsBenchmarkHandler) {
        this.clientCount = clientCount;
        this.elementsInArray = elementsInArray;
        this.deltaInMs = deltaInMs;
        this.requestNum = requestNum;
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
        this.executorService = executorService;
        this.clientsBenchmarkHandler = clientsBenchmarkHandler;
    }

    public void start() {
        for (int i = 0; i < clientCount; i++) {
            new Client(elementsInArray, deltaInMs, requestNum, serverPort,
                    serverAddress, executorService, clientsBenchmarkHandler).start();
        }
    }

}
