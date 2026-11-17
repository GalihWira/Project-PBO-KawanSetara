package com.example.projectpbokawansetara.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Lamaran {
    private int idLamaran;
    private Pelamar pelamar;
    private Lowongan lowongan;
    private LocalDate tanggalLamar;
    private String status;
    private LocalDateTime tanggalDiperbarui;

    public Lamaran(
            int idLamaran,
            Pelamar pelamar,
            Lowongan lowongan,
            LocalDate tanggalLamar,
            String status
    ) {
        this(idLamaran, pelamar, lowongan, tanggalLamar, status, null);
    }

    public Lamaran(
            int idLamaran,
            Pelamar pelamar,
            Lowongan lowongan,
            LocalDate tanggalLamar,
            String status,
            LocalDateTime tanggalDiperbarui
    ) {
        this.idLamaran = idLamaran;
        this.pelamar = pelamar;
        this.lowongan = lowongan;
        this.tanggalLamar = tanggalLamar;
        this.status = status;
        this.tanggalDiperbarui = tanggalDiperbarui;
    }

    public int getIdLamaran() {
        return idLamaran;
    }

    public void setIdLamaran(int idLamaran) {
        this.idLamaran = idLamaran;
    }

    public Pelamar getPelamar() {
        return pelamar;
    }

    public void setPelamar(Pelamar pelamar) {
        this.pelamar = pelamar;
    }

    public Lowongan getLowongan() {
        return lowongan;
    }

    public void setLowongan(Lowongan lowongan) {
        this.lowongan = lowongan;
    }

    public LocalDate getTanggalLamar() {
        return tanggalLamar;
    }

    public void setTanggalLamar(LocalDate tanggalLamar) {
        this.tanggalLamar = tanggalLamar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTanggalDiperbarui() {
        return tanggalDiperbarui;
    }

    public void setTanggalDiperbarui(LocalDateTime tanggalDiperbarui) {
        this.tanggalDiperbarui = tanggalDiperbarui;
    }
}
