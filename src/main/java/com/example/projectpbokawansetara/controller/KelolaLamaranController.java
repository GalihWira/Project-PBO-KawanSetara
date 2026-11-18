package com.example.projectpbokawansetara.controller;

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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

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
            tab.setStyle(tab.isSelected() ? STYLE_TAB_AKTIF : STYLE_TAB_NONAKTIF);
        }

        terapkanFilter();
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
                    lblStatus.setPadding(new Insets(2, 10, 2, 10));
                    lblStatus.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-background-radius: 10; " + warnaStatus(status));
                    setText(null);
                    setGraphic(lblStatus);
                }
            }
        });
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

                    Button btnTinjau = buatTombol("Tinjau", "-fx-background-color: white; -fx-border-color: #D0D5DD; -fx-text-fill: #344054;");
                    Button btnTerima = buatTombol("Terima", "-fx-background-color: #027A48; -fx-text-fill: white;");
                    Button btnTolak = buatTombol("Tolak", "-fx-background-color: white; -fx-border-color: #D92D20; -fx-text-fill: #D92D20;");

                    btnTinjau.setDisable(!data.getStatus().equals("diproses"));
                    btnTerima.setDisable(data.getStatus().equals("diterima"));
                    btnTolak.setDisable(data.getStatus().equals("ditolak"));

                    btnTinjau.setOnAction(event -> prosesUbahStatus(data, "ditinjau"));
                    btnTerima.setOnAction(event -> prosesUbahStatus(data, "diterima"));
                    btnTolak.setOnAction(event -> prosesUbahStatus(data, "ditolak"));

                    HBox kotakTombol = new HBox(6, btnTinjau, btnTerima, btnTolak);
                    kotakTombol.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(kotakTombol);
                }
            }
        });
    }

    private Button buatTombol(String teks, String warna) {
        Button tombol = new Button(teks);
        tombol.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-background-radius: 6; -fx-border-radius: 6; -fx-padding: 4 10; -fx-cursor: hand; " + warna);
        return tombol;
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

    private String warnaStatus(String status) {
        if (status.equals("diproses")) {
            return "-fx-background-color: #FEF0C7; -fx-text-fill: #B54708;";
        } else if (status.equals("ditinjau")) {
            return "-fx-background-color: #D1E9FF; -fx-text-fill: #175CD3;";
        } else if (status.equals("diterima")) {
            return "-fx-background-color: #D1FADF; -fx-text-fill: #027A48;";
        } else {
            return "-fx-background-color: #FEE4E2; -fx-text-fill: #B42318;";
        }
    }

    private void tampilkanPesan(String pesan, boolean berhasil) {
        if (berhasil) {
            pesanLabel.setStyle("-fx-background-color: #ECFDF3; -fx-text-fill: #027A48; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 10 14;");
        } else {
            pesanLabel.setStyle("-fx-background-color: #FEF3F2; -fx-text-fill: #B42318; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 10 14;");
        }
        pesanLabel.setText(pesan);
        pesanLabel.setVisible(true);
        pesanLabel.setManaged(true);
    }

    private void sembunyikanPesan() {
        pesanLabel.setVisible(false);
        pesanLabel.setManaged(false);
    }
}