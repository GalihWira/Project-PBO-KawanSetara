package com.example.projectpbokawansetara.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HapusLowonganController {

    @FXML
    private Button batalButton;

    @FXML
    private Button hapusButton;

    @FXML
    private Label judulLabel;

    @FXML
    private Label pesanLabel;

    private boolean dihapus = false;

    public void setData(String namaLowongan, int jumlahLamaran) {
        judulLabel.setText("Apakah Anda ingin menghapus lowongan " + namaLowongan + "?");
        pesanLabel.setText(jumlahLamaran + " lamaran terkait lowongan ini.");
    }

    public boolean isDihapus() {
        return dihapus;
    }

    @FXML
    void handleBatal(ActionEvent event) {
        dihapus = false;
        tutupDialog();
    }

    @FXML
    void handleHapus(ActionEvent event) {
        dihapus = true;
        tutupDialog();
    }

    private void tutupDialog() {
        Stage stage = (Stage) batalButton.getScene().getWindow();
        stage.close();
    }

}
