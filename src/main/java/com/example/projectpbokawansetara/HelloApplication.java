package com.example.projectpbokawansetara;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private static final String HALAMAN_AWAL = "LoginView.fxml";

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