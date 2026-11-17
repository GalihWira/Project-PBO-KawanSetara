package com.example.projectpbokawansetara.controller;

import com.example.projectpbokawansetara.dao.DashboardDAO;
import com.example.projectpbokawansetara.model.DashboardStatistik;
import com.example.projectpbokawansetara.model.LamaranTerbaru;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import java.util.List;

import java.io.IOException;
import java.net.URL;

import com.example.projectpbokawansetara.helper.DialogHelper;
import com.example.projectpbokawansetara.model.Session;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;

public class DashboardAdminController {

    private Node halamanDashboardAwal;

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

    @FXML
    private void initialize() {
        // if (!Session.isLoggedIn()) {
        //     Platform.runLater(() -> {
        //         try {
        //             Parent root = FXMLLoader.load(
        //                 getClass().getResource(
        //                     "/com/example/projectpbokawansetara/view/LoginView.fxml"
        //                 )
        //             );

        //             Stage stage = (Stage) mainBorderPane.getScene().getWindow();
        //             stage.setScene(new Scene(root));
        //             stage.setTitle("Masuk - KawanSetara");
        //             stage.sizeToScene();
        //             stage.centerOnScreen();

        //         } catch (IOException e) {
        //             e.printStackTrace();
        //         }
        //     });

        //     return;
        //}
        loadDataDashboard();
        halamanDashboardAwal = mainBorderPane.getCenter();
        aturMenuAktif(dashboardMenu);
    }

    // Dashboard
    @FXML
    void handleDashboardMenu(ActionEvent event) {
        kembaliKeDashboard();
    }

    @FXML
    void handleDashboardMenuClick(MouseEvent event) {
        kembaliKeDashboard();
    }

    private void loadDataDashboard() {
        DashboardDAO dao = new DashboardDAO();
        
        // 1. Load Statistik Angka
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

        List<LamaranTerbaru> listLamaran = dao.getLamaranTerbaru();
        
        daftarLamaranBox.getChildren().clear(); 

        if (listLamaran.isEmpty()) {
            lamaranKosongLabel.setVisible(true);
            lamaranKosongLabel.setManaged(true);
        } else {
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
                HBox.setHgrow(lblNama, Priority.ALWAYS); // Biar responsif mengikuti header

                Label lblPosisi = new Label(lamaran.getPosisi());
                lblPosisi.setStyle("-fx-text-fill: #667085; -fx-font-size: 13px;");
                lblPosisi.setPrefWidth(110.0);

                Label lblTanggal = new Label(lamaran.getTanggalMasuk());
                lblTanggal.setStyle("-fx-text-fill: #667085; -fx-font-size: 13px;");
                lblTanggal.setPrefWidth(70.0);

                Label lblStatus = new Label(lamaran.getStatus().substring(0, 1).toUpperCase() + lamaran.getStatus().substring(1));
                lblStatus.setPrefWidth(70.0);
                lblStatus.setPadding(new Insets(2, 8, 2, 8));
                lblStatus.setStyle("-fx-font-size: 12px; -fx-background-radius: 10; -fx-alignment: center; " + 
                    (lamaran.getStatus().equals("diproses") ? "-fx-background-color: #FEF0C7; -fx-text-fill: #B54708;" : 
                    (lamaran.getStatus().equals("diterima") ? "-fx-background-color: #D1FADF; -fx-text-fill: #027A48;" : 
                    "-fx-background-color: #F2F4F7; -fx-text-fill: #344054;")));

                row.getChildren().addAll(lblNama, lblPosisi, lblTanggal, lblStatus);
                daftarLamaranBox.getChildren().add(row);
            }
        }
    }

    private void kembaliKeDashboard() {
        try {
            if (halamanDashboardAwal != null) {
                mainBorderPane.setCenter(halamanDashboardAwal);
            } else {
                URL url = getClass().getResource("/com/example/projectpbokawansetara/view/DashboardAdminView.fxml");
                Parent root = FXMLLoader.load(url);
                if (root instanceof BorderPane bp && bp.getCenter() != null) {
                    mainBorderPane.setCenter(bp.getCenter());
                }
            }
            aturMenuAktif(dashboardMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Lowongan
    @FXML
    void handleBukaLowongan(ActionEvent event) {
        tampilkanHalaman("KelolaLowonganView.fxml", lowonganMenu);
    }

    @FXML
    void handleKelolaLowongan(ActionEvent event) {
        tampilkanHalaman("KelolaLowonganView.fxml", lowonganMenu);
    }

    // Perusahaan
    @FXML
    void handleBukaPerusahaan(ActionEvent event) {
        tampilkanHalaman("KelolaPerusahaanView.fxml", perusahaanMenu);
    }

    @FXML
    void handleKelolaPerusahaan(ActionEvent event) {
        tampilkanHalaman("KelolaPerusahaanView.fxml", perusahaanMenu);
    }

    @FXML
    void handleBukaPerusahaanMouse(MouseEvent event) {
        tampilkanHalaman("KelolaPerusahaanView.fxml", perusahaanMenu);
    }

    // Item Cheklist
    @FXML
    void handleBukaItemChecklist(ActionEvent event) {
        tampilkanHalaman("KelolaChecklistView.fxml", itemChecklistMenu);
    }

    @FXML
    void handleKelolaChecklist(ActionEvent event) {
        tampilkanHalaman("KelolaChecklistView.fxml", itemChecklistMenu);
    }

    // Jenis disabilitas
    @FXML
    void handleBukaDisabilitas(ActionEvent event) {
        tampilkanHalaman("KelolaDisabilitasView.fxml", jenisDisabilitasMenu);
    }

    @FXML
    void handleKelolaDisabilitas(ActionEvent event) {
        tampilkanHalaman("KelolaDisabilitasView.fxml", jenisDisabilitasMenu);
    }

    // --- AKUN ADMIN ---
    @FXML
    void handleAkunAdmin(ActionEvent event) {
        tampilkanHalaman("KelolaAkunAdminView.fxml", akunAdminMenu);
    }

    @FXML
    void handleKelolaAkunAdmin(ActionEvent event) {
        tampilkanHalaman("KelolaAkunAdminView.fxml", akunAdminMenu);
    }

    // --- KELUAR ---
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
                System.err.println("FILE FXML TIDAK DITEMUKAN: " + lokasi);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent halaman = loader.load();

            if (halaman instanceof BorderPane halamanBorder && halamanBorder.getCenter() != null) {
                mainBorderPane.setCenter(halamanBorder.getCenter());
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