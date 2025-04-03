package com.example.whitebboardedition2nd;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;


public class HelloApplication extends Application
{

    InterfaceManager interfaceManager = new InterfaceManager();
    private double xOffset = 0;//for dragging the stage
    private double yOffset = 0;//for dragging the stage
    HBox outSideFunctions = interfaceManager.externalFunctions();
    MediaHandler medium = new MediaHandler(interfaceManager);

    @Override
    public void start(Stage stages) throws IOException
    {
        //interfaceManager.setStage(stage);
        interfaceManager.getStage().initStyle(StageStyle.UNDECORATED);
        FlowPane root = new FlowPane();//setting flow for resizing scene
        VBox    mainLayout = new VBox();

        mainLayout.getChildren().addAll(outSideFunctions,internalFunctions());



        root.setId("root");

        root.getChildren().add(mainLayout);
        root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());


        setPage(interfaceManager.getStage(),new Scene(root,1400,700));


    }

    public static void main(String[] args) {
        launch();
    }

    void setPage(Stage stage, Scene scene)//this is for displaying scenes
    {
        stage.setScene(scene);


        outSideFunctions.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        outSideFunctions.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        stage.show();
    }


    public StackPane ActivePanel()//holds current activities such as drawing
    {

        StackPane actionPanel = new StackPane();

        actionPanel.setId("actionPanel");
        actionPanel.setPrefWidth(1130);
        actionPanel.getChildren().add(currentActive());


        return actionPanel;
    }


    public HBox internalFunctions()//this is for internal activities
    {
        HBox internalFunction = new HBox();

        internalFunction.setPrefHeight(690);
        internalFunction.setPrefWidth(1550);
        internalFunction.getChildren().addAll(interfaceManager.toolPanel(),ActivePanel(),new SettingPanel(interfaceManager).settingPanel());

        return internalFunction;
    }

    public Pane currentActive()
    {

        interfaceManager.setActivities(new Pane());
        Pane activityPane = interfaceManager.getActivities();
        activityPane.setMaxWidth(1000);
        activityPane.setMaxHeight(530);
        activityPane.setId("currentActive");




        interfaceManager.getTextFile().setOnMouseClicked(event ->
        {
            activityPane.getChildren().clear();
            interfaceManager.setPane(new StackPane());
            StackPane paneForDoc = interfaceManager.getPane();
            interfaceManager.getDoc().setText("type something");
            interfaceManager.getDoc().setPrefSize(interfaceManager.getActivities().getWidth(),interfaceManager.getActivities().getHeight());
            paneForDoc.getChildren().add(interfaceManager.getDoc());
            activityPane.getChildren().add(paneForDoc);
            interfaceManager.setPicture(false);
        });

        interfaceManager.getEditCanvas().setOnMouseClicked(event -> {

            activityPane.getChildren().add(drawingImage());
            interfaceManager.setPicture(false);

        });

        interfaceManager.getSaveFile().setOnMouseClicked(event ->
        {

            interfaceManager.saveTextFile();
            interfaceManager.setPicture(false);
        });

        interfaceManager.getOpenFiles().setOnMouseClicked(event ->
        {
                interfaceManager.openTextFile();

        });

        interfaceManager.getOpenMultiMediav().setOnMouseClicked(event ->//uploading the video
        {

            medium.openVideoMediaFile();
            interfaceManager.setPicture(true);
        });

        interfaceManager.getOpenMultiMedia().setOnMouseClicked(event ->//uploading the pictures
        {

            interfaceManager.loadPictures(medium);
            interfaceManager.setPicture(false);

        });

        //saveCanvasDrawing()

        interfaceManager.getSaveCanvas().setOnMouseClicked(event ->
        {
            interfaceManager.saveCanvas();
            interfaceManager.setPicture(false);
        });

        interfaceManager.getNewCanvas().setOnMouseClicked(event -> {
            activityPane.getChildren().add(interfaceManager.drawingAction());
            interfaceManager.setPicture(false);
        });

        interfaceManager.getSound().setOnMouseClicked(event ->
        {
            interfaceManager.setPane(new StackPane());
            medium.openMediaFile();
            interfaceManager.setPicture(true);

        });

        interfaceManager.getClearStage().setOnMouseClicked(event -> {

            interfaceManager.removeActivities( medium);//removing running activity
        });

        return activityPane;
    }

    public StackPane drawingImage()//for drawable image
    {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/main/resources/MultimediaFiles"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        fileChooser.setTitle("Open Image File");
        File file = fileChooser.showOpenDialog(interfaceManager.getStage());

        if (file != null) {
            try {
                interfaceManager.getActivities().getChildren().clear();

                // Load the image
                Image image = new Image(file.toURI().toString());
                ImageView imageView = new ImageView(image);

                // Create canvas with same dimensions as image
                Canvas drawingCanvas = new Canvas(image.getWidth(), image.getHeight());
                interfaceManager.setGraphicsContext(drawingCanvas.getGraphicsContext2D());

                // Set up drawing tools
                interfaceManager.getGraphicsContext().setStroke(Color.BLACK);
                interfaceManager.getGraphicsContext().setLineWidth(2);

                // Create a stack pane to layer image and canvas
                StackPane imageCanvasPane = new StackPane();
                imageCanvasPane.getChildren().addAll(imageView, drawingCanvas);

                // Set up mouse events for drawing
                drawingCanvas.setOnMousePressed(e -> {
                    interfaceManager.getGraphicsContext().beginPath();
                    interfaceManager.getGraphicsContext().moveTo(e.getX(), e.getY());
                    if(interfaceManager.getPenTracker()==1 || interfaceManager.getEraserTracker() == 1) {
                        interfaceManager.getGraphicsContext().stroke();
                    }
                });

                drawingCanvas.setOnMouseDragged(e -> {
                    interfaceManager.getGraphicsContext().lineTo(e.getX(), e.getY());
                    if(interfaceManager.getPenTracker()==1 || interfaceManager.getEraserTracker() == 1) {
                        interfaceManager.getGraphicsContext().stroke();
                    }

                });


                // Add to main activities pane

                interfaceManager.setPane( new StackPane(imageCanvasPane));
                StackPane paneForNow = interfaceManager.getPane();
                interfaceManager.getActivities().getChildren().add(paneForNow);
                interfaceManager.getActivities().setLayoutX(400);
                interfaceManager.getActivities().setLayoutY(70);

                // Connect with existing drawing tools
                interfaceManager.getColorPicker().setOnAction(e -> interfaceManager.getGraphicsContext().setStroke(interfaceManager.getColorPicker().getValue()));
                interfaceManager.getSlider().valueProperty().addListener((obs, oldVal, newVal) ->
                        interfaceManager.graphicsContext.setLineWidth(newVal.doubleValue()));

            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error loading image: " + e.getMessage()).show();
            }
        }

        interfaceManager.setPicture(false);

        return interfaceManager.getPane();
    }

    public static void showErrorAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop()
    {
        // Clean up media player when application closes
        if (medium.getMediaPlayer() != null) {
            medium.getMediaPlayer().stop();
            medium.getMediaPlayer().dispose();
        }
    }


    // Helper method to make any node draggable
    public static void makeDraggable(Node node) {
        final double[] dragDelta = new double[2];

        node.setOnMousePressed(mouseEvent -> {
            // Record the mouse press coordinates relative to the node
            dragDelta[0] = mouseEvent.getX();
            dragDelta[1] = mouseEvent.getY();
            node.toFront(); // Bring to front when clicked
        });

        node.setOnMouseDragged(mouseEvent -> {
            // Calculate new position
            double newX = mouseEvent.getSceneX() - dragDelta[0];
            double newY = mouseEvent.getSceneY() - dragDelta[1];

            // For Pane containers, we need to adjust for their position
            if (node instanceof Pane) {
                node.setLayoutX(newX);
                node.setLayoutY(newY);
            }
            // For other nodes (ImageView, HBox, etc.)
            else {
                if (node.getParent() instanceof Pane) {
                    Pane parent = (Pane) node.getParent();
                    // Ensure we don't drag outside parent bounds
                    newX = Math.max(0, Math.min(newX, parent.getWidth() - node.getBoundsInParent().getWidth()));
                    newY = Math.max(0, Math.min(newY, parent.getHeight() - node.getBoundsInParent().getHeight()));

                    node.setLayoutX(newX);
                    node.setLayoutY(newY);
                }
            }
        });
    }






}