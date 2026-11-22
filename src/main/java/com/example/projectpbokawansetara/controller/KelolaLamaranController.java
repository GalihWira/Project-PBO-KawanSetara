package com.example.projectpbokawansetara.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.projectpbokawansetara.dao.LamaranDAO;
import com.example.projectpbokawansetara.model.Lamaran;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class KelolaLamaranController {

    @FXML private TableView<Lamaran> lamaranTable;
    @FXML private TableColumn<Lamaran, String> pelamarColumn;
    @FXML private TableColumn<Lamaran, String> posisiColumn;
    @FXML private TableColumn<Lamaran, String> perusahaanColumn;
    @FXML private TableColumn<Lamaran, String> tanggalColumn;
    @FXML private TableColumn<Lamaran, String> statusColumn;
    @FXML private TableColumn<Lamaran, Void> aksiColumn;

    @FXML private ToggleGroup statusTabGroup;
    @FXML private ToggleButton semuaTab;
    @FXML private ToggleButton diprosesTab;
    @FXML private ToggleButton ditinjauTab;
    @FXML private ToggleButton diterimaTab;
    @FXML private ToggleButton ditolakTab;

    @FXML private TextField cariField;
    @FXML private Label ringkasanLabel;
    @FXML private Label pesanLabel;

    private final LamaranDAO lamaranDAO = new LamaranDAO();
    private List<Lamaran> semuaLamaran = new ArrayList<>();
    private static final String KELAS_TAB_AKTIF = "tab-status-aktif";
    private static final String KELAS_TAB_NONAKTIF = "tab-status-nonaktif";

    private final DateTimeFormatter formatTanggal =
            DateTimeFormatter.ofPattern("d MMM yyyy", Locale.forLanguageTag("id-ID"));

    private static final String STYLE_TAB_AKTIF = "-fx-background-color: transparent; -fx-border-color: transparent transparent #1F4E8C transparent; -fx-border-width: 0 0 2 0; -fx-text-fill: #1F4E8C; -fx-font-size: 13px; -fx-font-weight: 600; -fx-padding: 6 12 10 12;";
    private static final String STYLE_TAB_NONAKTIF = "-fx-background-color: transparent; -fx-text-fill: #475467; -fx-font-size: 13px; -fx-padding: 6 12 10 12;";

    @FXML
    private void initialize() {
        pelamarColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPelamar().getNama()));
        posisiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLowongan().getNamaPekerjaan()));
        perusahaanColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getLowongan().getPerusahaan().getNamaPerusahaan()));
        tanggalColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getTanggalLamar().format(formatTanggal)));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        buatLabelStatus();
        buatTombolAksi();

        cariField.textProperty().addListener((obs, lama, baru) -> terapkanFilter());

        sembunyikanPesan();
        loadDataLamaran();
    }

    private void loadDataLamaran() {
        try {
            semuaLamaran = lamaranDAO.getSemuaLamaran();
        } catch (SQLException e) {
            e.printStackTrace();
            semuaLamaran = new ArrayList<>();
            tampilkanPesan("Data lamaran gagal dimuat. Silakan coba lagi.", false);
        }

        perbaruiJumlahTab();
        terapkanFilter();
    }

    private void perbaruiJumlahTab() {
        int diproses = hitungStatus("diproses");
        int ditinjau = hitungStatus("ditinjau");
        int diterima = hitungStatus("diterima");
        int ditolak = hitungStatus("ditolak");

        semuaTab.setText("Semua " + semuaLamaran.size());
        diprosesTab.setText("Diproses " + diproses);
        ditinjauTab.setText("Ditinjau " + ditinjau);
        diterimaTab.setText("Diterima " + diterima);
        ditolakTab.setText("Ditolak " + ditolak);

        ringkasanLabel.setText(semuaLamaran.size() + " lamaran masuk, " + diproses + " belum ditinjau");
    }

    private int hitungStatus(String status) {
        int jumlah = 0;
        for (Lamaran lamaran : semuaLamaran) {
            if (lamaran.getStatus().equals(status)) {
                jumlah++;
            }
        }
        return jumlah;
    }

    private String statusTerpilih() {
        if (diprosesTab.isSelected()) {
            return "diproses";
        } else if (ditinjauTab.isSelected()) {
            return "ditinjau";
        } else if (diterimaTab.isSelected()) {
            return "diterima";
        } else if (ditolakTab.isSelected()) {
            return "ditolak";
        }
        return null;
    }

    private void terapkanFilter() {
        String status = statusTerpilih();
        String kataCari = cariField.getText().trim().toLowerCase();

        List<Lamaran> hasil = new ArrayList<>();

        for (Lamaran lamaran : semuaLamaran) {
            boolean statusCocok = status == null || lamaran.getStatus().equals(status);

            String nama = lamaran.getPelamar().getNama().toLowerCase();
            String posisi = lamaran.getLowongan().getNamaPekerjaan().toLowerCase();
            boolean cariCocok = kataCari.isEmpty() || nama.contains(kataCari) || posisi.contains(kataCari);

            if (statusCocok && cariCocok) {
                hasil.add(lamaran);
            }
        }

        lamaranTable.setItems(FXCollections.observableArrayList(hasil));
    }

    @FXML
    void handleFilter(ActionEvent event) {
        ToggleButton tabDiklik = (ToggleButton) event.getSource();
        tabDiklik.setSelected(true);

        ToggleButton[] semuaTabStatus = { semuaTab, diprosesTab, ditinjauTab, diterimaTab, ditolakTab };
        for (ToggleButton tab : semuaTabStatus) {
            aturKelasTab(tab, tab.isSelected());
        }

        terapkanFilter();
    }

    private void aturKelasTab(ToggleButton tab, boolean aktif) {
        tab.getStyleClass().removeAll(KELAS_TAB_AKTIF, KELAS_TAB_NONAKTIF);
        tab.getStyleClass().add(aktif ? KELAS_TAB_AKTIF : KELAS_TAB_NONAKTIF);
    }

    private void buatLabelStatus() {
        statusColumn.setCellFactory(param -> new TableCell<Lamaran, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label lblStatus = new Label(hurufBesarAwal(status));
                    lblStatus.getStyleClass().addAll("status-badge", kelasStatus(status));
                    setText(null);
                    setGraphic(lblStatus);
                }
            }
        });
    }

    private String kelasStatus(String status) {
        if (status == null) return "status-badge-default";
        return switch (status) {
            case "diproses" -> "status-badge-diproses";
            case "ditinjau" -> "status-badge-ditinjau";
            case "diterima" -> "status-badge-diterima";
            case "ditolak"  -> "status-badge-ditolak";
            default -> "status-badge-default";
        };
    }

    private void buatTombolAksi() {
        aksiColumn.setCellFactory(param -> new TableCell<Lamaran, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Lamaran data = getTableView().getItems().get(getIndex());

                    Button btnTinjau = buatTombol("Tinjau", "tombol-aksi-tinjau");
                    Button btnTerima = buatTombol("Terima", "tombol-aksi-terima");
                    Button btnTolak  = buatTombol("Tolak",  "tombol-aksi-tolak");

                    btnTerima.setDisable(data.getStatus().equals("diterima"));
                    btnTolak.setDisable(data.getStatus().equals("ditolak"));

                    btnTinjau.setOnAction(event -> bukaDialogTinjau(data));
                    btnTerima.setOnAction(event -> prosesUbahStatus(data, "diterima"));
                    btnTolak.setOnAction(event -> prosesUbahStatus(data, "ditolak"));

                    HBox kotakTombol = new HBox(6, btnTinjau, btnTerima, btnTolak);
                    kotakTombol.getStyleClass().add("rata-kiri-tengah");
                    setGraphic(kotakTombol);
                }
            }
        });
    }

    private Button buatTombol(String teks, String kelas) {
        Button tombol = new Button(teks);
        tombol.getStyleClass().addAll("tombol-aksi-lamaran", kelas);
        return tombol;
    }

    private void bukaDialogTinjau(Lamaran lamaran) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/example/projectpbokawansetara/view/TinjauLamaranDialog.fxml"));
            Parent dialog = loader.load();

            TinjauLamaranController controller = loader.getController();
            controller.setData(lamaran);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Tinjau Lamaran");
            stage.setScene(new Scene(dialog));
            stage.showAndWait();

            if (controller.isDitandai()) {
                prosesUbahStatus(lamaran, "ditinjau");
            }
        } catch (IOException e) {
            tampilkanPesan("Dialog tinjau gagal dibuka.", false);
        }
    }

    private void prosesUbahStatus(Lamaran lamaran, String statusBaru) {
        sembunyikanPesan();
        String namaPelamar = lamaran.getPelamar().getNama();

        try {
            if (lamaranDAO.ubahStatus(lamaran.getIdLamaran(), statusBaru)) {
                loadDataLamaran();
                perbaruiBadgeMenu();
                tampilkanPesan("Lamaran " + namaPelamar + " sekarang " + hurufBesarAwal(statusBaru) + ".", true);
            } else {
                tampilkanPesan("Status gagal diubah. Silakan coba lagi.", false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            tampilkanPesan("Status gagal diubah. Silakan coba lagi.", false);
        }
    }

    private void perbaruiBadgeMenu() {
        if (lamaranTable.getScene() == null) {
            return;
        }

        Label badge = (Label) lamaranTable.getScene().lookup("#jumlahLamaranMenuLabel");
        if (badge != null) {
            badge.setText(String.valueOf(hitungStatus("diproses")));
        }
    }

    private String hurufBesarAwal(String teks) {
        return teks.substring(0, 1).toUpperCase() + teks.substring(1);
    }

    private void tampilkanPesan(String pesan, boolean berhasil) {
        pesanLabel.getStyleClass().removeAll("pesan-sukses", "pesan-error-box");
        pesanLabel.getStyleClass().add(berhasil ? "pesan-sukses" : "pesan-error-box");
        pesanLabel.setText(pesan);
        pesanLabel.setVisible(true);
        pesanLabel.setManaged(true);
    }

    private void sembunyikanPesan() {
        pesanLabel.setVisible(false);
        pesanLabel.setManaged(false);
    }
}