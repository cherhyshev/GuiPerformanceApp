package ru.hse.spb.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.hse.spb.common.protocol.ArrayMessageHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    private final int elementsInArray;
    private final int deltaInMs;
    private final int requestNum;
    private final int serverPort;
    private final InetAddress serverAddress;
    private final ScheduledExecutorService executorService;
    private final ClientsBenchmarkHandler clientsBenchmarkHandler;
    private volatile long startTime;


    public Client(int elementsInArray, int deltaInMs, int requestNum,
                  int serverPort, @NotNull InetAddress serverAddress,
                  @NotNull ScheduledExecutorService executorService,
                  @NotNull ClientsBenchmarkHandler clientsBenchmarkHandler) {
        this.elementsInArray = elementsInArray;
        this.deltaInMs = deltaInMs;
        this.requestNum = requestNum;
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
        this.executorService = executorService;
        this.clientsBenchmarkHandler = clientsBenchmarkHandler;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            socket = new Socket(serverAddress, serverPort);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            AtomicInteger requestCounter = new AtomicInteger();
            Socket finalSocket = socket;
            InputStream finalIs = is;
            OutputStream finalOs = os;
            executorService.schedule(
                    () -> connectServer(requestCounter, finalSocket, finalIs, finalOs),
                    deltaInMs, TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAllResources(socket, is, os);
        }

    }

    private void connectServer(@NotNull AtomicInteger requestCounter,
                               @NotNull Socket socket,
                               @NotNull InputStream is,
                               @NotNull OutputStream os) {
        do {
            try {
                byte[] arrayMessageBytes = ArrayMessageHandler.serialize(
                        ArrayMessageHandler.generate(elementsInArray));
                os.write(arrayMessageBytes);
                int readCount = is.read(arrayMessageBytes);
                while (readCount < arrayMessageBytes.length) {
                    readCount += is.read(arrayMessageBytes, readCount, arrayMessageBytes.length - readCount);
                }
            } catch (IOException e) {
                e.printStackTrace();
                closeAllResources(socket, is, os);
            }
        } while (requestCounter.incrementAndGet() < requestNum);
        closeAllResources(socket, is, os);
        long finishTime = System.currentTimeMillis();
        clientsBenchmarkHandler.addWorkTime(finishTime - startTime);
    }

    private void closeAllResources(@Nullable Socket socket,
                                   @Nullable InputStream is,
                                   @Nullable OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (is != null) {
            try {
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}
