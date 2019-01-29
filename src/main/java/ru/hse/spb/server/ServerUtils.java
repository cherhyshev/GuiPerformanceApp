package ru.hse.spb.server;

import org.jetbrains.annotations.Nullable;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public class ServerUtils {
    public static Messages.ArrayMessage getSortedMessage(@Nullable Messages.ArrayMessage message) {
        if (message != null){
            int[] arr = message.getArrayElementsList().stream().mapToInt(Integer::intValue).toArray();
            bubbleSort(arr);
            Messages.ArrayMessage.Builder builder = Messages.ArrayMessage.newBuilder();
            builder.setElementsNum(arr.length);
            for (int elem : arr) {
                builder.addArrayElements(elem);
            }
            return builder.build();
        }
        return null;
    }

    public static void sendArrayMessage(@Nullable Messages.ArrayMessage responseMessage, OutputStream os) {

        try {
            if (responseMessage != null) {
                responseMessage.writeDelimitedTo(os);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (arr[j - 1] > arr[j]) {
                    int tmp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
    }

}
