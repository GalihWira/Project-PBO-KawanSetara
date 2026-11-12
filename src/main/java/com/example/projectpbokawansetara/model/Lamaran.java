package com.example.projectpbokawansetara.model;

import java.time.LocalDate;

public class Lamaran {
    private int idLamaran;
    private Pelamar pelamar;
    private Lowongan lowongan;
    private LocalDate tanggalLamar;
    private String status;

    public Lamaran(int idLamaran, Pelamar pelamar, Lowongan lowongan, LocalDate tanggalLamar, String status){
        this.idLamaran = idLamaran;
        this.pelamar = pelamar;
        this.lowongan = lowongan;
        this.tanggalLamar = tanggalLamar;
        this.status = status;
    }
    public int getIdLamaran(){return idLamaran;}
    public void setIdLamaran(int idLamaran){this.idLamaran = idLamaran;}

    public Pelamar getPelamar(){return pelamar;}
    public void setPelamar(Pelamar pelamar){this.pelamar = pelamar;}

    public Lowongan getLowongan(){return lowongan;}
    public void setLowongan(Lowongan lowongan){this.lowongan = lowongan;}

    public LocalDate getTanggalLamar(){return tanggalLamar;}
    public void setTanggalLamar(LocalDate tanggalLamar){this.tanggalLamar = tanggalLamar;}

    public String getStatus(){return status;}
    public void setStatus(String status){this.status = status;}
}
