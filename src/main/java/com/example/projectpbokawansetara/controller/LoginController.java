package com.example.projectpbokawansetara.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.example.projectpbokawansetara.dao.UserDAO;
import com.example.projectpbokawansetara.model.Session;
import com.example.projectpbokawansetara.model.User;
import java.util.prefs.Preferences;
import javafx.scene.control.CheckBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import javafx.stage.Stage;
public class LoginController {
    private final UserDAO userDAO = new UserDAO();

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
    private final Preferences preferences = Preferences.userNodeForPackage(LoginController.class);
    private static final String KEY_EMAIL = "email_login";

    @FXML
    private ImageView togglePasswordIcon;

    private boolean passwordTerlihat = false;

    public void initialize (){
        sembunyikanPesan (emailErrorLabel);
        sembunyikanPesan (passwordErrorLabel);
        sembunyikanPesan (loginMessageLabel);
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
        sembunyikanPesan(emailErrorLabel);
        sembunyikanPesan(passwordErrorLabel);
        sembunyikanPesan(loginMessageLabel);

        String email = emailField.getText().trim();
        String password = passwordTerlihat
            ? passwordVisibleField.getText()
            : passwordField.getText();

        if (email.isEmpty()) {
            tampilkanPesan(emailErrorLabel, "Email Wajib Diisi!", 	"pesan-error");
            return;
        }

        if (!emailValid(email)) {
            tampilkanPesan(emailErrorLabel, "Domain email tidak didukung!", "pesan-error");
            return;
        }

        if (password.isEmpty()) {
            tampilkanPesan(passwordErrorLabel, "Kata Sandi Wajib Diisi!", "pesan-error");            return;
        }

        if (password.length() < 6) {
            tampilkanPesan(passwordErrorLabel, "Kata Sandi Minimal 6 Karakter!", 	"pesan-error");
            return;
        }

        try {
            User user = userDAO.cekLogin(email, password);

            if (user == null) {
                tampilkanPesan(
                    loginMessageLabel,
                    "Email atau kata sandi salah!",
                    	"pesan-error"
                );
                return;
            }

            Session.login(user);
            if (rememberCheckBox.isSelected()) {
                preferences.put(KEY_EMAIL, email);
            } else {
                preferences.remove(KEY_EMAIL);
            }

            String lokasiFxml =
                "/com/example/projectpbokawansetara/view/"
                + user.tampilkanDashboard();

            pindahHalaman(event, lokasiFxml);

        } catch (SQLException e) {
            tampilkanPesan(
                loginMessageLabel,
                "Terjadi kesalahan saat menghubungkan database!",
                	"pesan-error"
            );
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTogglePassword(MouseEvent event){
        if(!passwordTerlihat){
            passwordVisibleField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordVisibleField.setVisible(true);
            passwordVisibleField.setManaged(true);
            passwordTerlihat = true;
        } else {
            passwordField.setText(passwordVisibleField.getText());
            passwordVisibleField.setVisible(false);
            passwordVisibleField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordTerlihat = false;
        }
    }

    @FXML 
    private void handleForgotPassword (ActionEvent event){
        tampilkanPesan(loginMessageLabel, "Maaf, Lupa Kata Sandi Saat Ini Belum Tersedia ...", 	"pesan-error");
    }

    @FXML
    private void handleRegister(ActionEvent event){
        pindahHalaman (event, "/com/example/projectpbokawansetara/view/RegisterView.fxml");
    }

    @FXML 
    private void handleGoogleLogin(ActionEvent event){
        tampilkanPesan(loginMessageLabel, "Maaf, Login Google Saat Ini Belum Tersedia ...", 	"pesan-error");        
    }

    @FXML 
    private void handleFacebookLogin(ActionEvent event){
        tampilkanPesan(loginMessageLabel, "Maaf, Login Facebook Saat Ini Belum Tersedia ...", 	"pesan-error");        
    }

    private boolean emailValid(String email) {
        String[] domainDiizinkan = {
            "@gmail.com",
            "@yahoo.com",
            "@yahoo.co.id",
            "@outlook.com",
            "@hotmail.com",
            "@icloud.com",
            "@kawansetara.com" // untuk akun admin
        };

        String emailLower = email.toLowerCase();

        for (String domain : domainDiizinkan) {
            if (emailLower.endsWith(domain)) {
                return true;
            }
        }

        return false;
    }

    private void tampilkanPesan(Label label, String pesan, String styleClass) {
        label.setText(pesan);
        label.getStyleClass().removeAll(
            "pesan-error",
            "pesan-info"
        );
        label.getStyleClass().add(styleClass);
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
            stage.setScene(new Scene(root));
            stage.setTitle("Beranda - KawanSetara");
            stage.sizeToScene();
            stage.centerOnScreen(); 
            stage.show();
        } catch (IOException | NullPointerException e){
            tampilkanPesan(loginMessageLabel, "Maaf, Anda Gagal Membuka Halaman", "pesan-error");
            e.printStackTrace();
        }
    }
}

