package ru.hse.spb.client;

import org.jetbrains.annotations.NotNull;
import ru.hse.spb.common.CommonUtils;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

public class NonBlockingClient extends AbstractClient {
    public NonBlockingClient(int elementsInArray, int deltaInMs, int requestNum, int serverPort, @NotNull InetAddress serverAddress) {
        super(elementsInArray, deltaInMs, requestNum, serverPort, serverAddress);
    }

    @Override
    protected void process(InputStream is, OutputStream os) throws IOException {
        Messages.ArrayMessage msg = ClientUtils.generateMessage(100);
        byte[] messageBytes = CommonUtils.serialize(msg);
        os.write(messageBytes);
        os.flush();
        int readCount = is.read(messageBytes);
        while (readCount < messageBytes.length) {
            readCount += is.read(messageBytes, readCount, messageBytes.length - readCount);
        }

        Messages.ArrayMessage receivedMessage = CommonUtils.deserialize(messageBytes);
    }
}
