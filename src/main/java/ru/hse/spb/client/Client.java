package ru.hse.spb.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Client {
    private final int elementsInArray;
    private final int deltaInMs;
    private final int requestNum;
    private final int serverPort;
    private final InetAddress serverAddress;
    private final ScheduledExecutorService executorService;
    private final List<Messages.ArrayMessage> messageList = new ArrayList<>();
    private final AtomicLong startTime = new AtomicLong();
    private final ConcurrentLinkedQueue<Long> timesForOneRequestQueue = new ConcurrentLinkedQueue<>();


    public Client(int elementsInArray, int deltaInMs, int requestNum,
                  int serverPort, @NotNull InetAddress serverAddress,
                  @NotNull ScheduledExecutorService executorService) {
        this.elementsInArray = elementsInArray;
        this.deltaInMs = deltaInMs;
        this.requestNum = requestNum;
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
        this.executorService = executorService;
        for (int i = 0; i < requestNum; i++) {
            messageList.add(ClientUtils.generateMessage(elementsInArray));
        }
    }

    public void start() {
        startTime.set(System.currentTimeMillis());
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
            ClientUtils.closeAllResources(socket, is, os);
        }

    }

    private void connectServer(@NotNull AtomicInteger requestCounter,
                               @NotNull Socket socket,
                               @NotNull InputStream is,
                               @NotNull OutputStream os) {
        do {
            try {
                messageList.get(requestCounter.get()).writeDelimitedTo(os);
                Messages.ArrayMessage recievedMessage = Messages.ArrayMessage.parseDelimitedFrom(is);

            } catch (IOException e) {
                e.printStackTrace();
                ClientUtils.closeAllResources(socket, is, os);
            }

        } while (requestCounter.incrementAndGet() < requestNum);
        ClientUtils.closeAllResources(socket, is, os);
        timesForOneRequestQueue.add(System.currentTimeMillis() - startTime.get());
    }


}
