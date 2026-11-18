package com.example.projectpbokawansetara.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    @FXML private TableColumn<Lowongan, Void> aksiColumn;
    @FXML private Button berikutnyaButton;
    @FXML private TextField cariField;
    @FXML private TextField cariPekerjaanField;
    @FXML private TableColumn<Lowongan, String> gajiColumn;
    @FXML private Label infoHalamanLabel;
    @FXML private Label infoTampilLabel;
    @FXML private TableColumn<Lowongan, String> jenisColumn;
    @FXML private ComboBox<String> jenisComboBox;
    @FXML private TableColumn<Lowongan, String> lokasiColumn;
    @FXML private ComboBox<String> lokasiComboBox;
    @FXML private TableView<Lowongan> lowonganTable;
    @FXML private TableColumn<Lowongan, String> pelamarColumn;
    @FXML private TableColumn<Lowongan, String> perusahaanColumn;
    @FXML private ComboBox<String> perusahaanComboBox;
    @FXML private TableColumn<Lowongan, String> posisiColumn;
    @FXML private Label ringkasanLabel;
    @FXML private Button sebelumnyaButton;
    @FXML private ToggleButton semuaTab;
    @FXML private TableColumn<Lowongan, String> statusColumn;
    @FXML private ToggleGroup statusTabGroup;
    @FXML private Button tambahLowonganButton;
    @FXML private ToggleButton tersediaTab;
    @FXML private ToggleButton tidakTersediaTab;

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
        posisiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaPekerjaan()));
        perusahaanColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getPerusahaan() != null ? data.getValue().getPerusahaan().getNamaPerusahaan() : "-"));
        lokasiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLokasi()));
        jenisColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJenisLowongan()));
        gajiColumn.setCellValueFactory(data -> new SimpleStringProperty("Rp " + (long) data.getValue().getGaji()));
        pelamarColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getJumlahPelamar())));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        aksiColumn.setCellFactory(col -> new TableCell<>() {
            private final Button hapusBtn = new Button("Hapus");
            {
                hapusBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #B42318; -fx-cursor: hand;");
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

            if (controller.isDihapus()) {
                if (lowonganDAO.hapusLowongan(lowongan.getIdLowongan())) {
                    loadDataLowongan();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Lowongan tidak bisa dihapus karena sudah punya lamaran.");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDataLowongan() {
        semuaLowongan = lowonganDAO.getAllLowonganAdmin();

        int totalTersedia = 0;
        List<String> listPerusahaan = new ArrayList<>(List.of("Semua perusahaan"));
        List<String> listLokasi = new ArrayList<>(List.of("Semua lokasi"));

        for (Lowongan l : semuaLowongan) {
            if ("tersedia".equalsIgnoreCase(l.getStatus())) totalTersedia++;
            if (l.getPerusahaan() != null && !listPerusahaan.contains(l.getPerusahaan().getNamaPerusahaan())) {
                listPerusahaan.add(l.getPerusahaan().getNamaPerusahaan());
            }
            if (l.getLokasi() != null && !listLokasi.contains(l.getLokasi())) {
                listLokasi.add(l.getLokasi());
            }
        }

        int totalSemua = semuaLowongan.size();
        ringkasanLabel.setText(totalSemua + " lowongan dari " + (listPerusahaan.size() - 1) + " perusahaan mitra");
        semuaTab.setText("Semua " + totalSemua);
        tersediaTab.setText("Tersedia " + totalTersedia);
        tidakTersediaTab.setText("Tidak tersedia " + (totalSemua - totalTersedia));

        perusahaanComboBox.setItems(FXCollections.observableArrayList(listPerusahaan));
        jenisComboBox.setItems(FXCollections.observableArrayList("Semua jenis", "full time", "part time"));
        lokasiComboBox.setItems(FXCollections.observableArrayList(listLokasi));

        halamanSekarang = 1;
        terapkanFilter();
    }

    private void terapkanFilter() {
        String keyword = cariPekerjaanField.getText() == null ? "" : cariPekerjaanField.getText().toLowerCase().trim();
        String filterPerusahaan = perusahaanComboBox.getValue();
        String filterJenis = jenisComboBox.getValue();
        String filterLokasi = lokasiComboBox.getValue();

        lowonganTerfilter.clear();
        for (Lowongan l : semuaLowongan) {
            if (tersediaTab.isSelected() && !"tersedia".equalsIgnoreCase(l.getStatus())) continue;
            if (tidakTersediaTab.isSelected() && !"tidak tersedia".equalsIgnoreCase(l.getStatus())) continue;
            if (!keyword.isEmpty() && !l.getNamaPekerjaan().toLowerCase().contains(keyword)) continue;

            String namaPerusahaan = l.getPerusahaan() != null ? l.getPerusahaan().getNamaPerusahaan() : "";
            if (filterPerusahaan != null && !filterPerusahaan.equals("Semua perusahaan") && !filterPerusahaan.equals(namaPerusahaan)) continue;
            if (filterJenis != null && !filterJenis.equals("Semua jenis") && !filterJenis.equalsIgnoreCase(l.getJenisLowongan())) continue;
            if (filterLokasi != null && !filterLokasi.equals("Semua lokasi") && !filterLokasi.equalsIgnoreCase(l.getLokasi())) continue;

            lowonganTerfilter.add(l);
        }
        updateTampilanHalaman();
    }

    private void updateTampilanHalaman() {
        int totalData = lowonganTerfilter.size();
        int totalHalaman = Math.max(1, (int) Math.ceil((double) totalData / barisPerHalaman));
        if (halamanSekarang > totalHalaman) halamanSekarang = totalHalaman;

        int dariIndex = (halamanSekarang - 1) * barisPerHalaman;
        int sampaiIndex = Math.min(dariIndex + barisPerHalaman, totalData);

        lowonganTable.setItems(FXCollections.observableArrayList(lowonganTerfilter.subList(dariIndex, sampaiIndex)));
        infoTampilLabel.setText(totalData == 0 ? "Menampilkan 0 dari 0" : "Menampilkan " + (dariIndex + 1) + "–" + sampaiIndex + " dari " + totalData);
        infoHalamanLabel.setText("Halaman " + halamanSekarang + " dari " + totalHalaman);
        sebelumnyaButton.setDisable(halamanSekarang <= 1);
        berikutnyaButton.setDisable(halamanSekarang >= totalHalaman);
    }

    @FXML
    void handleTambah(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projectpbokawansetara/view/FormLowonganView.fxml"));
            Parent halamanBaru = loader.load();
            Node konten = (halamanBaru instanceof BorderPane) ? ((BorderPane) halamanBaru).getCenter() : halamanBaru;

            Scene scene = ((Node) event.getSource()).getScene();
            BorderPane mainBorderPane = (BorderPane) scene.lookup("#mainBorderPane");
            mainBorderPane.setCenter(konten);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleFilter(ActionEvent event) {
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
}