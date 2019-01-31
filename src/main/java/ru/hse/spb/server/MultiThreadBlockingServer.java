package ru.hse.spb.server;


import ru.hse.spb.client.ClientUtils;
import ru.hse.spb.common.CommonUtils;
import ru.hse.spb.common.benchmark.AverageTime;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;

public class MultiThreadBlockingServer extends AbstractBlockingServer {


    public MultiThreadBlockingServer(InetAddress serverAddress,
                                     int port,
                                     AverageTime sortingTime,
                                     AverageTime processingTime) {
        super(serverAddress, port, sortingTime, processingTime);
    }

    @Override
    protected Messages.ArrayMessage handleRequest(Messages.ArrayMessage messageToSort) {
        return ServerUtils.getSortedMessage(messageToSort);
    }

    @Override
    protected void sendResponse(Messages.ArrayMessage sortedMessage, OutputStream os) {
        ServerUtils.sendArrayMessage(sortedMessage, os);
    }
}