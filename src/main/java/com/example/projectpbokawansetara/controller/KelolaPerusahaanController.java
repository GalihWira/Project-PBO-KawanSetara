package com.example.projectpbokawansetara.controller;

import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class KelolaPerusahaanController {


    @FXML
    void handleUbah(ActionEvent event) {
    }

    @FXML
    void handleEdit(ActionEvent event) {
    }

    @FXML
    void handleHapus(ActionEvent event) {
    }

    @FXML
    void handleDetail(ActionEvent event) {
    }

    @FXML
    void handleCari(ActionEvent event) {
    }

    @FXML
    void handleBatal(ActionEvent event) {
    }

    @FXML
    void handleSimpan(ActionEvent event) {
    }

    @FXML
    void handleTambah(ActionEvent event) {
        pindahHalaman(event, "/com/example/projectpbokawansetara/view/FormPerusahaanView.fxml");
    }

    @FXML
    void handleFilter(ActionEvent event) {
    }

    @FXML
    void handleSebelumnya(ActionEvent event) {
    }

    @FXML
    void handleBerikutnya(ActionEvent event) {
    }

    private void pindahHalaman(ActionEvent event, String fxmlPath) {
        try {
            URL url = getClass().getResource(fxmlPath);
            if (url == null) {
                System.err.println("File FXML tidak ditemukan: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent halamanBaru = loader.load();

            Node kontenTengah = halamanBaru;
            if (halamanBaru instanceof BorderPane halamanBorder && halamanBorder.getCenter() != null) {
                kontenTengah = halamanBorder.getCenter();
            }

            Scene scene = ((Node) event.getSource()).getScene();
            BorderPane mainBorderPane = (BorderPane) scene.lookup("#mainBorderPane");

            if (mainBorderPane != null) {
                mainBorderPane.setCenter(kontenTengah);
            } else {
                scene.setRoot(halamanBaru);
            }
        } catch (Exception e) {
            System.err.println("Gagal pindah ke: " + fxmlPath);
            e.printStackTrace();
        }
    }
}