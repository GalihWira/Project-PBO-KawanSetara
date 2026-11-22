package com.example.projectpbokawansetara.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.projectpbokawansetara.dao.DisabilitasDAO;
import com.example.projectpbokawansetara.helper.DialogHelper;
import com.example.projectpbokawansetara.model.JenisDisabilitas;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class KelolaDisabilitasController {

    @FXML
    private TableView<JenisDisabilitas> disabilitasTable;
    @FXML
    private TableColumn<JenisDisabilitas, String> idColumn;
    @FXML
    private TableColumn<JenisDisabilitas, String> namaColumn;
    @FXML
    private TableColumn<JenisDisabilitas, String> pelamarColumn;
    @FXML
    private TableColumn<JenisDisabilitas, Void> aksiColumn;

    @FXML
    private TextField namaJenisField;
    @FXML
    private Label namaJenisErrorLabel;

    private DisabilitasDAO disabilitasDAO = new DisabilitasDAO();
    private List<JenisDisabilitas> semuaDisabilitas = new ArrayList<>();

    @FXML
    public void initialize() {
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("namaJenis"));
        idColumn.setCellValueFactory(data -> new SimpleStringProperty("#" + data.getValue().getIdJenisDisabilitas()));
        pelamarColumn
                .setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJumlahPelamar() + " pelamar"));

        buatTombolHapus();
        loadDataDisabilitas();

        Platform.runLater(() -> {
            if (disabilitasTable.getScene() != null) {
                TextField searchAtas = (TextField) disabilitasTable.getScene().lookup("#cariField");
                if (searchAtas != null) {
                    searchAtas.clear();
                    searchAtas.textProperty().addListener((obs, lama, baru) -> {
                        if (disabilitasTable.getScene() != null) {
                            String keyword = baru.toLowerCase().trim();
                            List<JenisDisabilitas> hasil = new ArrayList<>();
                            for (JenisDisabilitas d : semuaDisabilitas) {
                                if (d.getNamaJenis().toLowerCase().contains(keyword)) {
                                    hasil.add(d);
                                }
                            }
                            disabilitasTable.setItems(FXCollections.observableArrayList(hasil));
                        }
                    });
                }
            }
        });
    }

    private void loadDataDisabilitas() {
        semuaDisabilitas = disabilitasDAO.getAllDisabilitas();
        disabilitasTable.setItems(FXCollections.observableArrayList(semuaDisabilitas));
    }

    @FXML
    void handleTambah(ActionEvent event) {
        sembunyikanError();
        String nama = namaJenisField.getText().trim();

        if (nama.isEmpty()) {
            tampilkanError("Nama jenis disabilitas wajib diisi");
            return;
        }

        for (JenisDisabilitas d : semuaDisabilitas) {
            if (d.getNamaJenis().equalsIgnoreCase(nama)) {
                tampilkanError("Jenis disabilitas \"" + nama + "\" sudah terdaftar");
                return;
            }
        }

        if (disabilitasDAO.tambahDisabilitas(nama)) {
            namaJenisField.clear();
            loadDataDisabilitas();
        } else {
            tampilkanError("Gagal menyimpan data ke database");
        }
    }

    private void buatTombolHapus() {
        aksiColumn.setCellFactory(param -> new TableCell<JenisDisabilitas, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button btnHapus = new Button("Hapus");
                    btnHapus.getStyleClass().add("tombol-hapus-teks");
                    btnHapus.setOnAction(event -> {
                        JenisDisabilitas data = getTableView().getItems().get(getIndex());
                        prosesHapus(data);
                    });
                    setGraphic(btnHapus);
                }
            }
        });
    }

    private void prosesHapus(JenisDisabilitas data) {
        sembunyikanError();

        int jumlahPemakaian = disabilitasDAO.hitungPemakaian(data.getIdJenisDisabilitas());

        if (jumlahPemakaian > 0) {
            Alert dipakai = new Alert(Alert.AlertType.WARNING,
                    "Tidak bisa dihapus karena masih digunakan oleh data pelamar.");
            dipakai.showAndWait();
            return;
        }

        if (jumlahPemakaian < 0) {
            Alert gagalCek = new Alert(Alert.AlertType.WARNING, "Gagal memeriksa data. Silakan coba lagi.");
            gagalCek.showAndWait();
            return;
        }

        boolean yakin = DialogHelper.tampilkanKonfirmasiHapus(
                disabilitasTable.getScene().getWindow(),
                "Yakin ingin menghapus \"" + data.getNamaJenis() + "\"?",
                "Tindakan ini tidak bisa dibatalkan.");
        if (!yakin) {
            return;
        }

        if (disabilitasDAO.hapusDisabilitas(data.getIdJenisDisabilitas())) {
            loadDataDisabilitas();
        } else {
            Alert gagal = new Alert(Alert.AlertType.WARNING, "Gagal menghapus. Silakan coba lagi.");
            gagal.showAndWait();
        }
    }

    private void tampilkanError(String pesan) {
        namaJenisErrorLabel.setText(pesan);
        namaJenisErrorLabel.setVisible(true);
        namaJenisErrorLabel.setManaged(true);
    }

    private void sembunyikanError() {
        namaJenisErrorLabel.setVisible(false);
        namaJenisErrorLabel.setManaged(false);
    }
}