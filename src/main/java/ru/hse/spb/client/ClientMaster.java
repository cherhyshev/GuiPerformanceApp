package ru.hse.spb.client;

import ru.hse.spb.common.CommonUtils;
import ru.hse.spb.common.Constants;
import ru.hse.spb.common.benchmark.AverageTime;
import ru.hse.spb.common.exception.BadConfigurationException;
import ru.hse.spb.common.protocol.Messages;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ClientMaster implements Runnable {
    private final ClientUtils.ClientMasterConfig config;
    private final StringBuilder stringBuilder = new StringBuilder();

    public ClientMaster(ClientUtils.ClientMasterConfig config) {
        this.config = config;
    }

    @Override
    public final void run() {
        Messages.ServerMasterRequest.Builder builder = Messages.ServerMasterRequest.newBuilder();
        switch (config.getArchitectureType()) {
            case MULTI_THREAD_BLOCKING:
                builder.setRequest1(Messages.ArchitectureRequest.newBuilder()
                        .setType(Messages.ArchitectureRequest.ArchitectureType.MULTI_THREAD_BLOCKING).build());
                break;
            case THREAD_POOL_BLOCKING:
                builder.setRequest1(Messages.ArchitectureRequest.newBuilder()
                        .setType(Messages.ArchitectureRequest.ArchitectureType.THREAD_POOL_BLOCKING).build());
                break;
            case NON_BLOCKING:
                builder.setRequest1(Messages.ArchitectureRequest.newBuilder()
                        .setType(Messages.ArchitectureRequest.ArchitectureType.NON_BLOCKING).build());
                break;
            default:
                throw new BadConfigurationException("Wrong architecture " + config.getArchitectureType());
        }
        Messages.ServerMasterRequest request = builder.build();
        List<ClientFactory> factories = generateFactories();
        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            socket = new Socket(config.getServerAddress(), config.getServerMasterPort());
            is = socket.getInputStream();
            os = socket.getOutputStream();
            request.writeDelimitedTo(os);
            os.flush();
            Messages.ServerMasterResponse response = Messages.ServerMasterResponse.parseDelimitedFrom(is);

            if (!response.hasResponse1()
                    || response.getResponse1().getServerProcessingPort() != Constants.SERVER_PROCESSING_PORT) {
                System.err.println("ERROR! Bad port received from ServerMaster");
            }
            ExecutorService executorService;
            for (ClientFactory factory : factories) {
                AverageTime averageClientTime = new AverageTime();
                List<AbstractClient> clients = factory.generateClients();
                List<Thread> clientsThreads = new ArrayList<>();
                for (AbstractClient client : clients) {
                    clientsThreads.add(new Thread(client));
                }

                for (Thread clientsThread : clientsThreads) {
                    clientsThread.start();
                }

                for (Thread clientsThread : clientsThreads) {
                    try {
                        clientsThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                clients.forEach(cl -> averageClientTime.addTime(cl.getFinishTime() - cl.getStartTime()));

                Messages.ServerMasterRequest benchmarkRequest = Messages.ServerMasterRequest.newBuilder()
                        .clearRequest1()
                        .setRequest2(Messages.BenchmarkRequest.getDefaultInstance()).build();

                benchmarkRequest.writeDelimitedTo(os);
                os.flush();
                Messages.ServerMasterResponse benchmarkResponse = Messages.ServerMasterResponse.parseDelimitedFrom(is);

                if (!benchmarkResponse.hasResponse2()) {
                    System.err.println("ERROR! Bad benchmarks from ServerMaster");
                }

                double averageSortingTime = benchmarkResponse.getResponse2().getAverageSortingTime();
                double averageRequestTime = benchmarkResponse.getResponse2().getAverageRequestTime();
                switch (config.getVariableParameter().getParameterName()) {
                    case M:
                        stringBuilder.append(factory.getM()).append("|");
                        break;
                    case N:
                        stringBuilder.append(factory.getN()).append("|");
                        break;
                    case D:
                        stringBuilder.append(factory.getD()).append("|");
                        break;
                    default:
                        break;
                }
                stringBuilder
                        .append(averageSortingTime)
                        .append("|")
                        .append(averageRequestTime)
                        .append("|")
                        .append(averageClientTime.getAverageTime())
                        .append("\n");
                averageClientTime.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ClientUtils.closeAllResources(socket, is, os);
        }

        String fileSuffix = "";
        switch (config.getVariableParameter().getParameterName()) {
            case D:
                fileSuffix += "delay_";
                break;
            case N:
                fileSuffix += "length_";
                break;
            case M:
                fileSuffix += "requests_";
                break;
            default:
                break;
        }

        File file = new File(fileSuffix + config.getArchitectureType().toString() + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<ClientFactory> generateFactories() {
        List<ClientFactory> factories = new ArrayList<>();
        ClientUtils.VariableParameter var = config.getVariableParameter();
        int count = (var.getLimit() == var.getStart()) ? 1 : (var.getLimit() - var.getStart()) / var.getStep();
        ClientUtils.RegularParameter parameterX = config.getParameterX();
        ClientUtils.RegularParameter parameterN;
        ClientUtils.RegularParameter parameterM;
        ClientUtils.RegularParameter parameterD;
        for (int i = 0; i < count; i++) {
            switch (config.getVariableParameter().getParameterName()) {
                case N:
                    parameterN = new ClientUtils.RegularParameter(ClientUtils.ParameterName.N, var.getStart() + var.getStep() * i);
                    parameterM = (config.getParameter1().getParameterName() == ClientUtils.ParameterName.M) ? config.getParameter1() : config.getParameter2();
                    parameterD = (config.getParameter2().getParameterName() == ClientUtils.ParameterName.D) ? config.getParameter2() : config.getParameter1();
                    break;
                case M:
                    parameterM = new ClientUtils.RegularParameter(ClientUtils.ParameterName.M, var.getStart() + var.getStep() * i);
                    parameterN = (config.getParameter1().getParameterName() == ClientUtils.ParameterName.N) ? config.getParameter1() : config.getParameter2();
                    parameterD = (config.getParameter2().getParameterName() == ClientUtils.ParameterName.D) ? config.getParameter2() : config.getParameter1();
                    break;
                case D:
                    parameterD = new ClientUtils.RegularParameter(ClientUtils.ParameterName.D, var.getStart() + var.getStep() * i);
                    parameterN = (config.getParameter1().getParameterName() == ClientUtils.ParameterName.N) ? config.getParameter1() : config.getParameter2();
                    parameterM = (config.getParameter2().getParameterName() == ClientUtils.ParameterName.M) ? config.getParameter2() : config.getParameter1();
                    break;
                default:
                    throw new BadConfigurationException("Variable parameter has label " + config.getVariableParameter().getParameterName());
            }


            factories.add(new ClientFactory(new ClientUtils.ClientFactoryConfig(parameterX, parameterN, parameterM, parameterD,
                    config.getServerProcessingPort(), config.getServerAddress(), config.isBlocking())));
        }
        return factories;
    }
}
