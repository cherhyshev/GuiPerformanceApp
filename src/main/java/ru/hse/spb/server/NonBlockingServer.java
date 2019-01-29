package ru.hse.spb.server;

import java.net.ServerSocket;

public class NonBlockingServer extends AbstractServer {
    private final int serverPort;

    public NonBlockingServer(int port) {
        this.serverPort = port;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("No implementation for NonBlockingServer");
    }
}
