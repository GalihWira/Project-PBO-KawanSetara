package com.example.projectpbokawansetara.model;

public class ItemChecklist {
    private int idItem;
    private String namaItem;

    public ItemChecklist(int idItem, String namaItem){
        this.idItem = idItem;
        this.namaItem = namaItem;
    }
    public int getIdItem(){return idItem;}
    public void setIdItem(int idItem){this.idItem = idItem;}

    public String getNamaItem(){return namaItem;}
    public void setNamaItem(String namaItem){this.namaItem = namaItem;}

    @Override
    public String toString(){
        return namaItem;
    }
}
