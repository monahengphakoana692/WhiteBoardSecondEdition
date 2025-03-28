package com.example.whitebboardedition2nd;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class HelloApplication extends Application
{
    private IntegerProperty penTracker = new SimpleIntegerProperty(0);
    private IntegerProperty eraserTracker = new SimpleIntegerProperty(0);
    GraphicsContext graphicsContext = null;
    Slider slider = null;
    StackPane pane;//for current display of files and activities
    ColorPicker colorPicker = null;
    HBox externalFunction = new HBox();
    private double xOffset = 0;//for dragging the stage
    private double yOffset = 0;//for dragging the stage
    private TextArea doc = new TextArea();//for typing text to be saved
    Label textFile = new Label("NewFile");//for creating text file
    Label SaveFile = new Label("SaveFile"); // for saving files on editing board
    Label OpenFiles = new Label("OpenFiles");
    Label OpenMultiMedia = new Label("Pictures");
    Label OpenMultiMediav = new Label("Videos");
    Label SaveCanvas = new Label("SaveCanvas");
    Label sound = new Label("Audio");
    Stage stage = new Stage();
    private MediaPlayer mediaPlayer;
    private File lastDirectory = null;
    StackPane activities;




    @Override
    public void start(Stage stages) throws IOException
    {
        stage.initStyle(StageStyle.UNDECORATED);
        FlowPane root = new FlowPane();//setting flow for resizing scene
        VBox    mainLayout = new VBox();

        mainLayout.getChildren().addAll(externalFunctions(),internalFunctions());



        root.setId("root");

        root.getChildren().add(mainLayout);
        root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());


        setPage(stage,new Scene(root,1400,700));
    }

    public static void main(String[] args) {
        launch();
    }

    void setPage(Stage stage, Scene scene)//this is for displaying scenes
    {
        stage.setScene(scene);
        externalFunction.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        externalFunction.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        stage.setTitle("Multimedia");
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
        penTool.setFitWidth(30);
        penTool.setFitHeight(30);
        erasertool.setFitWidth(30);
        erasertool.setFitHeight(20);

        Pane toolHolder = new Pane(penTool);
        Pane eraserHolder = new Pane(erasertool);

        toolHolder.setId("toolHolder");
        toolHolder.setPrefSize(40, 40);

        eraserHolder.setPrefSize(20, 20);
        eraserHolder.setId("eraserHolder");

        // When clicked, update penTracker and change the background color
        toolHolder.setOnMouseClicked(event ->
        {
            penTracker.set(1);
            toolHolder.setStyle("-fx-background-color:gray;");
            eraserHolder.setStyle("-fx-background-color:white;");
            graphicsContext.setStroke(colorPicker.getValue());
            graphicsContext.setLineWidth(slider.getValue());
            ImageCursor penCursor = new ImageCursor(penImage);
            pane.setCursor(penCursor);

        });
        eraserHolder.setOnMouseClicked(event ->
        {
            //preparing eraser
            eraserTracker.set(1);
            eraserHolder.setStyle("-fx-background-color:gray;");
            toolHolder.setStyle("-fx-background-color:white;");
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.setLineWidth(8);
            Image eraserImage = new Image(getClass().getResourceAsStream("/eraser.png"));
            ImageCursor eraserCursor = new ImageCursor(eraserImage);
            pane.setCursor(eraserCursor);
        });

        toolsSet.getChildren().addAll(toolHolder,eraserHolder);

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

    public VBox settingsPanel()//for holding color setting and other setting
    {
        VBox settings = new VBox(20);

        colorPicker = new ColorPicker();

        slider = new Slider();
        slider.setMax(1);
        slider.setMax(100);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMaxWidth(200);
        colorPicker.setValue(Color.BLACK);

        slider.valueProperty().addListener(event->
        {
            graphicsContext.setLineWidth(slider.getValue());
        });
        colorPicker.setOnAction(event ->
        {
             graphicsContext.setStroke(colorPicker.getValue());

        });
        settings.setId("settings");
        settings.setPrefWidth(300);
        settings.setPrefHeight(770);
        settings.getChildren().addAll(colorPicker,slider);


        return settings;
    }

   public HBox externalFunctions()//this is for external source
   {


       textFile.setStyle("-fx-font-size:20px;" +
               "-fx-text-fill:white;" +
               "-fx-spacing:10px;");
       SaveFile.setStyle("-fx-font-size:20px;" +
               "-fx-text-fill:white;" +
               "-fx-spacing:10px;");

       OpenFiles.setStyle("-fx-font-size:20px;" +
               "-fx-text-fill:white;" +
               "-fx-spacing:10px;");

       OpenMultiMedia.setStyle("-fx-font-size:20px;" +
               "-fx-text-fill:white;" +
               "-fx-spacing:10px;");
       OpenMultiMediav.setStyle("-fx-font-size:20px;" +
               "-fx-text-fill:white;" +
               "-fx-spacing:10px;");
       SaveCanvas.setStyle("-fx-font-size:20px;" +
               "-fx-text-fill:white;" +
               "-fx-spacing:10px;");

       sound.setStyle("-fx-font-size:20px;" +
               "-fx-text-fill:white;" +
               "-fx-spacing:10px;");

       externalFunction.setId("externalFunctions");
       externalFunction.setPrefHeight(40);
       externalFunction.setPrefWidth(1550);
       externalFunction.getChildren().addAll(textFile, SaveFile, OpenFiles, OpenMultiMedia,OpenMultiMediav, SaveCanvas, sound);


       return externalFunction;
   }

    public HBox internalFunctions()//this is for internal activities
    {
        HBox internalFunction = new HBox();

        internalFunction.setPrefHeight(690);
        internalFunction.setPrefWidth(1550);
        internalFunction.getChildren().addAll(toolsPanel(),ActivePanel(),settingsPanel());

        return internalFunction;
    }

    public StackPane currentActive()
    {


        activities = new StackPane();
        activities.setMaxWidth(1000);
        activities.setMaxHeight(530);
        activities.setId("currentActive");


        penTracker.addListener((obs, oldVal, newVal) ->
        {
            activities.getChildren().clear(); // Clear previous content
            if (newVal.intValue() == 1)
            {
                activities.getChildren().add(drawingAction()); // Add drawing area dynamically

            }
        });

        eraserTracker.addListener((obs, oldVal, newVal) ->
        {

        });
        textFile.setOnMouseClicked(event ->
        {
            activities.getChildren().clear();
            pane = new StackPane();
            doc.setText("type something");
            pane.getChildren().add(doc);
            activities.getChildren().add(pane);
        });

        SaveFile.setOnMouseClicked(event ->
        {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/main/resources/textFiles"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Add All","*"));
            fileChooser.setTitle("save files");
            File file = fileChooser.showSaveDialog(stage);
            if(file!=null)
            {
                try
                {

                    PrintStream print = new PrintStream(file);
                    print.println(doc.getText());
                    print.flush();
                } catch (Exception e)
                {

                    throw new RuntimeException(e);
                }
            }
        });

        OpenFiles.setOnMouseClicked(event ->
        {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/main/resources/textFiles"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Add All","*"));
            fileChooser.setTitle("Open files");
            File file = fileChooser.showOpenDialog(stage);
            if(file!=null)
            {
                try
                {
                    activities.getChildren().clear();
                    pane = new StackPane();
                    pane.getChildren().add(doc);
                    activities.getChildren().add(pane);

                    Scanner filereader = new Scanner(file);
                    while(filereader.hasNextLine())
                    {
                        doc.appendText(filereader.next() + " ");
                    }

                } catch (Exception e)
                {

                    throw new RuntimeException(e);
                }
            }
        });
        OpenMultiMediav.setOnMouseClicked(event ->
        {



            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/main/resources/MultimediaFiles"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Add All","*"));
            fileChooser.setTitle("Open files");
            File file = fileChooser.showOpenDialog(stage);
            if(file!=null)
            {
                Media video = new Media(file.toURI().toString());

                mediaPlayer  = new MediaPlayer(video);
                MediaView viewVideo = new MediaView(mediaPlayer);
                viewVideo.setFitHeight(700);
                viewVideo.setFitWidth(800);
                mediaPlayer.play();
                try
                {
                    activities.getChildren().clear();
                    pane = new StackPane();
                    pane.getChildren().add(viewVideo);
                    activities.getChildren().add(pane);

                    Scanner filereader = new Scanner(file);
                    while(filereader.hasNextLine())
                    {
                        doc.appendText(filereader.next() + " ");
                    }

                } catch (Exception e)
                {

                    throw new RuntimeException(e);
                }
            }
        });

        OpenMultiMedia.setOnMouseClicked(event ->
        {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/main/resources/MultimediaFiles"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Add All","*"));
            fileChooser.setTitle("Open files");
            File file = fileChooser.showOpenDialog(stage);
            if(file!=null)
            {
                try
                {

                    activities.getChildren().clear();
                    pane = new StackPane();
                    pane.getChildren().add(new ImageView(new Image(String.valueOf(file))));
                    activities.getChildren().add(pane);

                    Scanner filereader = new Scanner(file);
                    while(filereader.hasNextLine())
                    {
                        doc.appendText(filereader.next() + " ");
                    }

                } catch (Exception e)
                {

                    throw new RuntimeException(e);
                }
            }
        });

        //saveCanvasDrawing()
        SaveCanvas.setOnMouseClicked(event -> {
            saveCanvasDrawing();
        });

        sound.setOnMouseClicked(event ->
        {
            pane = new StackPane();
            openMediaFile();
        });

        return activities;
    }

    public StackPane drawingAction()
    {
        Canvas canvas = new Canvas(1000, 530); // Set canvas size
        graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(2);

        pane = new StackPane();
        pane.setId("DrawingSpace");

        pane.setOnMousePressed(event -> {
            graphicsContext.beginPath();
            graphicsContext.moveTo(event.getX(), event.getY()); // Use local coordinates
            graphicsContext.stroke();

        });

        pane.setOnMouseDragged(event -> {
            graphicsContext.lineTo(event.getX(), event.getY()); // Use local coordinates
            graphicsContext.stroke();

        });


        pane.getChildren().add(canvas);

        return pane;
    }

    private void saveCanvasDrawing()
    {
        try {
            // Create a snapshot of the pane (which contains the canvas)
            WritableImage image = pane.snapshot(null, null);

            // Show file chooser dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/main/resources/MultimediaFiles"));
            fileChooser.setTitle("Save Drawing");
            fileChooser.setInitialFileName("drawing_" + System.currentTimeMillis() + ".png");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("JPEG", "*.jpg", "*.jpeg")
            );
            File file = fileChooser.showSaveDialog(stage);

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
                alert.setContentText("Your drawing has been saved successfully!");
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

    private void openMediaFile() {
        FileChooser fileChooser = new FileChooser();

        // Set initial directory (use last directory if available, or user home directory)
        if (lastDirectory != null && lastDirectory.exists()) {
            fileChooser.setInitialDirectory(lastDirectory);
        } else {
            fileChooser.setInitialDirectory(new File("src/main/resources/MultimediaFiles"));
        }

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Media Files", "*.mp3","*.mp4" ,"*.wav"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        fileChooser.setTitle("Open sound File");

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                // Remember the directory for next time
                lastDirectory = file.getParentFile();

                // Clean up previous media player if exists
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                }

                // Setup new MediaPlayer
                Media media = new Media(file.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                setupMediaPlayerUI();

            } catch (Exception e) {
                showErrorAlert("Error loading media", e.getMessage());
                //createInitialUI();
            }
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        // Clean up media player when application closes
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }

    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void togglePlayPause(Button playButton) {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            playButton.setText("▶");
        } else {
            mediaPlayer.play();
            playButton.setText("⏸");
        }
    }

    private void setupMediaPlayerUI()
    {
        VBox audio = new VBox();
        audio.setPrefHeight(20);
        audio.setPrefWidth(30);
        audio.setStyle("-fx-background-color:black;");


        // Media View
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(50);
        mediaView.setFitHeight(20);
        mediaView.setPreserveRatio(true);

        // Volume Slider
        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.valueProperty().bindBidirectional(mediaPlayer.volumeProperty());

        // Progress Slider
        Slider progressSlider = new Slider();
        progressSlider.setMin(0);

        // Update progress slider while playing
        mediaPlayer.currentTimeProperty().addListener((obs, oldVal, newVal) -> {
            if (!progressSlider.isValueChanging()) {
                progressSlider.setValue(newVal.toSeconds());
            }
        });

        // Update total duration when it's known
        mediaPlayer.setOnReady(() -> {
            progressSlider.setMax(mediaPlayer.getMedia().getDuration().toSeconds());
        });

        // Allow seeking
        progressSlider.setOnMousePressed(e -> mediaPlayer.seek(Duration.seconds(progressSlider.getValue())));
        progressSlider.setOnMouseDragged(e -> mediaPlayer.seek(Duration.seconds(progressSlider.getValue())));

        // Play/Pause Button
        Button playButton = new Button("▶");
        playButton.setOnAction(e -> togglePlayPause(playButton));

        // Stop Button
        Button stopButton = new Button("⏹");
        stopButton.setOnAction(e -> {
            mediaPlayer.stop();
            playButton.setText("▶");
        });

        // Time Labels
        Label currentTimeLabel = new Label("00:00");
        Label totalTimeLabel = new Label("00:00");

        // Update time labels
        mediaPlayer.currentTimeProperty().addListener((obs, oldVal, newVal) -> {
            currentTimeLabel.setText(formatTime(newVal));
        });

        mediaPlayer.setOnReady(() -> {
            totalTimeLabel.setText(formatTime(mediaPlayer.getMedia().getDuration()));
        });

        // Control Layout
        HBox timeBox = new HBox(10, currentTimeLabel, progressSlider, totalTimeLabel);
        timeBox.setAlignment(Pos.CENTER);

        HBox controlBox = new HBox(10, playButton, stopButton, new Label("Volume:"), volumeSlider);
        controlBox.setAlignment(Pos.CENTER);

        VBox mediaContainer = new VBox(10, mediaView, timeBox, controlBox);
        mediaContainer.setAlignment(Pos.CENTER);
        mediaContainer.setPadding(new Insets(10));

        audio.getChildren().add(mediaContainer);
        VBox tempHolder = new VBox(audio);
        tempHolder.setAlignment(Pos.CENTER);
        activities.getChildren().clear();
        activities.getChildren().add(tempHolder);
        mediaPlayer.play();
    }

}