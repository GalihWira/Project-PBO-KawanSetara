package com.example.projectpbokawansetara.helper;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static final String BASE = "/com/example/projectpbokawansetara/view/";
    private static final double LEBAR = 1280;
    private static final double TINGGI = 800;

    private static Stage stage;

    private SceneManager() {
    }

    public static void setStage(Stage stageUtama) {
        stage = stageUtama;
    }

    public static void pindah(String namaFxml, String judul) {
        if (stage == null) {
            throw new IllegalStateException("Stage belum di-set. Panggil SceneManager.setStage() dulu di HelloApplication.");
        }

        URL lokasi = SceneManager.class.getResource(BASE + namaFxml);
        if (lokasi == null) {
            throw new IllegalArgumentException("File FXML tidak ditemukan: " + BASE + namaFxml);
        }

        try {
            Parent root = FXMLLoader.load(lokasi);

            if (stage.getScene() == null) {
                stage.setScene(new Scene(root, LEBAR, TINGGI));
            } else {
                stage.getScene().setRoot(root);
            }

            stage.setTitle(judul);
        } catch (IOException e) {
            throw new RuntimeException("Gagal membuka halaman " + namaFxml, e);
        }
    }
}