package ru.hse.spb.common;

import com.google.protobuf.InvalidProtocolBufferException;
import org.jetbrains.annotations.NotNull;
import ru.hse.spb.common.protocol.Messages;

import java.io.*;

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

}
