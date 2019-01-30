package ru.hse.spb.client;

import org.jetbrains.annotations.NotNull;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

public class BlockingClient extends AbstractClient {
    public BlockingClient(int elementsInArray, int deltaInMs, int requestNum, int serverPort, @NotNull InetAddress serverAddress) {
        super(elementsInArray, deltaInMs, requestNum, serverPort, serverAddress);
    }

    @Override
    protected void process(InputStream is, OutputStream os) throws IOException {
        Messages.ArrayMessage msg = ClientUtils.generateMessage(1000);
        msg.writeDelimitedTo(os);
        os.flush();
        Messages.ArrayMessage receivedMessage = Messages.ArrayMessage.parseDelimitedFrom(is);

    }
}
