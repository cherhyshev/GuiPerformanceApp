package ru.hse.spb.gui;

import cucumber.api.java.eo.Do;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import ru.hse.spb.client.ClientMaster;
import ru.hse.spb.client.ClientUtils;
import ru.hse.spb.common.CommonUtils;
import ru.hse.spb.common.Constants;
import ru.hse.spb.common.benchmark.AverageTime;
import ru.hse.spb.common.benchmark.SingleIterationBenchmark;
import ru.hse.spb.server.ServerMaster;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Controller {
    private String serverAddress;
    private CommonUtils.ArchitectureType architectureType;
    private ClientUtils.ParameterName variableParameter;
    ClientUtils.ClientMasterConfig clientMasterConfig;
    private boolean isSelectedValArraySize, isSelectedValClientsNum, isSelectedValDelay;

    private int lowBound, highBound, step;

    @FXML
    private TextField tf_serverAddress;

    @FXML
    private TextField tf_requestsNum;

    @FXML
    private TextField tf_varLowBound;

    @FXML
    private TextField tf_varHighBound;

    @FXML
    private TextField tf_varStep;

    @FXML
    private CheckBox cb_valOnArraySize;

    @FXML
    private TextField tf_valOnArraySize;

    @FXML
    private CheckBox cb_valOnClientsNum;

    @FXML
    private TextField tf_valOnClientsNum;

    @FXML
    private CheckBox cb_valOnDelay;

    @FXML
    private TextField tf_valOnDelay;

    @FXML
    private LineChart<Integer, Double> lc_AvgSortingTime;

    @FXML
    private NumberAxis x_axis_AvgSortingTime;

    @FXML
    private NumberAxis y_axis_AvgSortingTime;

    @FXML
    private LineChart<Integer, Double> lc_AvgProcessingTime;

    @FXML
    private NumberAxis x_axis_AvgProcessingTime;

    @FXML
    private NumberAxis y_axis_AvgProcessingTime;

    @FXML
    private LineChart<Integer, Double> lc_AvgWorkingTime;

    @FXML
    private NumberAxis x_axis_AvgWorkingTime;

    @FXML
    private NumberAxis y_axis_AvgWorkingTime;
    @FXML
    private MenuButton mb_SelectArch;

    @FXML
    private MenuItem mi_simpleBlockArch;

    @FXML
    private MenuItem mi_ThreadPoolArch;

    @FXML
    private MenuItem mi_NonBlockArch;

    @FXML
    private MenuButton mb_variableParameter;

    @FXML
    private MenuItem mi_varArraySize;

    @FXML
    private MenuItem mi_varClientsNum;

    @FXML
    private MenuItem mi_varClientsDelay;

    @FXML
    void OnBeginAction(ActionEvent event) throws UnknownHostException {
        lc_AvgSortingTime.getData().clear();
        lc_AvgProcessingTime.getData().clear();
        lc_AvgWorkingTime.getData().clear();


        InetAddress address = InetAddress.getLocalHost();
        if (tf_serverAddress.getText() != null && !tf_serverAddress.getText().equals("")) {
            serverAddress = tf_serverAddress.getText();
            address = InetAddress.getByName(serverAddress);

        }

        ServerMaster serverMaster = new ServerMaster(address, Constants.SERVER_MASTER_PORT,
                new AverageTime(), new AverageTime());
        ExecutorService serverMasterService = Executors.newSingleThreadExecutor();
        serverMasterService.submit(serverMaster);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        lowBound = Integer.parseInt(tf_varLowBound.getText());
        highBound = Integer.parseInt(tf_varHighBound.getText());
        step = Integer.parseInt(tf_varStep.getText());
        ClientUtils.VariableParameter var = new ClientUtils.VariableParameter(variableParameter, lowBound, highBound, step);


        switch (variableParameter) {
            case N:
                clientMasterConfig = new ClientUtils.ClientMasterConfig(
                        var,
                        new ClientUtils.RegularParameter(ClientUtils.ParameterName.X,
                                Integer.parseInt(tf_requestsNum.getText())),
                        new ClientUtils.RegularParameter(ClientUtils.ParameterName.M,
                                Integer.parseInt(tf_valOnClientsNum.getText())),
                        new ClientUtils.RegularParameter(ClientUtils.ParameterName.D,
                                Integer.parseInt(tf_valOnDelay.getText())),
                        address,
                        Constants.SERVER_MASTER_PORT,
                        Constants.SERVER_PROCESSING_PORT,
                        architectureType);
                break;
            case M:
                clientMasterConfig = new ClientUtils.ClientMasterConfig(
                        var,
                        new ClientUtils.RegularParameter(ClientUtils.ParameterName.X,
                                Integer.parseInt(tf_requestsNum.getText())),
                        new ClientUtils.RegularParameter(ClientUtils.ParameterName.N,
                                Integer.parseInt(tf_valOnArraySize.getText())),
                        new ClientUtils.RegularParameter(ClientUtils.ParameterName.D,
                                Integer.parseInt(tf_valOnDelay.getText())),
                        address,
                        Constants.SERVER_MASTER_PORT,
                        Constants.SERVER_PROCESSING_PORT,
                        architectureType);
                break;
            case D:
                clientMasterConfig = new ClientUtils.ClientMasterConfig(
                        var,
                        new ClientUtils.RegularParameter(ClientUtils.ParameterName.X,
                                Integer.parseInt(tf_requestsNum.getText())),
                        new ClientUtils.RegularParameter(ClientUtils.ParameterName.N,
                                Integer.parseInt(tf_valOnArraySize.getText())),
                        new ClientUtils.RegularParameter(ClientUtils.ParameterName.M,
                                Integer.parseInt(tf_valOnClientsNum.getText())),
                        address,
                        Constants.SERVER_MASTER_PORT,
                        Constants.SERVER_PROCESSING_PORT,
                        architectureType);
                break;
            default:
//                TODO: как-нибудь поругаться, но надо ли
                clientMasterConfig = new ClientUtils.ClientMasterConfig(
                        new ClientUtils.VariableParameter(ClientUtils.ParameterName.N, 100, 5101, 500),
                        new ClientUtils.RegularParameter(ClientUtils.ParameterName.X, 10),
                        new ClientUtils.RegularParameter(ClientUtils.ParameterName.M, 10),
                        new ClientUtils.RegularParameter(ClientUtils.ParameterName.D, 10),
                        address,
                        Constants.SERVER_MASTER_PORT,
                        Constants.SERVER_PROCESSING_PORT,
                        architectureType);

        }

        ClientMaster clientMaster = new ClientMaster(clientMasterConfig);
        ExecutorService clientMasterService = Executors.newSingleThreadExecutor();
        clientMasterService.submit(clientMaster);
        CommonUtils.delicateFinishTasks(clientMasterService);
        List<SingleIterationBenchmark> benchmarkList = clientMaster.getBenchmarkList();
        serverMaster.stop();
        serverMasterService.shutdownNow();

        XYChart.Series<Integer, Double> sortingTimes = new XYChart.Series<>();
        XYChart.Series<Integer, Double> processingTimes = new XYChart.Series<>();
        XYChart.Series<Integer, Double> workingTimes = new XYChart.Series<>();

        StringBuilder builder = new StringBuilder();

        for (SingleIterationBenchmark benchmark : benchmarkList) {
            sortingTimes.getData().add(new XYChart.Data<>(benchmark.getValueParam(), benchmark.getAverageSortingTime()));
            processingTimes.getData().add(new XYChart.Data<>(benchmark.getValueParam(), benchmark.getAverageRequestTime()));
            workingTimes.getData().add(new XYChart.Data<>(benchmark.getValueParam(), benchmark.getAverageClientTime()));
            builder.append(benchmark.getValueParam()).append(";")
                    .append(benchmark.getAverageSortingTime()).append(";")
                    .append(benchmark.getAverageRequestTime()).append(";")
                    .append(benchmark.getAverageClientTime()).append(";\n");
        }

        lc_AvgSortingTime.getData().add(sortingTimes);
        lc_AvgProcessingTime.getData().add(processingTimes);
        lc_AvgWorkingTime.getData().add(workingTimes);

        String fileName = variableParameter.name() + "_" + architectureType.name() + "_"
                + clientMasterConfig.getParameter1().getParameterName() + "="
                + clientMasterConfig.getParameter1().getValue() + "_"
                + clientMasterConfig.getParameter2().getParameterName() + "="
                + clientMasterConfig.getParameter2().getValue();

        File file = new File(fileName + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void on_saveCharts(ActionEvent event) {
        String fileName = variableParameter.name() + "_" + architectureType.name() + "_"
                + clientMasterConfig.getParameter1().getParameterName() + "="
                + clientMasterConfig.getParameter1().getValue() + "_"
                + clientMasterConfig.getParameter2().getParameterName() + "="
                + clientMasterConfig.getParameter2().getValue();

        GuiUtils.saveSceneAsImage(fileName, lc_AvgSortingTime.getScene());
    }

    @FXML
    void on_NonBlockArch(ActionEvent event) {
        architectureType = CommonUtils.ArchitectureType.NON_BLOCKING;
        mb_SelectArch.setText(mi_NonBlockArch.getText());
    }

    @FXML
    void on_ThreadPoolArch(ActionEvent event) {
        architectureType = CommonUtils.ArchitectureType.THREAD_POOL_BLOCKING;
        mb_SelectArch.setText(mi_ThreadPoolArch.getText());
    }

    @FXML
    void on_simpleBlockArch(ActionEvent event) {
        architectureType = CommonUtils.ArchitectureType.MULTI_THREAD_BLOCKING;
        mb_SelectArch.setText(mi_simpleBlockArch.getText());
    }

    @FXML
    void on_varArraySize(ActionEvent event) {
        variableParameter = ClientUtils.ParameterName.N;
        mb_variableParameter.setText(mi_varArraySize.getText());
    }

    @FXML
    void on_varClientsDelay(ActionEvent event) {
        variableParameter = ClientUtils.ParameterName.D;
        mb_variableParameter.setText(mi_varClientsDelay.getText());

    }

    @FXML
    void on_varClientsNum(ActionEvent event) {
        variableParameter = ClientUtils.ParameterName.M;
        mb_variableParameter.setText(mi_varClientsNum.getText());

    }

    @FXML
    void on_valArraySize(ActionEvent event) {
        isSelectedValArraySize = true;

    }

    @FXML
    void on_valClientsNum(ActionEvent event) {
        isSelectedValClientsNum = true;

    }

    @FXML
    void on_valDelay(ActionEvent event) {
        isSelectedValDelay = true;

    }


}
