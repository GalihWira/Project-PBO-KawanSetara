package com.example.projectpbokawansetara.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class KonfirmasiHapusDialogController {

    @FXML
    private Button batalButton;

    @FXML
    private Button hapusButton;

    @FXML
    private Label judulLabel;

    @FXML
    private Label pesanLabel;

    private boolean dikonfirmasi = false;

    public void setData(String judul, String pesan) {
        judulLabel.setText(judul);

        boolean adaPesan = pesan != null && !pesan.isBlank();
        pesanLabel.setText(adaPesan ? pesan : "");
        pesanLabel.setVisible(adaPesan);
        pesanLabel.setManaged(adaPesan);
    }

    public boolean isDikonfirmasi() {
        return dikonfirmasi;
    }

    @FXML
    void handleBatal(ActionEvent event) {
        dikonfirmasi = false;
        tutupDialog();
    }

    @FXML
    void handleHapus(ActionEvent event) {
        dikonfirmasi = true;
        tutupDialog();
    }

    private void tutupDialog() {
        Stage stage = (Stage) batalButton.getScene().getWindow();
        stage.close();
    }
}