package com.example.projectpbokawansetara.model;

public class Perusahaan {
    private int idPerusahaan;
    private String namaPerusahaan;
    private String alamat;
    private String email;
    private String noTelepon;
    private String deskripsi;

    public Perusahaan(int idPerusahaan, String namaPerusahaan, String alamat, String email, String noTelepon, String deskripsi){
        this.idPerusahaan = idPerusahaan;
        this.namaPerusahaan = namaPerusahaan;
        this.alamat = alamat;
        this.email = email;
        this.noTelepon = noTelepon;
        this.deskripsi = deskripsi;
    }
    public int getIdPerusahaan(){return idPerusahaan;}
    public void setIdPerusahaan(int idPerusahaan){this.idPerusahaan = idPerusahaan;}

    public String getNamaPerusahaan(){return namaPerusahaan;}
    public void setNamaPerusahaan(String namaPerusahaan){this.namaPerusahaan = namaPerusahaan;}

    public String getAlamat(){return alamat;}
    public void setAlamat(String alamat){this.alamat = alamat;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    public String getNoTelepon(){return noTelepon;}
    public void setNoTelepon(String noTelepon){this.noTelepon = noTelepon;}

    public String getDeskripsi(){return deskripsi;}
    public void setDeskripsi(String deskripsi){this.deskripsi = deskripsi;}

    @Override
    public String toString(){
        return namaPerusahaan;
    }
}