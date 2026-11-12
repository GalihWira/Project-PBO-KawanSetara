package com.example.projectpbokawansetara.model;
public class Admin extends User{
    public Admin(int idPengguna, String nama, String email, String password){
        super(idPengguna, nama, email, password);
    }
    @Override
    public String tampilkanDashboard(){
        return "DashboardAdminView.fxml";
    }
    public String kelolaLowongan(){
        return "KelolaLowonganView.fxml";
    }
    public String kelolaKeahlian(){
        return "KelolaChecklistView.fxml";
    }
}
