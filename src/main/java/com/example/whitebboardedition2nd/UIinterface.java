package com.example.whitebboardedition2nd;

import javafx.scene.layout.StackPane;

public interface UIinterface {
    void createNewTextFile();

    void createNewCanvas();

    void editCanvas();

    void saveTextFile();

    void openTextFile();

    void loadPictures();

    void loadVideos();

    void saveCanvas();

    void loadAudio();       // Corrected method name


    void removeActivities(MediaHandler medium);

    StackPane drawingAction();

}