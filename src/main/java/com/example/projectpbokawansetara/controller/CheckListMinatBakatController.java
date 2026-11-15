package com.example.projectpbokawansetara.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.projectpbokawansetara.dao.ChecklistDAO;
import com.example.projectpbokawansetara.model.ItemChecklist;
import com.example.projectpbokawansetara.model.Session;
import com.example.projectpbokawansetara.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CheckListMinatBakatController {

    private static final String KELAS_BANNER_ERROR = "banner-error";
    private static final String KELAS_BANNER_SUKSES = "banner-sukses";
    private static final String KELAS_CHECKBOX_ERROR = "checkbox-error";
    private static final String KELAS_JUMLAH_ERROR = "jumlah-dipilih-error";
    private static final String KELAS_PEMISAH = "baris-pemisah";

    @FXML private VBox kategoriContainer;
    @FXML private HBox errorBanner;
    @FXML private Label pesanErrorLabel;
    @FXML private Label detailErrorLabel;
    @FXML private Label checklistKosongLabel;
    @FXML private Label jumlahDipilihLabel;
    @FXML private Label statusPerubahanLabel;
    @FXML private TextField pencarianField;
    @FXML private Button batalkanButton;
    @FXML private Button simpanButton;

    private final ChecklistDAO checklistDAO = new ChecklistDAO();

    private final Map<Integer, CheckBox> checkboxKeahlian = new HashMap<>();
    private final Map<String, List<Integer>> keahlianPerKategori = new HashMap<>();
    private final Map<String, Label> jumlahPerKategori = new HashMap<>();
    private final Map<String, HBox> barisPerKategori = new LinkedHashMap<>();

    private Set<Integer> keahlianAwal = new HashSet<>();
    private int idPelamar;

    @FXML
    public void initialize() {
        User user = Session.getCurrentUser();

        if (user == null) {
            tampilkanError("Sesi pengguna tidak ditemukan", "Silakan login kembali.");
            return;
        }

        idPelamar = user.getIdPengguna();

        pencarianField.textProperty().addListener(
            (observable, nilaiLama, nilaiBaru) -> filterKeahlian(nilaiBaru)
        );

        muatChecklist();
    }

    private void muatChecklist() {
        try {
            Map<String, List<ItemChecklist>> daftarKategori =
                checklistDAO.ambilSemuaKeahlian();

            keahlianAwal = checklistDAO.ambilKeahlianPelamar(idPelamar);

            tampilkanKategori(daftarKategori);

            perbaruiRingkasan();
            sembunyikanError();

        } catch (SQLException e) {
            tampilkanError("Gagal memuat checklist", e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean apakahBaru(ItemChecklist item) {
        return false;
    }

    private void tampilkanKategori(Map<String, List<ItemChecklist>> daftarKategori) {
        kategoriContainer.getChildren().clear();

        checkboxKeahlian.clear();
        keahlianPerKategori.clear();
        jumlahPerKategori.clear();
        barisPerKategori.clear();

        if (daftarKategori.isEmpty()) {
            checklistKosongLabel.setText("Belum ada item checklist dari admin");
            checklistKosongLabel.setManaged(true);
            checklistKosongLabel.setVisible(true);
            kategoriContainer.getChildren().add(checklistKosongLabel);
            return;
        }

        VBox kartuDaftar = new VBox();
        kartuDaftar.setMaxWidth(Double.MAX_VALUE);
        kartuDaftar.getStyleClass().add("kartu-daftar");

        for (Map.Entry<String, List<ItemChecklist>> kategori : daftarKategori.entrySet()) {

            String namaKategori = kategori.getKey();
            List<ItemChecklist> daftarItem = kategori.getValue();

            HBox baris = new HBox();
            baris.setAlignment(Pos.TOP_LEFT);
            baris.setPadding(new Insets(18, 19, 18, 20));

            VBox kiri = new VBox(3);
            kiri.setMinWidth(190);
            kiri.setPrefWidth(190);
            kiri.setMaxWidth(190);

            Label namaLabel = new Label(namaKategori);
            namaLabel.getStyleClass().add("kategori-nama");

            Label jumlahLabel = new Label();
            jumlahLabel.getStyleClass().add("kategori-jumlah");

            kiri.getChildren().addAll(namaLabel, jumlahLabel);

            GridPane gridKeahlian = new GridPane();
            gridKeahlian.setHgap(8);
            gridKeahlian.setVgap(8);
            gridKeahlian.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(gridKeahlian, Priority.ALWAYS);

            for (int i = 0; i < 3; i++) {
                ColumnConstraints kolom = new ColumnConstraints();
                kolom.setPercentWidth(100.0 / 3);
                kolom.setHgrow(Priority.ALWAYS);
                gridKeahlian.getColumnConstraints().add(kolom);
            }

            List<Integer> idKeahlianKategori = new ArrayList<>();

            for (int i = 0; i < daftarItem.size(); i++) {
                ItemChecklist item = daftarItem.get(i);

                CheckBox checkbox = new CheckBox(item.getNamaItem());
                checkbox.setWrapText(true);
                checkbox.getStyleClass().add("checkbox-keahlian");
                checkbox.setSelected(keahlianAwal.contains(item.getIdItem()));

                checkbox.setOnAction(event -> {
                    sembunyikanError();
                    perbaruiRingkasan();
                    perbaruiJumlahKategori();
                });

                checkboxKeahlian.put(item.getIdItem(), checkbox);
                idKeahlianKategori.add(item.getIdItem());

                HBox sel = new HBox(8);
                sel.setAlignment(Pos.CENTER_LEFT);
                sel.getChildren().add(checkbox);

                if (apakahBaru(item)) {
                    Label baru = new Label("Baru");
                    baru.getStyleClass().add("keahlian-baru");
                    sel.getChildren().add(baru);
                }

                gridKeahlian.add(sel, i % 3, i / 3);
            }

            baris.getChildren().addAll(kiri, gridKeahlian);
            kartuDaftar.getChildren().add(baris);

            keahlianPerKategori.put(namaKategori, idKeahlianKategori);
            jumlahPerKategori.put(namaKategori, jumlahLabel);
            barisPerKategori.put(namaKategori, baris);
        }

        checklistKosongLabel.setManaged(false);
        checklistKosongLabel.setVisible(false);

        kategoriContainer.getChildren().addAll(kartuDaftar, checklistKosongLabel);

        perbaruiJumlahKategori();
        perbaruiGarisPemisah();
    }

    private void perbaruiGarisPemisah() {
        boolean pertama = true;

        for (HBox baris : barisPerKategori.values()) {
            baris.getStyleClass().remove(KELAS_PEMISAH);

            if (!baris.isVisible()) {
                continue;
            }

            if (!pertama) {
                baris.getStyleClass().add(KELAS_PEMISAH);
            }
            pertama = false;
        }
    }

    private void filterKeahlian(String kataKunci) {
        String pencarian = kataKunci == null ? "" : kataKunci.trim().toLowerCase();

        for (Map.Entry<String, List<Integer>> kategori : keahlianPerKategori.entrySet()) {

            boolean adaHasil = false;

            for (int id : kategori.getValue()) {
                CheckBox checkbox = checkboxKeahlian.get(id);
                boolean cocok = checkbox.getText().toLowerCase().contains(pencarian);

                checkbox.getParent().setVisible(cocok);
                checkbox.getParent().setManaged(cocok);

                if (cocok) {
                    adaHasil = true;
                }
            }

            HBox baris = barisPerKategori.get(kategori.getKey());
            baris.setVisible(adaHasil);
            baris.setManaged(adaHasil);
        }

        perbaruiGarisPemisah();

        boolean adaKategori = adaBarisTerlihat();

        checklistKosongLabel.setText(
            pencarian.isEmpty()
                ? "Belum ada item checklist dari admin"
                : "Keahlian tidak ditemukan"
        );
        checklistKosongLabel.setVisible(!adaKategori);
        checklistKosongLabel.setManaged(!adaKategori);
    }

    private boolean adaBarisTerlihat() {
        for (HBox baris : barisPerKategori.values()) {
            if (baris.isVisible()) {
                return true;
            }
        }
        return false;
    }

    private Set<Integer> ambilPilihanSaatIni() {
        Set<Integer> pilihan = new HashSet<>();

        for (Map.Entry<Integer, CheckBox> entry : checkboxKeahlian.entrySet()) {
            if (entry.getValue().isSelected()) {
                pilihan.add(entry.getKey());
            }
        }
        return pilihan;
    }

    private void perbaruiJumlahKategori() {
        for (Map.Entry<String, List<Integer>> kategori : keahlianPerKategori.entrySet()) {

            int jumlahDipilih = 0;
            int jumlahTotal = kategori.getValue().size();

            for (int id : kategori.getValue()) {
                if (checkboxKeahlian.get(id).isSelected()) {
                    jumlahDipilih++;
                }
            }

            jumlahPerKategori.get(kategori.getKey()).setText(
                jumlahDipilih + " dari " + jumlahTotal + " dipilih"
            );
        }
    }

    private void perbaruiRingkasan() {
        Set<Integer> pilihan = ambilPilihanSaatIni();

        jumlahDipilihLabel.setText(pilihan.size() + " keahlian dipilih");

        boolean adaPerubahan = !pilihan.equals(keahlianAwal);

        statusPerubahanLabel.setText("· ada perubahan yang belum disimpan");
        statusPerubahanLabel.setVisible(adaPerubahan);
        statusPerubahanLabel.setManaged(adaPerubahan);

        batalkanButton.setDisable(!adaPerubahan);
    }

    @FXML
    void handleSimpan(ActionEvent event) {
        Set<Integer> pilihan = ambilPilihanSaatIni();

        if (pilihan.isEmpty()) {
            tampilkanError(
                "Pilih minimal 1 keahlian sebelum menyimpan",
                "Rekomendasi lowongan dihitung dari keahlian yang kamu centang."
            );
            tandaiValidasiGagal(true);
            return;
        }

        try {
            checklistDAO.simpanKeahlianPelamar(idPelamar, pilihan);

            keahlianAwal = new HashSet<>(pilihan);

            perbaruiRingkasan();
            perbaruiJumlahKategori();

            tampilkanSukses(
                "Checklist berhasil disimpan",
                "Pilihan keahlian kamu sudah diperbarui."
            );

        } catch (SQLException e) {
            tampilkanError("Gagal menyimpan checklist", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleBatalkan(ActionEvent event) {
        for (Map.Entry<Integer, CheckBox> entry : checkboxKeahlian.entrySet()) {
            entry.getValue().setSelected(keahlianAwal.contains(entry.getKey()));
        }

        sembunyikanError();
        perbaruiRingkasan();
        perbaruiJumlahKategori();
    }

    private void tandaiValidasiGagal(boolean gagal) {
        for (CheckBox checkbox : checkboxKeahlian.values()) {
            checkbox.getStyleClass().remove(KELAS_CHECKBOX_ERROR);
            if (gagal) {
                checkbox.getStyleClass().add(KELAS_CHECKBOX_ERROR);
            }
        }

        jumlahDipilihLabel.getStyleClass().remove(KELAS_JUMLAH_ERROR);
        if (gagal) {
            jumlahDipilihLabel.getStyleClass().add(KELAS_JUMLAH_ERROR);
        }
    }

    private void tampilkanBanner(String kelas, String pesan, String detail) {
        pesanErrorLabel.setText(pesan);
        detailErrorLabel.setText(detail);

        errorBanner.getStyleClass().removeAll(KELAS_BANNER_ERROR, KELAS_BANNER_SUKSES);
        errorBanner.getStyleClass().add(kelas);

        errorBanner.setManaged(true);
        errorBanner.setVisible(true);
    }

    private void tampilkanError(String pesan, String detail) {
        tampilkanBanner(KELAS_BANNER_ERROR, pesan, detail);
    }

    private void tampilkanSukses(String pesan, String detail) {
        tampilkanBanner(KELAS_BANNER_SUKSES, pesan, detail);
        tandaiValidasiGagal(false);
    }

    private void sembunyikanError() {
        errorBanner.setManaged(false);
        errorBanner.setVisible(false);
        tandaiValidasiGagal(false);
    }
}