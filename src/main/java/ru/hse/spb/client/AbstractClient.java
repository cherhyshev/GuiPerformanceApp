package ru.hse.spb.client;

import org.jetbrains.annotations.NotNull;
import ru.hse.spb.common.CommonUtils;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractClient implements Runnable {
    private final ClientUtils.ClientConfig clientConfig;
    private volatile long startTime;
    private volatile long finishTime;

    public AbstractClient(@NotNull ClientUtils.ClientConfig clientConfig) {

        this.clientConfig = clientConfig;
    }

    @Override
    public void run() {
        this.startTime = System.currentTimeMillis();
        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            socket = new Socket(clientConfig.getServerAddress(), clientConfig.getServerPort());
            is = socket.getInputStream();
            os = socket.getOutputStream();
            startScheduling(socket, is, os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.finishTime = System.currentTimeMillis();
            ClientUtils.closeAllResources(socket, is, os);
        }
    }

    private void startScheduling(Socket socket, InputStream is, OutputStream os) throws IOException {
        startTime = System.currentTimeMillis();
        for (int j = 0; j < clientConfig.getRequestNum(); j++) {
            process(is, os);
            try {
                Thread.sleep(clientConfig.getDeltaInMs());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        socket.close();
        finishTime = System.currentTimeMillis();
    }

    protected abstract void process(InputStream is, OutputStream os) throws IOException;

    public ClientUtils.ClientConfig getClientConfig() {
        return clientConfig;
    }
}
