package com.example.projectpbokawansetara.helper;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javafx.scene.control.Label;

public class FileHelper {
    public static File folderUpload() {
        File folder = new File("uploads");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    public static String simpanPdf(File fileAsal, String jenis, String email) throws IOException {
        return simpanFile(fileAsal, jenis, email, "pdf");
    }

    public static String simpanGambar(File fileAsal, String email) throws IOException {
        String namaAsli = fileAsal.getName().toLowerCase();
        String ekstensi = "jpg";
        if (namaAsli.endsWith(".png")) {
            ekstensi = "png";
        } else if (namaAsli.endsWith(".jpeg")) {
            ekstensi = "jpeg";
        }
        return simpanFile(fileAsal, "foto", email, ekstensi);
    }

    private static String simpanFile(File fileAsal, String jenis, String email, String ekstensi) throws IOException {
        File folderJenis = new File(folderUpload(), jenis);
        if (!folderJenis.exists()) {
            folderJenis.mkdirs();
        }

        String emailAman = email.replace("@", "_").replace(".", "_");
        String namaBaru = jenis + "_" + emailAman + "_" + System.currentTimeMillis() + "." + ekstensi;
        File tujuan = new File(folderJenis, namaBaru);

        try (FileInputStream masuk = new FileInputStream(fileAsal);
             FileOutputStream keluar = new FileOutputStream(tujuan)) {
            byte[] buffer = new byte[1024];
            int jumlahBaca;
            while ((jumlahBaca = masuk.read(buffer)) != -1) {
                keluar.write(buffer, 0, jumlahBaca);
            }
        }

        return "uploads/" + jenis + "/" + namaBaru;
    }

    public static void bukaFile(String path, Label pesanLabel) {
        if (path == null || path.trim().isEmpty()) {
            pesanLabel.setText("Maaf, Belum Terdapat File yang Diunggah!");
            return;
        }

        File file = new File(path);
        if (!file.exists()) {
            pesanLabel.setText("Maaf, File Tidak Ditemukan Pada Komputer Ini!");
            return;
        }

        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            pesanLabel.setText("Maaf, File Gagal Dibuka!");
        }
    }
}