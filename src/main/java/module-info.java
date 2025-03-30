module com.example.whitebboardedition2nd {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    requires java.compiler;


    opens com.example.whitebboardedition2nd to javafx.fxml;
    exports com.example.whitebboardedition2nd;
}