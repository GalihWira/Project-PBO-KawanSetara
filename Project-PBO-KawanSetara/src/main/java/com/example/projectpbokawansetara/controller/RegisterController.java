package com.example.projectpbokawansetara.controller;

import java.io.File;
import java.io.IOException;

import java.time.LocalDate;
import java.time.Period;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node; 

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private CheckBox agreeCheckBox;

    @FXML
    private Label agreeErrorLabel;

    @FXML
    private Label confirmPasswordErrorLabel;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField confirmPasswordVisibleField;

    @FXML
    private Button cvButton;

    @FXML
    private Label cvErrorLabel;

    @FXML
    private Label cvFileLabel;

    @FXML
    private HBox cvRow;

    @FXML
    private Label cvSizeLabel;

    @FXML
    private ComboBox<String> disabilitasComboBox;

    @FXML
    private Label disabilitasErrorLabel;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private TextField emailField;

    @FXML
    private Button facebookButton;

    @FXML
    private Button googleButton;

    @FXML
    private Button ijazahButton;

    @FXML
    private Label ijazahErrorLabel;

    @FXML
    private Label ijazahFileLabel;

    @FXML
    private HBox ijazahRow;

    @FXML
    private Label ijazahSizeLabel;

    @FXML
    private Hyperlink loginLink;

    @FXML
    private Label namaErrorLabel;

    @FXML
    private TextField namaField;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisibleField;

    @FXML
    private ComboBox<String> pendidikanComboBox ;

    @FXML
    private Label pendidikanErrorLabel;

    @FXML
    private Hyperlink privacyLink;

    @FXML
    private Button registerButton;

    @FXML
    private Button skdButton;

    @FXML
    private Label skdErrorLabel;

    @FXML
    private Label skdFileLabel;

    @FXML
    private HBox skdRow;

    @FXML
    private Label skdSizeLabel;

    @FXML
    private HBox step1Alert;

    @FXML
    private Label step1Circle;

    @FXML
    private Button step1NextButton;

    @FXML
    private VBox step1Pane;

    @FXML
    private Label step1Title;

    @FXML
    private HBox step2Alert;

    @FXML
    private Button step2BackButton;

    @FXML
    private Label step2Circle;

    @FXML
    private Button step2NextButton;

    @FXML
    private VBox step2Pane;

    @FXML
    private Label step2Title;

    @FXML
    private HBox step3Alert;

    @FXML
    private Button step3BackButton;

    @FXML
    private Label step3Circle;

    @FXML
    private VBox step3Pane;

    @FXML
    private Label step3Title;

    @FXML
    private Label tanggalLahirErrorLabel;

    @FXML
    private DatePicker tanggalLahirPicker;

    @FXML
    private Label teleponErrorLabel;

    @FXML
    private TextField teleponField;

    @FXML
    private Hyperlink termsLink;

    @FXML
    private ImageView toggleConfirmPasswordIcon;

    @FXML
    private ImageView togglePasswordIcon;

    @FXML
    private TextField usiaField;

    @FXML
    void handleBukaPrivasi(ActionEvent event) {

    }

    @FXML
    void handleBukaSyarat(ActionEvent event) {

    }

    @FXML
    void handleDaftar(ActionEvent event) {

    }

    @FXML
    void handleHitungUsia(ActionEvent event) {

    }

    @FXML
    void handleKembaliStep1(ActionEvent event) {

    }

    @FXML
    void handleKembaliStep2(ActionEvent event) {

    }

    @FXML
    void handleLanjutStep1(ActionEvent event) {

    }

    @FXML
    void handleLanjutStep2(ActionEvent event) {

    }

    @FXML
    void handleLoginSosial(ActionEvent event) {

    }

    @FXML
    void handleMasuk(ActionEvent event) {

    }

    @FXML
    void handlePilihCv(ActionEvent event) {

    }

    @FXML
    void handlePilihIjazah(ActionEvent event) {

    }

    @FXML
    void handlePilihSkd(ActionEvent event) {

    }

    @FXML
    void handleToggleConfirmPassword(MouseEvent event) {

    }

    @FXML
    void handleTogglePassword(MouseEvent event) {

    }

    private File fileCv = null;
    private File fileIjazah = null;
    private File fileSkd = null;

    @FXML 
    public void initialize(){
        pendidikanComboBox.getItems().addAll("SD", "SMP", "SMA", "Sarjana", "Magister", "Lainnya");

        disabilitasComboBox.getItems().addAll("Fisik", "Tuna Netra", "Tuna Rungu", "Lainnya");

        sembunyikanSemuaError();
        tampilkanLangkah(1);
    }

    void handleBukaPrivasi(ActionEvent event){
        System.out.println("Membuka Privasi!");
    }

    void handleDaftar(ActionEvent event){
        sembunyikanSemuaError();
        boolean valid = true;

        if(disabilitasComboBox.getValue () == null){
            tampilkanPesan (disabilitasErrorLabel, "Wajib Memilih Jenis Disabilitas!", "#F04438");
            valid = false;
        }

        if(fileCv.getValue () == null){
            tampilkanPesan (cvErrorLabel, "Wajib Mengisi CV!", "#F04438");
            valid = false;
        }

        if(fileIjazah.getValue () == null){
            tampilkanPesan (ijazahErrorLabel, "Wajib Mengisi Ijazah!", "#F04438");
            valid = false;
        }

        if(fileSkd.getValue () == null){
            tampilkanPesan (skdErrorLabel, "Wajib Mengisi SKD!", "#F04438");
            valid = false;
        }

        if (valid){
            pindahHalaman (event, "/com/example/projectpbokawansetara/view/LoginView.fxml");
        } 
    }

    void handleHitungUsia (ActionEvent event){
        LocalDate tanggal = tanggallahirPicker.getvalue();
        if (tanggal != null){
            int usia = Period.between(tanggal, LocalDate.now()).getYears();
            usiaField.setText (usia + "tahun");
        }
    }

    void handleKembaliStep1(ActionEvent event){
        tampilkanLangkah (1);
    }

    void handleKembaliStep2(ActionEvent event){
        tampilkanLangkah (2);
    }

    void handleLanjutStep1 (ActionEvent event){
        String nama = namaField.getText();
        String password = ambilPassword();
        String konfirmasi = ambilKonfirmasiPassword();

        if(nama.isEmpty()){
            tampilkanPesan (namaErrorLabel, "Wajib Mengisi Bagian Nama!", "#F04438");
            valid = false;
        }

        if(email.isEmpt()){
            tampilkanPesan (emailErrorLabel, "Wajib Mengisi Bagian Email!", "#F04438");
            valid = false;
        } else if (!email.endsWith("@gmail.com")){
            tampilkanPesan (emailErrorLabel, "Format Email Anda Salah!", "#F04438");
            valid = false;
        }

        if (password.length() < 8){
            tampilkanPesan (passwordErrorLabel, "Maaf, Password Minimal 8 Karakter!", "#F04438");
            valid = false;
        }

        if (konfirmasi.isEmpty()){
            System.out.println("Masukkan Password Anda!");
            valid = false;
        } else if (!konfirmasi.equals(password)){
            System.out.println("Maaf, Password Anda Tidak Sama!");
            valid = false;
        }

        if (!agreeCheckBox.isSelected()){
            valid = false;
        }

        if (valid){
            tampilkanLangkah(2);
        }
    }
    






}