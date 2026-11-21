package com.example.projectpbokawansetara.model;

public class JenisDisabilitas {
    private int idJenisDisabilitas;
    private String namaJenis;
    private int jumlahPelamar;

    public JenisDisabilitas(int idJenisDisabilitas, String namaJenis){
        this.idJenisDisabilitas = idJenisDisabilitas;
        this.namaJenis = namaJenis;
    }

    public JenisDisabilitas(int idJenisDisabilitas, String namaJenis, int jumlahPelamar) {
        this.idJenisDisabilitas = idJenisDisabilitas;
        this.namaJenis = namaJenis;
        this.jumlahPelamar = jumlahPelamar;
    }

    public int getIdJenisDisabilitas(){return idJenisDisabilitas;}
    public void setIdJenisDisabilitas(int idJenisDisabilitas){this.idJenisDisabilitas = idJenisDisabilitas;}

    public String getNamaJenis(){return namaJenis;}
    public void setNamaJenis(String namaJenis){this.namaJenis = namaJenis;}

    public int getJumlahPelamar() {return jumlahPelamar;}
    public void setJumlahPelamar(int jumlahPelamar) {this.jumlahPelamar = jumlahPelamar;}
    
    @Override
    public String toString(){
        return namaJenis;
    }
}
