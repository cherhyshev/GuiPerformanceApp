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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class ServerMasterTest {
    @Test
    public void initializeConcreteArchitecture1() throws UnknownHostException {
        ServerMaster serverMaster = new ServerMaster(InetAddress.getLocalHost(), Constants.SERVER_MASTER_PORT,
                new AverageTime(), new AverageTime());

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(serverMaster);
        Messages.ServerMasterRequest.Builder builder = Messages.ServerMasterRequest.newBuilder();
        assertTrue(!builder.hasRequest1());
        assertTrue(!builder.hasRequest2());
        builder.setRequest1(Messages.ArchitectureRequest.newBuilder()
                .setType(Messages.ArchitectureRequest.ArchitectureType.THREAD_POOL_BLOCKING)
                .build());
        assertTrue(builder.hasRequest1());
        assertTrue(!builder.hasRequest2());


        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), Constants.SERVER_MASTER_PORT);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            builder.build().writeDelimitedTo(os);
            os.flush();
            Messages.ServerMasterResponse response = Messages.ServerMasterResponse.parseDelimitedFrom(is);

            serverMaster.stop();

            assertTrue(response.hasResponse1());
            assertEquals(response.getResponse1().getServerProcessingPort(), Constants.SERVER_PROCESSING_PORT);
            assertTrue(serverMaster.processingServer instanceof ThreadPoolBlockingServer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ClientUtils.closeAllResources(socket, is, os);
        }
    }

    @Test
    public void initializeConcreteArchitecture2() throws UnknownHostException {
        ServerMaster serverMaster = new ServerMaster(InetAddress.getLocalHost(), Constants.SERVER_MASTER_PORT,
                new AverageTime(), new AverageTime());

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(serverMaster);
        Messages.ServerMasterRequest.Builder builder = Messages.ServerMasterRequest.newBuilder();
        assertTrue(!builder.hasRequest1());
        assertTrue(!builder.hasRequest2());
        builder.setRequest1(Messages.ArchitectureRequest.newBuilder()
                .setType(Messages.ArchitectureRequest.ArchitectureType.MULTI_THREAD_BLOCKING)
                .build());
        assertTrue(builder.hasRequest1());
        assertTrue(!builder.hasRequest2());


        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), Constants.SERVER_MASTER_PORT);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            builder.build().writeDelimitedTo(os);
            os.flush();
            Messages.ServerMasterResponse response = Messages.ServerMasterResponse.parseDelimitedFrom(is);

            serverMaster.stop();

            assertTrue(response.hasResponse1());
            assertEquals(response.getResponse1().getServerProcessingPort(), Constants.SERVER_PROCESSING_PORT);
            assertTrue(serverMaster.processingServer instanceof MultiThreadBlockingServer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ClientUtils.closeAllResources(socket, is, os);
        }
    }

    @Test
    public void initializeConcreteArchitecture3() throws UnknownHostException {
        ServerMaster serverMaster = new ServerMaster(InetAddress.getLocalHost(), Constants.SERVER_MASTER_PORT,
                new AverageTime(), new AverageTime());

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(serverMaster);
        Messages.ServerMasterRequest.Builder builder = Messages.ServerMasterRequest.newBuilder();
        assertTrue(!builder.hasRequest1());
        assertTrue(!builder.hasRequest2());
        builder.setRequest1(Messages.ArchitectureRequest.newBuilder()
                .setType(Messages.ArchitectureRequest.ArchitectureType.NON_BLOCKING)
                .build());
        assertTrue(builder.hasRequest1());
        assertTrue(!builder.hasRequest2());


        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), Constants.SERVER_MASTER_PORT);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            builder.build().writeDelimitedTo(os);
            os.flush();
            Messages.ServerMasterResponse response = Messages.ServerMasterResponse.parseDelimitedFrom(is);

            serverMaster.stop();

            assertTrue(response.hasResponse1());
            assertEquals(response.getResponse1().getServerProcessingPort(), Constants.SERVER_PROCESSING_PORT);
            assertTrue(serverMaster.processingServer instanceof NonBlockingServer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ClientUtils.closeAllResources(socket, is, os);
        }
    }

    @Test
    public void initializeArchitectureAndGetStats() throws UnknownHostException {
        ServerMaster serverMaster = new ServerMaster(InetAddress.getLocalHost(), Constants.SERVER_MASTER_PORT,
                new AverageTime(), new AverageTime());

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(serverMaster);
        Messages.ServerMasterRequest.Builder builder = Messages.ServerMasterRequest.newBuilder();
        assertTrue(!builder.hasRequest1());
        assertTrue(!builder.hasRequest2());
        builder.setRequest1(Messages.ArchitectureRequest.newBuilder()
                .setType(Messages.ArchitectureRequest.ArchitectureType.THREAD_POOL_BLOCKING)
                .build());
        assertTrue(builder.hasRequest1());
        assertTrue(!builder.hasRequest2());


        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), Constants.SERVER_MASTER_PORT);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            builder.build().writeDelimitedTo(os);
            os.flush();
            Messages.ServerMasterResponse response1 = Messages.ServerMasterResponse.parseDelimitedFrom(is);

            Messages.ServerMasterRequest.newBuilder()
                    .setRequest2(Messages.BenchmarkRequest.newBuilder().getDefaultInstanceForType())
                    .build().writeDelimitedTo(os);
            os.flush();
            Messages.ServerMasterResponse response2 = Messages.ServerMasterResponse.parseDelimitedFrom(is);

            serverMaster.stop();

            assertTrue(response1.hasResponse1());
            assertEquals(response1.getResponse1().getServerProcessingPort(), Constants.SERVER_PROCESSING_PORT);
            assertTrue(serverMaster.processingServer instanceof ThreadPoolBlockingServer);

            assertTrue(response2.hasResponse2());
            assertEquals(response2.getResponse2().getAverageSortingTime(), -1, 0.0);
            assertEquals(response2.getResponse2().getAverageRequestTime(), -1, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ClientUtils.closeAllResources(socket, is, os);
        }
    }


}