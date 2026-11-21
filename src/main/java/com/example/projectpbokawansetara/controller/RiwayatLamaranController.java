package com.example.projectpbokawansetara.controller;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.projectpbokawansetara.dao.LamaranDAO;
import com.example.projectpbokawansetara.model.Lamaran;
import com.example.projectpbokawansetara.model.Lowongan;
import com.example.projectpbokawansetara.model.Pelamar;
import com.example.projectpbokawansetara.model.Session;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class RiwayatLamaranController {
    private DashboardPelamarController dashboardController;

    @FXML
    private VBox daftarLamaranBox;
    @FXML
    private Label kosongLabel;

    @FXML
    private ToggleButton semuaTab;
    @FXML
    private ToggleButton diprosesTab;
    @FXML
    private ToggleButton ditinjauTab;
    @FXML
    private ToggleButton diterimaTab;
    @FXML
    private ToggleButton ditolakTab;

    private final LamaranDAO lamaranDAO = new LamaranDAO();
    private List<Lamaran> daftarLamaran = new ArrayList<>();

    private final DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern("d MMM yyyy",
            Locale.forLanguageTag("id-ID"));

    private final DateTimeFormatter formatDiperbarui = DateTimeFormatter.ofPattern("d MMM",
            Locale.forLanguageTag("id-ID"));

    @FXML
    private void initialize() {
        semuaTab.setUserData("Semua");
        diprosesTab.setUserData("diproses");
        ditinjauTab.setUserData("ditinjau");
        diterimaTab.setUserData("diterima");
        ditolakTab.setUserData("ditolak");

        boolean berhasil = muatRiwayatLamaran();
        perbaruiJumlahTab();
        if (berhasil) {
            tampilkanLamaran("Semua");
        } else {
            kosongLabel.setVisible(true);
            kosongLabel.setManaged(true);
        }
    }

    private boolean muatRiwayatLamaran() {
        if (!(Session.getCurrentUser() instanceof Pelamar pelamar)) {
            kosongLabel.setText("Silakan login sebagai pelamar");
            return false;
        }

        try {
            daftarLamaran = lamaranDAO.getRiwayatLamaran(pelamar.getIdPengguna());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            kosongLabel.setText("Gagal memuat riwayat lamaran.");
            return false;
        }
    }

    public void setDashboardController(DashboardPelamarController dashboardController) {
        this.dashboardController = dashboardController;
    }

    private void perbaruiJumlahTab() {
        semuaTab.setText("Semua " + daftarLamaran.size());
        diprosesTab.setText("Diproses " + hitungStatus("diproses"));
        ditinjauTab.setText("Ditinjau " + hitungStatus("ditinjau"));
        diterimaTab.setText("Diterima " + hitungStatus("diterima"));
        ditolakTab.setText("Ditolak " + hitungStatus("ditolak"));
    }

    private long hitungStatus(String status) {
        return daftarLamaran.stream()
                .filter(lamaran -> lamaran.getStatus() != null
                        && lamaran.getStatus().equalsIgnoreCase(status))
                .count();
    }

    @FXML
    private void handleFilter(ActionEvent event) {
        ToggleButton tombol = (ToggleButton) event.getSource();
        tampilkanLamaran((String) tombol.getUserData());
    }

    private void tampilkanLamaran(String filter) {
        daftarLamaranBox.getChildren().clear();

        List<Lamaran> hasilFilter = daftarLamaran.stream()
                .filter(lamaran -> "Semua".equals(filter)
                        || (lamaran.getStatus() != null
                                && lamaran.getStatus().equalsIgnoreCase(filter)))
                .toList();

        if (hasilFilter.isEmpty()) {
            kosongLabel.setText("Belum ada lamaran");
            kosongLabel.setVisible(true);
            kosongLabel.setManaged(true);
            return;
        }

        kosongLabel.setVisible(false);
        kosongLabel.setManaged(false);

        for (Lamaran lamaran : hasilFilter) {
            daftarLamaranBox.getChildren().add(buatBarisLamaran(lamaran));
        }
    }

    private GridPane buatBarisLamaran(Lamaran lamaran) {
        Lowongan lowongan = lamaran.getLowongan();

        String namaPerusahaan = lowongan.getPerusahaan() != null
                ? lowongan.getPerusahaan().getNamaPerusahaan()
                : "-";

        Label avatar = buatAvatar(namaPerusahaan);

        Label posisi = buatKolom(lowongan.getNamaPekerjaan());
        posisi.getStyleClass().add("riwayat-posisi");

        Label perusahaan = new Label(namaPerusahaan);
        perusahaan.getStyleClass().add("riwayat-perusahaan");

        VBox infoPosisi = new VBox(2);
        infoPosisi.getChildren().addAll(posisi, perusahaan);

        HBox posisiBox = new HBox(12);
        posisiBox.getStyleClass().add("rata-kiri-tengah");
        posisiBox.getChildren().addAll(avatar, infoPosisi);

        Label lokasi = buatKolom(lowongan.getLokasi());
        Label jenis = buatKolom(kapitalisasiJenis(lowongan.getJenisLowongan()));

        String tanggalLamar = lamaran.getTanggalLamar() != null
                ? lamaran.getTanggalLamar().format(formatTanggal)
                : "-";

        String tanggalDiperbarui = lamaran.getTanggalDiperbarui() != null
                ? lamaran.getTanggalDiperbarui().format(formatDiperbarui)
                : "-";

        Label tanggal = buatKolom(tanggalLamar);
        Label diperbarui = buatKolom(tanggalDiperbarui);

        HBox statusBox = buatStatusBox(lamaran.getStatus());

        Button lihatButton = new Button("Lihat lowongan");
        lihatButton.getStyleClass().add("tombol-lihat-lowongan");
        lihatButton.setOnAction(event -> bukaDetailLowongan(lowongan));

        GridPane baris = buatGridKolom();

        baris.add(posisiBox, 0, 0);
        baris.add(lokasi, 1, 0);
        baris.add(jenis, 2, 0);
        baris.add(tanggal, 3, 0);
        baris.add(statusBox, 4, 0);
        baris.add(diperbarui, 5, 0);
        baris.add(lihatButton, 6, 0);

        GridPane.setVgrow(posisiBox, Priority.ALWAYS);
        baris.getStyleClass().add("rata-kiri-tengah");
        baris.getStyleClass().add("riwayat-baris");

        return baris;
    }

    private Label buatAvatar(String namaPerusahaan) {
        String inisial = buatInisial(namaPerusahaan);

        Label avatar = new Label(inisial);
        avatar.getStyleClass().add("avatar-riwayat");
        avatar.getStyleClass().add(tentukanWarnaAvatar(namaPerusahaan));

        return avatar;
    }

    private String buatInisial(String nama) {
        if (nama == null || nama.isBlank() || nama.equals("-")) {
            return "?";
        }

        List<String> kata = pecahKata(nama.trim());
        StringBuilder sb = new StringBuilder();

        for (String k : kata) {
            if (k.equalsIgnoreCase("PT") || k.equalsIgnoreCase("CV") || k.isBlank()) {
                continue;
            }
            sb.append(Character.toUpperCase(k.charAt(0)));
            if (sb.length() >= 2)
                break;
        }

        if (sb.length() == 0) {
            sb.append(Character.toUpperCase(nama.charAt(0)));
        }

        return sb.toString();
    }

    private List<String> pecahKata(String teks) {
        List<String> hasil = new ArrayList<>();
        if (teks == null || teks.isEmpty()) {
            return hasil;
        }

        StringBuilder kata = new StringBuilder();
        for (int i = 0; i < teks.length(); i++) {
            char c = teks.charAt(i);
            if (Character.isWhitespace(c)) {
                if (kata.length() > 0) {
                    hasil.add(kata.toString());
                    kata.setLength(0);
                }
            } else {
                kata.append(c);
            }
        }
        if (kata.length() > 0) {
            hasil.add(kata.toString());
        }
        return hasil;
    }

    private String tentukanWarnaAvatar(String nama) {
        if (nama == null)
            return "avatar-biru";

        int hash = Math.abs(nama.hashCode() % 6);
        return switch (hash) {
            case 0 -> "avatar-biru";
            case 1 -> "avatar-ungu";
            case 2 -> "avatar-cyan";
            case 3 -> "avatar-oranye";
            case 4 -> "avatar-hijau";
            default -> "avatar-pink";
        };
    }

    private HBox buatStatusBox(String status) {
        HBox box = new HBox(6);
        box.getStyleClass().add("rata-kiri-tengah");

        Region titik = new Region();
        titik.setMinSize(8, 8);
        titik.setPrefSize(8, 8);
        titik.setMaxSize(8, 8);
        titik.getStyleClass().add("status-titik");

        Label teks = new Label(formatStatus(status));
        teks.getStyleClass().add("status-teks");

        if (status != null) {
            switch (status.toLowerCase()) {
                case "diproses" -> {
                    titik.getStyleClass().add("titik-diproses");
                    teks.getStyleClass().add("teks-diproses");
                }
                case "ditinjau" -> {
                    titik.getStyleClass().add("titik-ditinjau");
                    teks.getStyleClass().add("teks-ditinjau");
                }
                case "diterima" -> {
                    titik.getStyleClass().add("titik-diterima");
                    teks.getStyleClass().add("teks-diterima");
                }
                case "ditolak" -> {
                    titik.getStyleClass().add("titik-ditolak");
                    teks.getStyleClass().add("teks-ditolak");
                }
                default -> {
                    titik.getStyleClass().add("titik-default");
                    teks.getStyleClass().add("teks-default");
                }
            }
        }

        box.getChildren().addAll(titik, teks);
        return box;
    }

    private String kapitalisasiJenis(String jenis) {
        if (jenis == null || jenis.isBlank())
            return "-";
        return jenis.substring(0, 1).toUpperCase() + jenis.substring(1).toLowerCase();
    }

    private GridPane buatGridKolom() {
        GridPane grid = new GridPane();

        double[] proporsi = { 30, 12, 11, 14, 12, 11, 10 };

        for (double lebar : proporsi) {
            ColumnConstraints kolom = new ColumnConstraints();
            kolom.setPercentWidth(lebar);
            kolom.setHgrow(Priority.ALWAYS);
            kolom.setFillWidth(true);
            grid.getColumnConstraints().add(kolom);
        }

        grid.setMaxWidth(Double.MAX_VALUE);
        return grid;
    }

    private Label buatKolom(String teks) {
        Label label = new Label(teks == null || teks.isBlank() ? "-" : teks);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setWrapText(true);
        label.getStyleClass().add("rata-kiri-tengah");
        label.getStyleClass().add("riwayat-kolom");
        GridPane.setHgrow(label, Priority.ALWAYS);
        return label;
    }

    private String formatStatus(String status) {
        if (status == null)
            return "-";
        return switch (status.toLowerCase()) {
            case "diproses" -> "Diproses";
            case "ditinjau" -> "Ditinjau";
            case "diterima" -> "Diterima";
            case "ditolak" -> "Ditolak";
            default -> status;
        };
    }

    private void bukaDetailLowongan(Lowongan lowongan) {
        if (dashboardController != null) {
            dashboardController.bukaDetailLowongan(lowongan);
        }
    }
}