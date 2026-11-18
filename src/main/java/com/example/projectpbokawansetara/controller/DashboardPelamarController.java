package com.example.projectpbokawansetara.controller;

import java.io.IOException;

import com.example.projectpbokawansetara.helper.DialogHelper;
import com.example.projectpbokawansetara.helper.SceneManager;
import com.example.projectpbokawansetara.model.Lowongan;
import com.example.projectpbokawansetara.model.Session;
import com.example.projectpbokawansetara.model.User;

import javafx.application.Platform;
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

    private Node halamanCariLowongan;
    private Node halamanBeranda;

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

    @FXML
    private Label namaUserLabel;

    @FXML
    private Label emailUserLabel;

    @FXML
    private void initialize() {
        if (!Session.isLoggedIn()) {
            Platform.runLater(() -> SceneManager.pindah("LoginView.fxml", "Masuk - KawanSetara"));
            return;
        }

        halamanBeranda = mainBorderPane.getCenter();
        aturMenuAktif(berandaMenu);
    }

    private void tampilkanProfil() {
        User user = Session.getCurrentUser();
        namaUserLabel.setText(user.getNama());
        emailUserLabel.setText(user.getEmail());
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

    private void aturMenuAktif(ToggleButton menuAktif) {
        ToggleButton[] daftarMenu = {
                berandaMenu,
                cariLowonganMenu,
                riwayatMenu,
                checklistMenu
        };

        for (ToggleButton menu : daftarMenu) {
            menu.getStyleClass().remove("menu-aktif");
        }

        menuAktif.setSelected(true);
        menuAktif.getStyleClass().add("menu-aktif");

        riwayatMenuLabel.getStyleClass().remove("menu-label-aktif");

        if (menuAktif == riwayatMenu) {
            riwayatMenuLabel.getStyleClass().add("menu-label-aktif");
        }
    }

    private void tampilkanHalaman(String namaFile) {
        try {
            String lokasi = "/com/example/projectpbokawansetara/view/" + namaFile;

            FXMLLoader loader = new FXMLLoader(getClass().getResource(lokasi));
            Parent halaman = loader.load();

            BorderPane halamanBorder = (BorderPane) halaman;

            switch (namaFile) {
                case "CariLowonganView.fxml":
                    halamanCariLowongan = halamanBorder.getCenter();

                    CariLowonganController controller = loader.getController();
                    controller.setDashboardController(this);
                    break;

                case "RiwayatLamaranView.fxml":
                    break;

                case "CheckListMinatBakatView.fxml":
                    break;
            }

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

    public void bukaDetailLowongan(Lowongan lowongan) {
        try {
            String lokasi = "/com/example/projectpbokawansetara/view/DetailLowonganView.fxml";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(lokasi));
            Parent halaman = loader.load();

            DetailLowonganController controller = loader.getController();

            controller.setLowongan(lowongan);
            controller.setAksiKembali(this::kembaliKeCariLowongan);

            BorderPane halamanBorder = (BorderPane) halaman;
            mainBorderPane.setCenter(halamanBorder.getCenter());

        } catch (IOException | NullPointerException e) {
            System.err.println("Gagal membuka detail lowongan.");
            e.printStackTrace();
        }
    }

    private void kembaliKeCariLowongan() {
        if (halamanCariLowongan != null) {
            mainBorderPane.setCenter(halamanCariLowongan);
            aturMenuAktif(cariLowonganMenu);
        }
    }
}