package com.example.projectpbokawansetara;

import com.example.projectpbokawansetara.helper.SceneManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private static final String HALAMAN_AWAL = "DashboardPelamarView.fxml";
    // Kalo mau lgsg akses Dashboard tanpa login, ubah if (!Session.isLoggedIn()) di initialize() jadi if (false) {
    // Misal dah final, balikin lagi jadi if (!Session.isLoggedIn())

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/projectpbokawansetara/view/" + HALAMAN_AWAL));

        Scene scene = new Scene(root);
        primaryStage.setTitle("KawanSetara");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene(); // ukuran HALAMAN_AWAL dinamis
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}