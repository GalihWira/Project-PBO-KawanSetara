package com.example.projectpbokawansetara.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.projectpbokawansetara.dao.PerusahaanDAO;
import com.example.projectpbokawansetara.exception.ValidationException;
import com.example.projectpbokawansetara.helper.DialogHelper;
import com.example.projectpbokawansetara.model.Perusahaan;
import com.example.projectpbokawansetara.service.PerusahaanService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class KelolaPerusahaanController {

    @FXML
    private TableView<Perusahaan> perusahaanTable;
    @FXML
    private TableColumn<Perusahaan, String> namaColumn;
    @FXML
    private TableColumn<Perusahaan, String> alamatColumn;
    @FXML
    private TableColumn<Perusahaan, String> emailColumn;
    @FXML
    private TableColumn<Perusahaan, String> noTeleponColumn;

    @FXML
    private TextField cariPerusahaanField;
    @FXML
    private Label ringkasanLabel;
    @FXML
    private Label infoTampilLabel;
    @FXML
    private Button sebelumnyaButton;
    @FXML
    private Button berikutnyaButton;

    @FXML
    private Label judulFormLabel;
    @FXML
    private Label jumlahLowonganAktifLabel;
    @FXML
    private HBox errorBanner;
    @FXML
    private Label errorBannerJudulLabel;
    @FXML
    private Label errorBannerPesanLabel;

    @FXML
    private TextField namaPerusahaanField;
    @FXML
    private TextArea alamatArea;
    @FXML
    private TextField emailField;
    @FXML
    private TextField noTeleponField;
    @FXML
    private TextArea deskripsiArea;
    @FXML
    private Button hapusButton;

    private final PerusahaanDAO perusahaanDAO = new PerusahaanDAO();
    private final PerusahaanService perusahaanService = new PerusahaanService();
    private List<Perusahaan> semuaPerusahaan = new ArrayList<>();
    private List<Perusahaan> perusahaanTerfilter = new ArrayList<>();
    private Perusahaan perusahaanDipilih = null;

    private int halamanSekarang = 1;
    private final int barisPerHalaman = 8;

    @FXML
    public void initialize() {
        namaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaPerusahaan()));
        alamatColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAlamat()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        noTeleponColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNoTelepon()));

        perusahaanTable.getSelectionModel().selectedItemProperty().addListener((obs, lama, baru) -> {
            if (baru != null) {
                tampilkanDetailKeForm(baru);
            }
        });

        cariPerusahaanField.textProperty().addListener((obs, lama, baru) -> {
            halamanSekarang = 1;
            terapkanFilter();
        });

        loadDataPerusahaan();
    }

    private void loadDataPerusahaan() {
        semuaPerusahaan = perusahaanDAO.getAllPerusahaan();
        ringkasanLabel.setText(semuaPerusahaan.size() + " perusahaan mitra");
        halamanSekarang = 1;
        terapkanFilter();

        if (!perusahaanTerfilter.isEmpty()) {
            perusahaanTable.getSelectionModel().selectFirst();
        } else {
            siapkanModeTambah();
        }
    }

    private void terapkanFilter() {
        String keyword = cariPerusahaanField.getText() == null ? ""
                : cariPerusahaanField.getText().toLowerCase().trim();
        perusahaanTerfilter.clear();

        for (Perusahaan p : semuaPerusahaan) {
            boolean cocokNama = p.getNamaPerusahaan() != null && p.getNamaPerusahaan().toLowerCase().contains(keyword);
            boolean cocokAlamat = p.getAlamat() != null && p.getAlamat().toLowerCase().contains(keyword);
            if (keyword.isEmpty() || cocokNama || cocokAlamat) {
                perusahaanTerfilter.add(p);
            }
        }
        updateTampilanHalaman();
    }

    private void updateTampilanHalaman() {
        int totalData = perusahaanTerfilter.size();
        int totalHalaman = Math.max(1, (int) Math.ceil((double) totalData / barisPerHalaman));
        if (halamanSekarang > totalHalaman)
            halamanSekarang = totalHalaman;

        int dariIndex = (halamanSekarang - 1) * barisPerHalaman;
        int sampaiIndex = Math.min(dariIndex + barisPerHalaman, totalData);

        perusahaanTable
                .setItems(FXCollections.observableArrayList(perusahaanTerfilter.subList(dariIndex, sampaiIndex)));
        infoTampilLabel.setText(totalData == 0 ? "Menampilkan 0 dari 0"
                : "Menampilkan " + (dariIndex + 1) + "–" + sampaiIndex + " dari " + totalData);
        sebelumnyaButton.setDisable(halamanSekarang <= 1);
        berikutnyaButton.setDisable(halamanSekarang >= totalHalaman);
    }

    private void tampilkanDetailKeForm(Perusahaan p) {
        perusahaanDipilih = p;
        setError(false, "", "");

        judulFormLabel.setText("Detail perusahaan");
        jumlahLowonganAktifLabel.setText(p.getJumlahLowonganAktif() + " lowongan aktif");
        jumlahLowonganAktifLabel.setVisible(true);

        namaPerusahaanField.setText(p.getNamaPerusahaan());
        alamatArea.setText(p.getAlamat());
        emailField.setText(p.getEmail());
        noTeleponField.setText(p.getNoTelepon());
        deskripsiArea.setText(p.getDeskripsi());

        hapusButton.setVisible(true);
    }

    private void siapkanModeTambah() {
        perusahaanDipilih = null;
        perusahaanTable.getSelectionModel().clearSelection();
        setError(false, "", "");

        judulFormLabel.setText("Tambah perusahaan");
        jumlahLowonganAktifLabel.setVisible(false);

        namaPerusahaanField.clear();
        alamatArea.clear();
        emailField.clear();
        noTeleponField.clear();
        deskripsiArea.clear();

        hapusButton.setVisible(false);
    }

    @FXML
    void handleTambah(ActionEvent event) {
        siapkanModeTambah();
        namaPerusahaanField.requestFocus();
    }

    @FXML
    void handleSimpan(ActionEvent event) {
        String nama = namaPerusahaanField.getText().trim();
        String alamat = alamatArea.getText().trim();
        String email = emailField.getText().trim();
        String telepon = noTeleponField.getText().trim();
        String deskripsi = deskripsiArea.getText().trim();

        try {
            perusahaanService.validasiPerusahaan(nama, email, telepon);
        } catch (ValidationException e) {
            setError(true, "Data belum valid", e.getMessage());
            return;
        }

        boolean berhasil;
        if (perusahaanDipilih == null) {
            Perusahaan baru = new Perusahaan(0, nama, alamat, email, telepon, deskripsi);
            berhasil = perusahaanDAO.tambahPerusahaan(baru);
        } else {
            perusahaanDipilih.setNamaPerusahaan(nama);
            perusahaanDipilih.setAlamat(alamat);
            perusahaanDipilih.setEmail(email);
            perusahaanDipilih.setNoTelepon(telepon);
            perusahaanDipilih.setDeskripsi(deskripsi);
            berhasil = perusahaanDAO.updatePerusahaan(perusahaanDipilih);
        }

        if (berhasil) {
            loadDataPerusahaan();
        } else {
            setError(true, "Gagal menyimpan", "Gagal menyimpan data ke database.");
        }
    }

    @FXML
    void handleHapus(ActionEvent event) {
        if (perusahaanDipilih == null)
            return;

        boolean yakin = DialogHelper.tampilkanKonfirmasiHapus(
                perusahaanTable.getScene().getWindow(),
                "Hapus " + perusahaanDipilih.getNamaPerusahaan() + "?",
                "Tindakan ini tidak bisa dibatalkan.");
        if (!yakin) {
            return;
        }

        if (perusahaanDAO.hapusPerusahaan(perusahaanDipilih.getIdPerusahaan())) {
            loadDataPerusahaan();
        } else {
            setError(true, "Tidak bisa dihapus", "Perusahaan ini masih memiliki lowongan aktif.");
        }
    }

    @FXML
    void handleBatal(ActionEvent event) {
        if (perusahaanDipilih != null) {
            tampilkanDetailKeForm(perusahaanDipilih);
        } else {
            siapkanModeTambah();
        }
    }

    @FXML
    void handleSebelumnya(ActionEvent event) {
        if (halamanSekarang > 1) {
            halamanSekarang--;
            updateTampilanHalaman();
        }
    }

    @FXML
    void handleBerikutnya(ActionEvent event) {
        halamanSekarang++;
        updateTampilanHalaman();
    }

    private void setError(boolean tampil, String judul, String pesan) {
        errorBannerJudulLabel.setText(judul);
        errorBannerPesanLabel.setText(pesan);
        errorBanner.setVisible(tampil);
        errorBanner.setManaged(tampil);
    }
}