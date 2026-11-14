package com.example.projectpbokawansetara.controller;

import java.io.IOException;

import com.example.projectpbokawansetara.helper.DialogHelper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class DashboardAdminController {

    @FXML private ToggleGroup adminMenuGroup;
    @FXML private ToggleButton akunAdminMenu;
    @FXML private ImageView avatarImage;
    @FXML private TextField cariField;
    @FXML private VBox daftarLamaranBox;
    @FXML private ToggleButton dashboardMenu;
    @FXML private ToggleButton itemChecklistMenu;
    @FXML private ToggleButton jenisDisabilitasMenu;
    @FXML private Label jumlahAdminMenuLabel;
    @FXML private Label jumlahChecklistMenuLabel;
    @FXML private Label jumlahDisabilitasMenuLabel;
    @FXML private Label jumlahLowonganMenuLabel;
    @FXML private Label jumlahPerusahaanMenuLabel;
    @FXML private Button keluarButton;
    @FXML private Label lamaranKosongLabel;
    @FXML private Label lamaranMasukInfoLabel;
    @FXML private Label lamaranMasukLabel;
    @FXML private Hyperlink lihatSemuaLink;
    @FXML private ToggleButton lowonganMenu;
    @FXML private Label lowonganTersediaInfoLabel;
    @FXML private Label lowonganTersediaLabel;
    @FXML private BorderPane mainBorderPane;
    @FXML private Label namaUserLabel;
    @FXML private Label pelamarTerdaftarInfoLabel;
    @FXML private Label pelamarTerdaftarLabel;
    @FXML private Label peranUserLabel;
    @FXML private ToggleButton perusahaanMenu;
    @FXML private Label perusahaanMitraInfoLabel;
    @FXML private Label perusahaanMitraLabel;
    @FXML private Label tanggalLabel;
    @FXML private VBox tindakLanjutBox;
    @FXML private Label tindakLanjutKosongLabel;

    private Node halamanDashboardAwal;

    @FXML
    private void initialize() {
        halamanDashboardAwal = mainBorderPane.getCenter();
        aturMenuAktif(dashboardMenu);
    }

    @FXML
    void handleDashboardMenu(MouseEvent event) {
        mainBorderPane.setCenter(halamanDashboardAwal);
        aturMenuAktif(dashboardMenu);
    }

    @FXML
    void handleBukaLowongan(ActionEvent event) {
        System.out.println("=== TOMBOL LOWONGAN BERHASIL DIKLIK ===");
        tampilkanHalaman("KelolaLowonganView.fxml", lowonganMenu);
}

    @FXML
    void handleBukaPerusahaan(MouseEvent event) {
        tampilkanHalaman("KelolaPerusahaanView.fxml", perusahaanMenu);
    }

    @FXML
    void handleBukaItemChecklist(MouseEvent event) {
        tampilkanHalaman("KelolaChecklistView.fxml", itemChecklistMenu);
    }

    @FXML
    void handleBukaDisabilitas(MouseEvent event) {
        tampilkanHalaman("KelolaDisabilitasView.fxml", jenisDisabilitasMenu);
    }

    @FXML
    void handleAkunAdmin(MouseEvent event) {
        tampilkanHalaman("KelolaAkunAdminView.fxml", akunAdminMenu);
    }

    @FXML
    void handleKeluar(ActionEvent event) {
        DialogHelper.tampilkanDialogKeluar(
                keluarButton.getScene().getWindow(),
                "Kamu perlu masuk lagi untuk mengelola data lowongan dan pelamar.");
    }

    private void tampilkanHalaman(String namaFile, ToggleButton menuTujuan) {
        try {
            String lokasi = "/com/example/projectpbokawansetara/view/" + namaFile;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(lokasi));
            Parent halaman = loader.load();

            if (halaman instanceof BorderPane halamanBorder) {
                mainBorderPane.setCenter(halamanBorder.getCenter());
            } else {
                mainBorderPane.setCenter(halaman);
            }

            aturMenuAktif(menuTujuan);

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
                "-fx-background-radius: 8;";

        String styleTidakAktif =
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #344054;" +
                "-fx-background-radius: 8;";

        ToggleButton[] semuaMenu = {
            dashboardMenu, lowonganMenu, perusahaanMenu,
            itemChecklistMenu, jenisDisabilitasMenu, akunAdminMenu
        };

        for (ToggleButton menu : semuaMenu) {
            if (menu != null) {
                menu.setStyle(styleTidakAktif);
            }
        }

        if (menuAktif != null) {
            menuAktif.setSelected(true);
            menuAktif.setStyle(styleAktif);
        }
    }
}