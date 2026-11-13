package com.example.projectpbokawansetara.controller;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import com.example.projectpbokawansetara.dao.LowonganDAO;
import com.example.projectpbokawansetara.model.Lowongan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CariLowonganController {

    @FXML
    private CheckBox fullTimeCheckBox;

    @FXML
    private CheckBox partTimeCheckBox;

    @FXML
    private Label fullTimeJumlahLabel;

    @FXML
    private Label partTimeJumlahLabel;

    @FXML
    private VBox lokasiFilterBox;

    @FXML
    private TextField gajiMinimumField;

    @FXML
    private TextField kataKunciField;

    @FXML
    private ComboBox<String> urutkanComboBox;

    @FXML
    private Label jumlahLowonganLabel;

    @FXML
    private Label ringkasanFilterLabel;

    @FXML
    private VBox daftarLowonganBox;

    @FXML
    private Label kosongLabel;

    @FXML
    private Label infoHalamanLabel;

    @FXML
    private Button sebelumnyaButton;

    @FXML
    private Button berikutnyaButton;

    @FXML
    private Button resetFilterButton;

    private DashboardPelamarController dashboardController;

    private final LowonganDAO lowonganDAO = new LowonganDAO();

    private List<Lowongan> semuaLowongan = new ArrayList<>();

    private List<Lowongan> hasilFilterSaatIni = new ArrayList<>();

    private int halamanSekarang = 1;

    private final int jumlahPerHalaman = 5;

    @FXML
    private void initialize() {
        urutkanComboBox.getItems().addAll(
            "Paling cocok",
            "Gaji tertinggi",
            "Gaji terendah",
            "Nama pekerjaan A-Z"
        );

        urutkanComboBox.setValue("Paling cocok");
        urutkanComboBox.setOnAction(event -> terapkanFilter());

        muatLowongan();
        terapkanFilter();
        
        kataKunciField.textProperty().addListener((observable, nilaiLama, nilaiBaru) -> {
            terapkanFilter();
        });

        gajiMinimumField.textProperty().addListener((observable, nilaiLama, nilaiBaru) -> {
            terapkanFilter();
        });
    }

    public void setDashboardController(DashboardPelamarController controller) {
        this.dashboardController = controller;
    }

    private void muatLowongan() {
        try {
            semuaLowongan = lowonganDAO.getSemuaLowongan();

            hitungJenisLowongan();
            buatFilterLokasi();

            tampilkanLowongan(semuaLowongan);

        } catch (SQLException e) {
            System.err.println("Gagal mengambil data lowongan.");
            e.printStackTrace();
        }
    }

    private void terapkanFilter() {
        String kataKunci = kataKunciField.getText()
                .trim()
                .toLowerCase();

        double gajiMinimum = 0;

        try {
            if (!gajiMinimumField.getText().isBlank()) {
                gajiMinimum = Double.parseDouble(
                        gajiMinimumField.getText().replaceAll("[^0-9]", "")
                );
            }
        } catch (NumberFormatException e) {
            gajiMinimum = 0;
        }

        final double batasGaji = gajiMinimum;

        List<String> lokasiDipilih = new ArrayList<>();

        for (javafx.scene.Node node : lokasiFilterBox.getChildren()) {
            if (node instanceof CheckBox checkBox) {
                System.out.println(
                        checkBox.getText() + " = " + checkBox.isSelected()
                );

                if (checkBox.isSelected()) {
                    lokasiDipilih.add(checkBox.getText());
                }
            }
        }

        List<Lowongan> hasilFilter = new ArrayList<>(
            semuaLowongan.stream().filter(lowongan -> {
                String namaPekerjaan = lowongan.getNamaPekerjaan() != null
                        ? lowongan.getNamaPekerjaan().toLowerCase()
                        : "";

                String namaPerusahaan = lowongan.getPerusahaan() != null
                        && lowongan.getPerusahaan().getNamaPerusahaan() != null
                        ? lowongan.getPerusahaan().getNamaPerusahaan().toLowerCase()
                        : "";

                boolean cocokKataKunci = namaPekerjaan.contains(kataKunci)
                        || namaPerusahaan.contains(kataKunci);

                boolean cocokJenis = false;

                if (fullTimeCheckBox.isSelected()
                        && "full time".equalsIgnoreCase(lowongan.getJenisLowongan())) {
                    cocokJenis = true;
                }

                if (partTimeCheckBox.isSelected()
                        && "part time".equalsIgnoreCase(lowongan.getJenisLowongan())) {
                    cocokJenis = true;
                }

                boolean cocokLokasi = lokasiDipilih.isEmpty()
                        || lokasiDipilih.stream().anyMatch(lokasi ->
                                lokasi.equalsIgnoreCase(lowongan.getLokasi()));

                boolean cocokGaji = lowongan.getGaji() >= batasGaji;
                return cocokKataKunci && cocokJenis && cocokLokasi && cocokGaji;
            })
            .toList()       
        );

        for (Lowongan lowongan : hasilFilter) {
            System.out.println(
                    lowongan.getNamaPekerjaan() + " | " + lowongan.getLokasi()
            );
        }

        String urutan = urutkanComboBox.getValue();

        if (urutan != null) {
            switch (urutan) {
                case "Gaji tertinggi":
                    hasilFilter.sort((a, b) ->
                            Double.compare(b.getGaji(), a.getGaji()));
                    break;

                case "Gaji terendah":
                    hasilFilter.sort((a, b) ->
                            Double.compare(a.getGaji(), b.getGaji()));
                    break;

                case "Nama pekerjaan A-Z":
                    hasilFilter.sort((a, b) ->
                            a.getNamaPekerjaan().compareToIgnoreCase(
                                    b.getNamaPekerjaan()));
                    break;
            }
        }

        hasilFilterSaatIni = new ArrayList<>(hasilFilter);
        halamanSekarang = 1;
        tampilkanHalaman();
    }

    private void hitungJenisLowongan() {
        long jumlahFullTime = semuaLowongan.stream()
                .filter(l -> "full time".equalsIgnoreCase(l.getJenisLowongan()))
                .count();

        long jumlahPartTime = semuaLowongan.stream()
                .filter(l -> "part time".equalsIgnoreCase(l.getJenisLowongan()))
                .count();

        fullTimeJumlahLabel.setText(String.valueOf(jumlahFullTime));
        partTimeJumlahLabel.setText(String.valueOf(jumlahPartTime));
    }

    private void buatFilterLokasi() {
        lokasiFilterBox.getChildren().clear();

        Set<String> lokasiUnik = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        for (Lowongan lowongan : semuaLowongan) {
            if (lowongan.getLokasi() != null && !lowongan.getLokasi().isBlank()) {
                lokasiUnik.add(lowongan.getLokasi());
            }
        }

        for (String lokasi : lokasiUnik) {
            CheckBox checkBox = new CheckBox(lokasi);
            checkBox.setStyle("-fx-text-fill: #344054; -fx-font-size: 13px;");
            checkBox.setOnAction(event -> terapkanFilter());

            lokasiFilterBox.getChildren().add(checkBox);
        }
    }

    private void tampilkanLowongan(List<Lowongan> daftar) {
        daftarLowonganBox.getChildren().clear();

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(
                new Locale("id", "ID")
        );
        formatRupiah.setMaximumFractionDigits(0);

        for (Lowongan lowongan : daftar) {

            Label gambar = new Label("LOGO");
            gambar.getStyleClass().add("avatar-lowongan");

            Label nama = new Label(lowongan.getNamaPekerjaan());
            nama.getStyleClass().add("nama-lowongan");

            Label perusahaan = new Label(
                    lowongan.getPerusahaan() != null
                            ? lowongan.getPerusahaan().getNamaPerusahaan()
                            : "Perusahaan"
            );
            perusahaan.getStyleClass().add("perusahaan-lowongan");

            VBox identitas = new VBox(6, nama, perusahaan);
            identitas.getStyleClass().add("identitas-lowongan");
            identitas.setPrefWidth(230);
            identitas.setAlignment(Pos.CENTER_LEFT);

            String jenisLowongan = lowongan.getJenisLowongan() != null
                    ? lowongan.getJenisLowongan()
                    : "Belum tersedia";

            if (!jenisLowongan.equals("Belum tersedia")) {
                jenisLowongan = jenisLowongan.substring(0, 1).toUpperCase()
                        + jenisLowongan.substring(1);
            }

            Label jenis = new Label(jenisLowongan);
            jenis.getStyleClass().add("jenis-lowongan");

            Label durasi = new Label("Durasi belum tersedia");
            durasi.getStyleClass().add("detail-lowongan");

            VBox informasiJenis = new VBox(6, jenis, durasi);
            informasiJenis.getStyleClass().add("kolom-lowongan");
            informasiJenis.setPrefWidth(120);
            informasiJenis.setAlignment(Pos.CENTER_LEFT); 

            Label kecocokan = new Label("Belum dihitung");
            kecocokan.getStyleClass().add("nilai-kecocokan");

            Label keteranganCocok = new Label("Kecocokan");
            keteranganCocok.getStyleClass().add("detail-lowongan");

            VBox informasiCocok = new VBox(6, kecocokan, keteranganCocok);
            informasiCocok.setPrefWidth(100);
            informasiCocok.setAlignment(Pos.CENTER_LEFT);

            Label gaji = new Label(
                    formatRupiah.format(lowongan.getGaji())
            );
            gaji.getStyleClass().add("gaji-lowongan");

            Label update = new Label("Update belum tersedia");
            update.getStyleClass().add("detail-lowongan");

            VBox informasiGaji = new VBox(6, gaji, update);
            informasiGaji.setPrefWidth(150);
            informasiGaji.setAlignment(Pos.CENTER_RIGHT);
            informasiGaji.getStyleClass().add("informasi-gaji");

            gaji.setMaxWidth(Double.MAX_VALUE);
            gaji.setAlignment(Pos.CENTER_RIGHT);

            update.setMaxWidth(Double.MAX_VALUE);
            update.setAlignment(Pos.CENTER_RIGHT);

            HBox kartu = new HBox(
                    16,
                    gambar,
                    identitas,
                    informasiJenis,
                    informasiCocok,
                    informasiGaji
            );

            kartu.setAlignment(Pos.CENTER_LEFT);

            kartu.getStyleClass().add("kartu-lowongan-horizontal");
            kartu.setCursor(Cursor.HAND);

            kartu.setOnMouseClicked(event -> {
                if (dashboardController != null) {
                    dashboardController.bukaDetailLowongan(lowongan);
                }
            });

            daftarLowonganBox.getChildren().add(kartu);
        }

        boolean kosong = daftar.isEmpty();

        kosongLabel.setVisible(kosong);
        kosongLabel.setManaged(kosong);

        jumlahLowonganLabel.setText(daftar.size() + " lowongan");

        infoHalamanLabel.setText(
                "Menampilkan " + daftar.size() + " dari " + semuaLowongan.size()
        );
    }

    private void tampilkanHalaman() {
        int totalLowongan = hasilFilterSaatIni.size();

        int totalHalaman = (int) Math.ceil(
                (double) totalLowongan / jumlahPerHalaman
        );

        int indeksAwal = (halamanSekarang - 1) * jumlahPerHalaman;
        int indeksAkhir = Math.min(
                indeksAwal + jumlahPerHalaman,
                totalLowongan
        );

        List<Lowongan> lowonganHalaman = hasilFilterSaatIni.subList(
                indeksAwal,
                indeksAkhir
        );

        tampilkanLowongan(lowonganHalaman);

        jumlahLowonganLabel.setText(totalLowongan + " lowongan");

        infoHalamanLabel.setText(
                "Halaman " + halamanSekarang + " dari " + Math.max(1, totalHalaman)
        );

        sebelumnyaButton.setDisable(halamanSekarang <= 1);
        berikutnyaButton.setDisable(halamanSekarang >= totalHalaman);
    }

    @FXML
    private void handleFilter(ActionEvent event) {
        terapkanFilter();
    }

    @FXML
    private void handleResetFilter(ActionEvent event) {
        kataKunciField.clear();
        gajiMinimumField.clear();

        fullTimeCheckBox.setSelected(true);
        partTimeCheckBox.setSelected(false);

        for (javafx.scene.Node node : lokasiFilterBox.getChildren()) {
            if (node instanceof CheckBox checkBox) {
                checkBox.setSelected(false);
            }
        }

        urutkanComboBox.setValue("Paling cocok");

        terapkanFilter();
    }

    @FXML
    private void handleSebelumnya(ActionEvent event) {
        if (halamanSekarang > 1) {
            halamanSekarang--;
            tampilkanHalaman();
        }
    }

    @FXML
    private void handleBerikutnya(ActionEvent event) {
        int totalHalaman = (int) Math.ceil(
                (double) hasilFilterSaatIni.size() / jumlahPerHalaman
        );

        if (halamanSekarang < totalHalaman) {
            halamanSekarang++;
            tampilkanHalaman();
        }
    }
}
