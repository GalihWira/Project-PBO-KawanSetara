package com.example.projectpbokawansetara.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class DetailLowonganController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnKeluar;

    @FXML
    private Button btnLamar;

    @FXML
    private ImageView imgAvatar;

    @FXML
    private ImageView imgLogoPerusahaan;

    @FXML
    private Label lblAlamatPerusahaan;

    @FXML
    private Label lblBreadcrumb;

    @FXML
    private Label lblDeskripsi;

    @FXML
    private Label lblDibuka;

    @FXML
    private Label lblDurasi;

    @FXML
    private Label lblEmailUser;

    @FXML
    private Label lblGaji;

    @FXML
    private Label lblJenis;

    @FXML
    private Label lblJumlahCocok;

    @FXML
    private Label lblKartuPerusahaan;

    @FXML
    private Label lblLokasi;

    @FXML
    private Label lblNamaPekerjaan;

    @FXML
    private Label lblNamaUser;

    @FXML
    private Label lblPelamar;

    @FXML
    private Label lblPerusahaan;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblSubjudul;

    @FXML
    private Label lblTagAkses;

    @FXML
    private Label lblTagDurasi;

    @FXML
    private Label lblTagGaji;

    @FXML
    private Label lblTagJenis;

    @FXML
    private ToggleGroup tgMenu;

    @FXML
    private TextField txtCari;

    @FXML
    private VBox vbKeahlian;

    @FXML
    private Label vbPersyaratan;

    @FXML
    private Label vbPersyaratan1;

    @FXML
    private Label vbPersyaratan11;

        private static final String MENU_AKTIF =
        "-fx-background-color: #DCE9F5; -fx-text-fill: #1F4E8C; -fx-background-radius: 6; -fx-padding: 8 12;";
    private static final String MENU_BIASA =
        "-fx-background-color: transparent; -fx-text-fill: #344054; -fx-background-radius: 6; -fx-padding: 8 12;";

    @FXML
    void handleLamar(ActionEvent event) {

    }
}
