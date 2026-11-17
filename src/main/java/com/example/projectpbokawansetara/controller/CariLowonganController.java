package com.example.projectpbokawansetara.controller;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.example.projectpbokawansetara.dao.LowonganDAO;
import com.example.projectpbokawansetara.dao.PelamarDAO;
import com.example.projectpbokawansetara.model.Lowongan;
import com.example.projectpbokawansetara.model.Pelamar;
import com.example.projectpbokawansetara.model.Session;
import com.example.projectpbokawansetara.model.Keahlian;

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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.geometry.Insets;

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
    private List<Keahlian> keahlianPelamar = new ArrayList<>();
    private final PelamarDAO pelamarDAO = new PelamarDAO(); 

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
            if (Session.getCurrentUser() instanceof Pelamar pelamar) {
                keahlianPelamar = pelamarDAO.getKeahlianByPelamar(pelamar.getIdPengguna());
            }

            hitungJenisLowongan();
            buatFilterLokasi();
            terapkanFilter();

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

        // Hitung jumlah per lokasi
        Map<String, Long> jumlahPerLokasi = semuaLowongan.stream()
                .filter(l -> l.getLokasi() != null && !l.getLokasi().isBlank())
                .collect(Collectors.groupingBy(
                        l -> l.getLokasi().trim(),
                        TreeMap::new,
                        Collectors.counting()
                ));

        for (Map.Entry<String, Long> entry : jumlahPerLokasi.entrySet()) {
            String lokasi = entry.getKey();
            long jumlah = entry.getValue();

            CheckBox checkBox = new CheckBox(lokasi);
            checkBox.setStyle("-fx-text-fill: #344054; -fx-font-size: 13px;");
            checkBox.setOnAction(event -> terapkanFilter());

            Label jumlahLabel = new Label(String.valueOf(jumlah));
            jumlahLabel.setStyle("-fx-text-fill: #98A2B3; -fx-font-size: 12px;");

            HBox baris = new HBox();
            baris.setAlignment(Pos.CENTER_LEFT);
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            baris.getChildren().addAll(checkBox, spacer, jumlahLabel);

            lokasiFilterBox.getChildren().add(baris);
        }
    }

    private void tampilkanLowongan(List<Lowongan> daftar) {
        daftarLowonganBox.getChildren().clear();

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatRupiah.setMaximumFractionDigits(0);

        for (Lowongan lowongan : daftar) {
            String namaPerusahaan = lowongan.getPerusahaan() != null
                    ? lowongan.getPerusahaan().getNamaPerusahaan()
                    : "Perusahaan";

            Label avatar = buatAvatar(namaPerusahaan);
            avatar.setMinWidth(44);
            avatar.setPrefWidth(44);
            avatar.setMaxWidth(44);

            Label nama = new Label(lowongan.getNamaPekerjaan() != null
                    ? lowongan.getNamaPekerjaan() : "-");
            nama.getStyleClass().add("nama-lowongan");
            nama.setMaxWidth(Double.MAX_VALUE);
            nama.setWrapText(false);         
            nama.setEllipsisString("...");   

            String lokasi = (lowongan.getLokasi() != null && !lowongan.getLokasi().isBlank())
                    ? lowongan.getLokasi() : "-";

            Label perusahaanLokasi = new Label(namaPerusahaan + " · " + lokasi);
            perusahaanLokasi.getStyleClass().add("perusahaan-lowongan");
            perusahaanLokasi.setMaxWidth(Double.MAX_VALUE);
            perusahaanLokasi.setEllipsisString("...");

            VBox identitas = new VBox(3, nama, perusahaanLokasi);
            identitas.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(identitas, Priority.ALWAYS);   

            String jenisTeks = kapitalisasi(lowongan.getJenisLowongan());
            Label jenis = new Label(jenisTeks);
            jenis.getStyleClass().add("jenis-lowongan");

            String durasiTeks = (lowongan.getDurasiKerja() != null
                    && !lowongan.getDurasiKerja().isBlank())
                    ? lowongan.getDurasiKerja()
                    : "Durasi belum tersedia";

            Label durasi = new Label(durasiTeks);
            durasi.getStyleClass().add("detail-lowongan");

            VBox kolomJenis = new VBox(4, jenis, durasi);
            kolomJenis.setAlignment(Pos.CENTER_LEFT);
            kolomJenis.setMinWidth(110);
            kolomJenis.setPrefWidth(110);
            kolomJenis.setMaxWidth(110);

            int[] skor = hitungSkorKecocokan(lowongan);

            Label kecocokan;
            if (skor[1] == 0) {
                kecocokan = new Label("Belum dihitung");
                kecocokan.getStyleClass().add("nilai-kecocokan-kosong");
            } else {
                kecocokan = new Label("Cocok " + skor[0] + "/" + skor[1]);
                kecocokan.getStyleClass().add("nilai-kecocokan");
            }

            Label ketCocok = new Label("Kecocokan");
            ketCocok.getStyleClass().add("detail-lowongan");

            VBox kolomCocok = new VBox(4, kecocokan, ketCocok);
            kolomCocok.setAlignment(Pos.CENTER_LEFT);
            kolomCocok.setMinWidth(95);
            kolomCocok.setPrefWidth(95);
            kolomCocok.setMaxWidth(95);

           Label gaji = new Label(formatRupiah.format(lowongan.getGaji()));
            gaji.getStyleClass().add("gaji-lowongan");
            gaji.setAlignment(Pos.CENTER_RIGHT);
            gaji.setMaxWidth(Double.MAX_VALUE);

            Label update = new Label("Baru saja"); 
            update.getStyleClass().add("detail-lowongan");
            update.setAlignment(Pos.CENTER_RIGHT);
            update.setMaxWidth(Double.MAX_VALUE);

            VBox kolomGaji = new VBox(4, gaji, update);
            kolomGaji.setAlignment(Pos.CENTER_RIGHT);
            kolomGaji.setMinWidth(120);
            kolomGaji.setPrefWidth(120);
            kolomGaji.setMaxWidth(120);

            HBox kartu = new HBox(16);
            kartu.setAlignment(Pos.CENTER_LEFT);
            kartu.getStyleClass().add("kartu-lowongan-horizontal");
            kartu.setCursor(Cursor.HAND);
            kartu.setPadding(new Insets(14, 16, 14, 16));

            kartu.getChildren().addAll(
                    avatar,
                    identitas,      
                    kolomJenis,
                    kolomCocok,
                    kolomGaji
            );

            kartu.setOnMouseClicked(e -> {
                if (dashboardController != null) {
                    dashboardController.bukaDetailLowongan(lowongan);
                }
            });

            daftarLowonganBox.getChildren().add(kartu);
        }

        boolean kosong = daftar.isEmpty();
        kosongLabel.setVisible(kosong);
        kosongLabel.setManaged(kosong);
    }

    private Label buatAvatar(String namaPerusahaan) {
        String inisial = buatInisial(namaPerusahaan);

        Label avatar = new Label(inisial);
        avatar.getStyleClass().add("avatar-lowongan");
        avatar.getStyleClass().add(tentukanWarnaAvatar(namaPerusahaan));
        return avatar;
    }

    private String buatInisial(String nama) {
        if (nama == null || nama.isBlank()) return "?";

        String[] kata = nama.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (String k : kata) {
            if (k.equalsIgnoreCase("PT") || k.equalsIgnoreCase("CV") || k.isBlank()) continue;
            sb.append(Character.toUpperCase(k.charAt(0)));
            if (sb.length() >= 2) break;
        }

        if (sb.length() == 0) {
            sb.append(Character.toUpperCase(nama.charAt(0)));
        }
        return sb.toString();
    }

    private String tentukanWarnaAvatar(String nama) {
        if (nama == null) return "avatar-biru";
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

    private String kapitalisasi(String teks) {
        if (teks == null || teks.isBlank()) return "-";
        return teks.substring(0, 1).toUpperCase() + teks.substring(1).toLowerCase();
    }

    private void tampilkanHalaman() {
        int totalLowongan = hasilFilterSaatIni.size();
        int totalHalaman = (int) Math.ceil((double) totalLowongan / jumlahPerHalaman);

        int indeksAwal = (halamanSekarang - 1) * jumlahPerHalaman;
        int indeksAkhir = Math.min(indeksAwal + jumlahPerHalaman, totalLowongan);

        List<Lowongan> lowonganHalaman = hasilFilterSaatIni.subList(indeksAwal, indeksAkhir);

        tampilkanLowongan(lowonganHalaman);

        jumlahLowonganLabel.setText(totalLowongan + " lowongan");

        ringkasanFilterLabel.setText(buatRingkasanFilter());

        if (totalLowongan == 0) {
            infoHalamanLabel.setText("Menampilkan 0 dari 0");
        } else {
            infoHalamanLabel.setText(
                "Menampilkan " + (indeksAwal + 1) + "-" + indeksAkhir + " dari " + totalLowongan
            );
        }

        sebelumnyaButton.setDisable(halamanSekarang <= 1);
        berikutnyaButton.setDisable(halamanSekarang >= totalHalaman || totalHalaman == 0);
    }

    private String buatRingkasanFilter() {
        List<String> bagian = new ArrayList<>();

        if (fullTimeCheckBox.isSelected() && !partTimeCheckBox.isSelected()) {
            bagian.add("Full time");
        } else if (partTimeCheckBox.isSelected() && !fullTimeCheckBox.isSelected()) {
            bagian.add("Part time");
        }

        List<String> lokasiDipilih = new ArrayList<>();
        for (javafx.scene.Node node : lokasiFilterBox.getChildren()) {
            if (node instanceof HBox hbox) {
                for (javafx.scene.Node child : hbox.getChildren()) {
                    if (child instanceof CheckBox cb && cb.isSelected()) {
                        lokasiDipilih.add(cb.getText());
                    }
                }
            }
        }
        if (!lokasiDipilih.isEmpty()) {
            bagian.add(String.join(", ", lokasiDipilih));
        }

        String gajiTeks = gajiMinimumField.getText().replaceAll("[^0-9]", "");
        if (!gajiTeks.isBlank() && !gajiTeks.equals("0")) {
            bagian.add("min. Rp" + NumberFormat.getInstance(new Locale("id", "ID")).format(Long.parseLong(gajiTeks)));
        }

        if (bagian.isEmpty()) return "";
        return " · " + String.join(" · ", bagian);
    }

    private int[] hitungSkorKecocokan(Lowongan lowongan) {
        List<Keahlian> dibutuhkan = lowongan.getDaftarKeahlian();
        if (dibutuhkan == null || dibutuhkan.isEmpty()) {
            return new int[]{0, 0};
        }

        if (keahlianPelamar == null || keahlianPelamar.isEmpty()) {
            return new int[]{0, dibutuhkan.size()};
        }

        java.util.Set<Integer> idKeahlianPelamar = new java.util.HashSet<>();
        for (Keahlian k : keahlianPelamar) {
            idKeahlianPelamar.add(k.getIdKeahlian());
        }

        int jumlahCocok = 0;
        for (Keahlian k : dibutuhkan) {
            if (idKeahlianPelamar.contains(k.getIdKeahlian())) {
                jumlahCocok++;
            }
        }

        return new int[]{jumlahCocok, dibutuhkan.size()};
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
