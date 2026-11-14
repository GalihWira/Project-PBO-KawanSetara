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
    private CheckBox fisikCheckBox;

    @FXML
    private CheckBox runguWicaraCheckBox;

    @FXML
    private CheckBox mentalCheckBox;

    @FXML
    private CheckBox netraCheckBox;

    @FXML
    private CheckBox intelektualCheckBox;


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

    private File fileCv = null;
    private File fileIjazah = null;
    private File fileSkd = null;

    @FXML 
    public void initialize(){
        pendidikanComboBox.getItems().addAll("SD", "SMP", "SMA", "Sarjana", "Magister", "Lainnya");

        sembunyikanSemuaError();
        tampilkanLangkah(1);
    }

    @FXML 
    void handleBukaPrivasi(ActionEvent event){
        System.out.println("Membuka Privasi!");
    }

    @FXML 
    void handleBukaSyarat (ActionEvent event ){
        System.out.println("Membuka Syarat!");
    }

    @FXML 
    void handleDaftar(ActionEvent event){
        sembunyikanSemuaError();
        boolean valid = true;

        if(!fisikCheckBox.isSelected () && !runguWicaraCheckBox.isSelected() && !mentalCheckBox.isSelected() && !netraCheckBox.isSelected() && !intelektualCheckBox.isSelected()){
            tampilkanPesan (disabilitasErrorLabel, "Wajib Memilih Jenis Disabilitas!");
            valid = false;
        }

        if(fileCv == null){
            tampilkanPesan (cvErrorLabel, "Wajib Mengisi CV!");
            valid = false;
        }

        if(fileIjazah == null){
            tampilkanPesan (ijazahErrorLabel, "Wajib Mengisi Ijazah!");
            valid = false;
        }

        if(fileSkd == null){
            tampilkanPesan (skdErrorLabel, "Wajib Mengisi SKD!");
            valid = false;
        }

        if (valid){
            pindahHalaman (event, "/com/example/projectpbokawansetara/view/LoginView.fxml", "Masuk - KawanSetara");
        } 
    }

    @FXML 
    void handleHitungUsia (ActionEvent event){
        LocalDate tanggal = tanggalLahirPicker.getValue();
        if (tanggal != null){
            int usia = Period.between(tanggal, LocalDate.now()).getYears();
            usiaField.setText (usia + " tahun");
        }
    }

    @FXML 
    void handleKembaliStep1(ActionEvent event){
        tampilkanLangkah (1);
    }

    @FXML 
    void handleKembaliStep2(ActionEvent event){
        tampilkanLangkah (2);
    }

    @FXML 
    void handleLanjutStep1 (ActionEvent event){
        sembunyikanSemuaError();
        boolean valid = true;
        String nama = namaField.getText();
        String email = namaField.getText();
        String password = ambilPassword();
        String konfirmasi = ambilKonfirmasiPassword();

        if(nama.isEmpty()){
            tampilkanPesan (namaErrorLabel, "Wajib Mengisi Bagian Nama!");
            valid = false;
        }

        if(email.isEmpty()){
            tampilkanPesan (emailErrorLabel, "Wajib Mengisi Bagian Email!");
            valid = false;
        } else if (!email.endsWith("@gmail.com")){
            tampilkanPesan (emailErrorLabel, "Format Email Anda Salah!");
            valid = false;
        }

        if (password.length() < 8){
            tampilkanPesan (passwordErrorLabel, "Maaf, Password Minimal 8 Karakter!");
            valid = false;
        }

        if (konfirmasi.isEmpty()){
            tampilkanPesan(confirmPasswordErrorLabel, "Maaf, Bagian Ini Wajib Diisi!");
            valid = false;
        } else if (!konfirmasi.equals(password)){
            tampilkanPesan(confirmPasswordErrorLabel, "Maaf, Password Anda Tidak Sama!");
            valid = false;
        }

        if (!agreeCheckBox.isSelected()){
            tampilkanPesan(agreeErrorLabel, "Wajib Menyetujui Syarat dan Ketentuan yang Berlaku");
            valid = false;
        }

        if (valid){
            tampilkanLangkah(2);
        }
    }

    @FXML 
    void handleLanjutStep2 (ActionEvent event){
        sembunyikanSemuaError();
        boolean valid = true;
        String telepon = teleponField.getText();

        if(telepon.isEmpty()){
            tampilkanPesan(teleponErrorLabel, "Maaf, Bagian ini Wajib Diisi!");
            valid = false;
        } else if (!hanyaAngka(telepon)){
            tampilkanPesan(tanggalLahirErrorLabel, "Maaf, Bagian ini Wajib Diisi!");
            valid = false;
        }

        if (tanggalLahirPicker.getValue() == null){
            tampilkanPesan(tanggalLahirErrorLabel, "Maaf, Bagian ini Wajib Diisi");
            valid = false;
        }

        if (pendidikanComboBox.getValue () == null){
            tampilkanPesan (pendidikanErrorLabel, "Maaf, Bagian ini Wajib Diisi!");
            valid = false;
        }

        if (valid){
            tampilkanLangkah (3);
        }
    }
    
    @FXML
    void handleLoginSosial (ActionEvent event){
        System.out.println("Maaf, Login Sosial Belum Tersedia");
    }

    @FXML
    void handleMasuk(ActionEvent event){
        pindahHalaman (event, "/com/example/projectpbokawansetara/view/LoginView.fxml", "Masuk - KawanSetara");
    }

    @FXML
    void handlePilihCv (ActionEvent event){
        File file = pilihPdf (event, cvErrorLabel);

        if (file != null){
            fileCv = file;
            tampilkanFile (file, cvFileLabel, cvSizeLabel);
        }
    }

    @FXML
    void handlePilihIjazah (ActionEvent event){
        File file = pilihPdf (event, ijazahErrorLabel);

        if (file != null){
            fileIjazah = file;
            tampilkanFile (file, ijazahFileLabel, ijazahSizeLabel);
        }
    }

    @FXML
    void handlePilihSkd (ActionEvent event){
        File file = pilihPdf (event, skdErrorLabel);

        if (file != null){
            fileSkd = file;
            tampilkanFile (file, skdFileLabel, skdSizeLabel);
        }
    }

    @FXML
    void handleToggleConfirmPassword (MouseEvent event){
        if (confirmPasswordField.isVisible()){
            confirmPasswordVisibleField.setText(confirmPasswordField.getText());
            tukarField (confirmPasswordVisibleField, confirmPasswordField);
        } else {
            confirmPasswordVisibleField.setText(confirmPasswordField.getText());
            tukarField (confirmPasswordVisibleField, confirmPasswordField);
        }
    }

    @FXML 
    void handleTogglePassword (MouseEvent event){
        if (passwordField.isVisible()){
            passwordVisibleField.setText(passwordField.getText());
            tukarField(passwordField, passwordVisibleField);
        } else {
            passwordVisibleField.setText(passwordVisibleField.getText());
            tukarField(passwordVisibleField, passwordField);
        }
    }


    private void tampilkanLangkah (int langkah){
        step1Pane.setVisible (langkah == 1);
        step1Pane.setManaged (langkah == 1);

        step2Pane.setVisible (langkah == 2);
        step2Pane.setManaged (langkah == 2);

        step3Pane.setVisible (langkah == 3);
        step3Pane.setManaged (langkah == 3);

        aturLingkaran (step1Circle, step1Title, langkah >=1);
        aturLingkaran (step2Circle, step2Title, langkah >=2);
        aturLingkaran (step3Circle, step3Title, langkah >=3);
    }

    private void aturLingkaran (Label lingkaran, Label judul, boolean aktif){
      String warna;
      
      if (aktif){
        warna = "#1F4E8C";
      } else {
        warna = "#D0D5DD";
      }

      lingkaran.setStyle ("-fx-background-color:" + warna + "; -fx-background-radius: 50; -fx-text-fill: white");

      if (aktif){
        judul.setStyle ("-fx-text-fill: #101828;");
      } else {
        judul.setStyle ("-fx-text-fill: #667085;");
      }
    }

    private void tukarField (TextField disembunyikan, TextField ditampilkan){
        ditampilkan.setVisible(true);
        ditampilkan.setManaged(true);
        disembunyikan.setVisible(false);
        disembunyikan.setManaged(false);
    }

    private String ambilPassword (){
        if (passwordField.isVisible()){
            return passwordField.getText();
        }
        return passwordVisibleField.getText();
    }

    private String ambilKonfirmasiPassword(){
        if(confirmPasswordField.isVisible()){
            return confirmPasswordField.getText();
        }
        return confirmPasswordVisibleField.getText();
    }

    private boolean hanyaAngka (String teks){
        for (int i = 0; i < teks.length(); i++){
            if (!Character.isDigit(teks.charAt(i))){
                return false;
            }
        }

        return true;
    }

    private File pilihPdf (ActionEvent event, Label errorLabel){
        FileChooser chooser = new FileChooser();
        chooser.setTitle ("Pilih file PDF");

        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File PDF", "*.pdf"));

        File file = chooser.showOpenDialog(ambilStage(event));

        if (file == null){
            return null;
        }

        if (file.length() > 2 * 1024 *1024){
            tampilkanPesan (errorLabel, "Ukuran File Maksimal 2 MB");
            return null;
        }

        sembunyikanPesan(errorLabel);
        return file;
    }

    private void tampilkanFile (File file, Label namaLabel, Label ukuranLabel){
        long kb = file.length()/1024;
        namaLabel.setText(file.getName());
        ukuranLabel.setText(kb +"KB");
        ukuranLabel.setVisible(true);
        ukuranLabel.setManaged(true);
    }

    private void tampilkanPesan (Label label, String pesan){
        label.setText(pesan);
        label.setPrefHeight(-1);
        label.setVisible(true);
    }

    private void sembunyikanPesan (Label label){
        label.setText("");
        label.setPrefHeight(0);
        label.setVisible(false);
    }

    private void sembunyikanSemuaError(){
        sembunyikanPesan(namaErrorLabel);
        sembunyikanPesan(emailErrorLabel);
        sembunyikanPesan(passwordErrorLabel);
        sembunyikanPesan(confirmPasswordErrorLabel);
        sembunyikanPesan(agreeErrorLabel);
        sembunyikanPesan(teleponErrorLabel);
        sembunyikanPesan(tanggalLahirErrorLabel);
        sembunyikanPesan(pendidikanErrorLabel);
        sembunyikanPesan(disabilitasErrorLabel);
        sembunyikanPesan(cvErrorLabel);
        sembunyikanPesan(ijazahErrorLabel);
        sembunyikanPesan(skdErrorLabel);
    }

    private Stage ambilStage (ActionEvent event){
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    private void pindahHalaman (ActionEvent event, String lokasiFxml, String judul){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(lokasiFxml));
            Parent root = loader.load();
            Stage stage = ambilStage (event);
            stage.setScene (new Scene (root, 1280, 800));
            stage.setTitle (judul);
            stage.show();
        } catch (IOException | NullPointerException e){
            System.out.println ("Maaf, Anda Gagal Memuat Halaman " + lokasiFxml);
            e.printStackTrace();
        }
    }
}