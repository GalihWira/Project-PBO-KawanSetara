package com.example.projectpbokawansetara.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;

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
}