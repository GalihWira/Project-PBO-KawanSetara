package com.example.projectpbokawansetara.controller;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.example.projectpbokawansetara.dao.LowonganDAO;
import com.example.projectpbokawansetara.model.Lowongan;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class KelolaLowonganController {

    @FXML
    private TableColumn<Lowongan, Void> aksiColumn;
    @FXML
    private Button berikutnyaButton;
    @FXML
    private TextField cariField;
    @FXML
    private TextField cariPekerjaanField;
    @FXML
    private TableColumn<Lowongan, String> gajiColumn;
    @FXML
    private Label infoHalamanLabel;
    @FXML
    private Label infoTampilLabel;
    @FXML
    private TableColumn<Lowongan, String> jenisColumn;
    @FXML
    private ComboBox<String> jenisComboBox;
    @FXML
    private TableColumn<Lowongan, String> lokasiColumn;
    @FXML
    private ComboBox<String> lokasiComboBox;
    @FXML
    private TableView<Lowongan> lowonganTable;
    @FXML
    private TableColumn<Lowongan, String> pelamarColumn;
    @FXML
    private TableColumn<Lowongan, String> perusahaanColumn;
    @FXML
    private ComboBox<String> perusahaanComboBox;
    @FXML
    private TableColumn<Lowongan, String> posisiColumn;
    @FXML
    private Label ringkasanLabel;
    @FXML
    private Button sebelumnyaButton;
    @FXML
    private ToggleButton semuaTab;
    @FXML
    private TableColumn<Lowongan, String> statusColumn;
    @FXML
    private ToggleGroup statusTabGroup;
    @FXML
    private Button tambahLowonganButton;
    @FXML
    private ToggleButton tersediaTab;
    @FXML
    private ToggleButton tidakTersediaTab;

    private final LowonganDAO lowonganDAO = new LowonganDAO();
    private List<Lowongan> semuaLowongan = new ArrayList<>();
    private List<Lowongan> lowonganTerfilter = new ArrayList<>();

    private int halamanSekarang = 1;
    private final int barisPerHalaman = 8;

    @FXML
    public void initialize() {
        setupKolomTabel();
        cariPekerjaanField.textProperty().addListener((obs, oldVal, newVal) -> {
            halamanSekarang = 1;
            terapkanFilter();
        });
        loadDataLowongan();
    }

    private void setupKolomTabel() {
        NumberFormat rupiahFormat = NumberFormat.getInstance(new Locale("id", "ID"));

        posisiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaPekerjaan()));
        perusahaanColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getPerusahaan() != null ? data.getValue().getPerusahaan().getNamaPerusahaan() : "-"));
        lokasiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLokasi()));
        jenisColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJenisLowongan()));
        gajiColumn.setCellValueFactory(
                data -> new SimpleStringProperty("Rp " + rupiahFormat.format(data.getValue().getGaji())));
        pelamarColumn.setCellValueFactory(
                data -> new SimpleStringProperty(String.valueOf(data.getValue().getJumlahPelamar())));

        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        statusColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(status.substring(0, 1).toUpperCase() + status.substring(1));
                    boolean aktif = status.equalsIgnoreCase("tersedia");
                    badge.setStyle("-fx-font-size: 12px; -fx-padding: 2 8 2 8; -fx-background-radius: 10; " +
                            (aktif ? "-fx-background-color: #D1FADF; -fx-text-fill: #027A48;"
                                    : "-fx-background-color: #F2F4F7; -fx-text-fill: #344054;"));
                    setGraphic(badge);
                }
            }
        });

        aksiColumn.setCellFactory(col -> new TableCell<>() {
            private final Button hapusBtn = new Button("Hapus");
            {
                hapusBtn.setStyle(
                        "-fx-background-color: transparent; -fx-text-fill: #B42318; -fx-font-size: 12px; -fx-cursor: hand;");
                hapusBtn.setOnAction(e -> konfirmasiHapus(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setAlignment(Pos.CENTER);
                setGraphic(empty ? null : hapusBtn);
            }
        });
    }

    private void konfirmasiHapus(Lowongan lowongan) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/projectpbokawansetara/view/HapusLowonganDialog.fxml"));
            Parent dialog = loader.load();

            HapusLowonganController controller = loader.getController();
            controller.setData(lowongan.getNamaPekerjaan(), lowongan.getJumlahPelamar());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(lowonganTable.getScene().getWindow());
            stage.setTitle("Hapus Lowongan");
            stage.setScene(new Scene(dialog));
            stage.showAndWait();

            if (!controller.isDihapus()) {
                return;
            }

            if (lowonganDAO.hapusLowongan(lowongan.getIdLowongan())) {
                loadDataLowongan();
            } else {
                tampilkanPesanGagal("Lowongan gagal dihapus. Coba lagi nanti.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            tampilkanPesanGagal("Dialog hapus gagal dibuka.");
        }
    }

    private void tampilkanPesanGagal(String pesan) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Gagal");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }

    public void loadDataLowongan() {
        semuaLowongan = lowonganDAO.getAllLowonganAdmin();

        long totalSemua = semuaLowongan.size();
        long totalTersedia = semuaLowongan.stream().filter(l -> "tersedia".equalsIgnoreCase(l.getStatus())).count();
        long totalTidakTersedia = totalSemua - totalTersedia;
        long jumlahPerusahaan = semuaLowongan.stream()
                .filter(l -> l.getPerusahaan() != null)
                .map(l -> l.getPerusahaan().getIdPerusahaan())
                .distinct()
                .count();

        ringkasanLabel.setText(totalSemua + " lowongan dari " + jumlahPerusahaan + " perusahaan mitra");
        semuaTab.setText("Semua " + totalSemua);
        tersediaTab.setText("Tersedia " + totalTersedia);
        tidakTersediaTab.setText("Tidak tersedia " + totalTidakTersedia);

        Set<String> setPerusahaan = new TreeSet<>();
        Set<String> setJenis = new TreeSet<>();
        Set<String> setLokasi = new TreeSet<>();
        for (Lowongan l : semuaLowongan) {
            if (l.getPerusahaan() != null && l.getPerusahaan().getNamaPerusahaan() != null) {
                setPerusahaan.add(l.getPerusahaan().getNamaPerusahaan());
            }
            if (l.getJenisLowongan() != null)
                setJenis.add(l.getJenisLowongan());
            if (l.getLokasi() != null)
                setLokasi.add(l.getLokasi());
        }

        List<String> listPerusahaan = new ArrayList<>(List.of("Semua perusahaan"));
        listPerusahaan.addAll(setPerusahaan);
        perusahaanComboBox.setItems(FXCollections.observableArrayList(listPerusahaan));

        List<String> listJenis = new ArrayList<>(List.of("Semua jenis"));
        listJenis.addAll(setJenis);
        jenisComboBox.setItems(FXCollections.observableArrayList(listJenis));

        List<String> listLokasi = new ArrayList<>(List.of("Semua lokasi"));
        listLokasi.addAll(setLokasi);
        lokasiComboBox.setItems(FXCollections.observableArrayList(listLokasi));

        halamanSekarang = 1;
        terapkanFilter();
    }

    private void terapkanFilter() {
        String keyword = cariPekerjaanField.getText() == null ? "" : cariPekerjaanField.getText().toLowerCase().trim();
        String filterPerusahaan = perusahaanComboBox.getValue();
        String filterJenis = jenisComboBox.getValue();
        String filterLokasi = lokasiComboBox.getValue();

        lowonganTerfilter = semuaLowongan.stream().filter(l -> {
            if (tersediaTab.isSelected() && !"tersedia".equalsIgnoreCase(l.getStatus()))
                return false;
            if (tidakTersediaTab.isSelected() && !"tidak tersedia".equalsIgnoreCase(l.getStatus()))
                return false;
            if (!keyword.isEmpty() && !l.getNamaPekerjaan().toLowerCase().contains(keyword))
                return false;

            String namaPerusahaan = l.getPerusahaan() != null ? l.getPerusahaan().getNamaPerusahaan() : "";
            if (filterPerusahaan != null && !filterPerusahaan.equals("Semua perusahaan")
                    && !filterPerusahaan.equals(namaPerusahaan))
                return false;
            if (filterJenis != null && !filterJenis.equals("Semua jenis")
                    && !filterJenis.equalsIgnoreCase(l.getJenisLowongan()))
                return false;
            if (filterLokasi != null && !filterLokasi.equals("Semua lokasi")
                    && !filterLokasi.equalsIgnoreCase(l.getLokasi()))
                return false;
            return true;
        }).collect(Collectors.toList());

        updateTampilanHalaman();
    }

    private void updateTampilanHalaman() {
        int totalData = lowonganTerfilter.size();
        int totalHalaman = Math.max(1, (int) Math.ceil((double) totalData / barisPerHalaman));
        if (halamanSekarang > totalHalaman)
            halamanSekarang = totalHalaman;

        int dariIndex = (halamanSekarang - 1) * barisPerHalaman;
        int sampaiIndex = Math.min(dariIndex + barisPerHalaman, totalData);

        if (totalData == 0) {
            lowonganTable.setItems(FXCollections.observableArrayList());
            infoTampilLabel.setText("Menampilkan 0 dari 0");
        } else {
            lowonganTable
                    .setItems(FXCollections.observableArrayList(lowonganTerfilter.subList(dariIndex, sampaiIndex)));
            infoTampilLabel.setText("Menampilkan " + (dariIndex + 1) + "–" + sampaiIndex + " dari " + totalData);
        }

        infoHalamanLabel.setText("Halaman " + halamanSekarang + " dari " + totalHalaman);
        sebelumnyaButton.setDisable(halamanSekarang <= 1);
        berikutnyaButton.setDisable(halamanSekarang >= totalHalaman);
    }

    @FXML
    void handleTambah(ActionEvent event) {
        pindahHalaman(event, "/com/example/projectpbokawansetara/view/FormLowonganView.fxml");
    }

    @FXML
    void handleFilter(ActionEvent event) {
        String styleAktif = "-fx-background-color: transparent; -fx-border-color: transparent transparent #1F4E8C transparent; -fx-border-width: 0 0 2 0; -fx-text-fill: #1F4E8C; -fx-font-size: 13px; -fx-font-weight: 600; -fx-padding: 6 12 10 12;";
        String styleNonAktif = "-fx-background-color: transparent; -fx-text-fill: #475467; -fx-font-size: 13px; -fx-padding: 6 12 10 12;";

        semuaTab.setStyle(semuaTab.isSelected() ? styleAktif : styleNonAktif);
        tersediaTab.setStyle(tersediaTab.isSelected() ? styleAktif : styleNonAktif);
        tidakTersediaTab.setStyle(tidakTersediaTab.isSelected() ? styleAktif : styleNonAktif);

        halamanSekarang = 1;
        terapkanFilter();
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

    private void pindahHalaman(ActionEvent event, String fxmlPath) {
        try {
            URL url = getClass().getResource(fxmlPath);
            if (url == null) {
                System.err.println("File FXML tidak ditemukan: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent halamanBaru = loader.load();

            Node kontenTengah = halamanBaru;
            if (halamanBaru instanceof BorderPane halamanBorder && halamanBorder.getCenter() != null) {
                kontenTengah = halamanBorder.getCenter();
            }

            Scene scene = ((Node) event.getSource()).getScene();
            BorderPane mainBorderPane = (BorderPane) scene.lookup("#mainBorderPane");

            if (mainBorderPane != null) {
                mainBorderPane.setCenter(kontenTengah);
            } else {
                scene.setRoot(halamanBaru);
            }
        } catch (Exception e) {
            System.err.println("Gagal pindah ke: " + fxmlPath);
            e.printStackTrace();
        }
    }
}