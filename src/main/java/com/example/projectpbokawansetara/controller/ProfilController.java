package com.example.projectpbokawansetara.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.projectpbokawansetara.dao.DisabilitasDAO;
import com.example.projectpbokawansetara.dao.PelamarDAO;
import com.example.projectpbokawansetara.exception.ValidationException;
import com.example.projectpbokawansetara.helper.FileHelper;
import com.example.projectpbokawansetara.model.JenisDisabilitas;
import com.example.projectpbokawansetara.model.Pelamar;
import com.example.projectpbokawansetara.model.Session;
import com.example.projectpbokawansetara.service.ProfilService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class ProfilController {

    private static final String KELAS_BANNER_ERROR = "banner-error";
    private static final String KELAS_BANNER_SUKSES = "banner-sukses";
    private static final String KELAS_INPUT_ERROR = "input-error";
    private static final String KELAS_FILE_BARU = "file-dokumen-baru";

    @FXML
    private HBox pesanBanner;
    @FXML
    private Label pesanJudulLabel;
    @FXML
    private Label pesanDetailLabel;

    @FXML
    private StackPane avatarPane;
    @FXML
    private Label inisialLabel;
    @FXML
    private ImageView fotoImageView;
    @FXML
    private Button gantiFotoButton;
    @FXML
    private Label fotoInfoLabel;
    @FXML
    private Label fotoBaruLabel;
    @FXML
    private Label fotoErrorLabel;
    @FXML
    private Label namaHeaderLabel;
    @FXML
    private Label emailHeaderLabel;

    @FXML
    private TextField namaField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField teleponField;
    @FXML
    private DatePicker tanggalLahirPicker;
    @FXML
    private ComboBox<String> pendidikanComboBox;
    @FXML
    private Label usiaLabel;

    @FXML
    private Label namaErrorLabel;
    @FXML
    private Label teleponErrorLabel;
    @FXML
    private Label tanggalLahirErrorLabel;
    @FXML
    private Label pendidikanErrorLabel;

    @FXML
    private FlowPane disabilitasPane;
    @FXML
    private Label disabilitasErrorLabel;

    @FXML
    private Label cvFileLabel;
    @FXML
    private Label ijazahFileLabel;
    @FXML
    private Label skdFileLabel;
    @FXML
    private Label dokumenErrorLabel;

    @FXML
    private Button batalButton;
    @FXML
    private Button simpanButton;

    private final PelamarDAO pelamarDAO = new PelamarDAO();
    private final DisabilitasDAO disabilitasDAO = new DisabilitasDAO();
    private final ProfilService profilService = new ProfilService();

    private final Map<Integer, CheckBox> checkboxDisabilitas = new LinkedHashMap<>();

    private DashboardPelamarController dashboardController;
    private Pelamar pelamarAwal;
    private List<Integer> disabilitasAwal = new ArrayList<>();

    private String pathCv;
    private String pathIjazah;
    private String pathSkd;
    private File fileFoto;
    private File fileCvBaru;
    private File fileIjazahBaru;
    private File fileSkdBaru;

    @FXML
    public void initialize() {
        pendidikanComboBox.getItems().addAll("SD", "SMP", "SMA", "Sarjana", "Magister", "Lainnya");
        tanggalLahirPicker.valueProperty().addListener((observable, nilaiLama, nilaiBaru) -> perbaruiUsia(nilaiBaru));
        fotoImageView.setClip(new Circle(36, 36, 36));

        sembunyikanSemuaError();
        sembunyikanBanner();

        if (!(Session.getCurrentUser() instanceof Pelamar pelamar)) {
            tampilkanError("Sesi pengguna tidak ditemukan", "Silakan masuk kembali.");
            simpanButton.setDisable(true);
            return;
        }

        muatProfil(pelamar.getIdPengguna());
    }

    public void setDashboardController(DashboardPelamarController dashboardController) {
        this.dashboardController = dashboardController;
    }

    private void muatProfil(int idPelamar) {
        try {
            Pelamar pelamar = pelamarDAO.getPelamarById(idPelamar);

            if (pelamar == null) {
                tampilkanError("Data profil tidak ditemukan", "Silakan masuk kembali.");
                simpanButton.setDisable(true);
                return;
            }

            List<JenisDisabilitas> daftarJenis = disabilitasDAO.getAllDisabilitas();
            List<Integer> disabilitasPelamar = pelamarDAO.getIdDisabilitasPelamar(idPelamar);

            tampilkanData(pelamar, daftarJenis, disabilitasPelamar);

        } catch (SQLException e) {
            tampilkanError("Gagal memuat profil", "Periksa koneksi database lalu coba lagi.");
            simpanButton.setDisable(true);
            e.printStackTrace();
        }
    }

    private void tampilkanData(Pelamar pelamar, List<JenisDisabilitas> daftarJenis, List<Integer> disabilitasPelamar) {
        pelamarAwal = pelamar;
        disabilitasAwal = new ArrayList<>(disabilitasPelamar);

        disabilitasPane.getChildren().clear();
        checkboxDisabilitas.clear();

        for (JenisDisabilitas jenis : daftarJenis) {
            CheckBox checkbox = new CheckBox(jenis.getNamaJenis());
            checkbox.getStyleClass().addAll("checkbox-keahlian", "checkbox-profil");
            checkbox.setOnAction(event -> sembunyikanPesan(disabilitasErrorLabel));
            checkboxDisabilitas.put(jenis.getIdJenisDisabilitas(), checkbox);
            disabilitasPane.getChildren().add(checkbox);
        }

        isiForm();
        resetFoto();
    }

    private void isiForm() {
        namaField.setText(teksAman(pelamarAwal.getNama()));
        emailField.setText(teksAman(pelamarAwal.getEmail()));
        teleponField.setText(teksAman(pelamarAwal.getNoTelepon()));
        tanggalLahirPicker.setValue(pelamarAwal.getTanggalLahir());
        pendidikanComboBox.setValue(pelamarAwal.getPendidikan());

        for (Map.Entry<Integer, CheckBox> entry : checkboxDisabilitas.entrySet()) {
            entry.getValue().setSelected(disabilitasAwal.contains(entry.getKey()));
        }

        pathCv = pelamarAwal.getCv();
        pathIjazah = pelamarAwal.getIjazah();
        pathSkd = pelamarAwal.getSuratKeteranganDisabilitas();

        fileCvBaru = null;
        fileIjazahBaru = null;
        fileSkdBaru = null;

        tampilkanNamaFile(cvFileLabel, pathCv, false);
        tampilkanNamaFile(ijazahFileLabel, pathIjazah, false);
        tampilkanNamaFile(skdFileLabel, pathSkd, false);

        perbaruiHeader();
        perbaruiUsia(pelamarAwal.getTanggalLahir());
    }

    @FXML
    void handleGantiFoto(ActionEvent event) {
        pilihFoto();
    }

    @FXML
    void handleKlikAvatar(MouseEvent event) {
        pilihFoto();
    }

    private void pilihFoto() {
        sembunyikanPesan(fotoErrorLabel);

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih foto profil");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Gambar (JPG, PNG)", "*.jpg", "*.jpeg", "*.png"));

        File file = chooser.showOpenDialog(avatarPane.getScene().getWindow());

        if (file == null) {
            return;
        }

        try {
            profilService.validasiFoto(file);
        } catch (ValidationException e) {
            tampilkanPesan(fotoErrorLabel, e.getMessage());
            return;
        }

        Image gambar = new Image(file.toURI().toString());

        if (gambar.isError()) {
            tampilkanPesan(fotoErrorLabel, "Foto tidak bisa dibuka, coba file lain ya.");
            return;
        }

        fileFoto = file;
        tampilkanFoto(gambar);
    }

    private void tampilkanFoto(Image gambar) {
        double sisi = Math.min(gambar.getWidth(), gambar.getHeight());
        double x = (gambar.getWidth() - sisi) / 2;
        double y = (gambar.getHeight() - sisi) / 2;

        fotoImageView.setImage(gambar);
        fotoImageView.setViewport(new Rectangle2D(x, y, sisi, sisi));
        fotoImageView.setVisible(true);
        inisialLabel.setVisible(false);

        tampilkanLabel(fotoInfoLabel, false);
        tampilkanLabel(fotoBaruLabel, true);
    }

    private void resetFoto() {
        fileFoto = null;

        fotoImageView.setImage(null);
        fotoImageView.setVisible(false);
        inisialLabel.setVisible(true);

        tampilkanLabel(fotoInfoLabel, true);
        tampilkanLabel(fotoBaruLabel, false);
        sembunyikanPesan(fotoErrorLabel);
    }

    private void tampilkanLabel(Label label, boolean tampil) {
        label.setVisible(tampil);
        label.setManaged(tampil);
    }

    @FXML
    void handleGantiCv(ActionEvent event) {
        File file = pilihPdf(event);
        if (file != null) {
            fileCvBaru = file;
            pathCv = file.getAbsolutePath();
            tampilkanNamaFile(cvFileLabel, pathCv, true);
        }
    }

    @FXML
    void handleGantiIjazah(ActionEvent event) {
        File file = pilihPdf(event);
        if (file != null) {
            fileIjazahBaru = file;
            pathIjazah = file.getAbsolutePath();
            tampilkanNamaFile(ijazahFileLabel, pathIjazah, true);
        }
    }

    @FXML
    void handleGantiSkd(ActionEvent event) {
        File file = pilihPdf(event);
        if (file != null) {
            fileSkdBaru = file;
            pathSkd = file.getAbsolutePath();
            tampilkanNamaFile(skdFileLabel, pathSkd, true);
        }
    }

    @FXML
    void handleSimpan(ActionEvent event) {
        if (pelamarAwal == null) {
            return;
        }

        sembunyikanSemuaError();
        sembunyikanBanner();

        String nama = namaField.getText().trim();
        String telepon = ambilTelepon();
        LocalDate tanggalLahir = tanggalLahirPicker.getValue();
        String pendidikan = pendidikanComboBox.getValue();
        List<Integer> daftarIdDisabilitas = ambilDisabilitas();

        try {
            profilService.validasiProfil(nama, telepon, tanggalLahir, pendidikan, daftarIdDisabilitas);
            profilService.validasiDokumen(pathCv, pathIjazah, pathSkd);
        } catch (ValidationException e) {
            tampilkanErrorField(e);
            tampilkanError("Profil belum bisa disimpan", e.getMessage());
            return;
        }

        try {
            salinDokumenBaru();
        } catch (IOException e) {
            tampilkanPesan(dokumenErrorLabel, "File gagal disimpan. Silakan pilih ulang.");
            tampilkanError("Profil belum bisa disimpan", "File gagal disimpan. Silakan pilih ulang.");
            e.printStackTrace();
            return;
        }

        Pelamar pelamarBaru = new Pelamar(
                pelamarAwal.getIdPengguna(),
                nama,
                pelamarAwal.getEmail(),
                null,
                telepon,
                tanggalLahir,
                pendidikan,
                null,
                pathIjazah,
                pathSkd,
                pathCv);

        try {
            pelamarDAO.perbaruiProfil(pelamarBaru, daftarIdDisabilitas);

            pelamarAwal = pelamarBaru;
            disabilitasAwal = new ArrayList<>(daftarIdDisabilitas);

            perbaruiSession(pelamarBaru);
            isiForm();

            tampilkanSukses("Profil berhasil disimpan", "Data diri kamu sudah diperbarui.");

        } catch (SQLException e) {
            tampilkanError("Gagal menyimpan profil", "Silakan coba lagi beberapa saat.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleBatal(ActionEvent event) {
        if (pelamarAwal == null) {
            return;
        }

        sembunyikanSemuaError();
        sembunyikanBanner();
        isiForm();
        resetFoto();
    }

    private void salinDokumenBaru() throws IOException {
        String email = pelamarAwal.getEmail();

        if (fileCvBaru != null) {
            pathCv = FileHelper.simpanPdf(fileCvBaru, "cv", email);
            fileCvBaru = null;
        }
        if (fileIjazahBaru != null) {
            pathIjazah = FileHelper.simpanPdf(fileIjazahBaru, "ijazah", email);
            fileIjazahBaru = null;
        }
        if (fileSkdBaru != null) {
            pathSkd = FileHelper.simpanPdf(fileSkdBaru, "skd", email);
            fileSkdBaru = null;
        }
    }

    private void perbaruiSession(Pelamar pelamarBaru) {
        if (Session.getCurrentUser() instanceof Pelamar pelamarSession) {
            pelamarSession.setNama(pelamarBaru.getNama());
            pelamarSession.setNoTelepon(pelamarBaru.getNoTelepon());
            pelamarSession.setTanggalLahir(pelamarBaru.getTanggalLahir());
            pelamarSession.setPendidikan(pelamarBaru.getPendidikan());
            pelamarSession.setCv(pelamarBaru.getCv());
            pelamarSession.setIjazah(pelamarBaru.getIjazah());
            pelamarSession.setSuratKeteranganDisabilitas(pelamarBaru.getSuratKeteranganDisabilitas());
        }

        if (dashboardController != null) {
            dashboardController.perbaruiInfoPengguna();
        }
    }

    private File pilihPdf(ActionEvent event) {
        sembunyikanPesan(dokumenErrorLabel);

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih file PDF");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File PDF", "*.pdf"));

        Window window = ((Node) event.getSource()).getScene().getWindow();
        File file = chooser.showOpenDialog(window);

        if (file == null) {
            return null;
        }

        try {
            profilService.validasiFilePdf(file);
        } catch (ValidationException e) {
            tampilkanPesan(dokumenErrorLabel, e.getMessage());
            return null;
        }

        return file;
    }

    private List<Integer> ambilDisabilitas() {
        List<Integer> daftar = new ArrayList<>();

        for (Map.Entry<Integer, CheckBox> entry : checkboxDisabilitas.entrySet()) {
            if (entry.getValue().isSelected()) {
                daftar.add(entry.getKey());
            }
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

    private void perbaruiHeader() {
        String nama = teksAman(pelamarAwal.getNama());
        namaHeaderLabel.setText(nama.isEmpty() ? "Pelamar" : nama);
        emailHeaderLabel.setText(teksAman(pelamarAwal.getEmail()));
        inisialLabel.setText(ambilInisial(nama));
    }

    private void perbaruiUsia(LocalDate tanggal) {
        if (tanggal == null) {
            usiaLabel.setText("Usia dihitung otomatis dari tanggal lahir");
            return;
        }

        int usia = Period.between(tanggal, LocalDate.now()).getYears();
        usiaLabel.setText("Usia: " + usia + " tahun");
    }

    private String ambilInisial(String nama) {
        if (nama == null || nama.isBlank()) {
            return "P";
        }

        String bersih = nama.trim();
        String inisial = bersih.substring(0, 1);

        int spasiTerakhir = bersih.lastIndexOf(' ');
        if (spasiTerakhir > 0 && spasiTerakhir < bersih.length() - 1) {
            inisial += bersih.substring(spasiTerakhir + 1, spasiTerakhir + 2);
        }

        return inisial.toUpperCase();
    }

    private void tampilkanNamaFile(Label label, String path, boolean baru) {
        label.getStyleClass().remove(KELAS_FILE_BARU);

        if (path == null || path.isEmpty()) {
            label.setText("Belum ada file");
            return;
        }

        int posisiPemisah = Math.max(path.lastIndexOf('\\'), path.lastIndexOf('/'));
        String namaFile = path.substring(posisiPemisah + 1);

        if (baru) {
            label.setText(namaFile + " (belum disimpan)");
            label.getStyleClass().add(KELAS_FILE_BARU);
        } else {
            label.setText(namaFile);
        }
    }

    private String teksAman(String teks) {
        return teks == null ? "" : teks;
    }

    private void tampilkanErrorField(ValidationException e) {
        String field = e.getNamaField();

        if ("nama".equals(field)) {
            tampilkanPesan(namaErrorLabel, e.getMessage());
            tandaiInput(namaField);
        } else if ("telepon".equals(field)) {
            tampilkanPesan(teleponErrorLabel, e.getMessage());
            tandaiInput(teleponField);
        } else if ("tanggalLahir".equals(field)) {
            tampilkanPesan(tanggalLahirErrorLabel, e.getMessage());
            tandaiInput(tanggalLahirPicker);
        } else if ("pendidikan".equals(field)) {
            tampilkanPesan(pendidikanErrorLabel, e.getMessage());
            tandaiInput(pendidikanComboBox);
        } else if ("disabilitas".equals(field)) {
            tampilkanPesan(disabilitasErrorLabel, e.getMessage());
        } else if ("dokumen".equals(field)) {
            tampilkanPesan(dokumenErrorLabel, e.getMessage());
        }
    }

    private void tandaiInput(Node input) {
        if (!input.getStyleClass().contains(KELAS_INPUT_ERROR)) {
            input.getStyleClass().add(KELAS_INPUT_ERROR);
        }
    }

    private void tampilkanPesan(Label label, String pesan) {
        label.setText(pesan);
        label.setManaged(true);
        label.setVisible(true);
    }

    private void sembunyikanPesan(Label label) {
        label.setText("");
        label.setManaged(false);
        label.setVisible(false);
    }

    private void sembunyikanSemuaError() {
        sembunyikanPesan(namaErrorLabel);
        sembunyikanPesan(teleponErrorLabel);
        sembunyikanPesan(tanggalLahirErrorLabel);
        sembunyikanPesan(pendidikanErrorLabel);
        sembunyikanPesan(disabilitasErrorLabel);
        sembunyikanPesan(dokumenErrorLabel);

        namaField.getStyleClass().remove(KELAS_INPUT_ERROR);
        teleponField.getStyleClass().remove(KELAS_INPUT_ERROR);
        tanggalLahirPicker.getStyleClass().remove(KELAS_INPUT_ERROR);
        pendidikanComboBox.getStyleClass().remove(KELAS_INPUT_ERROR);
    }

    private void tampilkanBanner(String kelas, String judul, String detail) {
        pesanJudulLabel.setText(judul);
        pesanDetailLabel.setText(detail);

        pesanBanner.getStyleClass().removeAll(KELAS_BANNER_ERROR, KELAS_BANNER_SUKSES);
        pesanBanner.getStyleClass().add(kelas);

        pesanBanner.setManaged(true);
        pesanBanner.setVisible(true);
    }

    private void tampilkanError(String judul, String detail) {
        tampilkanBanner(KELAS_BANNER_ERROR, judul, detail);
    }

    private void tampilkanSukses(String judul, String detail) {
        tampilkanBanner(KELAS_BANNER_SUKSES, judul, detail);
    }

    private void sembunyikanBanner() {
        pesanBanner.setManaged(false);
        pesanBanner.setVisible(false);
    }
}