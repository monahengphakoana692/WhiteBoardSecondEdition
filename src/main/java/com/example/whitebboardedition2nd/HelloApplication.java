package com.example.whitebboardedition2nd;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class HelloApplication extends Application
{

    InterfaceManager interfaceManager = new InterfaceManager();
    private double xOffset = 0;//for dragging the stage
    private double yOffset = 0;//for dragging the stage
    private double startX, startY;//for creating a text field
    private Rectangle selectionRect;
    private Circle tempCircle;
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

    public VBox toolsPanel()//holds tools to be used for drawing
    {
        VBox toolsSet = new VBox();
        toolsSet.setId("toolPanel");
        toolsSet.setPrefHeight(600);
        toolsSet.setPrefWidth(70);

        Image penImage = new Image(getClass().getResourceAsStream("/pen.png"));
        Image erazeImage = new Image(getClass().getResourceAsStream("/eraser.png"));
        ImageView penTool = new ImageView(penImage);
        ImageView erasertool = new ImageView(erazeImage);
        penTool.setFitWidth(40);
        penTool.setFitHeight(40);
        erasertool.setFitWidth(40);
        erasertool.setFitHeight(40);

        Pane toolHolder = new Pane(penTool);
        Pane eraserHolder = new Pane(erasertool);
        Label textTool = new Label("T");
        textTool.setStyle("-fx-text-fill:white;" +
                "-fx-font-size:30px;");

        toolHolder.setId("toolHolder");
        toolHolder.setPrefSize(20, 40);

        eraserHolder.setPrefSize(20, 40);
        eraserHolder.setId("eraserHolder");

        // When clicked, update penTracker and change the background color
        toolHolder.setOnMouseClicked(event ->
        {
            interfaceManager.setPenTracker(1);
            interfaceManager.setEraserTracker(0);
            interfaceManager.setTextTool(false);
            toolHolder.setStyle("-fx-background-color:gray;");
            eraserHolder.setStyle("-fx-background-color:white;");
            interfaceManager.getGraphicsContext().setStroke(interfaceManager.getColorPicker().getValue());
            interfaceManager.getGraphicsContext().setLineWidth(interfaceManager.getSlider().getValue());
            ImageCursor penCursor = new ImageCursor(penImage);
            interfaceManager.getPane().setCursor(penCursor);

        });
        eraserHolder.setOnMouseClicked(event ->
        {
            //preparing eraser

            interfaceManager.setPenTracker(0);
            interfaceManager.setEraserTracker(1);
            interfaceManager.setTextTool(false);
            eraserHolder.setStyle("-fx-background-color:gray;");
            toolHolder.setStyle("-fx-background-color:white;");
           interfaceManager.getGraphicsContext().setStroke(Color.WHITE);
            interfaceManager.getGraphicsContext().setLineWidth(8);
            Image eraserImage = new Image(getClass().getResourceAsStream("/eraser.png"));
            ImageCursor eraserCursor = new ImageCursor(eraserImage);
            interfaceManager.getPane().setCursor(eraserCursor);
        });

        textTool.setOnMouseClicked(event -> {

            interfaceManager.setPenTracker(0);
            interfaceManager.setEraserTracker(1);
            eraserHolder.setStyle("-fx-background-color:white;");
            toolHolder.setStyle("-fx-background-color:white;");

            interfaceManager.setTextTool(true);
            interfaceManager.getPane().setCursor(Cursor.DEFAULT);

                CreateText();


        });

        toolsSet.getChildren().addAll(toolHolder,eraserHolder, textTool);

        return toolsSet;
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
        internalFunction.getChildren().addAll(toolsPanel(),ActivePanel(),new SettingPanel(interfaceManager).settingPanel());

        return internalFunction;
    }

    private Canvas findExistingCanvas(Pane parent)
    {
        for (javafx.scene.Node node : parent.getChildren())
        {
            if (node instanceof Canvas) {
                return (Canvas) node;
            }
        }
        return null;
    }

    private void setupDrawingEvents(Canvas canvas) {
        canvas.setOnMousePressed(event -> {
            interfaceManager.getGraphicsContext().beginPath();
            interfaceManager.getGraphicsContext().moveTo(event.getX(), event.getY());
            interfaceManager.getGraphicsContext().stroke();
        });

        canvas.setOnMouseDragged(event -> {
            interfaceManager.getGraphicsContext().lineTo(event.getX(), event.getY());
            interfaceManager.getGraphicsContext().stroke();
        });
    }

    public Pane currentActive()
    {

        interfaceManager.setActivities(new Pane());
        Pane activityPane = interfaceManager.getActivities();
        activityPane.setMaxWidth(1000);
        activityPane.setMaxHeight(530);
        activityPane.setId("currentActive");


        interfaceManager.penTrackerProperty().addListener((obs, oldVal, newVal) ->
        {
            if (newVal.intValue() == 1 && interfaceManager.getActivities() != null) {
                Canvas canvas = findExistingCanvas(activityPane);
                if (canvas == null) {
                    canvas = new Canvas(interfaceManager.getActivities().getWidth(), interfaceManager.getActivities().getHeight());
                    interfaceManager.getActivities().getChildren().add(canvas);
                }

                interfaceManager.setGraphicsContext(canvas.getGraphicsContext2D());
                setupDrawingEvents(canvas);
            }
            interfaceManager.setPicture(false);
        });

       interfaceManager.eraserTrackerProperty().addListener((obs, oldVal, newVal) ->
        {

        });

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
                    interfaceManager.getGraphicsContext().stroke();
                });

                drawingCanvas.setOnMouseDragged(e -> {
                    interfaceManager.getGraphicsContext().lineTo(e.getX(), e.getY());
                    interfaceManager.getGraphicsContext().stroke();

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
                if (node.getParent().getParent() instanceof Pane) {
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


    public void CreateText()
    {
        // Only proceed if text tool is selected and other tools are not
        if(interfaceManager.isTextTool() && interfaceManager.getPenTracker() == 0 && interfaceManager.getPenTracker() == 0)
        {
            selectionRect = new Rectangle();
            selectionRect.setFill(Color.LIGHTBLUE.deriveColor(0, 1, 1, 0.5));
            selectionRect.setStroke(Color.BLUE);
            selectionRect.setVisible(false);
            interfaceManager.getActivities().getChildren().add(selectionRect);

            interfaceManager.getActivities().getChildren().getFirst().setOnMousePressed(e -> {
                if(interfaceManager.isTextTool()) {
                    startX = e.getX();
                    startY = e.getY();

                    // Initialize selection rectangle
                    selectionRect.setX(startX);
                    selectionRect.setY(startY);
                    selectionRect.setWidth(0);
                    selectionRect.setHeight(0);
                    selectionRect.setVisible(true);
                }
            });

            interfaceManager.getActivities().getChildren().getFirst().setOnMouseDragged(e -> {
                if(interfaceManager.isTextTool() && selectionRect.isVisible()) {
                    // Update selection rectangle dimensions during drag
                    double x = Math.min(startX, e.getX());
                    double y = Math.min(startY, e.getY());
                    double width = Math.abs(e.getX() - startX);
                    double height = Math.abs(e.getY() - startY);

                    selectionRect.setX(x);
                    selectionRect.setY(y);
                    selectionRect.setWidth(width);
                    selectionRect.setHeight(height);
                }
            });

            interfaceManager.getActivities().getChildren().getFirst().setOnMouseReleased(e -> {
                if(interfaceManager.isTextTool() && selectionRect != null && selectionRect.isVisible()) {
                    selectionRect.setVisible(false);

                    // Only create text field if drag was significant
                    if (selectionRect.getWidth() > 10 && selectionRect.getHeight() > 10) {
                        createTextField(selectionRect.getX(), selectionRect.getY(),
                                selectionRect.getWidth(), selectionRect.getHeight());
                    }
                }
            });
        }
        else {
            // Clean up if switching away from text tool
            if(selectionRect != null && interfaceManager.getActivities().getChildren().contains(selectionRect)) {
                interfaceManager.getActivities().getChildren().remove(selectionRect);
            }

        }
    }

    private void createTextField(double x, double y, double width, double height) {
        TextField textField = new TextField();
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        textField.setPrefWidth(width);
        textField.setPrefHeight(height);

        // Add to the root pane
        ((Pane) selectionRect.getParent()).getChildren().add(textField);

        // Focus the new text field immediately
        textField.requestFocus();
    }

}