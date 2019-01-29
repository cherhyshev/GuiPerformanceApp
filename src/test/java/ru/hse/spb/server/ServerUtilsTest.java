package ru.hse.spb.server;

import org.junit.Test;
import ru.hse.spb.client.ClientUtils;
import ru.hse.spb.common.protocol.Messages;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ServerUtilsTest {

    @Test
    public void getArrayMessageTest1() {
        Random rand = new Random();

        Messages.ArrayMessage.Builder builder = Messages.ArrayMessage.newBuilder();
        builder.setElementsNum(5000);
        for (int i = 0; i < 5000; i++) {
            builder.addArrayElements(rand.nextInt());
        }
        Messages.ArrayMessage messageToSort = builder.build();
        Messages.ArrayMessage sortedMessage = ServerUtils.getSortedMessage(messageToSort);
        for (int i = 1; i < sortedMessage.getElementsNum(); i++) {
            assertTrue(sortedMessage.getArrayElements(i - 1)
                    <= sortedMessage.getArrayElements(i));
        }
    }

    @Test
    public void getArrayMessageTest2() {
        Random rand = new Random();

        List<Messages.ArrayMessage> messageList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            messageList.add(ClientUtils.generateMessage(1000));
        }
        List<Messages.ArrayMessage> sortedMessagesList = new ArrayList<>();
        for (Messages.ArrayMessage msg : messageList) {
            sortedMessagesList.add(ServerUtils.getSortedMessage(msg));
        }
        for (Messages.ArrayMessage sortedMessage : sortedMessagesList) {
            for (int i = 1; i < sortedMessage.getElementsNum(); i++) {
                assertTrue(sortedMessage.getArrayElements(i - 1)
                        <= sortedMessage.getArrayElements(i));
            }
        }
    }


    @Test
    public void sendArrayMessageTest1() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Messages.ArrayMessage originMessage1 = ClientUtils.generateMessage(10);
        ServerUtils.sendArrayMessage(originMessage1, os);

        Messages.ArrayMessage originMessage2 = ClientUtils.generateMessage(10);
        Messages.ArrayMessage receivedMessage1 = null;
        Messages.ArrayMessage receivedMessage2 = null;
        ServerUtils.sendArrayMessage(originMessage2, os);

        try {
            final InputStream is = new ByteArrayInputStream(os.toByteArray());
            receivedMessage1 = Messages.ArrayMessage.parseDelimitedFrom(is);
            receivedMessage2 = Messages.ArrayMessage.parseDelimitedFrom(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(receivedMessage1);
        assertEquals(originMessage1.getElementsNum(), receivedMessage1.getElementsNum());
        assertEquals(originMessage1.getArrayElementsList(), receivedMessage1.getArrayElementsList());

        assertNotNull(receivedMessage2);
        assertEquals(originMessage2.getElementsNum(), receivedMessage2.getElementsNum());
        assertEquals(originMessage2.getArrayElementsList(), receivedMessage2.getArrayElementsList());
    }

    @Test
    public void sendArrayMessageTest2() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        List<Messages.ArrayMessage> messageList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            messageList.add(ClientUtils.generateMessage(1000));
        }

        for (int i = 0; i < 1000; i++) {
            ServerUtils.sendArrayMessage(messageList.get(i), os);
        }

        List<Messages.ArrayMessage> receivedMessageList = new ArrayList<>();
        final InputStream is = new ByteArrayInputStream(os.toByteArray());
        while (is.available() > 0) {
            try {
                receivedMessageList.add(Messages.ArrayMessage.parseDelimitedFrom(is));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        assert (messageList.size() == receivedMessageList.size());
        for (int i = 0; i < 1000; i++) {
            assertEquals(messageList.get(i).getElementsNum(),
                    receivedMessageList.get(i).getElementsNum());
            assertEquals(messageList.get(i).getArrayElementsList(),
                    receivedMessageList.get(i).getArrayElementsList());
        }

    }


}