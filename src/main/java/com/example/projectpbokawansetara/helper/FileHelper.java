package com.example.projectpbokawansetara.helper;

import java.awt.Desktop;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javafx.scene.control.Label;

public class FileHelper {
    public static File folderUpload(){
        File folder = new File ("uploads");
        if (!folder.exists()){
            folder.mkdirs();
        }
        return folder;
    }

    public static String simpanPdf(File fileAsal, String jenis, String email) throws IOException {
        File folderJenis = new File (folderUpload(), jenis);
        if (!folderJenis.exists()){
            folderJenis.mkdirs();
        }

        String emailAman = email.replace("@", "_");
        emailAman = emailAman.replace (".", "_");

        String namaBaru = jenis + "_" + emailAman + "_" + System.currentTimeMillis() + ".pdf";

        File tujuan = new File (folderJenis, namaBaru);

        FileInputStream masuk = new FileInputStream(fileAsal);
        FileOutputStream keluar = new FileOutputStream(tujuan);

        byte[] buffer = new byte[1024];

        int jumlahBaca = masuk.read(buffer);

        while (jumlahBaca != -1){
            keluar.write(buffer, 0, jumlahBaca);
            jumlahBaca = masuk.read(buffer);
        }
        
        masuk.close();
        keluar.close();
        return "uploads/" + jenis + "/" + namaBaru;
    }

    public static void bukaFile (String path, Label pesanLabel){
        if (path == null || path.trim().isEmpty()){
            pesanLabel.setText("Maaf, Belum Terdapat File yang Diunggah!");
            return;
        }

        File file = new File (path);
        if (!file.exists()){
            pesanLabel.setText("Maaf, File Tidak Ditemukan Pada Komputer Ini!");
            return;
        }

        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e){
            pesanLabel.setText ("Maaf, File Gagal Dibuka!");

        }
    }
}
