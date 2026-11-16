package com.example.projectpbokawansetara.controller;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import com.example.projectpbokawansetara.model.Lowongan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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

        deskripsiLabel.setText(
                lowongan.getDeskripsi() != null
                        ? lowongan.getDeskripsi()
                        : "Deskripsi belum tersedia."
        );

        statusLabel.setText(
                lowongan.getStatus().equalsIgnoreCase("tersedia")
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

        pelamarLabel.setText("-");
        jumlahCocokLabel.setText("-");

        persyaratanBox.getChildren().clear();
        persyaratanBox.getChildren().add(
                new Label("Informasi persyaratan belum tersedia.")
        );

        keahlianBox.getChildren().clear();
        keahlianBox.getChildren().add(
                new Label("Informasi keahlian belum tersedia.")
        );
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
