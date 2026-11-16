package com.example.projectpbokawansetara.controller;

import java.sql.SQLException;
import java.util.prefs.Preferences;

import com.example.projectpbokawansetara.dao.UserDAO;
import com.example.projectpbokawansetara.exception.ValidationException;
import com.example.projectpbokawansetara.helper.SceneManager;
import com.example.projectpbokawansetara.model.Session;
import com.example.projectpbokawansetara.model.User;
import com.example.projectpbokawansetara.service.AuthService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class LoginController {

    private static final String KEY_EMAIL = "email_login";

    private final UserDAO userDAO = new UserDAO();
    private final AuthService authService = new AuthService();
    private final Preferences preferences = Preferences.userNodeForPackage(LoginController.class);

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
    private TextField passwordVisibleField;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private CheckBox rememberCheckBox;

    @FXML
    private ImageView togglePasswordIcon;

    private boolean passwordTerlihat = false;

    @FXML
    public void initialize() {
        sembunyikanSemuaPesan();

        passwordVisibleField.setVisible(false);
        passwordVisibleField.setManaged(false);

        emailField.setOnAction(event -> passwordField.requestFocus());
        passwordField.setOnAction(this::handleLogin);
        passwordVisibleField.setOnAction(this::handleLogin);

        String emailTersimpan = preferences.get(KEY_EMAIL, "");
        emailField.setText(emailTersimpan);
        rememberCheckBox.setSelected(!emailTersimpan.isEmpty());
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        sembunyikanSemuaPesan();

        String email = emailField.getText().trim().toLowerCase();
        String password = passwordTerlihat
                ? passwordVisibleField.getText()
                : passwordField.getText();

        try {
            authService.validasiLogin(email, password);
        } catch (ValidationException e) {
            if (e.getNamaField().equals("email")) {
                tampilkanPesan(emailErrorLabel, e.getMessage());
            } else {
                tampilkanPesan(passwordErrorLabel, e.getMessage());
            }
            return;
        }

        try {
            User user = userDAO.cekLogin(email, password);

            if (user == null) {
                tampilkanPesan(loginMessageLabel, "Email atau kata sandi salah!");
                return;
            }

            Session.login(user);
            simpanEmailJikaDiingat(email);
            SceneManager.pindah(user.tampilkanDashboard(), "Beranda - KawanSetara");

        } catch (SQLException e) {
            tampilkanPesan(loginMessageLabel, "Terjadi kesalahan saat menghubungkan database!");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTogglePassword(MouseEvent event) {
        if (!passwordTerlihat) {
            passwordVisibleField.setText(passwordField.getText());
            tukarField(passwordField, passwordVisibleField);
        } else {
            passwordField.setText(passwordVisibleField.getText());
            tukarField(passwordVisibleField, passwordField);
        }
        passwordTerlihat = !passwordTerlihat;
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        tampilkanPesan(loginMessageLabel, "Maaf, fitur lupa kata sandi belum tersedia.");
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        SceneManager.pindah("RegisterView.fxml", "Daftar - KawanSetara");
    }

    @FXML
    private void handleGoogleLogin(ActionEvent event) {
        tampilkanPesan(loginMessageLabel, "Maaf, login Google belum tersedia.");
    }

    @FXML
    private void handleFacebookLogin(ActionEvent event) {
        tampilkanPesan(loginMessageLabel, "Maaf, login Facebook belum tersedia.");
    }

    private void simpanEmailJikaDiingat(String email) {
        if (rememberCheckBox.isSelected()) {
            preferences.put(KEY_EMAIL, email);
        } else {
            preferences.remove(KEY_EMAIL);
        }
    }

    private void tukarField(TextField disembunyikan, TextField ditampilkan) {
        disembunyikan.setVisible(false);
        disembunyikan.setManaged(false);
        ditampilkan.setVisible(true);
        ditampilkan.setManaged(true);
    }

    private void tampilkanPesan(Label label, String pesan) {
        label.setText(pesan);
        label.getStyleClass().remove("pesan-error");
        label.getStyleClass().add("pesan-error");
        label.setPrefHeight(-1);
        label.setVisible(true);
    }

    private void sembunyikanPesan(Label label) {
        label.setText("");
        label.setPrefHeight(0);
        label.setVisible(false);
    }

    private void sembunyikanSemuaPesan() {
        sembunyikanPesan(emailErrorLabel);
        sembunyikanPesan(passwordErrorLabel);
        sembunyikanPesan(loginMessageLabel);
    }
}