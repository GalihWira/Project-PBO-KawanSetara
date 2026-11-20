package com.example.projectpbokawansetara.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.example.projectpbokawansetara.dao.DashboardDAO;
import com.example.projectpbokawansetara.helper.DialogHelper;
import com.example.projectpbokawansetara.helper.SceneManager;
import com.example.projectpbokawansetara.model.DashboardStatistik;
import com.example.projectpbokawansetara.model.LamaranTerbaru;
import com.example.projectpbokawansetara.model.Session;
import com.example.projectpbokawansetara.model.User;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DashboardAdminController {

    private Node halamanDashboardAwal;

    @FXML
    private ToggleGroup adminMenuGroup;
    @FXML
    private ToggleButton akunAdminMenu;
    @FXML
    private ImageView avatarImage;
    @FXML
    private TextField cariField;
    @FXML
    private VBox daftarLamaranBox;
    @FXML
    private ToggleButton dashboardMenu;
    @FXML
    private ToggleButton itemChecklistMenu;
    @FXML
    private ToggleButton jenisDisabilitasMenu;
    @FXML
    private Label jumlahAdminMenuLabel;
    @FXML
    private Label jumlahChecklistMenuLabel;
    @FXML
    private Label jumlahDisabilitasMenuLabel;
    @FXML
    private Label jumlahLamaranMenuLabel;
    @FXML
    private Label jumlahLowonganMenuLabel;
    @FXML
    private Label jumlahPerusahaanMenuLabel;
    @FXML
    private Button keluarButton;
    @FXML
    private ToggleButton lamaranMenu;
    @FXML
    private Label lamaranKosongLabel;
    @FXML
    private Label lamaranMasukInfoLabel;
    @FXML
    private Label lamaranMasukLabel;
    @FXML
    private Hyperlink lihatSemuaLink;
    @FXML
    private ToggleButton lowonganMenu;
    @FXML
    private Label lowonganTersediaInfoLabel;
    @FXML
    private Label lowonganTersediaLabel;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Label namaUserLabel;
    @FXML
    private Label pelamarTerdaftarInfoLabel;
    @FXML
    private Label pelamarTerdaftarLabel;
    @FXML
    private Label peranUserLabel;
    @FXML
    private ToggleButton perusahaanMenu;
    @FXML
    private Label perusahaanMitraInfoLabel;
    @FXML
    private Label perusahaanMitraLabel;
    @FXML
    private Label tanggalLabel;
    @FXML
    private VBox tindakLanjutBox;
    @FXML
    private Label tindakLanjutKosongLabel;

    @FXML
    private void initialize() {
        if (!Session.isLoggedIn()) {
            Platform.runLater(() -> SceneManager.pindah("LoginView.fxml", "Masuk - KawanSetara"));
            return;
        }

        tampilkanProfil();
        loadDataDashboard();

        halamanDashboardAwal = mainBorderPane.getCenter();
        aturMenuAktif(dashboardMenu);
    }

    private void tampilkanProfil() {
        User user = Session.getCurrentUser();
        namaUserLabel.setText(user.getNama());
        peranUserLabel.setText("Admin");
    }

    private void loadDataDashboard() {
        DashboardDAO dao = new DashboardDAO();
        DashboardStatistik stat = dao.getStatistik();

        lowonganTersediaLabel.setText(String.valueOf(stat.getLowonganTersedia()));
        lamaranMasukLabel.setText(String.valueOf(stat.getLamaranMasuk()));
        perusahaanMitraLabel.setText(String.valueOf(stat.getPerusahaanMitra()));
        pelamarTerdaftarLabel.setText(String.valueOf(stat.getPelamarTerdaftar()));

        lowonganTersediaInfoLabel.setText(stat.getLowonganDitutup() + " lowongan ditutup");
        lamaranMasukInfoLabel.setText(stat.getLamaranBelumDitinjau() + " belum ditinjau");
        perusahaanMitraInfoLabel.setText(stat.getPerusahaanTanpaLowongan() + " belum buka lowongan");
        pelamarTerdaftarInfoLabel.setText(stat.getPelamarAktifMelamar() + " aktif melamar");

        jumlahLowonganMenuLabel.setText(String.valueOf(stat.getTotalLowongan()));
        jumlahPerusahaanMenuLabel.setText(String.valueOf(stat.getPerusahaanMitra()));
        jumlahChecklistMenuLabel.setText(String.valueOf(stat.getTotalChecklist()));
        jumlahDisabilitasMenuLabel.setText(String.valueOf(stat.getTotalDisabilitas()));
        jumlahAdminMenuLabel.setText(String.valueOf(stat.getTotalAdmin()));
        jumlahLamaranMenuLabel.setText(String.valueOf(stat.getLamaranBelumDitinjau()));

        tampilkanDaftarLamaran(dao.getLamaranTerbaru());
    }

    private void tampilkanDaftarLamaran(List<LamaranTerbaru> listLamaran) {
        daftarLamaranBox.getChildren().clear();

        if (listLamaran.isEmpty()) {
            lamaranKosongLabel.setVisible(true);
            lamaranKosongLabel.setManaged(true);
            return;
        }

        lamaranKosongLabel.setVisible(false);
        lamaranKosongLabel.setManaged(false);

        for (LamaranTerbaru lamaran : listLamaran) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPrefHeight(40.0);
            row.setSpacing(12.0);
            row.setStyle("-fx-border-color: transparent transparent #EAECF0 transparent; -fx-padding: 0 14 0 14;");

            Label lblNama = new Label(lamaran.getNamaPelamar());
            lblNama.setStyle("-fx-text-fill: #101828; -fx-font-size: 13px; -fx-font-weight: 500;");
            lblNama.setPrefWidth(120.0);
            HBox.setHgrow(lblNama, Priority.ALWAYS);

            Label lblPosisi = new Label(lamaran.getPosisi());
            lblPosisi.setStyle("-fx-text-fill: #667085; -fx-font-size: 13px;");
            lblPosisi.setPrefWidth(110.0);

            Label lblTanggal = new Label(lamaran.getTanggalMasuk());
            lblTanggal.setStyle("-fx-text-fill: #667085; -fx-font-size: 13px;");
            lblTanggal.setPrefWidth(70.0);

            String statusText = lamaran.getStatus();
            String statusKapital = statusText.substring(0, 1).toUpperCase() + statusText.substring(1);

            Label lblStatus = new Label(statusKapital);
            lblStatus.setPrefWidth(70.0);
            lblStatus.setPadding(new Insets(2, 8, 2, 8));

            String warnaStatus;
            if (statusText.equals("diproses")) {
                warnaStatus = "-fx-background-color: #FEF0C7; -fx-text-fill: #B54708;";
            } else if (statusText.equals("ditinjau")) {
                warnaStatus = "-fx-background-color: #D1E9FF; -fx-text-fill: #175CD3;";
            } else if (statusText.equals("diterima")) {
                warnaStatus = "-fx-background-color: #D1FADF; -fx-text-fill: #027A48;";
            } else {
                warnaStatus = "-fx-background-color: #FEE4E2; -fx-text-fill: #B42318;";
            }
            lblStatus.setStyle("-fx-font-size: 12px; -fx-background-radius: 10; -fx-alignment: center; " + warnaStatus);

            row.getChildren().addAll(lblNama, lblPosisi, lblTanggal, lblStatus);
            daftarLamaranBox.getChildren().add(row);
        }
    }

    @FXML
    void handleDashboardMenu(ActionEvent event) {
        if (halamanDashboardAwal != null) {
            mainBorderPane.setCenter(halamanDashboardAwal);
        }
        loadDataDashboard();
        aturMenuAktif(dashboardMenu);
    }

    @FXML
    void handleBukaLowongan(ActionEvent event) {
        tampilkanHalaman("KelolaLowonganView.fxml", lowonganMenu);
    }

    @FXML
    void handleKelolaLowongan(ActionEvent event) {
        tampilkanHalaman("KelolaLowonganView.fxml", lowonganMenu);
    }

    @FXML
    void handleKelolaLamaran(ActionEvent event) {
        tampilkanHalaman("KelolaLamaranView.fxml", lamaranMenu);
    }

    @FXML
    void handleBukaPerusahaan(ActionEvent event) {
        tampilkanHalaman("KelolaPerusahaanView.fxml", perusahaanMenu);
    }

    @FXML
    void handleKelolaPerusahaan(ActionEvent event) {
        tampilkanHalaman("KelolaPerusahaanView.fxml", perusahaanMenu);
    }

    @FXML
    void handleBukaItemChecklist(ActionEvent event) {
        tampilkanHalaman("KelolaChecklistView.fxml", itemChecklistMenu);
    }

    @FXML
    void handleKelolaChecklist(ActionEvent event) {
        tampilkanHalaman("KelolaChecklistView.fxml", itemChecklistMenu);
    }

    @FXML
    void handleBukaDisabilitas(ActionEvent event) {
        tampilkanHalaman("KelolaDisabilitasView.fxml", jenisDisabilitasMenu);
    }

    @FXML
    void handleKelolaDisabilitas(ActionEvent event) {
        tampilkanHalaman("KelolaDisabilitasView.fxml", jenisDisabilitasMenu);
    }

    @FXML
    void handleAkunAdmin(ActionEvent event) {
        tampilkanHalaman("KelolaAkunAdminView.fxml", akunAdminMenu);
    }

    @FXML
    void handleKelolaAkunAdmin(ActionEvent event) {
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
            URL url = getClass().getResource(lokasi);
            if (url == null) {
                System.err.println("File FXML tidak ditemukan: " + lokasi);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent halaman = loader.load();

            if (halaman instanceof BorderPane) {
                BorderPane bp = (BorderPane) halaman;
                if (bp.getCenter() != null) {
                    mainBorderPane.setCenter(bp.getCenter());
                } else {
                    mainBorderPane.setCenter(halaman);
                }
            } else {
                mainBorderPane.setCenter(halaman);
            }

            aturMenuAktif(menuTujuan);
        } catch (IOException e) {
            System.err.println("Gagal membuka halaman: " + namaFile);
            e.printStackTrace();
        }
    }

    private void aturMenuAktif(ToggleButton menuAktif) {
        String styleAktif = "-fx-background-color: #DCE9F5; -fx-text-fill: #1F4E8C; -fx-font-weight: 600; -fx-background-radius: 8;";
        String styleTidakAktif = "-fx-background-color: transparent; -fx-text-fill: #344054; -fx-background-radius: 8;";

        ToggleButton[] semuaMenu = {
                dashboardMenu, lowonganMenu, lamaranMenu, perusahaanMenu,
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