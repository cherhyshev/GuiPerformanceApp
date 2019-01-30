package ru.hse.spb.server;

import ru.hse.spb.common.CommonUtils;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolBlockingServer extends AbstractBlockingServer {
    private final ExecutorService handlingService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public ThreadPoolBlockingServer(InetAddress serverAddress, int port) {
        super(serverAddress, port);
    }

    @Override
    protected Messages.ArrayMessage handleRequest(Messages.ArrayMessage messageToSort) {
        Messages.ArrayMessage handledMessage = null;

        try {
            handledMessage = handlingService.submit(() -> ServerUtils.getSortedMessage(messageToSort)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return handledMessage;
    }

    @Override
    protected void sendResponse(Messages.ArrayMessage sortedMessage, OutputStream os) {
        Executors.newSingleThreadExecutor().execute(() -> ServerUtils.sendArrayMessage(sortedMessage, os));
    }


}
