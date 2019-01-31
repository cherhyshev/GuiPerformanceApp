package ru.hse.spb.server;

import org.junit.Test;
import ru.hse.spb.client.ClientUtils;
import ru.hse.spb.common.CommonUtils;
import ru.hse.spb.common.Constants;
import ru.hse.spb.common.benchmark.AverageTime;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NonBlockingServerTest {
    @Test
    public void handleRequestFromSingleClient() throws UnknownHostException {
        NonBlockingServer server = new NonBlockingServer(InetAddress.getLocalHost(), Constants.SERVER_PROCESSING_PORT, new AverageTime(), new AverageTime());
        new Thread(server).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), Constants.SERVER_PROCESSING_PORT);
            is = socket.getInputStream();
            os = socket.getOutputStream();

            Messages.ArrayMessage msg = ClientUtils.generateMessage(100);
            byte[] messageBytes = CommonUtils.serialize(msg);
            os.write(messageBytes);
            os.flush();
            int readCount = is.read(messageBytes);
            while (readCount < messageBytes.length) {
                readCount += is.read(messageBytes, readCount, messageBytes.length - readCount);
            }

            Messages.ArrayMessage receivedMessage = CommonUtils.deserialize(messageBytes);
            assertEquals(receivedMessage.getArrayElementsCount(), msg.getArrayElementsCount());

            for (int i = 1; i < receivedMessage.getElementsNum(); i++) {
                assertTrue(receivedMessage.getArrayElements(i - 1)
                        <= receivedMessage.getArrayElements(i));
            }
            server.stop();
            System.out.println(server.sortingTime.getStatCount());
            System.out.println(server.processingTime.getStatCount());
            System.out.println(server.getAverageSortingTime());
            System.out.println(server.getAverageProcessingTime());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ClientUtils.closeAllResources(socket, is, os);
        }
    }

    @Test
    public void handleManyRequestsFromSingleClient() throws UnknownHostException {
        NonBlockingServer server = new NonBlockingServer(InetAddress.getLocalHost(), Constants.SERVER_PROCESSING_PORT, new AverageTime(), new AverageTime());
        new Thread(server).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), Constants.SERVER_PROCESSING_PORT);
            is = socket.getInputStream();
            os = socket.getOutputStream();

            for (int j = 0; j < 625; j++) {
                Messages.ArrayMessage msg = ClientUtils.generateMessage(100);
                byte[] messageBytes = CommonUtils.serialize(msg);
                os.write(messageBytes);
                os.flush();
                int readCount = is.read(messageBytes);
                while (readCount < messageBytes.length) {
                    readCount += is.read(messageBytes, readCount, messageBytes.length - readCount);
                }

                Messages.ArrayMessage receivedMessage = CommonUtils.deserialize(messageBytes);
                assertEquals(receivedMessage.getArrayElementsCount(), msg.getArrayElementsCount());

                for (int i = 1; i < receivedMessage.getElementsNum(); i++) {
                    assertTrue(receivedMessage.getArrayElements(i - 1)
                            <= receivedMessage.getArrayElements(i));
                }
            }
            server.stop();
            System.out.println(server.sortingTime.getStatCount());
            System.out.println(server.processingTime.getStatCount());
            System.out.println(server.getAverageSortingTime());
            System.out.println(server.getAverageProcessingTime());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ClientUtils.closeAllResources(socket, is, os);
        }
    }

    @Test
    public void handleManyRequestsFromManyClients() throws UnknownHostException {
        NonBlockingServer server = new NonBlockingServer(InetAddress.getLocalHost(), Constants.SERVER_PROCESSING_PORT, new AverageTime(), new AverageTime());
        new Thread(server).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread[] clientThreads = new Thread[25];
        for (int id = 0; id < 25; id++) {
            clientThreads[id] = new Thread(() -> {

                Socket socket = null;
                InputStream is = null;
                OutputStream os = null;
                try {
                    socket = new Socket(InetAddress.getLocalHost(), Constants.SERVER_PROCESSING_PORT);
                    is = socket.getInputStream();
                    os = socket.getOutputStream();

                    for (int j = 0; j < 25; j++) {
                        Messages.ArrayMessage msg = ClientUtils.generateMessage(100);
                        byte[] messageBytes = CommonUtils.serialize(msg);
                        os.write(messageBytes);
                        os.flush();
                        int readCount = is.read(messageBytes);
                        while (readCount < messageBytes.length) {
                            readCount += is.read(messageBytes, readCount, messageBytes.length - readCount);
                        }

                        Messages.ArrayMessage receivedMessage = CommonUtils.deserialize(messageBytes);
                        assertEquals(receivedMessage.getArrayElementsCount(), msg.getArrayElementsCount());

                        for (int i = 1; i < receivedMessage.getElementsNum(); i++) {
                            assertTrue(receivedMessage.getArrayElements(i - 1)
                                    <= receivedMessage.getArrayElements(i));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    ClientUtils.closeAllResources(socket, is, os);
                }
            });
            clientThreads[id].start();
        }

        for (Thread client : clientThreads) {
            try {
                client.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        server.stop();
        System.out.println(server.sortingTime.getStatCount());
        System.out.println(server.processingTime.getStatCount());
        System.out.println(server.getAverageSortingTime());
        System.out.println(server.getAverageProcessingTime());

    }
}