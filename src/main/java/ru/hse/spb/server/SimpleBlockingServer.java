package ru.hse.spb.server;


import ru.hse.spb.common.protocol.Messages;

import java.io.OutputStream;

public class SimpleBlockingServer extends AbstractBlockingServer {


    public SimpleBlockingServer(int port) {
        super(port);
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