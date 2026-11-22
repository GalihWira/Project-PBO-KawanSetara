package com.example.projectpbokawansetara.controller;

import java.sql.SQLException;

import com.example.projectpbokawansetara.dao.LamaranDAO;
import com.example.projectpbokawansetara.model.Lowongan;
import com.example.projectpbokawansetara.model.Pelamar;
import com.example.projectpbokawansetara.model.Session;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class KonfirmasiLamarController {

    @FXML
    private Label judulLabel;

    @FXML
    private Text posisiText;

    @FXML
    private Button batalButton;

    @FXML
    private Button kirimLamaranButton;

    private Lowongan lowongan;

    private Runnable aksiBerhasil;
    private Runnable aksiBatal;

    private final LamaranDAO lamaranDAO = new LamaranDAO();

    public void setLowongan(Lowongan lowongan) {
        this.lowongan = lowongan;

        String namaPerusahaan = lowongan.getPerusahaan() != null
                ? lowongan.getPerusahaan().getNamaPerusahaan()
                : "Perusahaan";

        judulLabel.setText("Kirim lamaran ke " + namaPerusahaan + "?");
        posisiText.setText(lowongan.getNamaPekerjaan());
    }

    public void setAksiBerhasil(Runnable aksiBerhasil) {
        this.aksiBerhasil = aksiBerhasil;
    }

    public void setAksiBatal(Runnable aksiBatal) {
        this.aksiBatal = aksiBatal;
    }

    @FXML
    private void handleBatal() {
        if (aksiBatal != null) {
            aksiBatal.run();
        }
    }

    @FXML
    private void handleKirimLamaran() {
        if (!(Session.getCurrentUser() instanceof Pelamar pelamar)) {
            tampilkanPesan(
                    Alert.AlertType.ERROR,
                    "Akses Ditolak",
                    "Silakan masuk menggunakan akun Pelamar."
            );
            return;
        }

        if (lowongan == null) {
            tampilkanPesan(
                    Alert.AlertType.ERROR,
                    "Gagal",
                    "Data lowongan tidak ditemukan."
            );
            return;
        }

        kirimLamaranButton.setDisable(true);

        try {
            int idPelamar = pelamar.getIdPengguna();
            int idLowongan = lowongan.getIdLowongan();

            if (lamaranDAO.sudahMelamar(idPelamar, idLowongan)) {
                tampilkanPesan(
                        Alert.AlertType.WARNING,
                        "Lamaran Sudah Dikirim",
                        "Kamu sudah pernah melamar pekerjaan ini."
                );
                return;
            }
            boolean berhasil = lamaranDAO.simpanLamaran(
                    idPelamar,
                    idLowongan
            );
            if (berhasil) {
                if (aksiBerhasil != null) {
                    aksiBerhasil.run();
                }
            } else {
                tampilkanPesan(
                        Alert.AlertType.ERROR,
                        "Lamaran Gagal",
                        "Lamaran tidak berhasil disimpan."
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();

            tampilkanPesan(
                    Alert.AlertType.ERROR,
                    "Kesalahan Database",
                    "Terjadi kesalahan saat menyimpan lamaran."
            );

        } finally {
            kirimLamaranButton.setDisable(false);
        }
    }

    private void tampilkanPesan(
            Alert.AlertType tipe,
            String judul,
            String pesan
    ) {
        Alert alert = new Alert(tipe);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}
