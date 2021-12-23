module com.example.a4basics {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ships_application to javafx.fxml;
    exports com.example.ships_application;
}