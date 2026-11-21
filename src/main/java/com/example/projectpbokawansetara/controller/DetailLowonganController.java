package com.example.projectpbokawansetara.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.example.projectpbokawansetara.dao.PelamarDAO;
import com.example.projectpbokawansetara.model.Keahlian;
import com.example.projectpbokawansetara.model.Lowongan;
import com.example.projectpbokawansetara.model.Pelamar;
import com.example.projectpbokawansetara.model.Session;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DetailLowonganController {

    @FXML
    private Hyperlink kembaliLink;

    @FXML
    private Label breadcrumbLabel;

    @FXML
    private ImageView logoPerusahaanImage;

    @FXML
    private Label namaPekerjaanLabel;

    @FXML
    private Label perusahaanLabel;

    @FXML
    private Label subjudulLabel;

    @FXML
    private Label tagJenisLabel;

    @FXML
    private Label tagGajiLabel;

    @FXML
    private Label tagAksesLabel;

    @FXML
    private Button lamarButton;

    @FXML
    private Label deskripsiLabel;

    @FXML
    private VBox persyaratanBox;

    @FXML
    private Label jumlahCocokLabel;

    @FXML
    private VBox keahlianBox;

    @FXML
    private Label statusLabel;

    @FXML
    private Label jenisLabel;

    @FXML
    private Label gajiLabel;

    @FXML
    private Label lokasiLabel;

    @FXML
    private Label pelamarLabel;

    @FXML
    private Label kartuPerusahaanLabel;

    @FXML
    private Label alamatPerusahaanLabel;

    private final PelamarDAO pelamarDAO = new PelamarDAO();

    private Lowongan lowongan;
    private Runnable aksiKembali;

    public void setLowongan(Lowongan lowongan) {
        this.lowongan = lowongan;
        tampilkanDetail();
    }

    public void setAksiKembali(Runnable aksiKembali) {
        this.aksiKembali = aksiKembali;
    }

    private void tampilkanDetail() {
        if (lowongan == null) {
            return;
        }

        String namaPerusahaan = lowongan.getPerusahaan() != null
                ? lowongan.getPerusahaan().getNamaPerusahaan()
                : "Perusahaan";

        String lokasi = lowongan.getLokasi() != null
                ? lowongan.getLokasi()
                : "Lokasi belum tersedia";

        String jenis = lowongan.getJenisLowongan() != null
                ? lowongan.getJenisLowongan()
                : "-";

        String gaji = formatRupiah(lowongan.getGaji());

        namaPekerjaanLabel.setText(lowongan.getNamaPekerjaan());
        breadcrumbLabel.setText(lowongan.getNamaPekerjaan());

        perusahaanLabel.setText(namaPerusahaan);
        subjudulLabel.setText(" · " + lokasi);

        tagJenisLabel.setText(jenis);
        tagGajiLabel.setText(gaji + "/bulan");
        tagAksesLabel.setText("Informasi aksesibilitas belum tersedia");

        // Deskripsi
        deskripsiLabel.setText(
                lowongan.getDeskripsi() != null && !lowongan.getDeskripsi().isBlank()
                        ? lowongan.getDeskripsi()
                        : "Deskripsi belum tersedia."
        );

        String status = lowongan.getStatus() != null ? lowongan.getStatus() : "";
        statusLabel.setText(
                status.equalsIgnoreCase("tersedia")
                        ? "● Tersedia"
                        : "● Tidak tersedia"
        );

        jenisLabel.setText(jenis);
        gajiLabel.setText(gaji + " / bulan");
        lokasiLabel.setText(lokasi);

        kartuPerusahaanLabel.setText(namaPerusahaan);

        alamatPerusahaanLabel.setText(
                lowongan.getPerusahaan() != null
                        && lowongan.getPerusahaan().getAlamat() != null
                        ? lowongan.getPerusahaan().getAlamat()
                        : "Alamat belum tersedia"
        );

        pelamarLabel.setText(lowongan.getJumlahPelamar() + " orang");

        tampilkanPersyaratan();
        tampilkanKeahlianDanKecocokan();
    }

    private void tampilkanPersyaratan() {
        persyaratanBox.getChildren().clear();

        String persyaratan = lowongan.getPersyaratan();
        if (persyaratan == null || persyaratan.isBlank()) {
            Label kosong = new Label("Informasi persyaratan belum tersedia.");
            kosong.setStyle("-fx-text-fill: #667085; -fx-font-size: 13px;");
            persyaratanBox.getChildren().add(kosong);
            return;
        }

        String[] baris = persyaratan.split("\\r?\\n|•|;");
        boolean adaIsi = false;

        for (String item : baris) {
            String teks = item.trim();
            if (teks.isEmpty()) {
                continue;
            }
            // buang nomor urut di awal (1. / 1) / - )
            teks = teks.replaceFirst("^\\d+[.)]\\s*", "").replaceFirst("^[-–]\\s*", "");
            if (teks.isEmpty()) {
                continue;
            }

            adaIsi = true;
            Label label = new Label("•  " + teks);
            label.setWrapText(true);
            label.setStyle("-fx-text-fill: #475467; -fx-font-size: 13px;");
            persyaratanBox.getChildren().add(label);
        }

        if (!adaIsi) {
            Label label = new Label(persyaratan.trim());
            label.setWrapText(true);
            label.setStyle("-fx-text-fill: #475467; -fx-font-size: 13px;");
            persyaratanBox.getChildren().add(label);
        }
    }

    private void tampilkanKeahlianDanKecocokan() {
        keahlianBox.getChildren().clear();

        List<Keahlian> dibutuhkan = lowongan.getDaftarKeahlian();
        if (dibutuhkan == null || dibutuhkan.isEmpty()) {
            jumlahCocokLabel.setText("Belum ada data keahlian");
            jumlahCocokLabel.setStyle("-fx-text-fill: #667085; -fx-font-size: 13px; -fx-font-weight: 600;");

            Label kosong = new Label("Informasi keahlian belum tersedia.");
            kosong.setStyle("-fx-text-fill: #667085; -fx-font-size: 13px;");
            keahlianBox.getChildren().add(kosong);
            return;
        }

        Set<Integer> idKeahlianPelamar = ambilIdKeahlianPelamar();

        int jumlahCocok = 0;
        FlowPane chipPane = new FlowPane();
        chipPane.setHgap(8);
        chipPane.setVgap(8);

        for (Keahlian k : dibutuhkan) {
            boolean cocok = idKeahlianPelamar.contains(k.getIdKeahlian());
            if (cocok) {
                jumlahCocok++;
            }

            Label chip = new Label(k.getNamaKeahlian() != null ? k.getNamaKeahlian() : "-");
            chip.setPadding(new Insets(4, 10, 4, 10));
            if (cocok) {
                chip.setStyle(
                        "-fx-background-color: #D1FADF; -fx-text-fill: #027A48;"
                        + "-fx-font-size: 12px; -fx-font-weight: 600; -fx-background-radius: 16;");
            } else {
                chip.setStyle(
                        "-fx-background-color: #F2F4F7; -fx-text-fill: #475467;"
                        + "-fx-font-size: 12px; -fx-font-weight: 600; -fx-background-radius: 16;");
            }
            chipPane.getChildren().add(chip);
        }

        keahlianBox.getChildren().add(chipPane);

        jumlahCocokLabel.setText(jumlahCocok + " dari " + dibutuhkan.size() + " cocok");
        if (jumlahCocok == dibutuhkan.size()) {
            jumlahCocokLabel.setStyle("-fx-text-fill: #12B76A; -fx-font-size: 13px; -fx-font-weight: 600;");
        } else if (jumlahCocok > 0) {
            jumlahCocokLabel.setStyle("-fx-text-fill: #F79009; -fx-font-size: 13px; -fx-font-weight: 600;");
        } else {
            jumlahCocokLabel.setStyle("-fx-text-fill: #F04438; -fx-font-size: 13px; -fx-font-weight: 600;");
        }
    }

    private Set<Integer> ambilIdKeahlianPelamar() {
        Set<Integer> hasil = new HashSet<>();
        if (!(Session.getCurrentUser() instanceof Pelamar pelamar)) {
            return hasil;
        }
        try {
            List<Keahlian> daftar = pelamarDAO.getKeahlianByPelamar(pelamar.getIdPengguna());
            if (daftar != null) {
                for (Keahlian k : daftar) {
                    hasil.add(k.getIdKeahlian());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hasil;
    }

    private String formatRupiah(double nominal) {
        NumberFormat format = NumberFormat.getCurrencyInstance(
                new Locale("id", "ID")
        );

        format.setMaximumFractionDigits(0);
        format.setMinimumFractionDigits(0);

        return format.format(nominal);
    }

    @FXML
    private void handleKembali(ActionEvent event) {
        if (aksiKembali != null) {
            aksiKembali.run();
        }
    }

    @FXML
    private void handleLamar(ActionEvent event) {
        try {
            String lokasi = "/com/example/projectpbokawansetara/view/KonfirmasiLamarDialog.fxml";

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(lokasi)
            );

            Parent halamanDialog = loader.load();

            KonfirmasiLamarController controller = loader.getController();
            controller.setLowongan(lowongan);

            Scene scene = lamarButton.getScene();
            Parent halamanAwal = scene.getRoot();

            StackPane pembungkus = new StackPane();
            StackPane overlay = new StackPane();

            pembungkus.getStylesheets().add(
                    getClass().getResource("/css/style.css").toExternalForm()
            );

            overlay.getStyleClass().add("overlay-dialog");
            overlay.setPickOnBounds(true);

            if (halamanDialog instanceof Region) {
                ((Region) halamanDialog).setMaxSize(
                        Region.USE_PREF_SIZE,
                        Region.USE_PREF_SIZE
                );
            }

            overlay.getChildren().add(halamanDialog);

            scene.setRoot(pembungkus);

            pembungkus.getChildren().addAll(
                    halamanAwal,
                    overlay
            );

            Runnable tutupOverlay = () -> {
                pembungkus.getChildren().clear();
                scene.setRoot(halamanAwal);
            };

            controller.setAksiBatal(tutupOverlay);

            controller.setAksiBerhasil(() -> {
                tutupOverlay.run();

                javafx.scene.control.Alert alert =
                        new javafx.scene.control.Alert(
                                javafx.scene.control.Alert.AlertType.INFORMATION
                        );

                alert.setTitle("Lamaran Berhasil");
                alert.setHeaderText("Lamaran berhasil dikirim!");
                alert.setContentText(
                        "Kamu dapat memantau status lamaran melalui Riwayat Lamaran."
                );

                alert.showAndWait();
            });

        } catch (IOException | NullPointerException e) {
            System.err.println("Gagal membuka dialog konfirmasi lamaran.");
            e.printStackTrace();
        }
    }
}