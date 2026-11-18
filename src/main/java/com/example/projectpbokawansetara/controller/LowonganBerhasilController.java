package com.example.projectpbokawansetara.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class LowonganBerhasilController {

    @FXML
    private Label judulLabel;

    @FXML
    private Text posisiText;

    @FXML
    private Text pesanText;

    @FXML
    private Button tambahLagiButton;

    @FXML
    private Button lihatDaftarButton;

    private Runnable onTambahLagi;
    private Runnable onLihatDaftar;

    @FXML
    private void initialize() {
        tambahLagiButton.setOnAction(event -> jalankan(onTambahLagi));
        lihatDaftarButton.setOnAction(event -> jalankan(onLihatDaftar));
    }

    public void setData(String posisi, String namaPerusahaan) {
        posisiText.setText(posisi);
        pesanText.setText(" di " + namaPerusahaan + " sekarang tampil di pencarian pelamar.");
    }

    public void setOnTambahLagi(Runnable onTambahLagi) {
        this.onTambahLagi = onTambahLagi;
    }

    public void setOnLihatDaftar(Runnable onLihatDaftar) {
        this.onLihatDaftar = onLihatDaftar;
    }

    private void jalankan(Runnable aksi) {
        if (aksi != null) {
            aksi.run();
        }
    }
}