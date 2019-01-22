package ru.hse.spb.common.protocol;

import com.google.protobuf.InvalidProtocolBufferException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class ArrayMessageHandler {
    private ArrayMessageHandler() {
    }

    public static Messages.ArrayMessage generate(int elementsCount) {
        Messages.ArrayMessage.Builder builder = Messages.ArrayMessage.newBuilder();
        builder.setElementsNum(elementsCount);
        Random random = new Random(3571);
        for (int i = 0; i < elementsCount; i++) {
            builder.addArrayElements(random.nextInt());
        }
        return builder.build();
    }


    public static byte[] serialize(Messages.ArrayMessage message) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(message.getSerializedSize() + 4);
        new DataOutputStream(byteStream).writeInt(message.getSerializedSize());
        message.writeTo(byteStream);
        return byteStream.toByteArray();
    }

    public static Messages.ArrayMessage deserialize(byte[] message) throws InvalidProtocolBufferException {
        return Messages.ArrayMessage.parseFrom(message);
    }

    public static Messages.ArrayMessage process(Messages.ArrayMessage arrayMessage) {
        int[] elements = new int[arrayMessage.getElementsNum()];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = arrayMessage.getArrayElements(i);
        }
        bubbleSort(elements);
        Messages.ArrayMessage.Builder builder = arrayMessage.toBuilder();
        for (int i = 0; i < elements.length; i++) {
            builder.setArrayElements(i, elements[i]);
        }
        return builder.build();
    }

    private static void bubbleSort(int[] elements) {
        for (int i = 0; i < elements.length; i++) {
            for (int j = i + 1; j < elements.length; j++) {
                if (elements[j] < elements[i]) {
                    int temp = elements[i];
                    elements[i] = elements[j];
                    elements[j] = temp;
                }
            }
        }
    }

}
