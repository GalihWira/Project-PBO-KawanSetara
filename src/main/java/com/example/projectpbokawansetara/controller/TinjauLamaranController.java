package com.example.projectpbokawansetara.controller;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.example.projectpbokawansetara.dao.PelamarDAO;
import com.example.projectpbokawansetara.helper.FileHelper;
import com.example.projectpbokawansetara.model.Lamaran;
import com.example.projectpbokawansetara.model.Pelamar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class TinjauLamaranController {

    @FXML
    private Label inisialLabel;
    @FXML
    private ImageView fotoImageView;
    @FXML
    private Label namaLabel;
    @FXML
    private Label umurLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private FlowPane disabilitasChipPane;
    @FXML
    private Label disabilitasKosongLabel;
    @FXML
    private Label posisiLabel;
    @FXML
    private Label perusahaanLabel;
    @FXML
    private Label cvLabel;
    @FXML
    private Label skdLabel;
    @FXML
    private Label pesanLabel;
    @FXML
    private Label infoStatusLabel;
    @FXML
    private Button bukaCvButton;
    @FXML
    private Button bukaSkdButton;
    @FXML
    private Button tutupButton;
    @FXML
    private Button tandaiButton;

    private final PelamarDAO pelamarDAO = new PelamarDAO();

    private String pathCv;
    private String pathSkd;
    private boolean ditandai = false;

    private static final String STYLE_CHIP = "-fx-background-color: #EFF4FF;"
            + "-fx-text-fill: #1F4E8C;"
            + "-fx-font-size: 12px;"
            + "-fx-font-weight: 600;"
            + "-fx-background-radius: 16;"
            + "-fx-padding: 4 10 4 10;";

    public void setData(Lamaran lamaran) {
        Pelamar ringkas = lamaran.getPelamar();
        int idPelamar = ringkas.getIdPengguna();

        Pelamar pelamar = ringkas;
        try {
            Pelamar lengkap = pelamarDAO.getPelamarById(idPelamar);
            if (lengkap != null) {
                pelamar = lengkap;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String nama = pelamar.getNama() != null ? pelamar.getNama() : "-";
        namaLabel.setText(nama);
        emailLabel.setText(pelamar.getEmail() != null ? pelamar.getEmail() : "-");
        umurLabel.setText(formatUmur(pelamar.getTanggalLahir()));

        tampilkanAvatar(nama, pelamar.getFoto());

        posisiLabel.setText("Melamar: " + lamaran.getLowongan().getNamaPekerjaan());
        if (lamaran.getLowongan().getPerusahaan() != null) {
            perusahaanLabel.setText(lamaran.getLowongan().getPerusahaan().getNamaPerusahaan());
        } else {
            perusahaanLabel.setText("");
        }

        tampilkanChipDisabilitas(idPelamar);

        pathCv = pelamar.getCv();
        pathSkd = pelamar.getSuratKeteranganDisabilitas();

        aturDokumen(cvLabel, bukaCvButton, pathCv, "CV");
        aturDokumen(skdLabel, bukaSkdButton, pathSkd, "SKD");

        aturTombolTandai(lamaran.getStatus());
    }

    public boolean isDitandai() {
        return ditandai;
    }

    @FXML
    void handleBukaCv(ActionEvent event) {
        pesanLabel.setText("");
        FileHelper.bukaFile(pathCv, pesanLabel);
    }

    @FXML
    void handleBukaSkd(ActionEvent event) {
        pesanLabel.setText("");
        FileHelper.bukaFile(pathSkd, pesanLabel);
    }

    @FXML
    void handleTandai(ActionEvent event) {
        ditandai = true;
        tutupDialog();
    }

    @FXML
    void handleTutup(ActionEvent event) {
        ditandai = false;
        tutupDialog();
    }

    private void tampilkanAvatar(String nama, String pathFoto) {
        inisialLabel.setText(ambilInisial(nama));
        inisialLabel.setVisible(true);
        fotoImageView.setVisible(false);
        fotoImageView.setImage(null);

        if (pathFoto == null || pathFoto.isEmpty()) {
            return;
        }

        File file = new File(pathFoto);
        if (!file.exists()) {
            return;
        }

        Image gambar = new Image(file.toURI().toString());
        if (gambar.isError()) {
            return;
        }

        double sisi = Math.min(gambar.getWidth(), gambar.getHeight());
        double x = (gambar.getWidth() - sisi) / 2;
        double y = (gambar.getHeight() - sisi) / 2;

        fotoImageView.setClip(new Circle(36, 36, 36));
        fotoImageView.setImage(gambar);
        fotoImageView.setViewport(new Rectangle2D(x, y, sisi, sisi));
        fotoImageView.setVisible(true);
        inisialLabel.setVisible(false);
    }

    private void aturDokumen(Label label, Button tombol, String path, String jenis) {
        if (path == null || path.isEmpty()) {
            label.setText(jenis + ": belum diunggah");
            tombol.setDisable(true);
        } else {
            label.setText(jenis + ": " + namaFile(path));
            tombol.setDisable(false);
        }
    }

    private void tampilkanChipDisabilitas(int idPelamar) {
        disabilitasChipPane.getChildren().clear();
        disabilitasKosongLabel.setVisible(false);
        disabilitasKosongLabel.setManaged(false);

        List<String> daftarNama;
        try {
            daftarNama = pelamarDAO.getNamaDisabilitasPelamar(idPelamar);
        } catch (SQLException e) {
            e.printStackTrace();
            daftarNama = List.of();
        }

        if (daftarNama == null || daftarNama.isEmpty()) {
            disabilitasKosongLabel.setVisible(true);
            disabilitasKosongLabel.setManaged(true);
            return;
        }

        for (String nama : daftarNama) {
            Label chip = new Label(nama);
            chip.setStyle(STYLE_CHIP);
            chip.setPadding(new Insets(4, 10, 4, 10));
            disabilitasChipPane.getChildren().add(chip);
        }
    }

    private String formatUmur(LocalDate tanggalLahir) {
        if (tanggalLahir == null) {
            return "Umur tidak diketahui";
        }
        int umur = Period.between(tanggalLahir, LocalDate.now()).getYears();
        return umur + " tahun";
    }

    private String ambilInisial(String nama) {
        if (nama == null || nama.isBlank() || "-".equals(nama)) {
            return "?";
        }
        List<String> bagian = pecahKata(nama.trim());
        if (bagian.isEmpty()) {
            return "?";
        }
        if (bagian.size() == 1) {
            return bagian.get(0).substring(0, 1).toUpperCase();
        }
        String pertama = bagian.get(0);
        String terakhir = bagian.get(bagian.size() - 1);
        return (pertama.substring(0, 1) + terakhir.substring(0, 1)).toUpperCase();
    }

    private List<String> pecahKata(String teks) {
        List<String> hasil = new ArrayList<>();
        if (teks == null || teks.isEmpty()) {
            return hasil;
        }

        StringBuilder kata = new StringBuilder();
        for (int i = 0; i < teks.length(); i++) {
            char c = teks.charAt(i);
            if (Character.isWhitespace(c)) {
                if (kata.length() > 0) {
                    hasil.add(kata.toString());
                    kata.setLength(0);
                }
            } else {
                kata.append(c);
            }
        }
        if (kata.length() > 0) {
            hasil.add(kata.toString());
        }
        return hasil;
    }

    private String namaFile(String path) {
        if (path == null || path.isEmpty()) {
            return path;
        }
        int idx = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        return idx >= 0 ? path.substring(idx + 1) : path;
    }

    private void tutupDialog() {
        Stage stage = (Stage) tutupButton.getScene().getWindow();
        stage.close();
    }

    private void aturTombolTandai(String status) {
        boolean masihDiproses = "diproses".equals(status);
        tandaiButton.setDisable(!masihDiproses);

        if (masihDiproses) {
            infoStatusLabel.setText("");
            infoStatusLabel.setVisible(false);
            infoStatusLabel.setManaged(false);
            tandaiButton.setAccessibleHelp("Tandai lamaran ini sebagai sudah ditinjau.");
        } else {
            String pesan = status == null
                    ? "Status lamaran ini tidak diketahui. Kamu tetap bisa membuka dokumennya."
                    : "Lamaran ini sudah " + status + ". Kamu tetap bisa membuka dokumennya.";
            infoStatusLabel.setText(pesan);
            infoStatusLabel.setVisible(true);
            infoStatusLabel.setManaged(true);
            tandaiButton.setAccessibleHelp(pesan);
        }
    }
}