package com.example.projectpbokawansetara.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class KeluarDialogController {

    @FXML
    private Label judulLabel;

    @FXML
    private Label pesanLabel;

    @FXML
    private Button batalButton;

    @FXML
    private Button keluarButton;

    private Runnable onBatal;
    private Runnable onKeluar;

    @FXML
    private void initialize() {
        batalButton.setOnAction(event -> handleBatal());
        keluarButton.setOnAction(event -> handleKeluar());
    }

    public void setOnBatal(Runnable onBatal) {
        this.onBatal = onBatal;
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

    private void handleBatal() {
        if (onBatal != null) {
            onBatal.run();
        }
    }

    private void handleKeluar() {
        if (onKeluar != null) {
            onKeluar.run();
        }
    }
}
