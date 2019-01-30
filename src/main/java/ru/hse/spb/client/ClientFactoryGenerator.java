package ru.hse.spb.client;

import ru.hse.spb.common.exception.BadConfigurationException;

import java.util.ArrayList;
import java.util.List;

public class ClientFactoryGenerator {
    private final ClientUtils.ClientFactoryGeneratorConfig config;

    public ClientFactoryGenerator(ClientUtils.ClientFactoryGeneratorConfig config) {
        this.config = config;
    }

    public final List<ClientFactory> generateFactories() {
        List<ClientFactory> factories = new ArrayList<>();
        ClientUtils.VariableParameter var = config.getVariableParameter();
        int count = (var.getLimit() - var.getStart()) / var.getStep();
        ClientUtils.RegularParameter parameterX = config.getParameterX();
        ClientUtils.RegularParameter parameterN;
        ClientUtils.RegularParameter parameterM;
        ClientUtils.RegularParameter parameterD;
        for (int i = 0; i < count; i++) {
            switch (config.getVariableParameter().getParameterName()) {
                case N:
                    parameterN = new ClientUtils.RegularParameter(ClientUtils.ParameterName.N, var.getStart() + var.getStep() * count);
                    parameterM = (config.getParameter1().getParameterName() == ClientUtils.ParameterName.M) ? config.getParameter1() : config.getParameter2();
                    parameterD = (config.getParameter2().getParameterName() == ClientUtils.ParameterName.D) ? config.getParameter2() : config.getParameter1();
                    break;
                case M:
                    parameterM = new ClientUtils.RegularParameter(ClientUtils.ParameterName.M, var.getStart() + var.getStep() * count);
                    parameterN = (config.getParameter1().getParameterName() == ClientUtils.ParameterName.N) ? config.getParameter1() : config.getParameter2();
                    parameterD = (config.getParameter2().getParameterName() == ClientUtils.ParameterName.D) ? config.getParameter2() : config.getParameter1();
                    break;
                case D:
                    parameterD = new ClientUtils.RegularParameter(ClientUtils.ParameterName.D, var.getStart() + var.getStep() * count);
                    parameterN = (config.getParameter1().getParameterName() == ClientUtils.ParameterName.N) ? config.getParameter1() : config.getParameter2();
                    parameterM = (config.getParameter2().getParameterName() == ClientUtils.ParameterName.M) ? config.getParameter2() : config.getParameter1();
                    break;
                default:
                    throw new BadConfigurationException("Variable parameter has label " + config.getVariableParameter().getParameterName());
            }


            factories.add(new ClientFactory(new ClientUtils.ClientFactoryConfig(parameterX, parameterN, parameterM, parameterD,
                    config.getServerPort(), config.getServerAddress(), config.isBlocking())));
        }
        return factories;
    }
}
