package com.example.whitebboardedition2nd;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class WhiteboardApp extends Application {
    private List<String> serverLog = new ArrayList<>();
    private TextArea serverLogArea = new TextArea();
    private int userCount = 0;

    @Override
    public void start(Stage primaryStage) {
        // Server Window
        Stage serverStage = new Stage();
        serverStage.setTitle("Server Log");
        serverLogArea.setEditable(false);
        serverStage.setScene(new Scene(new VBox(new Label("Server Log"), serverLogArea), 300, 400));
        serverStage.show();

        // Button to create new user windows
        Button addUserButton = new Button("Add User Window");
        addUserButton.setOnAction(e -> createUserWindow());

        Stage controlStage = new Stage();
        controlStage.setTitle("Control Panel");
        controlStage.setScene(new Scene(new VBox(new Label("Manage Users"), addUserButton), 200, 150));
        controlStage.show();
    }

    private void createUserWindow() {
        userCount++;
        Stage userStage = new Stage();
        userStage.setTitle("User " + userCount);
        TextField inputField = new TextField();
        Button sendButton = new Button("Send to Server");
        sendButton.setOnAction(e -> sendToServer("User " + userCount + ": " + inputField.getText()));
        userStage.setScene(new Scene(new VBox(new Label("Enter Text:"), inputField, sendButton), 300, 200));
        userStage.show();
    }

    private void sendToServer(String message) {
        if (!message.isEmpty()) {
            serverLog.add(message);
            serverLogArea.appendText(message + "\n");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
