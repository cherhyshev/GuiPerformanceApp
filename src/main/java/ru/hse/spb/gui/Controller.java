package ru.hse.spb.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import ru.hse.spb.common.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Controller {

    @FXML
    private TextField tf_serverAddress;

    @FXML
    private RadioButton rb_blocking1;

    @FXML
    private RadioButton rb_blocking2;

    @FXML
    private RadioButton rb_nonblocking;

    @FXML
    private TextField tf_requestsNum;

    @FXML
    private RadioButton rb_varOnArraySize;

    @FXML
    private RadioButton rb_varOnClientsNum;

    @FXML
    private RadioButton rb_varOnDelay;

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
    void OnBeginAction(ActionEvent event) throws UnknownHostException {
        GuiResponse response = null;
        int architecture = 0;
        if (rb_blocking1.isSelected()) {
            architecture = 0;
        } else if (rb_blocking2.isSelected()) {
            architecture = 1;
        } else if (rb_nonblocking.isSelected()) {
            architecture = 2;
        }

        InetAddress serverAddress = InetAddress.getByName(tf_serverAddress.getText());

        int requestsPerClient = Integer.parseInt(tf_requestsNum.getText());

        Constants.VariableParameter variableParameter = null;
        int lowBound = Integer.parseInt(tf_varLowBound.getText());
        int highBound = Integer.parseInt(tf_varHighBound.getText());
        int step = Integer.parseInt(tf_varStep.getText());
        int firstParam = 0, secondParam = 0;

        if (rb_varOnArraySize.isSelected() && cb_valOnClientsNum.isSelected() && cb_valOnDelay.isSelected()) {
            variableParameter = Constants.VariableParameter.N;
            firstParam = Integer.parseInt(tf_valOnClientsNum.getText());
            secondParam = Integer.parseInt(tf_valOnDelay.getText());
        } else if (rb_varOnClientsNum.isSelected() && cb_valOnArraySize.isSelected() && cb_valOnDelay.isSelected()) {
            variableParameter = Constants.VariableParameter.M;
            firstParam = Integer.parseInt(tf_valOnArraySize.getText());
            secondParam = Integer.parseInt(tf_valOnDelay.getText());

        } else if (rb_varOnDelay.isSelected() && cb_valOnArraySize.isSelected() && cb_valOnDelay.isSelected()) {
            variableParameter = Constants.VariableParameter.D;
            firstParam = Integer.parseInt(tf_valOnArraySize.getText());
            secondParam = Integer.parseInt(tf_valOnClientsNum.getText());
        }

        response = new GuiResponse(architecture, serverAddress, requestsPerClient, variableParameter, lowBound, highBound, step, firstParam, secondParam);
        //TODO запускаем сервер, клиенты, начинаем работу
        System.exit(0);

    }

}
