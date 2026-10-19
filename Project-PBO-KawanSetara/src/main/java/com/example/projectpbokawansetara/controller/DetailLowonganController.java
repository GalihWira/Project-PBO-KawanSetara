package com.example.projectpbokawansetara.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

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

    @FXML
    void initialize() {
        assert btnKeluar != null : "fx:id=\"btnKeluar\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert btnLamar != null : "fx:id=\"btnLamar\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert imgAvatar != null : "fx:id=\"imgAvatar\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert imgLogoPerusahaan != null : "fx:id=\"imgLogoPerusahaan\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblAlamatPerusahaan != null : "fx:id=\"lblAlamatPerusahaan\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblBreadcrumb != null : "fx:id=\"lblBreadcrumb\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblDeskripsi != null : "fx:id=\"lblDeskripsi\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblDibuka != null : "fx:id=\"lblDibuka\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblDurasi != null : "fx:id=\"lblDurasi\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblEmailUser != null : "fx:id=\"lblEmailUser\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblGaji != null : "fx:id=\"lblGaji\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblJenis != null : "fx:id=\"lblJenis\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblJumlahCocok != null : "fx:id=\"lblJumlahCocok\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblKartuPerusahaan != null : "fx:id=\"lblKartuPerusahaan\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblLokasi != null : "fx:id=\"lblLokasi\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblNamaPekerjaan != null : "fx:id=\"lblNamaPekerjaan\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblNamaUser != null : "fx:id=\"lblNamaUser\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblPelamar != null : "fx:id=\"lblPelamar\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblPerusahaan != null : "fx:id=\"lblPerusahaan\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblStatus != null : "fx:id=\"lblStatus\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblSubjudul != null : "fx:id=\"lblSubjudul\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblTagAkses != null : "fx:id=\"lblTagAkses\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblTagDurasi != null : "fx:id=\"lblTagDurasi\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblTagGaji != null : "fx:id=\"lblTagGaji\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert lblTagJenis != null : "fx:id=\"lblTagJenis\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert tgMenu != null : "fx:id=\"tgMenu\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert txtCari != null : "fx:id=\"txtCari\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert vbKeahlian != null : "fx:id=\"vbKeahlian\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert vbPersyaratan != null : "fx:id=\"vbPersyaratan\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert vbPersyaratan1 != null : "fx:id=\"vbPersyaratan1\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";
        assert vbPersyaratan11 != null : "fx:id=\"vbPersyaratan11\" was not injected: check your FXML file 'DetailLowonganView.fxml'.";

        imgAvatar.setClip(new Circle(16, 16, 16));
        Rectangle sudut = new Rectangle(48, 48);
        sudut.setArcWidth(12);
        sudut.setArcHeight(12);
        imgLogoPerusahaan.setClip(sudut);
        tgMenu.selectedToggleProperty().addListener((obs, lama, baru) -> {
            if (baru == null && lama != null) {
                tgMenu.selectToggle(lama);
                return;
            }
            aturMenu();
        });
        aturMenu();
    }
        private void aturMenu() {
        for (Toggle t : tgMenu.getToggles()) {
            ToggleButton tombol = (ToggleButton) t;
            tombol.setStyle(tombol.isSelected() ? MENU_AKTIF : MENU_BIASA);
        }
    }
}
