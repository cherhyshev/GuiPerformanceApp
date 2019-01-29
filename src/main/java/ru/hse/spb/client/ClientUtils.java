package ru.hse.spb.client;

import org.jetbrains.annotations.Nullable;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

}
