package com.example.whitebboardedition2nd;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SettingPanel
{
    InterfaceManager interfaceManager;


    public SettingPanel(){}

    public SettingPanel(InterfaceManager interfaceManager)
    {
        this.interfaceManager = interfaceManager;
    }
    public VBox settingPanel()
    {
        VBox settings = new VBox(20);

        interfaceManager.setColorPicker(new ColorPicker());

        interfaceManager.setSlider(new Slider());
        interfaceManager.getSlider().setMax(1);
        interfaceManager.getSlider().setMax(100);
        interfaceManager.getSlider().setShowTickLabels(true);
        interfaceManager.getSlider().setShowTickMarks(true);
        interfaceManager.getSlider().setMaxWidth(200);
        interfaceManager.getColorPicker().setValue(Color.BLACK);

        interfaceManager.getSlider().valueProperty().addListener(event->
        {
            interfaceManager.getGraphicsContext().setLineWidth(interfaceManager.getSlider().getValue());
        });
        interfaceManager.getColorPicker().setOnAction(event ->
        {
            interfaceManager.getGraphicsContext().setStroke(interfaceManager.getColorPicker().getValue());

        });
        settings.setId("settings");
        settings.setPrefWidth(300);
        settings.setPrefHeight(770);
        settings.getChildren().addAll(interfaceManager.getColorPicker(),interfaceManager.getSlider());


        return settings;
    }
}
