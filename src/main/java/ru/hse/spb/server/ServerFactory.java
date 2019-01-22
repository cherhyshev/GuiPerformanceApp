package ru.hse.spb.server;

import ru.hse.spb.common.exception.NoSuchArchitectureException;
import ru.hse.spb.common.protocol.Configurations;

public class ServerFactory {
    public static AbstractServer generate(Configurations.ServerConfig config) {
        switch (config.getTypeValue()) {
            case 0:
                return new SimpleBlockingServer();
            case 1:
                return new ThreadPoolBlockingServer();
            case 2:
                return new NonBlockingServer();
            default:
                throw new NoSuchArchitectureException();
        }
    }
}
