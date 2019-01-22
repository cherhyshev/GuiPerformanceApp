package ru.hse.spb.server;

import ru.hse.spb.common.protocol.ArrayMessageHandler;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadPoolBlockingServer extends AbstractServer {
    private final ExecutorService handlingService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
    private final ExecutorService sendingService = Executors.newSingleThreadExecutor();

    private volatile ServerSocket serverSocket = null;

    @Override
    public void start(int port) {
        while (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        new Thread(() -> {
            while (!serverSocket.isClosed()) {
                try (Socket clientSocket = serverSocket.accept();
                     InputStream is = clientSocket.getInputStream();
                     OutputStream os = clientSocket.getOutputStream()) {

                    while (!clientSocket.isClosed()) {
                        long processStart = System.currentTimeMillis();
                        Messages.ArrayMessage receivedMessage = Messages.ArrayMessage.parseFrom(is);
                        AtomicReference<Messages.ArrayMessage> resultMessage = null;
                        long start = System.currentTimeMillis();
                        handlingService.execute(() -> {
                            resultMessage.set(ArrayMessageHandler.process(receivedMessage));
                        });
                        ArrayMessageHandler.process(receivedMessage);
                        addSortingTime(System.currentTimeMillis() - start);
                        sendingService.execute(() -> {
                            try {
                                resultMessage.get().writeTo(os);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        addClientProcessingTime(System.currentTimeMillis() - processStart);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void stop() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
