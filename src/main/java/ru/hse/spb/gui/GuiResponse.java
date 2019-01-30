package ru.hse.spb.gui;

import ru.hse.spb.client.ClientUtils;
import ru.hse.spb.common.Constants;

import java.net.InetAddress;

public class GuiResponse {
    private final int architectureType;
    private final InetAddress serverAddress;
    private final int requestsPerClient;
    private final ClientUtils.ParameterName parameterName;
    private final int lowBound;
    private final int highBound;
    private final int step;
    private final int firstValParameter;
    private final int secondValParameter;

    public GuiResponse(int architectureType,
                       InetAddress serverAddress,
                       int requestsPerClient,
                       ClientUtils.ParameterName parameterName,
                       int lowBound, int highBound, int step,
                       int firstValParameter, int secondValParameter) {
        this.architectureType = architectureType;
        this.serverAddress = serverAddress;
        this.requestsPerClient = requestsPerClient;
        this.parameterName = parameterName;
        this.lowBound = lowBound;
        this.highBound = highBound;
        this.step = step;
        this.firstValParameter = firstValParameter;
        this.secondValParameter = secondValParameter;
    }

    public int getArchitectureType() {
        return architectureType;
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public int getRequestsPerClient() {
        return requestsPerClient;
    }

    public int getLowBound() {
        return lowBound;
    }

    public int getHighBound() {
        return highBound;
    }

    public int getStep() {
        return step;
    }

    public int getFirstValParameter() {
        return firstValParameter;
    }

    public int getSecondValParameter() {
        return secondValParameter;
    }

    public ClientUtils.ParameterName getParameterName() {
        return parameterName;
    }
}
