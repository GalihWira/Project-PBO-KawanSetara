package com.example.projectpbokawansetara.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CariLowonganController {

    @FXML private TextField txtCari;
    @FXML private ComboBox<String> cbUrutkan;
    @FXML private CheckBox chkFullTime;

    @FXML
    private void initialize() {
        cbUrutkan.getItems().addAll("Paling cocok", "Terbaru", "Gaji tertinggi");
    }

    @FXML
    private void onCariClicked() {
        System.out.println("Cari: " + txtCari.getText());
    }
}