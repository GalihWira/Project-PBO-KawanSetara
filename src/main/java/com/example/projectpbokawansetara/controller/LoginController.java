package com.example.projectpbokawansetara.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent; 
import javafx.scene.Node;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Label emailErrorLabel;

    @FXML
    private TextField emailField;

    @FXML
    private Button facebookButton;

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private Button googleButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private CheckBox rememberCheckBox;

    @FXML
    private ImageView togglePasswordIcon;

    private boolean passwordTerlihat = false;
    private String passwordAsli = "";

    public void initialize (){
        sembunyikanPesan (emailErrorLabel);
        sembunyikanPesan (passwordErrorLabel);
        sembunyikanPesan (loginMessageLabel);
    }

    @FXML 
    private void handleLogin(ActionEvent event){
        sembunyikanPesan (emailErrorLabel);
        sembunyikanPesan (passwordErrorLabel);
        sembunyikanPesan (loginMessageLabel);

        String email = emailField.getText();
        String password = passwordField.getText();

        if(passwordTerlihat){
            password = passwordAsli;
        } 

        if(email.isEmpty()){
            tampilkanPesan (emailErrorLabel, "Email Wajib Diisi !", "#F04438");

            return;
        }

        if(password.isEmpty()){
            tampilkanPesan (passwordErrorLabel, "Kata Sandi Wajib Diisi!", "#F04438");

            return;
        }

        if(password.length() < 6){
            tampilkanPesan (passwordErrorLabel, "Kata Sandi Minimal 6 Karakter!", "#F04438");

            return;
        }

        tampilkanPesan (loginMessageLabel, "Login Sudah Berhasil Dilakukan!", "#12B76A");

        pindahHalaman(event,"/com/example/projectpbokawansetara/view/DashboardPelamarView.fxml");
    }

    @FXML
    private void handleTogglePassword(MouseEvent event){
        if(passwordTerlihat == false){
            passwordAsli = passwordField.getText();
            passwordField.clear();
            passwordField.setPromptText(passwordAsli);
            passwordField.setEditable(false);
            passwordTerlihat = true;
        } else {
            passwordField.setText(passwordAsli);
            passwordField.setPromptText("Masukkan Kata Sandi Anda ...");
            passwordField.setEditable(true);
            passwordTerlihat = false;
        }
    }

    @FXML 
    private void handleForgotPassword (ActionEvent event){
        tampilkanPesan(loginMessageLabel, "Maaf, Lupa Kata Sandi Saat Ini Belum Tersedia ...", "#475467");
    }

    @FXML
    private void handleRegister(ActionEvent event){
        pindahHalaman (event, "/com/example/projectpbokawansetara/view/RegisterView.fxml");
    }

    @FXML 
    private void handleGoogleLogin(ActionEvent event){
        tampilkanPesan(loginMessageLabel, "Maaf, Login Google Saat Ini Belum Tersedia ...", "#475467");        
    }

    @FXML 
    private void handleFacebookLogin(ActionEvent event){
        tampilkanPesan(loginMessageLabel, "Maaf, Login Facebook Saat Ini Belum Tersedia ...", "#475467");        
    }

    private void tampilkanPesan (Label label, String pesan, String warna){
        label.setText(pesan);
        label.setStyle ("-fx-text-fill: " + warna + ";");
        label.setPrefHeight(-1);
        label.setVisible(true);
    }

    private void sembunyikanPesan(Label label){
        label.setText("");
        label.setPrefHeight(0);
        label.setVisible(false);
    }

    private void pindahHalaman (ActionEvent event, String lokasiFxml){
        try {
            FXMLLoader loader = new
            FXMLLoader(getClass().getResource(lokasiFxml));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 800));
            stage.setTitle("Beranda - KawanSetara");
            stage.show();
        } catch (IOException | NullPointerException e){
            tampilkanPesan(loginMessageLabel, "Maaf, Anda Gagal Membuka Halaman", "#F04438");
            e.printStackTrace();
        }
    }
}
