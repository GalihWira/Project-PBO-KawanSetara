package com.example.projectpbokawansetara.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class KelolaAkunAdminController {

    @FXML
    private TableView<Admin> adminTable;

    @FXML
    private TextField cariField;

    @FXML
    private TableColumn<Admin, String> emailColumn;

    @FXML
    private TableColumn<Admin, String> namaColumn;

    @FXML
    private TableColumn<Admin, String> passwordColumn;

    private ObservableList <Admin> semuaAdmin=FXCollections.observableArrayList();

    @FXML
    private void initialize (){
        namaColumn.setCellValueFactory(new PropertyValueFactory<Admin, String>("nama"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Admin, String>("email"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<Admin, String>("password"));

        semuaAdmin.add(new Admin("Made Satya Kinanta", "madeSatya@gmail.com", "kinanta123456"));
        semuaAdmin.add(new Admin("I Gusti Ngurah Agung Yuda Darma", "ngurahAgung@gmail.com", "Agung123456"));
        semuaAdmin.add(new Admin("I Wayan Galih Wira Setiawan", "galihWira@gmail.com", "galih123456"));
        semuaAdmin.add(new Admin("Ida Ayu Intan Andini Putri", "intanAndini@gmail.com", "intan123456"));

        adminTable.setItems (semuaAdmin);
    }

    @FXML 
    private void handleCari (KeyEvent event){
        String kataKunci = cariField.getText().toLowerCase();
        ObservableList<Admin> hasilCari = FXCollections.observableArrayList();

        for (Admin admin : semuaAdmin){
            if (admin.getNama().toLowerCase().contains(kataKunci)){
                hasilCari.add(admin);
            }
        }

        adminTable.setItems(hasilCari);
    }

    public static class Admin {
        private String nama;
        private String email;
        private String password;

        public Admin (String nama, String email, String password){
            this.nama = nama;
            this.email = email;
            this.password = password;
        }

        public String getNama(){
            return nama;
        }

        public String getEmail(){
            return email;
        }

        public String getPassword(){
            return password;
        }
    }
}
