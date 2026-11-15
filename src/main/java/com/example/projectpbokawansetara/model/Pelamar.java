package com.example.projectpbokawansetara.model;

import java.time.LocalDate;

public class Pelamar extends User {
    private String noTelepon;
    private LocalDate tanggalLahir;
    private String pendidikan;
    private String jenisDisabilitas;
    private String ijazah;
    private String suratKeteranganDisabilitas;
    private String cv;

    public Pelamar(int idPengguna, String nama, String email, String password, String noTelepon,LocalDate tanggalLahir, String pendidikan, String jenisDisabilitas, String ijazah, String suratKeteranganDisabilitas, String cv){
        super(idPengguna, nama, email, password);
        this.noTelepon = noTelepon;
        this.tanggalLahir = tanggalLahir;
        this.pendidikan = pendidikan;
        this.jenisDisabilitas = jenisDisabilitas;
        this.ijazah = ijazah;
        this.suratKeteranganDisabilitas = suratKeteranganDisabilitas;
        this.cv = cv;
    }
    @Override
    public String tampilkanDashboard(){
        return "DashboardPelamarView.fxml";
    }
    public String getNoTelepon(){return noTelepon;}
    public void setNoTelepon(String noTelepon){this.noTelepon = noTelepon;}

    public LocalDate getTanggalLahir(){return tanggalLahir;}
    public void setTanggalLahir(LocalDate tanggalLahir){this.tanggalLahir = tanggalLahir;}

    public String getPendidikan(){return pendidikan;}
    public void setPendidikan(String pendidikan){this.pendidikan = pendidikan;}

    public String getJenisDisabilitas(){return jenisDisabilitas;}
    public void setJenisDisabilitas(String jenisDisabilitas){this.jenisDisabilitas = jenisDisabilitas;}

    public String getIjazah(){return ijazah;}
    public void setIjazah(String ijazah){this.ijazah = ijazah;}

    public String getSuratKeteranganDisabilitas(){return suratKeteranganDisabilitas;}
    public void setSuratKeteranganDisabilitas(String suratKeteranganDisabilitas){this.suratKeteranganDisabilitas = suratKeteranganDisabilitas;}

    public String getCv(){return cv;}
    public void setCv(String cv){this.cv = cv;}
}
