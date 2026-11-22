package com.example.projectpbokawansetara.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private List<LamaranTerbaru> semuaLamaranTerbaru = new ArrayList<>();

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

    private static final String KELAS_MENU_AKTIF = "menu-admin-aktif";
    private static final String KELAS_MENU_NONAKTIF = "menu-admin-nonaktif";

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

        if (cariField != null) {
            cariField.textProperty().addListener((obs, lama, baru) -> {
                if (mainBorderPane.getCenter() == halamanDashboardAwal) {
                    filterLamaranTerbaru(baru);
                }
            });
        }
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

        semuaLamaranTerbaru = dao.getLamaranTerbaru();
        if (cariField != null && cariField.getText() != null && !cariField.getText().trim().isEmpty()) {
            filterLamaranTerbaru(cariField.getText());
        } else {
            tampilkanDaftarLamaran(semuaLamaranTerbaru);
        }
    }

    private void filterLamaranTerbaru(String keyword) {
        String kunci = keyword == null ? "" : keyword.toLowerCase().trim();
        if (kunci.isEmpty()) {
            tampilkanDaftarLamaran(semuaLamaranTerbaru);
            return;
        }

        List<LamaranTerbaru> hasilFilter = new ArrayList<>();
        for (LamaranTerbaru lamaran : semuaLamaranTerbaru) {
            boolean cocokNama = lamaran.getNamaPelamar() != null
                    && lamaran.getNamaPelamar().toLowerCase().contains(kunci);
            boolean cocokPosisi = lamaran.getPosisi() != null
                    && lamaran.getPosisi().toLowerCase().contains(kunci);
            boolean cocokStatus = lamaran.getStatus() != null
                    && lamaran.getStatus().toLowerCase().contains(kunci);

            if (cocokNama || cocokPosisi || cocokStatus) {
                hasilFilter.add(lamaran);
            }
        }
        tampilkanDaftarLamaran(hasilFilter);
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
            row.getStyleClass().add("rata-kiri-tengah");
            row.setPrefHeight(40.0);

            row.getStyleClass().add("baris-lamaran-terbaru");

            Label lblNama = new Label(lamaran.getNamaPelamar());
            lblNama.getStyleClass().add("teks-nama-lamaran");
            lblNama.setMinWidth(140.0);
            lblNama.setPrefWidth(140.0);
            lblNama.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(lblNama, Priority.ALWAYS);

            Label lblPosisi = new Label(lamaran.getPosisi());
            lblPosisi.getStyleClass().add("teks-posisi-lamaran");
            lblPosisi.setMinWidth(150.0);
            lblPosisi.setPrefWidth(150.0);
            lblPosisi.setMaxWidth(150.0);
            lblPosisi.setEllipsisString("...");

            Label lblTanggal = new Label(lamaran.getTanggalMasuk());
            lblTanggal.getStyleClass().add("teks-tanggal-lamaran");
            lblTanggal.setMinWidth(100.0);
            lblTanggal.setPrefWidth(100.0);
            lblTanggal.setMaxWidth(100.0);

            String statusText = lamaran.getStatus() != null ? lamaran.getStatus() : "-";
            String statusKapital = statusText.isEmpty()
                    ? "-"
                    : statusText.substring(0, 1).toUpperCase() + statusText.substring(1);

            Label lblStatus = new Label(statusKapital);
            lblStatus.getStyleClass().addAll("status-badge", kelasStatus(statusText));

            HBox statusBox = new HBox(lblStatus);
            statusBox.getStyleClass().add("rata-kiri-tengah");
            statusBox.setMinWidth(100.0);
            statusBox.setPrefWidth(100.0);
            statusBox.setMaxWidth(100.0);

            row.getChildren().addAll(lblNama, lblPosisi, lblTanggal, statusBox);
            daftarLamaranBox.getChildren().add(row);
        }
    }

    private String kelasStatus(String status) {
        if (status == null) {
            return "status-badge-default";
        }
        return switch (status) {
            case "diproses" -> "status-badge-diproses";
            case "ditinjau" -> "status-badge-ditinjau";
            case "diterima" -> "status-badge-diterima";
            case "ditolak" -> "status-badge-ditolak";
            default -> "status-badge-default";
        };
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

            if (halaman instanceof BorderPane bp) {
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
        ToggleButton[] semuaMenu = {
                dashboardMenu,
                lowonganMenu,
                lamaranMenu,
                perusahaanMenu,
                itemChecklistMenu,
                jenisDisabilitasMenu,
                akunAdminMenu
        };

        for (ToggleButton menu : semuaMenu) {
            if (menu == null) {
                continue;
            }
            menu.setStyle(null);
            menu.getStyleClass().removeAll(
                    KELAS_MENU_AKTIF,
                    KELAS_MENU_NONAKTIF);
            menu.getStyleClass().add(KELAS_MENU_NONAKTIF);
            menu.setSelected(false);
        }

        if (menuAktif != null) {
            menuAktif.setSelected(true);
            menuAktif.getStyleClass().remove(KELAS_MENU_NONAKTIF);
            menuAktif.getStyleClass().add(KELAS_MENU_AKTIF);
        }
    }
}