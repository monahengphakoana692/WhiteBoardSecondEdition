package com.example.whitebboardedition2nd;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class InterfaceManager implements UIinterface
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
    public void openTextFile(){

    }
    @Override
    public void loadPictures(){

    }
    @Override
    public void loadVideos(){

    }
    @Override
    public void saveCanvas(){

    }

    @Override
    public void loadAudio(){

    }         // Corrected method name
    @Override
    public void removeActivities()
    {

    }
    @Override
    public void drawingAction()
    {

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
}
