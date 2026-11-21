package com.example.projectpbokawansetara.model;

public class DashboardStatistik {
    private int lowonganTersedia;
    private int lamaranMasuk;
    private int perusahaanMitra;
    private int pelamarTerdaftar;
    private int totalLowongan;
    private int totalChecklist;
    private int totalDisabilitas;
    private int totalAdmin;
    private int lowonganDitutup;
    private int lamaranBelumDitinjau;
    private int perusahaanTanpaLowongan;
    private int pelamarAktifMelamar;

    public DashboardStatistik() {}

    public int getLowonganTersedia() { return lowonganTersedia; }
    public void setLowonganTersedia(int lowonganTersedia) { this.lowonganTersedia = lowonganTersedia; }

    public int getLamaranMasuk() { return lamaranMasuk; }
    public void setLamaranMasuk(int lamaranMasuk) { this.lamaranMasuk = lamaranMasuk; }

    public int getPerusahaanMitra() { return perusahaanMitra; }
    public void setPerusahaanMitra(int perusahaanMitra) { this.perusahaanMitra = perusahaanMitra; }

    public int getPelamarTerdaftar() { return pelamarTerdaftar; }
    public void setPelamarTerdaftar(int pelamarTerdaftar) { this.pelamarTerdaftar = pelamarTerdaftar; }

    public int getTotalLowongan() { return totalLowongan; }
    public void setTotalLowongan(int totalLowongan) { this.totalLowongan = totalLowongan; }

    public int getTotalChecklist() { return totalChecklist; }
    public void setTotalChecklist(int totalChecklist) { this.totalChecklist = totalChecklist; }

    public int getTotalDisabilitas() { return totalDisabilitas; }
    public void setTotalDisabilitas(int totalDisabilitas) { this.totalDisabilitas = totalDisabilitas; }

    public int getTotalAdmin() { return totalAdmin; }
    public void setTotalAdmin(int totalAdmin) { this.totalAdmin = totalAdmin; }

    public int getLowonganDitutup() { return lowonganDitutup; }
    public void setLowonganDitutup(int lowonganDitutup) { this.lowonganDitutup = lowonganDitutup; }

    public int getLamaranBelumDitinjau() { return lamaranBelumDitinjau; }
    public void setLamaranBelumDitinjau(int lamaranBelumDitinjau) { this.lamaranBelumDitinjau = lamaranBelumDitinjau; }

    public int getPerusahaanTanpaLowongan() { return perusahaanTanpaLowongan; }
    public void setPerusahaanTanpaLowongan(int perusahaanTanpaLowongan) { this.perusahaanTanpaLowongan = perusahaanTanpaLowongan; }

    public int getPelamarAktifMelamar() { return pelamarAktifMelamar; }
    public void setPelamarAktifMelamar(int pelamarAktifMelamar) { this.pelamarAktifMelamar = pelamarAktifMelamar; }
}