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

    public final int getX() {
        return clientFactoryConfig.getClientConfig().getRequestNum();
    }

    public final int getN() {
        return clientFactoryConfig.getClientConfig().getElementsInArray();
    }

    public final int getD() {
        return clientFactoryConfig.getClientConfig().getDeltaInMs();
    }

    public final int getM() {
        return clientFactoryConfig.getParameterM().getValue();
    }


}
