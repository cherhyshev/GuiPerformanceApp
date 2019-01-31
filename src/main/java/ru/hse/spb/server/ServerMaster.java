package ru.hse.spb.server;

import ru.hse.spb.client.ClientUtils;
import ru.hse.spb.common.Constants;
import ru.hse.spb.common.benchmark.AverageTime;
import ru.hse.spb.common.exception.NoSuchArchitectureException;
import ru.hse.spb.common.protocol.Messages;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMaster extends AbstractServer {

    private ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    AbstractServer processingServer;
    private ExecutorService service = Executors.newSingleThreadExecutor();


    public ServerMaster(InetAddress serverAddress, int port, AverageTime sortingTime, AverageTime processingTime) {
        super(serverAddress, port, sortingTime, processingTime);
    }

    @Override
    public void run() {
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            InputStream input = null;
            OutputStream output = null;
            try {
                input = clientSocket.getInputStream();
                output = clientSocket.getOutputStream();
                while (!clientSocket.isClosed()) {
                    if (input.available() > 0) {
                        Messages.ServerMasterRequest request = Messages.ServerMasterRequest.parseDelimitedFrom(input);
                        if (request.hasRequest1()) {
                            switch (request.getRequest1().getType()) {
                                case MULTI_THREAD_BLOCKING:
                                    processingServer = new MultiThreadBlockingServer(this.serverAddress,
                                            Constants.SERVER_PROCESSING_PORT, this.sortingTime, this.processingTime);
                                    break;
                                case THREAD_POOL_BLOCKING:
                                    processingServer = new ThreadPoolBlockingServer(this.serverAddress,
                                            Constants.SERVER_PROCESSING_PORT, this.sortingTime, this.processingTime);
                                    break;
                                case NON_BLOCKING:
                                    processingServer = new NonBlockingServer(this.serverAddress,
                                            Constants.SERVER_PROCESSING_PORT, this.sortingTime, this.processingTime);
                                    break;
                                default:
                                    throw new NoSuchArchitectureException();
                            }
                            service.submit(processingServer);
                            Messages.ServerMasterResponse response = Messages.ServerMasterResponse.newBuilder()
                                    .setResponse1(Messages.PortResponse.newBuilder()
                                            .setServerProcessingPort(Constants.SERVER_PROCESSING_PORT).build()).build();
                            response.writeDelimitedTo(output);
                            output.flush();
                        } else {
                            double sortingTime = getAverageSortingTime();
                            double processingTime = getAverageProcessingTime();
                            resetStats();
                            Messages.ServerMasterResponse response = Messages.ServerMasterResponse.newBuilder()
                                    .setResponse2(Messages.ServerBenchmarkResponse.newBuilder()
                                            .setAverageSortingTime(sortingTime).setAverageRequestTime(processingTime).build()).build();
                            response.writeDelimitedTo(output);
                        }

                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                service.shutdown();
                ClientUtils.closeAllResources(clientSocket, input, output);
            }
        }

    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }


    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort, Integer.MAX_VALUE, serverAddress);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }

}
