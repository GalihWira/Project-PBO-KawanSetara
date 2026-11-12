package com.example.projectpbokawansetara.model;

public class JenisDisabilitas {
    private int idJenisDisabilitas;
    private String namaJenis;

    public JenisDisabilitas(int idJenisDisabilitas, String namaJenis){
        this.idJenisDisabilitas = idJenisDisabilitas;
        this.namaJenis = namaJenis;
    }
    public int getIdJenisDisabilitas(){return idJenisDisabilitas;}
    public void setIdJenisDisabilitas(int idJenisDisabilitas){this.idJenisDisabilitas = idJenisDisabilitas;}

    public String getNamaJenis(){return namaJenis;}
    public void setNamaJenis(String namaJenis){this.namaJenis = namaJenis;}

    @Override
    public String toString(){
        return namaJenis;
    }
}
