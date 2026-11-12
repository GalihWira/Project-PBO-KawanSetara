package com.example.projectpbokawansetara;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

<<<<<<< HEAD:Project-PBO-KawanSetara/src/main/java/com/example/projectpbokawansetara/HelloApplication.java
    private static final String HALAMAN_AWAL = "CariLowongan.fxml";
=======
    private static final String HALAMAN_AWAL = "DashboardAdminView.fxml";
>>>>>>> 0280f268fb908f73e77b6e9d6d3861796a2edafc:src/main/java/com/example/projectpbokawansetara/HelloApplication.java

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/projectpbokawansetara/view/" + HALAMAN_AWAL));

        Scene scene = new Scene(root, 1280, 800);
        primaryStage.setTitle("KawanSetara");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}