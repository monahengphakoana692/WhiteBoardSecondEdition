package com.example.whitebboardedition2nd;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application
{





    @Override
    public void start(Stage stage) throws IOException
    {
        FlowPane root = new FlowPane();//setting flow for resizing scene
        VBox    mainLayout = new VBox();


        HBox externalFunction = new HBox();//this is for functions of adding and removing external source like images
        HBox internalFunction = new HBox();//this is for functions of adding and removing internal sources like drawings


        externalFunction.setStyle("-fx-background-color:blue;");
        externalFunction.setPrefHeight(40);
        externalFunction.setPrefWidth(1550);

        internalFunction.setStyle("-fx-background-color:lime;");
        internalFunction.setPrefHeight(770);
        internalFunction.setPrefWidth(1550);
        internalFunction.getChildren().addAll(toolsPanel(),ActivePanel(),settingsPanel());


        mainLayout.getChildren().addAll(externalFunction,internalFunction);


        root.setStyle("-fx-background-color:red;");
        root.getChildren().add(mainLayout);


        setPage(stage,new Scene(root,1520,800));
    }

    public static void main(String[] args) {
        launch();
    }

    void setPage(Stage stage, Scene scene)//this is for displaying scenes
    {
        stage.setScene(scene);
        stage.setTitle("Multimedia");
        stage.show();
    }

    public VBox toolsPanel()//holds tools to be used for drawing
    {
        VBox toolsSet = new VBox();

        toolsSet.setStyle("-fx-background-color:purple;");
        toolsSet.setPrefHeight(770);
        toolsSet.setPrefWidth(50);

        return toolsSet;
    }

    public StackPane ActivePanel()//holds current activities such as drawing
    {
        StackPane actionPanel = new StackPane();
        actionPanel.setStyle("-fx-background-color:black;");
        actionPanel.setPrefWidth(1300);

        return actionPanel;
    }

    public VBox settingsPanel()//for holding color setting and other setting
    {
        VBox settings = new VBox();
        settings.setStyle("-fx-background-color:yellow;");
        settings.setPrefWidth(200);
        settings.setPrefHeight(770);

        return settings;
    }

}