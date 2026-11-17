package com.example.projectpbokawansetara.model;

public class Keahlian {
    private int idKeahlian;
    private String namaKeahlian;

    public Keahlian() {
    }

    public Keahlian(int idKeahlian, String namaKeahlian) {
        this.idKeahlian = idKeahlian;
        this.namaKeahlian = namaKeahlian;
    }

    public int getIdKeahlian() {
        return idKeahlian;
    }

    public void setIdKeahlian(int idKeahlian) {
        this.idKeahlian = idKeahlian;
    }

    public String getNamaKeahlian() {
        return namaKeahlian;
    }

    public void setNamaKeahlian(String namaKeahlian) {
        this.namaKeahlian = namaKeahlian;
    }

    @Override
    public String toString() {
        return namaKeahlian;
    }
}
