package ru.hse.spb.client;

import java.util.ArrayList;
import java.util.List;

public class ClientFactory {
    private final ClientUtils.ClientFactoryConfig clientFactoryConfig;

    public ClientFactory(ClientUtils.ClientFactoryConfig clientFactoryConfig) {
        this.clientFactoryConfig = clientFactoryConfig;
    }

    public final List<AbstractClient> generateClients() {
        List<AbstractClient> clients = new ArrayList<>();
        for (int i = 0; i < clientFactoryConfig.getParameterM().getValue(); i++) {
            if (clientFactoryConfig.isBlocking()) {
                clients.add(new BlockingClient(clientFactoryConfig.getClientConfig()));
            } else {
                clients.add(new NonBlockingClient(clientFactoryConfig.getClientConfig()));
            }
        }
        return clients;
    }
}
