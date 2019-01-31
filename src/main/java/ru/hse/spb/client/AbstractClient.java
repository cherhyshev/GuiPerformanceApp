package ru.hse.spb.client;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class AbstractClient implements Runnable {
    private final ClientUtils.ClientConfig clientConfig;
    private volatile long startTime;
    private volatile long finishTime;

    public AbstractClient(@NotNull ClientUtils.ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
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
            ClientUtils.closeAllResources(socket, is, os);
            finishTime = System.currentTimeMillis();
        }
    }

    private void startScheduling(@NotNull Socket socket,
                                 @NotNull InputStream is,
                                 @NotNull OutputStream os) throws IOException {

        for (int j = 0; j < clientConfig.getRequestNum(); j++) {
            process(is, os);
            try {
                Thread.sleep(clientConfig.getDeltaInMs());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    protected abstract void process(InputStream is, OutputStream os) throws IOException;

    public long getStartTime() {
        return startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }
}
