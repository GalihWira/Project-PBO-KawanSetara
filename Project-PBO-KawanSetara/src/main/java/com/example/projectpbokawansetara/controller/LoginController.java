package com.example.projectpbokawansetara.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.projectpbokawansetara.model.KoneksiDB;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController{
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;
    @FXML
    private Label usernameErrorLabel;
    @FXML
    private Label passwordErrorLabel;
    @FXML
    public void handleLoginButton(ActionEvent event) {
        String user = nameField.getText();
        String password = passwordField.getText();

        usernameErrorLabel.setText("");
        passwordErrorLabel.setText("");
        statusLabel.setText("");

        boolean isValid = true;

        if(user.isEmpty()){
            usernameErrorLabel.setText("Username tidak boleh kosong");
            usernameErrorLabel.setStyle("-fx-text-fill: red");
            isValid = false;
        }
        if(password.isEmpty()){
            passwordErrorLabel.setText("Password tidak boleh kosong");
            passwordErrorLabel.setStyle("-fx-text-fill: red");
            isValid = false;
        }
        if(isValid){
            statusLabel.setText("Login berhasil");
            statusLabel.setStyle("-fx-text-fill: green");
        }
        System.out.println("Email: " + user);
        System.out.println("Password: " + password);
    }

    private String cekLogin(String email, String password) throws SQLException{
        String sqlPelamar = "SELECT id_pelamar FROM Pelamar WHERE email = ? AND password = ?";
        try (Connection conn = KoneksiDB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sqlPelamar)){
            stmt.setString(1, email);
            stmt.setString(2, password);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return "Pelamar";
                }
            }
        } 
        String sqlAdmin = "SELECT id_admin FROM Admin WHERE email = ? AND password = ?";
        try (Connection conn = KoneksiDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlAdmin)){
            stmt.setString(1, email);
            stmt.setString(2, password);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return "Admin";
                }
            }
        }
        return null;
    }
}