package com.example.projectpbokawansetara.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class KeluarDialogController {

    @FXML
    private Label judulLabel;

    @FXML
    private Label pesanLabel;

    @FXML
    private Button batalButton;

    @FXML
    private Button keluarButton;

    private Runnable onKeluar;

    @FXML
    private void initialize() {
        batalButton.setOnAction(event -> tutupDialog());
        keluarButton.setOnAction(event -> handleKeluar());
    }

    public void setOnKeluar(Runnable onKeluar) {
        this.onKeluar = onKeluar;
    }

    public void setJudul(String judul) {
        judulLabel.setText(judul);
    }

    public void setPesan(String pesan) {
        pesanLabel.setText(pesan);
    }

    private void handleKeluar() {
        tutupDialog();

        if (onKeluar != null) {
            onKeluar.run();
        }
    }

    private void tutupDialog() {
        Stage stage = (Stage) batalButton.getScene().getWindow();
        stage.close();
    }
}
