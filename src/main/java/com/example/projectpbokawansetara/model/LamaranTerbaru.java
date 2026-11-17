package com.example.projectpbokawansetara.model;

public class LamaranTerbaru {
    private String namaPelamar;
    private String posisi;
    private String tanggalMasuk;
    private String status;

    public LamaranTerbaru(String namaPelamar, String posisi, String tanggalMasuk, String status) {
        this.namaPelamar = namaPelamar;
        this.posisi = posisi;
        this.tanggalMasuk = tanggalMasuk;
        this.status = status;
    }

    public String getNamaPelamar() { return namaPelamar; }
    public String getPosisi() { return posisi; }
    public String getTanggalMasuk() { return tanggalMasuk; }
    public String getStatus() { return status; }
}