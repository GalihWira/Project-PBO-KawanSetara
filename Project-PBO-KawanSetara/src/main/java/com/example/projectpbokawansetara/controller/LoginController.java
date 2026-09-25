package com.example.projectpbokawansetara.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class LoginController{
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;
    @FXML
    public void handleLoginButton(ActionEvent event) {
        String user = emailField.getText();
        String password = passwordField.getText();

        if(user.isEmpty() || password.isEmpty()){
            statusLabel.setText("Email atau Password harus diisi");
            statusLabel.setStyle("-fx-text-fill: red");
        }else{
            statusLabel.setText("Login Berhasil");
            statusLabel.setStyle("-fx-text-fill: green");
        }
        System.out.println("Email: " + user);
        System.out.println("Password: " + password);
    }
}