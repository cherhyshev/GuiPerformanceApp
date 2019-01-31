package ru.hse.spb;

import ru.hse.spb.client.ClientMaster;
import ru.hse.spb.client.ClientUtils;
import ru.hse.spb.common.CommonUtils;
import ru.hse.spb.common.Constants;
import ru.hse.spb.common.benchmark.AverageTime;
import ru.hse.spb.server.ServerMaster;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {
    public static void main(String[] args) throws UnknownHostException {

        ServerMaster serverMaster = new ServerMaster(InetAddress.getLocalHost(), Constants.SERVER_MASTER_PORT,
                new AverageTime(), new AverageTime());
        Thread serverMasterThread = new Thread(serverMaster);
        serverMasterThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ClientUtils.ClientMasterConfig config = new ClientUtils.ClientMasterConfig(
                new ClientUtils.VariableParameter(ClientUtils.ParameterName.M, 5, 25, 2),
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.X, 10),
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.N, 500),
                new ClientUtils.RegularParameter(ClientUtils.ParameterName.D, 10),
                InetAddress.getLocalHost(), Constants.SERVER_MASTER_PORT, Constants.SERVER_PROCESSING_PORT,
                CommonUtils.ArchitectureType.MULTI_THREAD_BLOCKING);

                ClientMaster clientMaster = new ClientMaster(config);

        ExecutorService service1 = Executors.newSingleThreadExecutor();
        service1.submit(clientMaster);
        CommonUtils.delicateFinishTasks(service1);
        serverMaster.stop();
        serverMasterThread.interrupt();
        System.exit(0);
    }

    private static List<ClientUtils.ClientMasterConfig> generateConfigList() throws UnknownHostException {
        List<ClientUtils.ClientMasterConfig> configs = new ArrayList<>();
        for (CommonUtils.ArchitectureType type : CommonUtils.ArchitectureType.values()) {
            configs.add(new ClientUtils.ClientMasterConfig(
                    new ClientUtils.VariableParameter(ClientUtils.ParameterName.N, 100, 5101, 500),
                    new ClientUtils.RegularParameter(ClientUtils.ParameterName.X, 10),
                    new ClientUtils.RegularParameter(ClientUtils.ParameterName.M, 10),
                    new ClientUtils.RegularParameter(ClientUtils.ParameterName.D, 10),
                    InetAddress.getLocalHost(), Constants.SERVER_MASTER_PORT, Constants.SERVER_PROCESSING_PORT,
                    type));
            configs.add(new ClientUtils.ClientMasterConfig(
                    new ClientUtils.VariableParameter(ClientUtils.ParameterName.M, 5, 25, 2),
                    new ClientUtils.RegularParameter(ClientUtils.ParameterName.X, 10),
                    new ClientUtils.RegularParameter(ClientUtils.ParameterName.N, 500),
                    new ClientUtils.RegularParameter(ClientUtils.ParameterName.D, 10),
                    InetAddress.getLocalHost(), Constants.SERVER_MASTER_PORT, Constants.SERVER_PROCESSING_PORT,
                    type));
            configs.add(new ClientUtils.ClientMasterConfig(
                    new ClientUtils.VariableParameter(ClientUtils.ParameterName.D, 1, 101, 10),
                    new ClientUtils.RegularParameter(ClientUtils.ParameterName.X, 10),
                    new ClientUtils.RegularParameter(ClientUtils.ParameterName.N, 500),
                    new ClientUtils.RegularParameter(ClientUtils.ParameterName.M, 10),
                    InetAddress.getLocalHost(), Constants.SERVER_MASTER_PORT, Constants.SERVER_PROCESSING_PORT,
                    type));

        }
        return configs;
    }

}
