package ru.hse.spb.client;

import org.junit.Test;
import ru.hse.spb.server.NonBlockingServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class NonBlockingClientTest {
    @Test
    public void singleClientTest() throws UnknownHostException {
        NonBlockingServer server = new NonBlockingServer(9013, InetAddress.getLocalHost());
        new Thread(server).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        NonBlockingClient client = new NonBlockingClient(100, 10, 50, 9013, InetAddress.getLocalHost());
        Thread clientThread = new Thread(client);
        clientThread.start();
        try {
            clientThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        server.stop();
        System.out.println(server.getSortingTimesCounter());
        System.out.println(server.getProcessingTimesCounter());
        System.out.println(server.getAverageSortingTime());
        System.out.println(server.getAverageProcessingTime());

    }

    @Test
    public void manyClientsTest() throws UnknownHostException {
        NonBlockingServer server = new NonBlockingServer(9013, InetAddress.getLocalHost());
        new Thread(server).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread[] clientThreads = new Thread[5];
        for (int i = 0; i < clientThreads.length; i++) {
            clientThreads[i] = new Thread(new NonBlockingClient(100, 10, 50, 9013, InetAddress.getLocalHost()));
            clientThreads[i].start();
        }

        for (Thread client : clientThreads) {
            try {
                client.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        server.stop();
        System.out.println(server.getSortingTimesCounter());
        System.out.println(server.getProcessingTimesCounter());
        System.out.println(server.getAverageSortingTime());
        System.out.println(server.getAverageProcessingTime());

    }


}