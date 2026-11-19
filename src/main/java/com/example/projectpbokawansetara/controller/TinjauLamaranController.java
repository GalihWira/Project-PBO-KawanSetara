package com.example.projectpbokawansetara.controller;

import com.example.projectpbokawansetara.helper.FileHelper;
import com.example.projectpbokawansetara.model.Lamaran;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TinjauLamaranController {

    @FXML private Label namaLabel;
    @FXML private Label emailLabel;
    @FXML private Label posisiLabel;
    @FXML private Label cvLabel;
    @FXML private Label pesanLabel;
    @FXML private Button bukaCvButton;
    @FXML private Button tutupButton;
    @FXML private Button tandaiButton;

    private String pathCv;
    private boolean ditandai = false;

    public void setData(Lamaran lamaran) {
        namaLabel.setText("Nama: " + lamaran.getPelamar().getNama());
        emailLabel.setText("Email: " + lamaran.getPelamar().getEmail());
        posisiLabel.setText("Posisi: " + lamaran.getLowongan().getNamaPekerjaan());

        pathCv = lamaran.getPelamar().getCv();
        if (pathCv == null || pathCv.isEmpty()) {
            cvLabel.setText("CV: belum diunggah");
        } else {
            cvLabel.setText("CV: " + pathCv);
        }
    }

    public boolean isDitandai() {
        return ditandai;
    }

    @FXML
    void handleBukaCv(ActionEvent event) {
        pesanLabel.setText("");
        FileHelper.bukaFile(pathCv, pesanLabel);
    }

    @FXML
    void handleTandai(ActionEvent event) {
        ditandai = true;
        tutupDialog();
    }

    @FXML
    void handleTutup(ActionEvent event) {
        ditandai = false;
        tutupDialog();
    }

    private void tutupDialog() {
        Stage stage = (Stage) tutupButton.getScene().getWindow();
        stage.close();
    }
}