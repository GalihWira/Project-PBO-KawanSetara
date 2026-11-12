package com.example.projectpbokawansetara.controller;

import java.io.IOException;

import com.example.projectpbokawansetara.helper.DialogHelper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
public class DashboardPelamarController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ToggleButton berandaMenu;

    @FXML
    private ToggleButton cariLowonganMenu;

    @FXML
    private ToggleButton riwayatMenu;

    @FXML
    private ToggleButton checklistMenu;

    @FXML
    private ToggleGroup menuGroup;

    @FXML
    private Label riwayatMenuLabel;

    @FXML
    private Button btnKeluar;

    @FXML
    private TextField cariField;

    private Node halamanBeranda;

    @FXML
    private void initialize() {
        halamanBeranda = mainBorderPane.getCenter();
        aturMenuAktif(berandaMenu);
    }

    @FXML
    void handleBukaBeranda(ActionEvent event) {
        mainBorderPane.setCenter(halamanBeranda);
        aturMenuAktif(berandaMenu);
    }

    @FXML
    void handleBukaCariLowongan(ActionEvent event) {
        tampilkanHalaman("CariLowonganView.fxml");
    }

    @FXML
    void handleBukaRiwayat(ActionEvent event) {
        tampilkanHalaman("RiwayatLamaranView.fxml");
    }

    @FXML
    void handleBukaChecklist(ActionEvent event) {
        tampilkanHalaman("CheckListMinatBakatView.fxml");
    }

    @FXML
    void handleCari(ActionEvent event) {
        handleBukaCariLowongan(event);
    }

    @FXML
    void handleKeluar(ActionEvent event) {
        DialogHelper.tampilkanDialogKeluar(
                btnKeluar.getScene().getWindow(),
                "Apakah kamu yakin ingin keluar dari akun KawanSetara?");
    }

    private void tampilkanHalaman(String namaFile) {
        try {
            String lokasi = "/com/example/projectpbokawansetara/view/" + namaFile;

            FXMLLoader loader = new FXMLLoader(getClass().getResource(lokasi));
            Parent halaman = loader.load();

            BorderPane halamanBorder = (BorderPane) halaman;
            mainBorderPane.setCenter(halamanBorder.getCenter());

            switch (namaFile) {
                case "CariLowonganView.fxml":
                    aturMenuAktif(cariLowonganMenu);
                    break;
                case "RiwayatLamaranView.fxml":
                    aturMenuAktif(riwayatMenu);
                    break;
                case "CheckListMinatBakatView.fxml":
                    aturMenuAktif(checklistMenu);
                    break;
            }

        } catch (IOException | NullPointerException e) {
            System.err.println("Gagal membuka halaman: " + namaFile);
            e.printStackTrace();
        }
    }

    private void aturMenuAktif(ToggleButton menuAktif) {
        String styleAktif =
            "-fx-background-color: #DCE9F5;" +
            "-fx-text-fill: #1F4E8C;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 0 12;";

        String styleTidakAktif =
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #344054;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 0 12;";

        berandaMenu.setStyle(styleTidakAktif);
        cariLowonganMenu.setStyle(styleTidakAktif);
        riwayatMenu.setStyle(styleTidakAktif);
        checklistMenu.setStyle(styleTidakAktif);

        menuAktif.setSelected(true);
        menuAktif.setStyle(styleAktif);
        riwayatMenuLabel.setStyle(
                menuAktif == riwayatMenu
                        ? "-fx-text-fill: #1F4E8C; -fx-font-weight: 600;"
                        : "-fx-text-fill: #344054;"
        );
    }
}
