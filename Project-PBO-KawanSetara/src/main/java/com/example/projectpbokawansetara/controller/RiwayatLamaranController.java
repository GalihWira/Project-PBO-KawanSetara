package com.example.projectpbokawansetara.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class RiwayatLamaranController {

    @FXML
    private void handleCariLowongan(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projectpbokawansetara/CariLowongan.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal memuat halaman CariLowongan.fxml! Pastikan nama file sudah benar.");
        }
    }
}