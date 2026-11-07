package com.example.projectpbokawansetara.model;

public abstract class User {
    private int idPengguna;
    private String nama;
    private String email;
    private String password;

    public User(int idPengguna, String nama, String email, String password){
        this.idPengguna = idPengguna;
        this.nama = nama;
        this.email = email;
        this.password = password;
    }
    public boolean login(String email, String password){
        return this.email.equalsIgnoreCase(email) && password.equals(password);
    }
    public abstract String tampilkanDashboard();

    public int getIdPenggunaa(){return idPengguna;}
    public void setIdPengguna(int idPengguna){this.idPengguna = idPengguna;}

    public String getNama(){return nama;}
    public void setNama(String nama){this.nama = nama;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}
}

