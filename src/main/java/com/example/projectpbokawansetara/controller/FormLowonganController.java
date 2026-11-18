package com.example.projectpbokawansetara.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.projectpbokawansetara.dao.LowonganDAO;
import com.example.projectpbokawansetara.helper.ValidasiHelper;
import com.example.projectpbokawansetara.model.Admin;
import com.example.projectpbokawansetara.model.Lowongan;
import com.example.projectpbokawansetara.model.Perusahaan;
import com.example.projectpbokawansetara.model.Session;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class FormLowonganController {

    @FXML
    private Button batalButton;
    @FXML
    private Label breadcrumbLabel;
    @FXML
    private TextField cariField;
    @FXML
    private TextArea deskripsiArea;
    @FXML
    private HBox errorBanner;
    @FXML
    private Label errorBannerLabel;
    @FXML
    private RadioButton fullTimeRadio;
    @FXML
    private HBox gajiBox;
    @FXML
    private Label gajiErrorLabel;
    @FXML
    private TextField gajiField;
    @FXML
    private ToggleGroup jenisGroup;
    @FXML
    private Label judulHalamanLabel;
    @FXML
    private Label jumlahDipilihLabel;
    @FXML
    private GridPane keahlianGrid;
    @FXML
    private Label lokasiErrorLabel;
    @FXML
    private TextField lokasiField;
    @FXML
    private HBox namaPekerjaanBox;
    @FXML
    private Label namaPekerjaanErrorLabel;
    @FXML
    private TextField namaPekerjaanField;
    @FXML
    private RadioButton partTimeRadio;
    @FXML
    private TextArea persyaratanArea;
    @FXML
    private Label persyaratanErrorLabel;
    @FXML
    private ComboBox<Perusahaan> perusahaanComboBox;
    @FXML
    private Label perusahaanErrorLabel;
    @FXML
    private Button simpanButton;
    @FXML
    private ToggleGroup statusGroup;
    @FXML
    private RadioButton tersediaRadio;
    @FXML
    private RadioButton tidakTersediaRadio;

    private final LowonganDAO lowonganDAO = new LowonganDAO();
    private final Map<CheckBox, Integer> keahlianCheckMap = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        perusahaanComboBox.setItems(FXCollections.observableArrayList(lowonganDAO.getAllPerusahaan()));
        perusahaanComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Perusahaan item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNamaPerusahaan());
            }
        });
        perusahaanComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Perusahaan item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNamaPerusahaan());
            }
        });

        Map<Integer, String> daftarKeahlian = lowonganDAO.getAllKeahlian();
        keahlianGrid.getChildren().clear();
        keahlianCheckMap.clear();

        int col = 0;
        int row = 0;
        for (Map.Entry<Integer, String> entry : daftarKeahlian.entrySet()) {
            CheckBox cb = new CheckBox(entry.getValue());
            cb.setStyle("-fx-font-size: 13px; -fx-text-fill: #344054;");
            cb.selectedProperty().addListener((obs, oldVal, newVal) -> updateJumlahDipilih());

            keahlianCheckMap.put(cb, entry.getKey());
            keahlianGrid.add(cb, col, row);

            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
        updateJumlahDipilih();
    }

    private void updateJumlahDipilih() {
        long count = keahlianCheckMap.keySet().stream().filter(CheckBox::isSelected).count();
        jumlahDipilihLabel.setText(count + " dipilih");
    }

    @FXML
    void handleBatal(ActionEvent event) {
        pindahHalaman(event, "/com/example/projectpbokawansetara/view/KelolaLowonganView.fxml");
    }

    @FXML
    void handleSimpan(ActionEvent event) {
        int jumlahError = 0;

        String namaPekerjaan = namaPekerjaanField.getText().trim();
        if (namaPekerjaan.isEmpty()) {
            tampilkanError(namaPekerjaanErrorLabel, true);
            jumlahError++;
        } else {
            tampilkanError(namaPekerjaanErrorLabel, false);
        }

        Perusahaan perusahaanTerpilih = perusahaanComboBox.getValue();
        if (perusahaanTerpilih == null) {
            tampilkanError(perusahaanErrorLabel, true);
            jumlahError++;
        } else {
            tampilkanError(perusahaanErrorLabel, false);
        }

        String lokasi = lokasiField.getText().trim();
        if (lokasi.isEmpty()) {
            tampilkanError(lokasiErrorLabel, true);
            jumlahError++;
        } else {
            tampilkanError(lokasiErrorLabel, false);
        }

        double gaji = 0;
        String gajiStr = gajiField.getText().trim();
        if (ValidasiHelper.gajiValid(gajiStr)) {
            gaji = Double.parseDouble(gajiStr);
            tampilkanError(gajiErrorLabel, false);
        } else {
            tampilkanError(gajiErrorLabel, true);
            jumlahError++;
        }

        String persyaratan = persyaratanArea.getText().trim();
        if (persyaratan.isEmpty()) {
            tampilkanError(persyaratanErrorLabel, true);
            jumlahError++;
        } else {
            tampilkanError(persyaratanErrorLabel, false);
        }

        if (jumlahError > 0) {
            errorBannerLabel.setText(jumlahError + " isian perlu diperbaiki sebelum lowongan disimpan");
            errorBanner.setVisible(true);
            errorBanner.setManaged(true);
            return;
        }

        errorBanner.setVisible(false);
        errorBanner.setManaged(false);

        Lowongan lowongan = new Lowongan(
                0,
                perusahaanTerpilih,
                namaPekerjaan,
                deskripsiArea.getText().trim(),
                lokasi,
                gaji,
                fullTimeRadio.isSelected() ? "full time" : "part time",
                tersediaRadio.isSelected() ? "tersedia" : "tidak tersedia");
        if (!(Session.getCurrentUser() instanceof Admin admin)) {
            errorBannerLabel.setText("Sesi admin berakhir, silakan masuk lagi.");
            errorBanner.setVisible(true);
            errorBanner.setManaged(true);
            return;
        }
        lowongan.setIdAdmin(admin.getIdPengguna());
        lowongan.setPersyaratan(persyaratan);

        List<Integer> selectedKeahlianIds = new ArrayList<>();
        for (Map.Entry<CheckBox, Integer> entry : keahlianCheckMap.entrySet()) {
            if (entry.getKey().isSelected()) {
                selectedKeahlianIds.add(entry.getValue());
            }
        }

        boolean berhasil = lowonganDAO.tambahLowongan(lowongan, selectedKeahlianIds);
        if (berhasil) {
            Scene scene = ((Node) event.getSource()).getScene();
            Label badgeLowongan = (Label) scene.lookup("#jumlahLowonganMenuLabel");
            if (badgeLowongan != null) {
                int totalSekarang = lowonganDAO.getAllLowonganAdmin().size();
                badgeLowongan.setText(String.valueOf(totalSekarang));
            }

            pindahHalaman(event, "/com/example/projectpbokawansetara/view/KelolaLowonganView.fxml");
        } else {
            errorBannerLabel.setText("Terjadi kesalahan saat menyimpan ke database.");
            errorBanner.setVisible(true);
            errorBanner.setManaged(true);
        }
    }

    private void tampilkanError(Label label, boolean show) {
        label.setVisible(show);
        label.setManaged(show);
    }

    private void pindahHalaman(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent halamanBaru = loader.load();

            Node kontenTengah = halamanBaru;
            if (halamanBaru instanceof BorderPane halamanBorder && halamanBorder.getCenter() != null) {
                kontenTengah = halamanBorder.getCenter();
            }

            BorderPane mainBorderPane = (BorderPane) ((Node) event.getSource()).getScene().lookup("#mainBorderPane");
            if (mainBorderPane != null) {
                mainBorderPane.setCenter(kontenTengah);
            } else {
                ((Node) event.getSource()).getScene().setRoot(halamanBaru);
            }
        } catch (IOException e) {
            System.err.println("Gagal pindah ke: " + fxmlPath);
            e.printStackTrace();
        }
    }
}