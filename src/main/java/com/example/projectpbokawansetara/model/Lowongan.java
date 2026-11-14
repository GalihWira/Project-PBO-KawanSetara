package com.example.projectpbokawansetara.model;

public class Lowongan {
    private int idLowongan;
    private Perusahaan perusahaan;
    private String namaPekerjaan;
    private String deskripsi;
    private String lokasi;
    private double gaji;
    private String jenisLowongan;
    private String status;

    public Lowongan(int idLowongan, Perusahaan perusahaan, String namaPekerjaan, String deskripsi, String lokasi, double gaji, String status) {
        this(idLowongan, perusahaan, namaPekerjaan, deskripsi, lokasi, gaji, null, status);
    }

    public Lowongan(int idLowongan, Perusahaan perusahaan, String namaPekerjaan, String deskripsi, String lokasi, double gaji, String jenisLowongan, String status) {
        this.idLowongan = idLowongan;
        this.perusahaan = perusahaan;
        this.namaPekerjaan = namaPekerjaan;
        this.deskripsi = deskripsi;
        this.lokasi = lokasi;
        this.gaji = gaji;
        this.jenisLowongan = jenisLowongan;
        this.status = status;
    }

    public int getIdLowongan() {
        return idLowongan;
    }

    public void setIdLowongan(int idLowongan) {
        this.idLowongan = idLowongan;
    }

    public Perusahaan getPerusahaan() {
        return perusahaan;
    }

    public void setPerusahaan(Perusahaan perusahaan) {
        this.perusahaan = perusahaan;
    }

    public String getNamaPekerjaan() {
        return namaPekerjaan;
    }

    public void setNamaPekerjaan(String namaPekerjaan) {
        this.namaPekerjaan = namaPekerjaan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public double getGaji() {
        return gaji;
    }

    public void setGaji(double gaji) {
        this.gaji = gaji;
    }

    public String getJenisLowongan() {
        return jenisLowongan;
    }

    public void setJenisLowongan(String jenisLowongan) {
        this.jenisLowongan = jenisLowongan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return namaPekerjaan;
    }
}