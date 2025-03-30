package com.example.whitebboardedition2nd;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextUtils
{
    public static double computeTextWidth(Font font, String text, double wrappingWidth) {
        Text helper = new Text();
        helper.setFont(font);
        helper.setText(text);
        helper.setWrappingWidth((int) wrappingWidth);
        return helper.getLayoutBounds().getWidth();
    }
}
