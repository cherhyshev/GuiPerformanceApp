package ru.hse.spb.server;

import org.junit.Test;
import ru.hse.spb.client.ClientUtils;
import ru.hse.spb.common.Constants;
import ru.hse.spb.common.benchmark.AverageTime;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class MultiThreadBlockingServerTest {
    @Test
    public void handleRequestFromSingleClient() throws UnknownHostException {
        MultiThreadBlockingServer server = new MultiThreadBlockingServer(InetAddress.getLocalHost(), Constants.SERVER_PROCESSING_PORT, new AverageTime(), new AverageTime());
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

            Messages.ArrayMessage msg = ClientUtils.generateMessage(1000);
            msg.writeDelimitedTo(os);
            os.flush();
            Messages.ArrayMessage receivedMessage = Messages.ArrayMessage.parseDelimitedFrom(is);
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
        MultiThreadBlockingServer server = new MultiThreadBlockingServer(InetAddress.getLocalHost(), Constants.SERVER_PROCESSING_PORT, new AverageTime(), new AverageTime());
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

            for (int j = 0; j < 100; j++) {
                Messages.ArrayMessage msg = ClientUtils.generateMessage(1000);
                msg.writeDelimitedTo(os);
                os.flush();
                Messages.ArrayMessage receivedMessage = Messages.ArrayMessage.parseDelimitedFrom(is);
                assertEquals(receivedMessage.getArrayElementsCount(), msg.getArrayElementsCount());

                for (int i = 1; i < receivedMessage.getElementsNum(); i++) {
                    assertTrue(receivedMessage.getArrayElements(i - 1)
                            < receivedMessage.getArrayElements(i));
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ClientUtils.closeAllResources(socket, is, os);
        }
        server.stop();
        System.out.println(server.sortingTime.getStatCount());
        System.out.println(server.processingTime.getStatCount());
        System.out.println(server.getAverageSortingTime());
        System.out.println(server.getAverageProcessingTime());

    }

    @Test
    public void handleManyRequestsFromManyClients() throws UnknownHostException {
        MultiThreadBlockingServer server = new MultiThreadBlockingServer(InetAddress.getLocalHost(), Constants.SERVER_PROCESSING_PORT, new AverageTime(), new AverageTime());
        new Thread(server).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread[] clientThreads = new Thread[10];
        for (int id = 0; id < clientThreads.length; id++) {
            clientThreads[id] = new Thread(() -> {
                Socket socket = null;
                InputStream is = null;
                OutputStream os = null;
                try {
                    socket = new Socket(InetAddress.getLocalHost(), Constants.SERVER_PROCESSING_PORT);
                    is = socket.getInputStream();
                    os = socket.getOutputStream();

                    for (int j = 0; j < 10; j++) {
                        Messages.ArrayMessage msg = ClientUtils.generateMessage(1000);
                        msg.writeDelimitedTo(os);
                        os.flush();
                        Messages.ArrayMessage receivedMessage = Messages.ArrayMessage.parseDelimitedFrom(is);
                        assertEquals(msg.getArrayElementsCount(), receivedMessage.getArrayElementsCount());

                        for (int i = 1; i < receivedMessage.getElementsNum(); i++) {
                            assertTrue(receivedMessage.getArrayElements(i - 1)
                                    <= receivedMessage.getArrayElements(i));
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    socket.close();
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
