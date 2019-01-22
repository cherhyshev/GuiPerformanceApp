package ru.hse.spb.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClientGUI extends Application {
    public static void main(String[] args) {
        System.out.println("Start");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane mainPane = FXMLLoader.load(getClass().getResource("/Main.fxml"));
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.show();
    }
}
