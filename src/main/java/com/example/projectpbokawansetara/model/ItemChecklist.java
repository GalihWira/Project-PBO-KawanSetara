package com.example.projectpbokawansetara.model;

public class ItemChecklist {
    private int idItem;
    private String namaItem;
    private int idKategori;
    private String namaKategori;
    private int jumlahDiLowongan;
    private int jumlahDipilihPelamar;

    public ItemChecklist(int idItem, String namaItem){
        this.idItem = idItem;
        this.namaItem = namaItem;
    }

    public ItemChecklist(int idItem, String namaItem, int idKategori, String namaKategori, int jumlahDiLowongan, int jumlahDipilihPelamar) {
        this.idItem = idItem;
        this.namaItem = namaItem;
        this.idKategori = idKategori;
        this.namaKategori = namaKategori;
        this.jumlahDiLowongan = jumlahDiLowongan;
        this.jumlahDipilihPelamar = jumlahDipilihPelamar;
    }

    public int getIdItem(){return idItem;}
    public void setIdItem(int idItem){this.idItem = idItem;}

    public String getNamaItem(){return namaItem;}
    public void setNamaItem(String namaItem){this.namaItem = namaItem;}

    public int getIdKategori(){return idKategori;}
    public void setIdKategori(int idKategori){this.idKategori = idKategori;}

    public String getNamaKategori() { return namaKategori; }
    public void setNamaKategori(String namaKategori) { this.namaKategori = namaKategori; }

    public int getJumlahDiLowongan() { return jumlahDiLowongan; }
    public void setJumlahDiLowongan(int jumlahDiLowongan) { this.jumlahDiLowongan = jumlahDiLowongan; }

    public int getJumlahDipilihPelamar() { return jumlahDipilihPelamar; }
    public void setJumlahDipilihPelamar(int jumlahDipilihPelamar) { this.jumlahDipilihPelamar = jumlahDipilihPelamar; }
    
    public ItemChecklist() {
    }
    @Override
    public String toString(){
        return namaItem;
    }
}
