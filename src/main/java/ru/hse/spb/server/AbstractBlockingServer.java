package ru.hse.spb.server;

import ru.hse.spb.client.ClientUtils;
import ru.hse.spb.common.CommonUtils;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public abstract class AbstractBlockingServer extends AbstractServer {
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;

    protected AbstractBlockingServer(InetAddress serverAddress, int port) {
        super(serverAddress, port);
    }


    @Override
    public void run() {
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            new Thread(() -> {
                InputStream input = null;
                OutputStream output = null;
                try {
                    input = clientSocket.getInputStream();
                    output = clientSocket.getOutputStream();
                    while (!clientSocket.isClosed()) {
                        if (input.available() > 1) {
                            long processStart = System.currentTimeMillis();
                            Messages.ArrayMessage receivedMessage = Messages.ArrayMessage.parseDelimitedFrom(input);
                            long sortingStart = System.currentTimeMillis();
                            Messages.ArrayMessage sortedMessage = handleRequest(receivedMessage);
                            addSortingTime(System.currentTimeMillis() - sortingStart);
                            sendResponse(sortedMessage, output);
                            addProcessingTime(System.currentTimeMillis() - processStart);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    ClientUtils.closeAllResources(clientSocket, input, output);
                }
            }).start();
        }
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }


    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort, Integer.MAX_VALUE, serverAddress);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }


    protected abstract Messages.ArrayMessage handleRequest(Messages.ArrayMessage messageToSort);

    protected abstract void sendResponse(Messages.ArrayMessage sortedMessage, OutputStream os);

}
