package com.example.projectpbokawansetara;

import com.example.projectpbokawansetara.helper.SceneManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private static final String HALAMAN_AWAL = "HapusLowonganDialog.fxml";

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setStage(primaryStage);
        SceneManager.pindah(HALAMAN_AWAL, "Masuk - KawanSetara");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}