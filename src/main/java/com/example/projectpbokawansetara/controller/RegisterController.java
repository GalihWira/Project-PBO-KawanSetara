package com.example.projectpbokawansetara.controller;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.example.projectpbokawansetara.dao.PelamarDAO;
import com.example.projectpbokawansetara.dao.UserDAO;
import com.example.projectpbokawansetara.exception.ValidationException;
import com.example.projectpbokawansetara.helper.SceneManager;
import com.example.projectpbokawansetara.model.Pelamar;
import com.example.projectpbokawansetara.service.AuthService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    private static final long UKURAN_MAKS_FILE = 2 * 1024 * 1024;

    private final PelamarDAO pelamarDAO = new PelamarDAO();
    private final UserDAO userDAO = new UserDAO();
    private final AuthService authService = new AuthService();

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
    private Label registerMassageLabel;

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
    private ComboBox<String> pendidikanComboBox;

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

    private File fileCv;
    private File fileIjazah;
    private File fileSkd;

    @FXML
    public void initialize() {
        pendidikanComboBox.getItems().addAll("SD", "SMP", "SMA", "Sarjana", "Magister", "Lainnya");

        sembunyikanSemuaError();
        tampilkanLangkah(1);
    }

    @FXML
    void handleLanjutStep1(ActionEvent event) {
        sembunyikanSemuaError();

        String nama = namaField.getText().trim();
        String email = emailField.getText().trim().toLowerCase();
        String password = ambilPassword();
        String konfirmasi = ambilKonfirmasiPassword();

        if (!agreeCheckBox.isSelected()) {
            tampilkanPesan(agreeErrorLabel, "Wajib menyetujui syarat dan ketentuan!");
        }

        try {
            authService.validasiAkunDaftar(nama, email, password, konfirmasi);
        } catch (ValidationException e) {
            tampilkanErrorField(e);
            tampilkanAlert(step1Alert);
            return;
        }

        if (!agreeCheckBox.isSelected()) {
            tampilkanAlert(step1Alert);
            return;
        }

        try {
            if (userDAO.emailSudahTerdaftar(email)) {
                tampilkanPesan(emailErrorLabel, "Email sudah terdaftar!");
                tampilkanAlert(step1Alert);
                return;
            }
        } catch (SQLException e) {
            tampilkanPesan(registerMassageLabel, "Terjadi kesalahan saat menghubungkan database!");
            e.printStackTrace();
            return;
        }

        tampilkanLangkah(2);
    }

    @FXML
    void handleLanjutStep2(ActionEvent event) {
        sembunyikanSemuaError();
        boolean valid = true;

        try {
            authService.validasiTelepon(ambilTelepon());
        } catch (ValidationException e) {
            tampilkanPesan(teleponErrorLabel, e.getMessage());
            valid = false;
        }

        if (tanggalLahirPicker.getValue() == null) {
            tampilkanPesan(tanggalLahirErrorLabel, "Tanggal lahir wajib diisi!");
            valid = false;
        }

        if (pendidikanComboBox.getValue() == null) {
            tampilkanPesan(pendidikanErrorLabel, "Pendidikan wajib dipilih!");
            valid = false;
        }

        if (valid) {
            tampilkanLangkah(3);
        } else {
            tampilkanAlert(step2Alert);
        }
    }

    @FXML
    void handleDaftar(ActionEvent event) {
        sembunyikanSemuaError();
        boolean valid = true;

        List<String> daftarDisabilitas = ambilDisabilitas();

        if (daftarDisabilitas.isEmpty()) {
            tampilkanPesan(disabilitasErrorLabel, "Pilih minimal satu jenis disabilitas!");
            valid = false;
        }

        if (fileCv == null) {
            tampilkanPesan(cvErrorLabel, "CV wajib diunggah!");
            valid = false;
        }

        if (fileIjazah == null) {
            tampilkanPesan(ijazahErrorLabel, "Ijazah wajib diunggah!");
            valid = false;
        }

        if (fileSkd == null) {
            tampilkanPesan(skdErrorLabel, "Surat keterangan disabilitas wajib diunggah!");
            valid = false;
        }

        if (!valid) {
            tampilkanAlert(step3Alert);
            return;
        }

        Pelamar pelamar = new Pelamar(
            0,
            namaField.getText().trim(),
            emailField.getText().trim().toLowerCase(),
            ambilPassword(),
            ambilTelepon(),
            tanggalLahirPicker.getValue(),
            pendidikanComboBox.getValue(),
            String.join(", ", daftarDisabilitas),
            fileIjazah.getAbsolutePath(),
            fileSkd.getAbsolutePath(),
            fileCv.getAbsolutePath()
        );

        try {
            if (pelamarDAO.simpanPelamar(pelamar, daftarDisabilitas)) {
                SceneManager.pindah("LoginView.fxml", "Masuk - KawanSetara");
            }
        } catch (SQLException e) {
            tampilkanPesan(skdErrorLabel, "Gagal mendaftar, silakan coba lagi.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleKembaliStep1(ActionEvent event) {
        tampilkanLangkah(1);
    }

    @FXML
    void handleKembaliStep2(ActionEvent event) {
        tampilkanLangkah(2);
    }

    @FXML
    void handleHitungUsia(ActionEvent event) {
        LocalDate tanggal = tanggalLahirPicker.getValue();
        if (tanggal != null) {
            int usia = Period.between(tanggal, LocalDate.now()).getYears();
            usiaField.setText(usia + " tahun");
        }
    }

    @FXML
    void handlePilihCv(ActionEvent event) {
        File file = pilihPdf(event, cvErrorLabel);
        if (file != null) {
            fileCv = file;
            tampilkanFile(file, cvFileLabel, cvSizeLabel);
        }
    }

    @FXML
    void handlePilihIjazah(ActionEvent event) {
        File file = pilihPdf(event, ijazahErrorLabel);
        if (file != null) {
            fileIjazah = file;
            tampilkanFile(file, ijazahFileLabel, ijazahSizeLabel);
        }
    }

    @FXML
    void handlePilihSkd(ActionEvent event) {
        File file = pilihPdf(event, skdErrorLabel);
        if (file != null) {
            fileSkd = file;
            tampilkanFile(file, skdFileLabel, skdSizeLabel);
        }
    }

    @FXML
    void handleTogglePassword(MouseEvent event) {
        if (passwordField.isVisible()) {
            passwordVisibleField.setText(passwordField.getText());
            tukarField(passwordField, passwordVisibleField);
        } else {
            passwordField.setText(passwordVisibleField.getText());
            tukarField(passwordVisibleField, passwordField);
        }
    }

    @FXML
    void handleToggleConfirmPassword(MouseEvent event) {
        if (confirmPasswordField.isVisible()) {
            confirmPasswordVisibleField.setText(confirmPasswordField.getText());
            tukarField(confirmPasswordField, confirmPasswordVisibleField);
        } else {
            confirmPasswordField.setText(confirmPasswordVisibleField.getText());
            tukarField(confirmPasswordVisibleField, confirmPasswordField);
        }
    }

    @FXML
    void handleMasuk(ActionEvent event) {
        SceneManager.pindah("LoginView.fxml", "Masuk - KawanSetara");
    }

    @FXML
    void handleLoginSosial(ActionEvent event) {
        tampilkanPesan(registerMassageLabel, "Maaf, fitur ini belum tersedia.");
    }

    @FXML
    void handleBukaSyarat(ActionEvent event) {
        System.out.println("Membuka Syarat!");
    }

    @FXML
    void handleBukaPrivasi(ActionEvent event) {
        System.out.println("Membuka Privasi!");
    }

    private void tampilkanErrorField(ValidationException e) {
        String field = e.getNamaField();

        if ("nama".equals(field)) {
            tampilkanPesan(namaErrorLabel, e.getMessage());
        } else if ("email".equals(field)) {
            tampilkanPesan(emailErrorLabel, e.getMessage());
        } else if ("password".equals(field)) {
            tampilkanPesan(passwordErrorLabel, e.getMessage());
        } else if ("konfirmasi".equals(field)) {
            tampilkanPesan(confirmPasswordErrorLabel, e.getMessage());
        } else {
            tampilkanPesan(registerMassageLabel, e.getMessage());
        }
    }

    private List<String> ambilDisabilitas() {
        List<String> daftar = new ArrayList<>();

        if (fisikCheckBox.isSelected()) {
            daftar.add("Disabilitas Fisik");
        }
        if (netraCheckBox.isSelected()) {
            daftar.add("Disabilitas Sensorik Netra");
        }
        if (runguWicaraCheckBox.isSelected()) {
            daftar.add("Disabilitas Sensorik Rungu");
            daftar.add("Disabilitas Wicara");
        }
        if (intelektualCheckBox.isSelected()) {
            daftar.add("Disabilitas Intelektual");
        }
        if (mentalCheckBox.isSelected()) {
            daftar.add("Disabilitas Mental");
        }

        return daftar;
    }

    private String ambilTelepon() {
        String telepon = teleponField.getText().trim().replace(" ", "");

        if (telepon.startsWith("8")) {
            telepon = "0" + telepon;
        }

        return telepon;
    }

    private String ambilPassword() {
        if (passwordField.isVisible()) {
            return passwordField.getText();
        }
        return passwordVisibleField.getText();
    }

    private String ambilKonfirmasiPassword() {
        if (confirmPasswordField.isVisible()) {
            return confirmPasswordField.getText();
        }
        return confirmPasswordVisibleField.getText();
    }

    private void tampilkanLangkah(int langkah) {
        step1Pane.setVisible(langkah == 1);
        step1Pane.setManaged(langkah == 1);

        step2Pane.setVisible(langkah == 2);
        step2Pane.setManaged(langkah == 2);

        step3Pane.setVisible(langkah == 3);
        step3Pane.setManaged(langkah == 3);

        aturLingkaran(step1Circle, step1Title, langkah >= 1);
        aturLingkaran(step2Circle, step2Title, langkah >= 2);
        aturLingkaran(step3Circle, step3Title, langkah >= 3);
    }

    private void aturLingkaran(Label lingkaran, Label judul, boolean aktif) {
        String warna = aktif ? "#1F4E8C" : "#D0D5DD";
        lingkaran.setStyle("-fx-background-color: " + warna + "; -fx-background-radius: 50; -fx-text-fill: white;");

        if (aktif) {
            judul.setStyle("-fx-text-fill: #101828;");
        } else {
            judul.setStyle("-fx-text-fill: #667085;");
        }
    }

    private void tukarField(TextField disembunyikan, TextField ditampilkan) {
        ditampilkan.setVisible(true);
        ditampilkan.setManaged(true);
        disembunyikan.setVisible(false);
        disembunyikan.setManaged(false);
    }

    private File pilihPdf(ActionEvent event, Label errorLabel) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih file PDF");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File PDF", "*.pdf"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = chooser.showOpenDialog(stage);

        if (file == null) {
            return null;
        }

        if (file.length() > UKURAN_MAKS_FILE) {
            tampilkanPesan(errorLabel, "Ukuran file maksimal 2 MB!");
            return null;
        }

        sembunyikanPesan(errorLabel);
        return file;
    }

    private void tampilkanFile(File file, Label namaLabel, Label ukuranLabel) {
        long kb = file.length() / 1024;
        namaLabel.setText(file.getName());
        ukuranLabel.setText(kb + " KB");
        ukuranLabel.setVisible(true);
        ukuranLabel.setManaged(true);
    }

    private void tampilkanAlert(HBox alert) {
        alert.setVisible(true);
        alert.setManaged(true);
    }

    private void sembunyikanAlert(HBox alert) {
        alert.setVisible(false);
        alert.setManaged(false);
    }

    private void tampilkanPesan(Label label, String pesan) {
        label.setText(pesan);
        label.setPrefHeight(-1);
        label.setVisible(true);
    }

    private void sembunyikanPesan(Label label) {
        label.setText("");
        label.setPrefHeight(0);
        label.setVisible(false);
    }

    private void sembunyikanSemuaError() {
        sembunyikanPesan(namaErrorLabel);
        sembunyikanPesan(emailErrorLabel);
        sembunyikanPesan(passwordErrorLabel);
        sembunyikanPesan(confirmPasswordErrorLabel);
        sembunyikanPesan(agreeErrorLabel);
        sembunyikanPesan(registerMassageLabel);
        sembunyikanPesan(teleponErrorLabel);
        sembunyikanPesan(tanggalLahirErrorLabel);
        sembunyikanPesan(pendidikanErrorLabel);
        sembunyikanPesan(disabilitasErrorLabel);
        sembunyikanPesan(cvErrorLabel);
        sembunyikanPesan(ijazahErrorLabel);
        sembunyikanPesan(skdErrorLabel);
        sembunyikanAlert(step1Alert);
        sembunyikanAlert(step2Alert);
        sembunyikanAlert(step3Alert);
    }
}
