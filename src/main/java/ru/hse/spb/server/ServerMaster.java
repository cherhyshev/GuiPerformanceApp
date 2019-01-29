package ru.hse.spb.server;

import java.io.*;
import java.net.ServerSocket;

public class ServerMaster {

    private volatile ServerSocket serverSocket = null;

    public void start(int portNum) {
        try {
            serverSocket = new ServerSocket(portNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        new Thread(() -> {
//            while (!serverSocket.isClosed()) {
//                try (final Socket clientSocket = serverSocket.accept();
//                     final InputStream is = clientSocket.getInputStream();
//                     final OutputStream os = clientSocket.getOutputStream()) {
//
//                    Configurations.ServerConfig config = Configurations.ServerConfig.parseFrom(is);
//                    AbstractServer server = ServerFactory.generate(config);
//                    server.start(config.getPort());
//
//                    GetBenchmarksRequest request = (GetBenchmarksRequest) new ObjectInputStream(is).readObject();
//                    server.stop();
//                    Benchmarks.ServerBenchmark.Builder builder = Benchmarks.ServerBenchmark.newBuilder();
//                    Benchmarks.ServerBenchmark benchmark = builder
//                            .setAverageClientProcessingTime(server.getAverageClientProcessingTime())
//                            .setAverageRequestProcessingTime(server.getAverageSortingTime())
//                            .build();
//                    benchmark.writeTo(os);
//                } catch (IOException | ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }


}
