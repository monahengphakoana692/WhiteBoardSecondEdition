package com.example.whitebboardedition2nd;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.util.Scanner;

public class MediaHandler
{

    MediaView mediaView;
    private MediaPlayer mediaPlayer;
    InterfaceManager interfaceManager;
    ImageView musicImage;//image displayed when music play
    private File lastDirectory = null;



    public MediaView getMediaView() {
        return mediaView;
    }

    public void setMediaView(MediaView mediaView) {
        this.mediaView = mediaView;
    }

    public MediaPlayer getMediaPlayer()
    {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public InterfaceManager getInterfaceManager() {
        return interfaceManager;
    }

    public void setInterfaceManager(InterfaceManager interfaceManager) {
        this.interfaceManager = interfaceManager;
    }

    public ImageView getMusicImage() {
        return musicImage;
    }

    public void setMusicImage(ImageView musicImage) {
        this.musicImage = musicImage;
    }

    public File getLastDirectory() {
        return lastDirectory;
    }

    public void setLastDirectory(File lastDirectory) {
        this.lastDirectory = lastDirectory;
    }





    public MediaHandler(){}

    public MediaHandler(InterfaceManager interfaceManager)
    {
        this.interfaceManager = interfaceManager;
    }


    public void setupMediaPlayerUI() {
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
        HelloApplication.makeDraggable(mediaView);

        // Volume Slider
        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.valueProperty().bindBidirectional(mediaPlayer.volumeProperty());

        // Progress Slider
        Slider progressSlider = new Slider();
        progressSlider.setMin(0);

        // Update progress slider while playing
        mediaPlayer.currentTimeProperty().addListener((obs, oldVal, newVal) ->
        {
            if (!progressSlider.isValueChanging())
            {
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
        Button playButton = new Button("⏸");
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
        HelloApplication.makeDraggable(controlBox);
        HelloApplication.makeDraggable(timeBox);

        // Music Image - make draggable
        musicImage = new ImageView(new Image(getClass().getResourceAsStream("/headsets.jpg")));
        musicImage.setFitWidth(80);
        musicImage.setFitHeight(80);
        musicImage.setLayoutX(mediaView.getFitWidth() + 70);
        musicImage.setLayoutY(50);
        HelloApplication.makeDraggable(musicImage);

        // Add all components to the container
        mediaContainer.getChildren().addAll(mediaView, musicImage, timeBox, controlBox);

        // Make the entire media container draggable
        HelloApplication.makeDraggable(mediaContainer);


        // Add to activities pane
        interfaceManager.getActivities().getChildren().clear();
        interfaceManager.getActivities().getChildren().add(mediaContainer);
        mediaPlayer.play();
    }

    public void togglePlayPause(Button playButton) {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            playButton.setText("▶");
        } else {
            mediaPlayer.play();
            playButton.setText("⏸");
        }
    }

   public String formatTime(Duration duration)
    {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void openVideoMediaFile()
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

        File file = fileChooser.showOpenDialog(interfaceManager.getStage());

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
                HelloApplication.showErrorAlert("Error loading media", e.getMessage());
                //createInitialUI();
            }
        }

        musicImage.setStyle("-fx-opacity:0px;");//disabling the music image when video places
        mediaView.setId("mediaView");
        musicImage.setFitWidth(80);
        musicImage.setFitHeight(80);
    }

    public void openMediaFile()
    {
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

        File file = fileChooser.showOpenDialog(interfaceManager.getStage());

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
                HelloApplication.showErrorAlert("Error loading media", e.getMessage());
                //createInitialUI();
            }
        }

        musicImage.setStyle("-fx-opacity:1px;");//abling the music image when video places
        mediaView.setId("mediaView");
        musicImage.setFitWidth(500);
        musicImage.setFitHeight(500);
    }

    public void fetchPictures()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/main/resources/MultimediaFiles"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Add1 All","*"));
        fileChooser.setTitle("Open files");
        File file = fileChooser.showOpenDialog(interfaceManager.getStage());
        if(file!=null)
        {
            try
            {

                interfaceManager.getActivities().getChildren().clear();
                interfaceManager.setPane(new StackPane());
                StackPane paneForNow = interfaceManager.getPane();
                ImageView imageView =new ImageView(new Image(String.valueOf(file)));
                paneForNow.getChildren().add(imageView );

                HelloApplication.makeDraggable(paneForNow);
                interfaceManager.getActivities().getChildren().add(paneForNow);

                Scanner filereader = new Scanner(file);
                while(filereader.hasNextLine())
                {
                    interfaceManager.getDoc().appendText(filereader.next() + " ");
                }

            } catch (Exception e)
            {

                throw new RuntimeException(e);
            }
        }
    }

    public void fetchMusic()
    {

    }

}
