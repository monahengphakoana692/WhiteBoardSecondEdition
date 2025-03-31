package com.example.whitebboardedition2nd;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.*;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.tools.Tool;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
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
    Label clearStage = new Label("RemoveActivity");
    Label editCanvas = new Label("EditCanvas");
    Label newCanvas = new Label("NewCanvas");
    Stage stage = new Stage();
    private MediaPlayer mediaPlayer;
    private File lastDirectory = null;
    Pane activities;
    ImageView musicImage;//image displayed when music play
    MediaView mediaView;
    private double startX, startY;//for creating a text field
    private Rectangle selectionRect;
    private boolean isTextTool = false;
    private Circle tempCircle;



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
            penTracker.set(1);
            eraserTracker.set(0);
            isTextTool = false;
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

            penTracker.set(0);
            eraserTracker.set(1);
            isTextTool = false;
            eraserHolder.setStyle("-fx-background-color:gray;");
            toolHolder.setStyle("-fx-background-color:white;");
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.setLineWidth(8);
            Image eraserImage = new Image(getClass().getResourceAsStream("/eraser.png"));
            ImageCursor eraserCursor = new ImageCursor(eraserImage);
            pane.setCursor(eraserCursor);
        });

        textTool.setOnMouseClicked(event -> {

            penTracker.set(0);
            eraserTracker.set(0);
            eraserHolder.setStyle("-fx-background-color:white;");
            toolHolder.setStyle("-fx-background-color:white;");
            //graphicsContext.setStroke(Color.WHITE);
            isTextTool = true;
            pane.setCursor(Cursor.DEFAULT);

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

    public HBox internalFunctions()//this is for internal activities
    {
        HBox internalFunction = new HBox();

        internalFunction.setPrefHeight(690);
        internalFunction.setPrefWidth(1550);
        internalFunction.getChildren().addAll(toolsPanel(),ActivePanel(),settingsPanel());

        return internalFunction;
    }

    private Canvas findExistingCanvas(Pane parent) {
        for (javafx.scene.Node node : parent.getChildren()) {
            if (node instanceof Canvas) {
                return (Canvas) node;
            }
        }
        return null;
    }

    private void setupDrawingEvents(Canvas canvas) {
        canvas.setOnMousePressed(event -> {
            graphicsContext.beginPath();
            graphicsContext.moveTo(event.getX(), event.getY());
            graphicsContext.stroke();
        });

        canvas.setOnMouseDragged(event -> {
            graphicsContext.lineTo(event.getX(), event.getY());
            graphicsContext.stroke();
        });
    }

    public Pane currentActive()
    {


        activities = new Pane();
        activities.setMaxWidth(1000);
        activities.setMaxHeight(530);
        activities.setId("currentActive");


        penTracker.addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() == 1 && pane != null) {
                Canvas canvas = findExistingCanvas(pane);
                if (canvas == null) {
                    canvas = new Canvas(pane.getWidth(), pane.getHeight());
                    pane.getChildren().add(canvas);
                }
                graphicsContext = canvas.getGraphicsContext2D();
                setupDrawingEvents(canvas);
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
        editCanvas.setOnMouseClicked(event -> {

           activities.getChildren().add(drawingImage());
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
                    doc.setText("");
                    makeDraggable(doc);
                    pane.getChildren().add(doc);
                    makeDraggable(pane);
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
        OpenMultiMediav.setOnMouseClicked(event ->//uploading the video
        {

            openVideoMediaFile();
        });

        OpenMultiMedia.setOnMouseClicked(event ->//uploading the pictures
        {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/main/resources/MultimediaFiles"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Add1 All","*"));
            fileChooser.setTitle("Open files");
            File file = fileChooser.showOpenDialog(stage);
            if(file!=null)
            {
                try
                {

                    activities.getChildren().clear();
                    pane = new StackPane();
                    ImageView imageView =new ImageView(new Image(String.valueOf(file)));
                    pane.getChildren().add(imageView );

                    makeDraggable(pane);
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

        newCanvas.setOnMouseClicked(event -> {
            activities.getChildren().add(drawingAction());
        });

        sound.setOnMouseClicked(event ->
        {
            pane = new StackPane();
            openMediaFile();
        });
        clearStage.setOnMouseClicked(event -> {
            activities.getChildren().clear();
            mediaPlayer.stop();
            mediaPlayer = null;
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

    public StackPane drawingImage()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/main/resources/MultimediaFiles"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        fileChooser.setTitle("Open Image File");
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                activities.getChildren().clear();

                // Load the image
                Image image = new Image(file.toURI().toString());
                ImageView imageView = new ImageView(image);

                // Create canvas with same dimensions as image
                Canvas drawingCanvas = new Canvas(image.getWidth(), image.getHeight());
                GraphicsContext gc = drawingCanvas.getGraphicsContext2D();

                // Set up drawing tools
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(2);

                // Create a stack pane to layer image and canvas
                StackPane imageCanvasPane = new StackPane();
                imageCanvasPane.getChildren().addAll(imageView, drawingCanvas);

                // Set up mouse events for drawing
                drawingCanvas.setOnMousePressed(e -> {
                    gc.beginPath();
                    gc.moveTo(e.getX(), e.getY());
                    gc.stroke();
                });

                drawingCanvas.setOnMouseDragged(e -> {
                    gc.lineTo(e.getX(), e.getY());
                    gc.stroke();

                });


                // Add to main activities pane
                pane = new StackPane(imageCanvasPane);
                activities.getChildren().add(pane);
                activities.setLayoutX(400);
                activities.setLayoutY(70);

                // Connect with existing drawing tools
                colorPicker.setOnAction(e -> gc.setStroke(colorPicker.getValue()));
                slider.valueProperty().addListener((obs, oldVal, newVal) ->
                        gc.setLineWidth(newVal.doubleValue()));

            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error loading image: " + e.getMessage()).show();
            }
        }


        return pane;
    }

    private void saveCanvasDrawing() {
        try {
            // Create a temporary StackPane to hold all content we want to save
            StackPane snapshotPane = new StackPane();

            // Add all children from the original pane to our snapshot pane
            snapshotPane.getChildren().addAll(pane.getChildren());

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

    private void openMediaFile() {
        FileChooser fileChooser = new FileChooser();

        // Set initial directory (use last directory if available, or user home directory)
        if (lastDirectory != null && lastDirectory.exists()) {
            fileChooser.setInitialDirectory(lastDirectory);
        } else {
            fileChooser.setInitialDirectory(new File("src/main/resources/MultimediaFiles"));
        }

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Media Files", "*.mp3")

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
        musicImage.setFitWidth(400);
        musicImage.setFitHeight(400);
    }

    private void openVideoMediaFile()
    {
        FileChooser fileChooser = new FileChooser();

        // Set initial directory (use last directory if available, or user home directory)
        if (lastDirectory != null && lastDirectory.exists()) {
            fileChooser.setInitialDirectory(lastDirectory);
        } else {
            fileChooser.setInitialDirectory(new File("src/main/resources/MultimediaFiles"));
        }

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Media Files", "*.mp4")

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

        musicImage.setStyle("-fx-opacity:0px;");//disabling the music image when video places
        mediaView.setId("mediaView");
        musicImage.setFitWidth(80);
        musicImage.setFitHeight(80);
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

    private String formatTime(Duration duration)
    {
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

    private void setupMediaPlayerUI() {
        // Create a container pane that will hold all draggable media components
        Pane mediaContainer = new Pane();
        mediaContainer.setStyle("-fx-background-color: rgba(46, 46, 46, 0); -fx-background-radius: 5;");

        // Media View - make draggable
        mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(600);  // Set reasonable default size
        mediaView.setFitHeight(400);
        mediaView.setPreserveRatio(true);
        mediaView.setLayoutX(50);    // Initial position
        mediaView.setLayoutY(50);

        // Make mediaView draggable
        makeDraggable(mediaView);

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
        currentTimeLabel.setStyle("-fx-text-fill:white;");
        Label totalTimeLabel = new Label("00:00");
        totalTimeLabel.setStyle("-fx-text-fill:white;");

        // Update time labels
        mediaPlayer.currentTimeProperty().addListener((obs, oldVal, newVal) -> {
            currentTimeLabel.setText(formatTime(newVal));
        });

        mediaPlayer.setOnReady(() -> {
            totalTimeLabel.setText(formatTime(mediaPlayer.getMedia().getDuration()));
        });

        // Control Layout - make draggable
        HBox timeBox = new HBox(10, currentTimeLabel, progressSlider, totalTimeLabel);
        timeBox.setAlignment(Pos.CENTER);
        timeBox.setLayoutX(100);
        timeBox.setLayoutY(mediaView.getFitHeight() + 60);

        Label vol = new Label("Volume:");
        vol.setStyle("-fx-text-fill:white;");

        HBox controlBox = new HBox(10, playButton, stopButton, vol, volumeSlider);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.setLayoutX(100);
        controlBox.setLayoutY(mediaView.getFitHeight() + 90);

        // Make controls draggable
        makeDraggable(controlBox);
        makeDraggable(timeBox);

        // Music Image - make draggable
        musicImage = new ImageView(new Image(getClass().getResourceAsStream("/headsets.jpg")));
        musicImage.setFitWidth(80);
        musicImage.setFitHeight(80);
        musicImage.setLayoutX(mediaView.getFitWidth() + 70);
        musicImage.setLayoutY(50);
        makeDraggable(musicImage);

        // Add all components to the container
        mediaContainer.getChildren().addAll(mediaView, musicImage, timeBox, controlBox);

        // Make the entire media container draggable
        makeDraggable(mediaContainer);


        // Add to activities pane
        activities.getChildren().clear();
        activities.getChildren().add(mediaContainer);
        mediaPlayer.play();
    }

    // Helper method to make any node draggable
    private void makeDraggable(Node node) {
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

    private void makeResizable(Node node) {
        // First make it draggable
        makeDraggable(node);

        // Create resize handles (8 handles for all directions)
        final double handleSize = 8;

        // Resize rectangles for all directions
        Rectangle topLeft = createResizeHandle(handleSize);
        Rectangle top = createResizeHandle(handleSize);
        Rectangle topRight = createResizeHandle(handleSize);
        Rectangle right = createResizeHandle(handleSize);
        Rectangle bottomRight = createResizeHandle(handleSize);
        Rectangle bottom = createResizeHandle(handleSize);
        Rectangle bottomLeft = createResizeHandle(handleSize);
        Rectangle left = createResizeHandle(handleSize);

        // Add all handles to a group
        Group resizeHandles = new Group(topLeft, top, topRight, right,
                bottomRight, bottom, bottomLeft, left);

        // Make handles visible only when node is selected
        resizeHandles.setVisible(false);
        node.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                resizeHandles.setVisible(true);
                resizeHandles.toFront();
                node.toFront();
            }
        });

        // Hide handles when clicking elsewhere
        if (node.getParent() instanceof Pane) {
            Pane parent = (Pane) node.getParent();
            parent.setOnMouseClicked(e -> {
                if (e.getTarget() == parent) {
                    resizeHandles.setVisible(false);
                }
            });
        }

        // Position handles around the node
        positionHandles(node, resizeHandles);

        // Add handles to the node's parent
        if (node.getParent() instanceof Pane) {
            ((Pane) node.getParent()).getChildren().add(resizeHandles);
        }

        // Setup resize functionality for each handle
        setupResize(topLeft, node, true, true, true, true);
        setupResize(top, node, false, true, false, true);
        setupResize(topRight, node, false, true, true, false);
        setupResize(right, node, false, false, true, false);
        setupResize(bottomRight, node, false, false, true, true);
        setupResize(bottom, node, false, false, false, true);
        setupResize(bottomLeft, node, true, false, true, false);
        setupResize(left, node, true, false, false, false);

        // Update handle positions when node is dragged
        node.layoutXProperty().addListener((obs, oldVal, newVal) -> positionHandles(node, resizeHandles));
        node.layoutYProperty().addListener((obs, oldVal, newVal) -> positionHandles(node, resizeHandles));
        node.boundsInParentProperty().addListener((obs, oldVal, newVal) -> positionHandles(node, resizeHandles));
    }

    private Rectangle createResizeHandle(double size) {
        Rectangle handle = new Rectangle(size, size);
        handle.setFill(Color.BLUE);
        handle.setStroke(Color.WHITE);
        handle.setStrokeWidth(1);
        return handle;
    }

    private void positionHandles(Node node, Group handles) {
        Bounds bounds = node.getBoundsInParent();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        double x = bounds.getMinX();
        double y = bounds.getMinY();

        // Get all handles from the group
        List<Node> handleList = handles.getChildren();

        // Top-left
        ((Rectangle) handleList.get(0)).setX(x - 4);
        ((Rectangle) handleList.get(0)).setY(y - 4);

        // Top
        ((Rectangle) handleList.get(1)).setX(x + width/2 - 4);
        ((Rectangle) handleList.get(1)).setY(y - 4);

        // Top-right
        ((Rectangle) handleList.get(2)).setX(x + width - 4);
        ((Rectangle) handleList.get(2)).setY(y - 4);

        // Right
        ((Rectangle) handleList.get(3)).setX(x + width - 4);
        ((Rectangle) handleList.get(3)).setY(y + height/2 - 4);

        // Bottom-right
        ((Rectangle) handleList.get(4)).setX(x + width - 4);
        ((Rectangle) handleList.get(4)).setY(y + height - 4);

        // Bottom
        ((Rectangle) handleList.get(5)).setX(x + width/2 - 4);
        ((Rectangle) handleList.get(5)).setY(y + height - 4);

        // Bottom-left
        ((Rectangle) handleList.get(6)).setX(x - 4);
        ((Rectangle) handleList.get(6)).setY(y + height - 4);

        // Left
        ((Rectangle) handleList.get(7)).setX(x - 4);
        ((Rectangle) handleList.get(7)).setY(y + height/2 - 4);
    }

    private void setupResize(Rectangle handle, Node node,
                             boolean moveX, boolean moveY,
                             boolean resizeWidth, boolean resizeHeight) {

        final double[] dragDelta = new double[2];
        final double[] initialSize = new double[2];
        final double[] initialPos = new double[2];

        handle.setOnMousePressed(e -> {
            dragDelta[0] = e.getSceneX();
            dragDelta[1] = e.getSceneY();
            initialSize[0] = node.getBoundsInParent().getWidth();
            initialSize[1] = node.getBoundsInParent().getHeight();
            initialPos[0] = node.getLayoutX();
            initialPos[1] = node.getLayoutY();
            e.consume();
        });

        handle.setOnMouseDragged(e -> {
            double deltaX = e.getSceneX() - dragDelta[0];
            double deltaY = e.getSceneY() - dragDelta[1];

            // Calculate new width and height
            double newWidth = initialSize[0];
            double newHeight = initialSize[1];

            if (resizeWidth) {
                newWidth = Math.max(node.minWidth(-1), initialSize[0] + (moveX ? -deltaX : deltaX));
            }
            if (resizeHeight) {
                newHeight = Math.max(node.minHeight(-1), initialSize[1] + (moveY ? -deltaY : deltaY));
            }

            // Set new size
            if (node instanceof Region) {
                ((Region) node).setPrefSize(newWidth, newHeight);
            } else if (node instanceof ImageView) {
                ((ImageView) node).setFitWidth(newWidth);
                ((ImageView) node).setFitHeight(newHeight);
            } else {
                node.setScaleX(newWidth / node.getBoundsInLocal().getWidth());
                node.setScaleY(newHeight / node.getBoundsInLocal().getHeight());
            }

            // Adjust position if needed
            if (moveX) {
                node.setLayoutX(initialPos[0] + (initialSize[0] - newWidth));
            }
            if (moveY) {
                node.setLayoutY(initialPos[1] + (initialSize[1] - newHeight));
            }

            e.consume();
        });

        // Change cursor based on handle position
        if (moveX && moveY) {
            handle.setCursor(Cursor.NW_RESIZE);
        } else if (moveX && !moveY && resizeHeight) {
            handle.setCursor(Cursor.SW_RESIZE);
        } else if (!moveX && moveY && resizeWidth) {
            handle.setCursor(Cursor.NE_RESIZE);
        } else if (resizeWidth && resizeHeight) {
            handle.setCursor(Cursor.SE_RESIZE);
        } else if (resizeWidth) {
            handle.setCursor(Cursor.E_RESIZE);
        } else if (resizeHeight) {
            handle.setCursor(Cursor.S_RESIZE);
        }
    }

    public void CreateText()
    {
        // Only proceed if text tool is selected and other tools are not
        if(isTextTool && penTracker.get() == 0 && eraserTracker.get() == 0)
        {
            selectionRect = new Rectangle();
            selectionRect.setFill(Color.LIGHTBLUE.deriveColor(0, 1, 1, 0.5));
            selectionRect.setStroke(Color.BLUE);
            selectionRect.setVisible(false);
            activities.getChildren().add(selectionRect);

            activities.getChildren().getFirst().setOnMousePressed(e -> {
                if(isTextTool) {
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

            activities.getChildren().getFirst().setOnMouseDragged(e -> {
                if(isTextTool && selectionRect.isVisible()) {
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

            activities.getChildren().getFirst().setOnMouseReleased(e -> {
                if(isTextTool && selectionRect != null && selectionRect.isVisible()) {
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
            if(selectionRect != null && activities.getChildren().contains(selectionRect)) {
                activities.getChildren().remove(selectionRect);
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