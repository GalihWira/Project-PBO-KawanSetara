module com.example.projectpbokawansetara {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projectpbokawansetara to javafx.fxml;
    exports com.example.projectpbokawansetara;
    exports com.example.projectpbokawansetara.controller;
    opens com.example.projectpbokawansetara.controller to javafx.fxml;
}