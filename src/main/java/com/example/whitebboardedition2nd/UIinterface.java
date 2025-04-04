package com.example.whitebboardedition2nd;

import javafx.scene.layout.StackPane;

public interface UIinterface {

    void saveTextFile();

    void openTextFile();

    void loadPictures(MediaHandler medium);

    void saveCanvas();

    void removeActivities(MediaHandler medium);

    StackPane drawingAction();

}