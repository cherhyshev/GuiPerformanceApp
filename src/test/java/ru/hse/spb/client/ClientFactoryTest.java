package ru.hse.spb.client;

import org.junit.Test;
import ru.hse.spb.common.Constants;
import ru.hse.spb.common.benchmark.AverageTime;
import ru.hse.spb.server.MultiThreadBlockingServer;
import ru.hse.spb.server.NonBlockingServer;
import ru.hse.spb.server.ThreadPoolBlockingServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class ClientFactoryTest {
    @Test
    public void MultiThreadBlockingServerTest() throws UnknownHostException {
        MultiThreadBlockingServer server = new MultiThreadBlockingServer(InetAddress.getLocalHost(),
                Constants.SERVER_PROCESSING_PORT, new AverageTime(), new AverageTime());
        new Thread(server).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClientUtils.ClientFactoryConfig config = new ClientUtils.ClientFactoryConfig(
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.X, 50),
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.N, 1000),
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.M, 5),
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.D, 10),
                Constants.SERVER_PROCESSING_PORT, InetAddress.getLocalHost(), true);

        List<AbstractClient> clients = new ClientFactory(config).generateClients();

        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        clients.forEach(service::submit);
        service.shutdown();
        while (!service.isTerminated()) {
            try {
                Thread.sleep(100);
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

    @Test
    public void ThreadPoolBlockingServerTest() throws UnknownHostException {
        ThreadPoolBlockingServer server = new ThreadPoolBlockingServer(InetAddress.getLocalHost(),
                Constants.SERVER_PROCESSING_PORT, new AverageTime(), new AverageTime());
        new Thread(server).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClientUtils.ClientFactoryConfig config = new ClientUtils.ClientFactoryConfig(
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.X, 50),
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.N, 100),
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.M, 5),
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.D, 10),
                Constants.SERVER_PROCESSING_PORT, InetAddress.getLocalHost(), true);

        List<AbstractClient> clients = new ClientFactory(config).generateClients();

        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        clients.forEach(service::submit);
        service.shutdown();
        while (!service.isTerminated()) {
            try {
                Thread.sleep(100);
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

    @Test
    public void NonBlockingServerTest() throws UnknownHostException {
        NonBlockingServer server = new NonBlockingServer(InetAddress.getLocalHost(),
                Constants.SERVER_PROCESSING_PORT, new AverageTime(), new AverageTime());
        new Thread(server).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClientUtils.ClientFactoryConfig config = new ClientUtils.ClientFactoryConfig(
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.X, 50),
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.N, 100),
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.M, 5),
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.D, 10),
                Constants.SERVER_PROCESSING_PORT, InetAddress.getLocalHost(), false);

        List<AbstractClient> clients = new ClientFactory(config).generateClients();

        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        clients.forEach(service::submit);
        service.shutdown();
        while (!service.isTerminated()) {
            try {
                Thread.sleep(100);
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