package ru.hse.spb.server;

import ru.hse.spb.common.CommonUtils;
import ru.hse.spb.common.benchmark.AverageTime;
import ru.hse.spb.common.protocol.Messages;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NonBlockingServer extends AbstractServer {
    private final ExecutorService executorService =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private volatile Selector selector;
    private volatile ServerSocketChannel serverSocketChannel;

    public NonBlockingServer(InetAddress serverAddress,
                             int port,
                             AverageTime sortingTime,
                             AverageTime processingTime) {
        super(serverAddress, port, sortingTime, processingTime);
    }


    @Override
    public void run() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(serverAddress, serverPort), Integer.MAX_VALUE);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (serverSocketChannel.isOpen()) {
                int keysNum = selector.select();
                if (selector.isOpen() && keysNum > 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey selectionKey = keyIterator.next();
                        if (selectionKey.isAcceptable()) {
                            accept(selectionKey);
                        } else {
                            if (selectionKey.isReadable()) {
                                read(selectionKey);
                            }

                            if (selectionKey.isValid() && selectionKey.isWritable()) {
                                write(selectionKey);
                            }
                        }
                        keyIterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            close();
            throw new RuntimeException("Exception during non blocking server work", e);
        }
    }

    public void stop() {
        close();
    }

    private void close() {
        if (serverSocketChannel != null) {
            try {
                serverSocketChannel.close();
            } catch (IOException e) {
                throw new RuntimeException("Exception during close server socket channel", e);
            }
        }

        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                throw new RuntimeException("Exception during close selector", e);
            }
        }
        executorService.shutdown();
    }

    private void accept(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,
                SelectionKey.OP_READ,
                null);
    }

    private void read(SelectionKey selectionKey) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        if (selectionKey.attachment() == null) {
            selectionKey.attach(
                    new SizeReadingAttachment(System.currentTimeMillis()));
        }
        AbstractAttachment attachment = (AbstractAttachment) selectionKey.attachment();

        if (attachment instanceof SizeReadingAttachment) {
            SizeReadingAttachment sizeReadingAttachment = (SizeReadingAttachment) attachment;
            int read = channel.read(sizeReadingAttachment.sizeBuffer);
            if (read == -1) {
                selectionKey.cancel();
                channel.close();
            }
            if (!sizeReadingAttachment.sizeBuffer.hasRemaining()) {
                selectionKey.attach(
                        new MessageReadingAttachment(sizeReadingAttachment));
            }
        } else if (attachment instanceof MessageReadingAttachment) {
            MessageReadingAttachment messageReadingAttachment = (MessageReadingAttachment) attachment;
            channel.read(messageReadingAttachment.messageBuffer);
            if (!messageReadingAttachment.messageBuffer.hasRemaining()) {

                Messages.ArrayMessage arrayMessage = Messages.ArrayMessage.parseFrom(messageReadingAttachment.messageBuffer.array());

                Future<ByteBuffer> futureResponse = executorService.submit(() -> {
                    long processingStart = System.currentTimeMillis();
                    Messages.ArrayMessage resultMessage = ServerUtils.getSortedMessage(arrayMessage);
                    addSortingTime(System.currentTimeMillis() - processingStart);
                    return ByteBuffer.wrap(CommonUtils.serialize(resultMessage));
                });

                channel.register(selector,
                        SelectionKey.OP_WRITE,
                        new ProcessingAttachment(messageReadingAttachment, futureResponse));
            }
        }
    }

    private void write(SelectionKey selectionKey) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        if (selectionKey.attachment() == null) {
            return;
        }
        AbstractAttachment attachment = (AbstractAttachment) selectionKey.attachment();

        if (attachment instanceof ProcessingAttachment) {
            ProcessingAttachment processingAttachment = (ProcessingAttachment) attachment;
            if (processingAttachment.futureResponse.isDone()) {
                try {
                    WriteAttachment writeAttachment = new WriteAttachment(processingAttachment, processingAttachment.futureResponse.get());
                    channel.write(writeAttachment.messageBuffer);
                    if (writeAttachment.messageBuffer.hasRemaining()) {
                        selectionKey.attach(new WriteAttachment(processingAttachment, writeAttachment.messageBuffer));
                    } else {
                        addProcessingTime(System.currentTimeMillis() - attachment.startRequestProcessingTime);
                        selectionKey.attach(null);
                        channel.register(selector,
                                SelectionKey.OP_READ,
                                null);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    channel.close();
                    throw new RuntimeException("Exception while processing message.", e);
                }
            }
        } else if (attachment instanceof WriteAttachment) {
            WriteAttachment writeAttachment = (WriteAttachment) attachment;
            channel.write(writeAttachment.messageBuffer);
            if (!writeAttachment.messageBuffer.hasRemaining()) {
                addProcessingTime(System.currentTimeMillis() - writeAttachment.startRequestProcessingTime);
                selectionKey.attach(null);
                channel.register(selector,
                        SelectionKey.OP_READ,
                        null);
            }
        }
    }

    private abstract class AbstractAttachment {
        final long startRequestProcessingTime;

        AbstractAttachment(long startRequestProcessingTime) {
            this.startRequestProcessingTime = startRequestProcessingTime;
        }
    }

    private final class SizeReadingAttachment extends AbstractAttachment {
        private final ByteBuffer sizeBuffer;

        SizeReadingAttachment(long startRequestProcessingTime) {
            super(startRequestProcessingTime);
            sizeBuffer = ByteBuffer.allocate(Integer.BYTES);
        }
    }

    private final class MessageReadingAttachment extends AbstractAttachment {
        private final ByteBuffer messageBuffer;

        MessageReadingAttachment(SizeReadingAttachment sizeReadingAttachment) {
            super(sizeReadingAttachment.startRequestProcessingTime);
            sizeReadingAttachment.sizeBuffer.flip();
            messageBuffer = ByteBuffer.allocate(sizeReadingAttachment.sizeBuffer.getInt());
        }
    }

    private final class ProcessingAttachment extends AbstractAttachment {
        private final Future<ByteBuffer> futureResponse;

        ProcessingAttachment(MessageReadingAttachment messageReadingAttachment,
                             Future<ByteBuffer> futureResponse) {
            super(messageReadingAttachment.startRequestProcessingTime);
            this.futureResponse = futureResponse;
        }
    }

    private final class WriteAttachment extends AbstractAttachment {
        private final ByteBuffer messageBuffer;

        WriteAttachment(ProcessingAttachment processingAttachment, ByteBuffer messageBuffer) {
            super(processingAttachment.startRequestProcessingTime);
            this.messageBuffer = messageBuffer;
        }
    }


}
