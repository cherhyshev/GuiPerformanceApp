package ru.hse.spb.client;

import org.jetbrains.annotations.Nullable;
import ru.hse.spb.common.CommonUtils;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.stream.Collectors;

public class ClientUtils {
    public static Messages.ArrayMessage generateMessage(int size) {
        Messages.ArrayMessage.Builder builder1 = Messages.ArrayMessage.newBuilder();
        builder1.setElementsNum(size)
                .addAllArrayElements(new Random().ints().boxed().limit(size).collect(Collectors.toList()));
        return builder1.build();
    }

    public static void closeAllResources(@Nullable Socket socket,
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

    public static class ClientConfig {
        private final int elementsInArray;
        private final int deltaInMs;
        private final int requestNum;
        private final int serverPort;
        private final InetAddress serverAddress;

        public ClientConfig(int elementsInArray,
                            int deltaInMs,
                            int requestNum,
                            int serverPort,
                            InetAddress serverAddress) {
            this.elementsInArray = elementsInArray;
            this.deltaInMs = deltaInMs;
            this.requestNum = requestNum;
            this.serverPort = serverPort;
            this.serverAddress = serverAddress;
        }

        public int getElementsInArray() {
            return elementsInArray;
        }

        public int getDeltaInMs() {
            return deltaInMs;
        }

        public int getRequestNum() {
            return requestNum;
        }

        public int getServerPort() {
            return serverPort;
        }

        public InetAddress getServerAddress() {
            return serverAddress;
        }
    }

    public enum ParameterName {
        N, M, D, X;
    }

    public static abstract class Parameter {
        private final ParameterName parameterName;

        protected Parameter(ParameterName parameterName) {
            this.parameterName = parameterName;
        }

        public ParameterName getParameterName() {
            return parameterName;
        }
    }

    public static class RegularParameter extends Parameter {
        private final int value;

        public RegularParameter(ParameterName parameterName, int value) {
            super(parameterName);
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static class VariableParameter extends Parameter {
        private final int start;
        private final int limit;
        private final int step;


        public VariableParameter(ParameterName parameterName, int start, int limit, int step) {
            super(parameterName);
            this.start = start;
            this.limit = limit;
            this.step = step;
        }

        public int getStart() {
            return start;
        }

        public int getLimit() {
            return limit;
        }

        public int getStep() {
            return step;
        }
    }

    public static class ClientMasterConfig {
        private final VariableParameter variableParameter;
        private final RegularParameter parameterX;
        private final RegularParameter parameter1;
        private final RegularParameter parameter2;
        private final InetAddress serverAddress;
        private final int serverMasterPort;
        private final int serverProcessingPort;
        private final CommonUtils.ArchitectureType architectureType;

        public ClientMasterConfig(VariableParameter variableParameter,
                                  RegularParameter parameterX,
                                  RegularParameter parameter1,
                                  RegularParameter parameter2,
                                  InetAddress serverAddress,
                                  int serverMasterPort,
                                  int serverProcessingPort,
                                  CommonUtils.ArchitectureType architectureType) {

            this.variableParameter = variableParameter;
            this.parameterX = parameterX;
            this.parameter1 = parameter1;
            this.parameter2 = parameter2;
            this.serverMasterPort = serverMasterPort;
            this.serverProcessingPort = serverProcessingPort;
            this.serverAddress = serverAddress;
            this.architectureType = architectureType;
        }

        public VariableParameter getVariableParameter() {
            return variableParameter;
        }

        public RegularParameter getParameterX() {
            return parameterX;
        }

        public RegularParameter getParameter1() {
            return parameter1;
        }

        public RegularParameter getParameter2() {
            return parameter2;
        }

        public InetAddress getServerAddress() {
            return serverAddress;
        }

        public int getServerMasterPort() {
            return serverMasterPort;
        }

        public int getServerProcessingPort() {
            return serverProcessingPort;
        }

        public CommonUtils.ArchitectureType getArchitectureType() {
            return architectureType;
        }

        public boolean isBlocking() {
            return architectureType != CommonUtils.ArchitectureType.NON_BLOCKING;
        }
    }

    public static class ClientFactoryConfig {
        private final RegularParameter parameterM;
        private final ClientConfig clientConfig;
        private final boolean isBlocking;

        public ClientFactoryConfig(RegularParameter parameterX,
                                   RegularParameter parameterN,
                                   RegularParameter parameterM,
                                   RegularParameter parameterD,
                                   int serverPort, InetAddress serverAddress, boolean isBlocking) {

            this.parameterM = parameterM;
            this.isBlocking = isBlocking;
            this.clientConfig = new ClientConfig(parameterN.getValue(), parameterD.getValue(),
                    parameterX.getValue(), serverPort, serverAddress);
        }

        public ClientConfig getClientConfig() {
            return clientConfig;
        }

        public RegularParameter getParameterM() {
            return parameterM;
        }

        public boolean isBlocking() {
            return isBlocking;
        }
    }


}
