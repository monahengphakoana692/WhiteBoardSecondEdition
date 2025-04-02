package com.example.whitebboardedition2nd;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class InterfaceManager implements UIinterface,Tools
{
    HBox externalFunction = new HBox();
    Label textFile = new Label("NewFile");//for creating text file
    Label SaveFile = new Label("SaveFile"); // for saving files on editing board
    Label OpenFiles = new Label("OpenFiles");
    Label OpenMultiMedia = new Label("Pictures");
    Label OpenMultiMediav = new Label("Videos");
    Label SaveCanvas = new Label("SaveCanvas");
    Label sound = new Label("Audio");
    Label clearStage = new Label("RemoveActivity");
    Label editCanvas = new Label("EditCanvas");
    Label newCanvas = new Label("NewCanvas");
    Stage stage = new Stage();
    Pane activities;
    private TextArea doc = new TextArea();//for typing text to be saved
    StackPane pane;//for current display of files and activities
    GraphicsContext graphicsContext = null;
    Slider slider = null;
    ColorPicker colorPicker = null;
    private IntegerProperty penTracker = new SimpleIntegerProperty(0);
    private IntegerProperty eraserTracker = new SimpleIntegerProperty(0);
    private boolean isTextTool = false;


    public Pane getActivities() {
        return activities;
    }

    public void setActivities(Pane activities) {
        this.activities = activities;
    }

    public StackPane getPane() {
        return pane;
    }

    public void setPane(StackPane pane) {
        this.pane = pane;
    }

    public TextArea getDoc() {
        return doc;
    }

    public void setDoc(TextArea doc) {
        this.doc = doc;
    }


    public HBox getExternalFunction() {
        return externalFunction;
    }

    public void setExternalFunction(HBox externalFunction) {
        this.externalFunction = externalFunction;
    }

    public Label getTextFile() {
        return textFile;
    }

    public void setTextFile(Label textFile) {
        this.textFile = textFile;
    }

    public Label getSaveFile() {
        return SaveFile;
    }

    public void setSaveFile(Label saveFile) {
        SaveFile = saveFile;
    }

    public Label getOpenFiles()
    {
        return OpenFiles;
    }

    public void setOpenFiles(Label openFiles) {
        OpenFiles = openFiles;
    }

    public Label getOpenMultiMedia() {
        return OpenMultiMedia;
    }

    public void setOpenMultiMedia(Label openMultiMedia) {
        OpenMultiMedia = openMultiMedia;
    }

    public Label getOpenMultiMediav() {
        return OpenMultiMediav;
    }

    public void setOpenMultiMediav(Label openMultiMediav) {
        OpenMultiMediav = openMultiMediav;
    }

    public Label getSaveCanvas() {
        return SaveCanvas;
    }

    public void setSaveCanvas(Label saveCanvas) {
        SaveCanvas = saveCanvas;
    }

    public Label getSound() {
        return sound;
    }

    public void setSound(Label sound) {
        this.sound = sound;
    }

    public Label getClearStage() {
        return clearStage;
    }

    public void setClearStage(Label clearStage) {
        this.clearStage = clearStage;
    }

    public Label getEditCanvas() {
        return editCanvas;
    }

    public void setEditCanvas(Label editCanvas) {
        this.editCanvas = editCanvas;
    }

    public Label getNewCanvas() {
        return newCanvas;
    }

    public void setNewCanvas(Label newCanvas) {
        this.newCanvas = newCanvas;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @Override
    public void createNewTextFile()
    {
        activities = new Pane();
        activities.setMaxWidth(1000);
        activities.setMaxHeight(530);
        activities.setId("currentActive");
       /* getTextFile().setOnMouseClicked(event ->
        {*/
            activities.getChildren().clear();
            pane = new StackPane();
            doc.setText("type something");
            doc.setPrefSize(activities.getWidth(),activities.getHeight());
            pane.getChildren().add(doc);
            activities.getChildren().add(pane);
       // });

    } // Corrected method name

    @Override
    public void createNewCanvas()
    {

    }    // Corrected method name
    @Override
    public void editCanvas(){

    }
    @Override
    public void saveTextFile(){

    }
    @Override
    public void openTextFile()
    {

        try
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/main/resources/textFiles"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Add All","*"));
            fileChooser.setTitle("Open files");
            File file = fileChooser.showOpenDialog(getStage());

            getActivities().getChildren().clear();

            setPane(new StackPane());
            StackPane paneForNow = getPane();
            getDoc().setText("");
            getDoc().setPrefSize(getActivities().getWidth(),getActivities().getHeight());
            paneForNow.getChildren().add(getDoc());
            getActivities().getChildren().add(paneForNow);

            Scanner filereader = new Scanner(file);
            while(filereader.hasNextLine())
            {
                getDoc().appendText(filereader.next() + " ");
            }

        } catch (Exception e)
        {

            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadPictures()
    {

    }
    @Override
    public void loadVideos()
    {

    }
    @Override
    public void saveCanvas(){
        try {
            // Create a temporary StackPane to hold all content we want to save
            StackPane snapshotPane = new StackPane();

            // Add all children from the original pane to our snapshot pane
            snapshotPane.getChildren().addAll(getPane().getChildren());

            // Create a snapshot of the entire pane (which contains both canvas and text fields)
            WritableImage image = snapshotPane.snapshot(null, null);

            // Show file chooser dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/main/resources/MultimediaFiles"));
            fileChooser.setTitle("Save Drawing");
            fileChooser.setInitialFileName("drawing_" + System.currentTimeMillis() + ".png");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("JPEG", "*.jpg", "*.jpeg")
            );
            File file = fileChooser.showSaveDialog(getStage());

            if (file != null) {
                // Get file extension
                String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);

                // Extract pixel data from WritableImage
                int width = (int) image.getWidth();
                int height = (int) image.getHeight();
                int[] pixels = new int[width * height];

                image.getPixelReader().getPixels(
                        0, 0, width, height,
                        javafx.scene.image.PixelFormat.getIntArgbInstance(),
                        pixels, 0, width
                );

                // Convert to BufferedImage
                java.awt.image.BufferedImage bufferedImage = new java.awt.image.BufferedImage(
                        width, height,
                        java.awt.image.BufferedImage.TYPE_INT_ARGB
                );
                bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);

                // Save using ImageIO
                ImageIO.write(bufferedImage, extension, file);

                // Show confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Drawing Saved");
                alert.setHeaderText(null);
                alert.setContentText("Your drawing and text fields have been saved successfully!");
                alert.showAndWait();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error saving drawing");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void loadAudio(){

    }         // Corrected method name
    @Override
    public void removeActivities(MediaHandler medium)
    {
        try
        {

            medium.getMediaPlayer().stop();
            medium.getMediaPlayer().dispose();
            medium.setMediaPlayer(null);
            getActivities().getChildren().removeFirst();
        } catch (Exception e)
        {
            HelloApplication.showErrorAlert("Error to close:" ,e.toString());
        }
    }
    @Override
    public StackPane drawingAction()
    {
            Canvas canvas = new Canvas(1000, 530); // Set canvas size
            graphicsContext = canvas.getGraphicsContext2D();

            graphicsContext.setStroke(Color.BLACK);
            graphicsContext.setLineWidth(2);

            setPane(new StackPane());
            StackPane paneForNow = getPane();
            paneForNow.setId("DrawingSpace");

            paneForNow.setOnMousePressed(event -> {
                graphicsContext.beginPath();
                graphicsContext.moveTo(event.getX(), event.getY()); // Use local coordinates
                graphicsContext.stroke();

            });

            paneForNow.setOnMouseDragged(event -> {
                graphicsContext.lineTo(event.getX(), event.getY()); // Use local coordinates
                graphicsContext.stroke();

            });


            paneForNow.getChildren().add(canvas);

            return paneForNow;
    }

    public HBox externalFunctions()
    {
        textFile.setId("ExLabels");

        SaveFile.setId("ExLabels");


        OpenFiles.setId("ExLabels");


        OpenMultiMedia.setId("ExLabels");

        OpenMultiMediav.setId("ExLabels");

        SaveCanvas.setId("ExLabels");

        editCanvas.setId("ExLabels");
        sound.setId("ExLabels");

        clearStage.setId("ExLabels");

        newCanvas.setId("ExLabels");

        externalFunction.setId("externalFunctions");
        externalFunction.setPrefHeight(40);
        externalFunction.setPrefWidth(1550);

        Image penImage = new Image(getClass().getResourceAsStream("/ps.png"));
        ImageView ico = new ImageView(penImage);//icon of the app
        ico.setFitHeight(30);
        ico.setFitWidth(30);
        HBox titleBar = new HBox();
        titleBar.setStyle("-fx-background-color: #5b5b5b; -fx-padding: 5;");
        titleBar.setAlignment(Pos.CENTER_RIGHT);


        Button minimizeBtn = new Button("─");
        minimizeBtn.setId("windowsBtns");
        minimizeBtn.setOnAction(e ->
                stage.setIconified(true));


        Button maximizeBtn = new Button("□");
        maximizeBtn.setId("windowsBtns");
        maximizeBtn.setOnAction(e -> {
            if (stage.isMaximized()) {
                stage.setMaximized(false);
                maximizeBtn.setText("□");
            } else {
                stage.setMaximized(true);
                maximizeBtn.setText("❐");
            }
        });

// Close button
        Button closeBtn = new Button("✕");
        closeBtn.setId("windowsBtns");
        closeBtn.setOnAction(e -> stage.close());

        titleBar.getChildren().addAll(minimizeBtn, maximizeBtn, closeBtn);
        titleBar.setId("windowsBtns");
        externalFunction.getChildren().addAll( ico,textFile,newCanvas, editCanvas,SaveFile, OpenFiles, OpenMultiMedia,OpenMultiMediav, SaveCanvas, sound, clearStage,titleBar);
        externalFunction.setStyle("-fx-spacing:50px;");


        return externalFunction;
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    public Slider getSlider() {
        return slider;
    }

    public void setSlider(Slider slider) {
        this.slider = slider;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    public void setColorPicker(ColorPicker colorPicker) {
        this.colorPicker = colorPicker;
    }

    public int getPenTracker() {
        return penTracker.get();
    }

    public IntegerProperty penTrackerProperty() {
        return penTracker;
    }

    public void setPenTracker(int penTracker) {
        this.penTracker.set(penTracker);
    }

    public int getEraserTracker() {
        return eraserTracker.get();
    }

    public IntegerProperty eraserTrackerProperty()
    {
        return eraserTracker;
    }

    public void setEraserTracker(int eraserTracker)
    {
        this.eraserTracker.set(eraserTracker);
    }

    public boolean isTextTool()
    {
        return isTextTool;
    }

    public void setTextTool(boolean textTool)
    {
        isTextTool = textTool;
    }

    @Override
    public VBox toolPanel()
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
            penTracker.set(1);
            eraserTracker.set(0);
            isTextTool = false;
            toolHolder.setStyle("-fx-background-color:gray;");
            eraserHolder.setStyle("-fx-background-color:white;");
            graphicsContext.setStroke(colorPicker.getValue());
            graphicsContext.setLineWidth(slider.getValue());
            ImageCursor penCursor = new ImageCursor(penImage);
            getPane().setCursor(penCursor);

        });
        eraserHolder.setOnMouseClicked(event ->
        {
            //preparing eraser

            penTracker.set(0);
            eraserTracker.set(1);
            isTextTool = false;
            eraserHolder.setStyle("-fx-background-color:gray;");
            toolHolder.setStyle("-fx-background-color:white;");
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.setLineWidth(8);
            Image eraserImage = new Image(getClass().getResourceAsStream("/eraser.png"));
            ImageCursor eraserCursor = new ImageCursor(eraserImage);
            getPane().setCursor(eraserCursor);
        });

        textTool.setOnMouseClicked(event -> {

            penTracker.set(0);
            eraserTracker.set(0);
            eraserHolder.setStyle("-fx-background-color:white;");
            toolHolder.setStyle("-fx-background-color:white;");
            isTextTool = true;
            getPane().setCursor(Cursor.DEFAULT);

            //CreateText();
            new HelloApplication().CreateText();


        });

        toolsSet.getChildren().addAll(toolHolder,eraserHolder, textTool);

        return toolsSet;
    }
}
