module com.example.whitebboardedition2nd {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.whitebboardedition2nd to javafx.fxml;
    exports com.example.whitebboardedition2nd;
}