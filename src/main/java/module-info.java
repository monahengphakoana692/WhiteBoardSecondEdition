module com.example.whitebboardedition2nd {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.compiler;
    requires javafx.swing;


    opens com.example.whitebboardedition2nd to javafx.fxml;
    exports com.example.whitebboardedition2nd;
}