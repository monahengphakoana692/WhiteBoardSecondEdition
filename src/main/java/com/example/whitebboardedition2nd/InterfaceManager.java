package com.example.whitebboardedition2nd;

//including necessary libraries
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class InterfaceManager implements UIinterface,Tools
{
    //for all activities that the user can perform
    HBox externalFunction = new HBox();
    Label textFile = new Label("NewFile");//for creating text file
    Label SaveFile = new Label("SaveFile"); // for saving files on editing board
    Label OpenFiles = new Label("OpenFiles");
    Label OpenMultiMedia = new Label("Pictures");
    Label OpenMultiMediav = new Label("Videos");
    Label SaveCanvas = new Label("SaveCanvas");
    Label sound = new Label("Audio");
    Label clearStage = new Label("RemoveActivity");
    Label newCanvas = new Label("NewCanvas");


    Stage stage = new Stage();
    Pane activities;//for holding every activity
    private TextArea doc = new TextArea();//for typing text to be saved
    StackPane pane;//for current display of files and activities
    GraphicsContext graphicsContext = null;//for drawing
    private boolean isPicture = true;//for tracking whether


    Slider slider = null;//for resizing the brush size
    ColorPicker colorPicker = null;//for changing the color of the brush

    //for checking whether the tool is on use or not
    private IntegerProperty penTracker = new SimpleIntegerProperty(0);
    private IntegerProperty eraserTracker = new SimpleIntegerProperty(0);
    private boolean isTextTool = false;


    public void setPicture(boolean picture) {
        isPicture = picture;
    }

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

    public Label getTextFile() {
        return textFile;
    }

    public Label getSaveFile() {
        return SaveFile;
    }

    public Label getOpenFiles()
    {
        return OpenFiles;
    }

    public Label getOpenMultiMedia() {
        return OpenMultiMedia;
    }

    public Label getOpenMultiMediav() {
        return OpenMultiMediav;
    }

    public Label getSaveCanvas() {
        return SaveCanvas;
    }
    public Label getSound() {
        return sound;
    }

    public Label getClearStage() {
        return clearStage;
    }

    public Label getNewCanvas() {
        return newCanvas;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void saveTextFile()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/main/resources/textFiles"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Add All","*"));
        fileChooser.setTitle("save files");
        File file = fileChooser.showSaveDialog(getStage());
        if(file!=null)
        {
            try
            {

                PrintStream print = new PrintStream(file);
                print.println(getDoc().getText());
                print.flush();
            } catch (Exception e)
            {

                throw new RuntimeException(e);
            }
        }
        setPicture(false);

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
    public void loadPictures(MediaHandler medium)
    {
        medium.fetchPictures();

    }
    @Override
    public void saveCanvas() {
        try {
            // Get the original pane
            Pane originalPane = getPane();

            // Create a snapshot of the entire pane (which contains all nodes)
            WritableImage image = originalPane.snapshot(null, null);

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
                String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();

                // Save the image directly without converting to BufferedImage
                javax.imageio.ImageIO.write(SwingFXUtils.fromFXImage(image, null), extension, file);

                // Show confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Drawing Saved");
                alert.setHeaderText(null);
                alert.setContentText("Your drawing and all elements have been saved successfully!");
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
        // Corrected method name
    @Override
    public void removeActivities(MediaHandler medium)
    {
        try
        {

           if(isPicture!= false)//catering for video and the sound
           {
               medium.getMediaPlayer().stop();
               medium.getMediaPlayer().dispose();
               medium.setMediaPlayer(null);
           }
            getActivities().getChildren().removeFirst();
        } catch (Exception e)
        {
            HelloApplication.showErrorAlert("Error to close:" ,e.toString());
        }
    }
    @Override
    public StackPane drawingAction()
    {
            setPenTracker(0);//to make sure the pen tool is not on, without the user s intentions
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
                if(getPenTracker() == 1 || getEraserTracker()==1 ) {
                    graphicsContext.stroke();
                }

            });

            paneForNow.setOnMouseDragged(event -> {
                graphicsContext.lineTo(event.getX(), event.getY()); // Use local coordinates
                if(getPenTracker() == 1 || getEraserTracker()==1 )
                {
                    graphicsContext.stroke();
                }

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
        externalFunction.getChildren().addAll( ico,textFile,newCanvas,SaveFile, OpenFiles, OpenMultiMedia,OpenMultiMediav, SaveCanvas, sound, clearStage,titleBar);
        externalFunction.setStyle("-fx-spacing:50px;");

        return externalFunction;
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
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

    public void setPenTracker(int penTracker) {
        this.penTracker.set(penTracker);
    }

    public int getEraserTracker() {
        return eraserTracker.get();
    }

    public void setEraserTracker(int eraserTracker)
    {
        this.eraserTracker.set(eraserTracker);
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
            toolHolder.setStyle("-fx-background-color:white;");
            eraserHolder.setStyle("-fx-background-color:gray;");
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
            eraserHolder.setStyle("-fx-background-color:white;");
            toolHolder.setStyle("-fx-background-color:gray;");
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.setLineWidth(8);
            Image eraserImage = new Image(getClass().getResourceAsStream("/eraser.png"));
            ImageCursor eraserCursor = new ImageCursor(eraserImage);
            getPane().setCursor(eraserCursor);
        });

        textTool.setOnMouseClicked(event ->
        {
            isTextTool = true;

            // Set up tool states
            setPenTracker(0);
            setEraserTracker(0);
            eraserHolder.setStyle("-fx-background-color:gray;");
            toolHolder.setStyle("-fx-background-color:gray;");

            getPane().setCursor(Cursor.DEFAULT);

            // Create and configure the label
            Text label = new Text("Click to edit");
            label.setStyle("-fx-font-size: 16px; -fx-border-color: lightgray; -fx-border-width: 1px; -fx-padding: 5px;");

            // Make it draggable
            HelloApplication.makeDraggable(label);

            // Make it editable on click
            label.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 1)
                {
                    // Create and configure the editing TextField
                    TextField textField = new TextField(label.getText());
                    textField.setLayoutX(label.getLayoutX());
                    textField.setLayoutY(label.getLayoutY());
                    textField.setPrefWidth(label.getStrokeWidth() + 90); // adding Extra width for editing

                    // Style to match label appearance
                    textField.setStyle("-fx-font-size: 16px; -fx-border-color: #6699FF; -fx-border-width: 1px; -fx-padding: 5px;");

                    // Replace label with text field
                    getActivities().getChildren().remove(label);
                    getActivities().getChildren().add(textField);

                    // Select all text and focus
                    textField.selectAll();
                    //textField.requestFocus();//this makes it to focus immediately

                    // Handle editing completion
                    textField.setOnAction(e ->
                    {
                        label.setText(textField.getText());
                        getActivities().getChildren().remove(textField);
                        getActivities().getChildren().add(label);
                    });

                    textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                        if (!newVal)
                        {
                            label.setText(textField.getText());
                            getActivities().getChildren().remove(textField);
                            getActivities().getChildren().add(label);
                        }
                    });
                }
            });

                 pane.getChildren().add(label);
                getActivities().getChildren().add(pane);

        });

        toolsSet.getChildren().addAll(toolHolder,eraserHolder, textTool);

        return toolsSet;
    }
}
