package ru.hse.spb.common;

import ru.hse.spb.common.protocol.Messages;

import java.io.*;
import java.util.concurrent.ExecutorService;

public class CommonUtils {
    public static Messages.ArrayMessage deserialize(byte[] message) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message);
        DataInputStream dis = new DataInputStream(byteArrayInputStream);
        int serializedSize = dis.readInt();

        return Messages.ArrayMessage.parseFrom(dis);
    }

    public static byte[] serialize(Messages.ArrayMessage message) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(message.getSerializedSize() + 4);
        new DataOutputStream(byteArrayOutputStream).writeInt(message.getSerializedSize());
        message.writeTo(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static void delicateFinishTasks(ExecutorService service) {
        service.shutdown();
        while (!service.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public enum ArchitectureType {
        MULTI_THREAD_BLOCKING,
        THREAD_POOL_BLOCKING,
        NON_BLOCKING
    }


}
