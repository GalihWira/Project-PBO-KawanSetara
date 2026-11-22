package com.example.projectpbokawansetara.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.projectpbokawansetara.dao.ChecklistDAO;
import com.example.projectpbokawansetara.helper.DialogHelper;
import com.example.projectpbokawansetara.model.ItemChecklist;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class KelolaChecklistController {

    @FXML
    private TableView<ItemChecklist> itemTable;
    @FXML
    private TableColumn<ItemChecklist, String> idColumn;
    @FXML
    private TableColumn<ItemChecklist, String> kategoriColumn;
    @FXML
    private TableColumn<ItemChecklist, String> namaColumn;
    @FXML
    private TableColumn<ItemChecklist, String> diLowonganColumn;
    @FXML
    private TableColumn<ItemChecklist, String> dipilihColumn;
    @FXML
    private TableColumn<ItemChecklist, Void> aksiColumn;
    @FXML
    private TextField cariItemField;
    @FXML
    private ComboBox<String> filterKategoriComboBox;
    @FXML
    private Label ringkasanLabel;
    @FXML
    private HBox suksesBanner;
    @FXML
    private Label suksesBannerJudulLabel;
    @FXML
    private Label suksesBannerPesanLabel;
    @FXML
    private HBox errorBanner;
    @FXML
    private Label errorBannerJudulLabel;
    @FXML
    private Label errorBannerPesanLabel;
    @FXML
    private TextField namaItemField;
    @FXML
    private ComboBox<String> kategoriComboBox;
    @FXML
    private VBox previewBox;
    @FXML
    private Label previewKategoriLabel;
    @FXML
    private CheckBox previewCheckBox;

    private ChecklistDAO checklistDAO = new ChecklistDAO();
    private List<ItemChecklist> semuaItem = new ArrayList<>();

    @FXML
    public void initialize() {
        kategoriColumn.setCellValueFactory(new PropertyValueFactory<>("namaKategori"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("namaItem"));

        idColumn.setCellValueFactory(data -> new SimpleStringProperty("#" + data.getValue().getIdItem()));
        diLowonganColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getJumlahDiLowongan() + " lowongan"));
        dipilihColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getJumlahDipilihPelamar() + " pelamar"));

        buatTombolHapus();
        muatKategori();
        loadDataItem();

        cariItemField.textProperty().addListener((obs, lama, baru) -> terapkanFilter());

        namaItemField.textProperty().addListener((obs, lama, baru) -> updatePreview());
        kategoriComboBox.valueProperty().addListener((obs, lama, baru) -> updatePreview());

        Platform.runLater(() -> {
            if (itemTable.getScene() != null) {
                TextField searchAtas = (TextField) itemTable.getScene().lookup("#cariField");
                if (searchAtas != null) {
                    searchAtas.clear();
                    searchAtas.textProperty().addListener((obs, lama, baru) -> {
                        if (itemTable.getScene() != null) {
                            cariItemField.setText(baru);
                        }
                    });
                }
            }
        });
    }

    private void muatKategori() {
        List<String> daftarKategori = checklistDAO.getAllKategori();
        kategoriComboBox.setItems(FXCollections.observableArrayList(daftarKategori));

        List<String> opsiFilter = new ArrayList<>();
        opsiFilter.add("Semua kategori");
        opsiFilter.addAll(daftarKategori);
        filterKategoriComboBox.setItems(FXCollections.observableArrayList(opsiFilter));
        filterKategoriComboBox.getSelectionModel().selectFirst();
    }

    private void loadDataItem() {
        semuaItem = checklistDAO.getAllItemAdmin();
        int jumlahKategori = kategoriComboBox.getItems().size();
        ringkasanLabel.setText(semuaItem.size() + " item · " + jumlahKategori + " kategori");
        terapkanFilter();
    }

    private void terapkanFilter() {
        String keyword = cariItemField.getText().toLowerCase().trim();
        String kategoriDipilih = filterKategoriComboBox.getValue();

        List<ItemChecklist> hasilFilter = new ArrayList<>();
        for (ItemChecklist item : semuaItem) {
            boolean cocokNama = item.getNamaItem().toLowerCase().contains(keyword);
            boolean cocokKategori = kategoriDipilih == null
                    || kategoriDipilih.equals("Semua kategori")
                    || kategoriDipilih.equals(item.getNamaKategori());

            if (cocokNama && cocokKategori) {
                hasilFilter.add(item);
            }
        }
        itemTable.setItems(FXCollections.observableArrayList(hasilFilter));
    }

    @FXML
    void handleFilter(ActionEvent event) {
        terapkanFilter();
    }

    @FXML
    void handleTambah(ActionEvent event) {
        sembunyikanSemuaBanner();
        String nama = namaItemField.getText().trim();
        String kategori = kategoriComboBox.getValue();

        if (nama.isEmpty()) {
            tampilkanBanner(errorBanner, errorBannerJudulLabel, errorBannerPesanLabel,
                    "Item belum ditambahkan", "Nama item wajib diisi.");
            return;
        }
        if (kategori == null) {
            tampilkanBanner(errorBanner, errorBannerJudulLabel, errorBannerPesanLabel,
                    "Item belum ditambahkan", "Pilih kategori terlebih dahulu.");
            return;
        }
        for (ItemChecklist item : semuaItem) {
            if (item.getNamaItem().equalsIgnoreCase(nama)) {
                tampilkanBanner(errorBanner, errorBannerJudulLabel, errorBannerPesanLabel,
                        "Item sudah ada", "Keahlian \"" + nama + "\" sudah terdaftar.");
                return;
            }
        }

        if (checklistDAO.tambahItem(nama, kategori)) {
            namaItemField.clear();
            kategoriComboBox.getSelectionModel().clearSelection();
            loadDataItem();
            tampilkanBanner(suksesBanner, suksesBannerJudulLabel, suksesBannerPesanLabel,
                    "“" + nama + "” ditambahkan ke " + kategori, "Pelamar langsung melihatnya di checklist.");
        } else {
            tampilkanBanner(errorBanner, errorBannerJudulLabel, errorBannerPesanLabel,
                    "Gagal menyimpan", "Terjadi kesalahan saat menyimpan ke database.");
        }
    }

    private void buatTombolHapus() {
        aksiColumn.setCellFactory(param -> new TableCell<ItemChecklist, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button btnHapus = new Button("Hapus");
                    btnHapus.getStyleClass().add("tombol-hapus-teks");
                    btnHapus.setOnAction(event -> {
                        ItemChecklist data = getTableView().getItems().get(getIndex());
                        prosesHapus(data);
                    });
                    setGraphic(btnHapus);
                }
            }
        });
    }

    private void prosesHapus(ItemChecklist item) {
        sembunyikanSemuaBanner();

        int jumlahPemakaian = checklistDAO.hitungPemakaian(item.getIdItem());

        if (jumlahPemakaian > 0) {
            tampilkanBanner(errorBanner, errorBannerJudulLabel, errorBannerPesanLabel,
                    "Tidak bisa dihapus", "Item ini masih dipakai pada lowongan atau pelamar.");
            return;
        }

        if (jumlahPemakaian < 0) {
            tampilkanBanner(errorBanner, errorBannerJudulLabel, errorBannerPesanLabel,
                    "Gagal memeriksa data", "Silakan coba lagi.");
            return;
        }

        boolean yakin = DialogHelper.tampilkanKonfirmasiHapus(
                itemTable.getScene().getWindow(),
                "Yakin ingin menghapus \"" + item.getNamaItem() + "\"?",
                "Tindakan ini tidak bisa dibatalkan.");
        if (!yakin) {
            return;
        }

        if (checklistDAO.hapusItem(item.getIdItem())) {
            loadDataItem();
        } else {
            tampilkanBanner(errorBanner, errorBannerJudulLabel, errorBannerPesanLabel,
                    "Gagal menghapus", "Silakan coba lagi.");
        }
    }

    private void updatePreview() {
        String nama = namaItemField.getText().trim();
        String kategori = kategoriComboBox.getValue();

        if (nama.isEmpty() && kategori == null) {
            previewBox.setVisible(false);
            previewBox.setManaged(false);
        } else {
            if (kategori == null) {
                previewKategoriLabel.setText("Pilih kategori");
            } else {
                previewKategoriLabel.setText(kategori);
            }

            if (nama.isEmpty()) {
                previewCheckBox.setText("Nama item...");
            } else {
                previewCheckBox.setText(nama);
            }

            previewBox.setVisible(true);
            previewBox.setManaged(true);
        }
    }

    private void tampilkanBanner(HBox banner, Label labelJudul, Label labelPesan, String judul, String pesan) {
        labelJudul.setText(judul);
        labelPesan.setText(pesan);
        banner.setVisible(true);
        banner.setManaged(true);
    }

    private void sembunyikanSemuaBanner() {
        suksesBanner.setVisible(false);
        suksesBanner.setManaged(false);
        errorBanner.setVisible(false);
        errorBanner.setManaged(false);
    }
}