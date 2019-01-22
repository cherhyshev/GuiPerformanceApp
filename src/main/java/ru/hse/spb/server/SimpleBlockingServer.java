package ru.hse.spb.server;


import ru.hse.spb.common.protocol.ArrayMessageHandler;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleBlockingServer extends AbstractServer {
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
                        long start = System.currentTimeMillis();
                        Messages.ArrayMessage resultMessage = ArrayMessageHandler.process(receivedMessage);
                        addSortingTime(System.currentTimeMillis() - start);
                        resultMessage.writeTo(os);
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