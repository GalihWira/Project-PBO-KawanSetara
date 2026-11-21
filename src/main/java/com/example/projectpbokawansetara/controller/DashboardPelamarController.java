package com.example.projectpbokawansetara.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.io.File;

import com.example.projectpbokawansetara.dao.LamaranDAO;
import com.example.projectpbokawansetara.dao.LowonganDAO;
import com.example.projectpbokawansetara.dao.PelamarDAO;
import com.example.projectpbokawansetara.helper.DialogHelper;
import com.example.projectpbokawansetara.helper.SceneManager;
import com.example.projectpbokawansetara.model.Keahlian;
import com.example.projectpbokawansetara.model.Lamaran;
import com.example.projectpbokawansetara.model.Lowongan;
import com.example.projectpbokawansetara.model.Pelamar;
import com.example.projectpbokawansetara.model.Session;
import com.example.projectpbokawansetara.model.User;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;

public class DashboardPelamarController {

    private Node halamanCariLowongan;
    private Node halamanBeranda;

    @FXML
    private ImageView avatarImage;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ToggleButton berandaMenu;
    @FXML
    private ToggleButton cariLowonganMenu;
    @FXML
    private ToggleButton riwayatMenu;
    @FXML
    private ToggleButton checklistMenu;
    @FXML
    private ToggleGroup menuGroup;
    @FXML
    private Label riwayatMenuLabel;
    @FXML
    private Label jumlahRiwayatLabel;
    @FXML
    private Button btnKeluar;
    @FXML
    private TextField cariField;
    @FXML
    private Label namaUserLabel;
    @FXML
    private Label emailUserLabel;
    @FXML
    private Label sapaanLabel;
    @FXML
    private Label ringkasanLabel;
    @FXML
    private VBox rekomendasiBox;
    @FXML
    private Label rekomendasiKosongLabel;
    @FXML
    private VBox lamaranTerakhirBox;
    @FXML
    private Label lamaranKosongLabel;
    @FXML
    private Label checklistInfoLabel;
    @FXML
    private Hyperlink lihatSemuaLink;
    @FXML
    private Hyperlink lihatRiwayatLink;
    @FXML
    private Hyperlink perbaruiChecklistLink;

    private final LowonganDAO lowonganDAO = new LowonganDAO();
    private final LamaranDAO lamaranDAO = new LamaranDAO();
    private final PelamarDAO pelamarDAO = new PelamarDAO();

    private List<Keahlian> keahlianPelamar = new ArrayList<>();
    private List<Lowongan> semuaLowongan = new ArrayList<>();
    private List<Lamaran> riwayatLamaran = new ArrayList<>();

    @FXML
    private void initialize() {
        if (!Session.isLoggedIn()) {
            Platform.runLater(() -> SceneManager.pindah("LoginView.fxml", "Masuk - KawanSetara"));
            return;
        }

        tampilkanProfil();
        halamanBeranda = mainBorderPane.getCenter();
        aturMenuAktif(berandaMenu);

        muatDataBeranda();
    }

    private void tampilkanProfil() {
        User user = Session.getCurrentUser();
        namaUserLabel.setText(user.getNama());
        emailUserLabel.setText(user.getEmail());
        tampilkanAvatarSidebar(user);
    }

    public void perbaruiInfoPengguna() {
        tampilkanProfil();
    }

    private void tampilkanAvatarSidebar(User user) {
        if (avatarImage == null) {
            return;
        }

        String pathFoto = null;
        if (user instanceof Pelamar pelamar) {
            pathFoto = pelamar.getFoto();
            if (pathFoto == null || pathFoto.isEmpty()) {
                try {
                    Pelamar lengkap = pelamarDAO.getPelamarById(pelamar.getIdPengguna());
                    if (lengkap != null && lengkap.getFoto() != null && !lengkap.getFoto().isEmpty()) {
                        pathFoto = lengkap.getFoto();
                        pelamar.setFoto(pathFoto);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (pathFoto != null && !pathFoto.isEmpty()) {
            File file = new File(pathFoto);
            if (file.exists()) {
                Image gambar = new Image(file.toURI().toString());
                if (!gambar.isError()) {
                    double sisi = Math.min(gambar.getWidth(), gambar.getHeight());
                    double x = (gambar.getWidth() - sisi) / 2;
                    double y = (gambar.getHeight() - sisi) / 2;

                    avatarImage.setClip(new Circle(17, 17, 17));
                    avatarImage.setImage(gambar);
                    avatarImage.setViewport(new Rectangle2D(x, y, sisi, sisi));
                    avatarImage.setFitWidth(34);
                    avatarImage.setFitHeight(34);
                    avatarImage.setPreserveRatio(false);
                    return;
                }
            }
        }

        // Fallback: icon default
        try {
            Image defaultIcon = new Image(getClass()
                    .getResourceAsStream("/com/example/projectpbokawansetara/images/icon_user.png"));
            avatarImage.setClip(null);
            avatarImage.setViewport(null);
            avatarImage.setImage(defaultIcon);
            avatarImage.setPreserveRatio(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void muatDataBeranda() {
        try {
            if (!(Session.getCurrentUser() instanceof Pelamar pelamar)) {
                return;
            }

            int idPelamar = pelamar.getIdPengguna();

            keahlianPelamar = pelamarDAO.getKeahlianByPelamar(idPelamar);
            semuaLowongan = lowonganDAO.getSemuaLowongan();
            riwayatLamaran = lamaranDAO.getRiwayatLamaran(idPelamar);

            perbaruiSapaanDanRingkasan(pelamar);

            jumlahRiwayatLabel.setText(riwayatLamaran.isEmpty() ? "" : String.valueOf(riwayatLamaran.size()));

            tampilkanRekomendasi();

            tampilkanLamaranTerakhir();

            perbaruiInfoChecklist();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void perbaruiSapaanDanRingkasan(Pelamar pelamar) {
        String namaDepan = ambilNamaDepan(pelamar.getNama());
        String sapaan = buatSapaan() + ", " + namaDepan;
        sapaanLabel.setText(sapaan);

        long jumlahCocok = hitungJumlahLowonganCocok();
        if (jumlahCocok == 0) {
            ringkasanLabel.setText("Belum ada lowongan yang cocok dengan checklist kamu.");
        } else {
            ringkasanLabel.setText(jumlahCocok + " lowongan baru cocok dengan checklist kamu.");
        }
    }

    private String buatSapaan() {
        int jam = LocalTime.now().getHour();
        if (jam >= 4 && jam < 11)
            return "Selamat pagi";
        if (jam >= 11 && jam < 15)
            return "Selamat siang";
        if (jam >= 15 && jam < 18)
            return "Selamat sore";
        return "Selamat malam";
    }

    private String ambilNamaDepan(String namaLengkap) {
        if (namaLengkap == null || namaLengkap.isBlank())
            return "Pengguna";
        return namaLengkap.trim().split("\\s+")[0];
    }

    private long hitungJumlahLowonganCocok() {
        return semuaLowongan.stream()
                .filter(l -> {
                    int[] skor = hitungSkorKecocokan(l);
                    return skor[1] > 0 && skor[0] > 0;
                })
                .count();
    }

    private void tampilkanRekomendasi() {
        rekomendasiBox.getChildren().clear();

        List<Lowongan> rekomendasi = new ArrayList<>(semuaLowongan);
        rekomendasi.sort((a, b) -> {
            int[] skorA = hitungSkorKecocokan(a);
            int[] skorB = hitungSkorKecocokan(b);

            int cmp = Integer.compare(skorB[0], skorA[0]);
            if (cmp != 0)
                return cmp;
            return Integer.compare(skorB[1], skorA[1]);
        });

        List<Lowongan> top = rekomendasi.stream().limit(6).toList();

        if (top.isEmpty()) {
            rekomendasiKosongLabel.setVisible(true);
            rekomendasiKosongLabel.setManaged(true);
            return;
        }

        rekomendasiKosongLabel.setVisible(false);
        rekomendasiKosongLabel.setManaged(false);

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatRupiah.setMaximumFractionDigits(0);

        for (Lowongan lowongan : top) {
            rekomendasiBox.getChildren().add(buatKartuRekomendasi(lowongan, formatRupiah));
        }
    }

    private HBox buatKartuRekomendasi(Lowongan lowongan, NumberFormat formatRupiah) {
        String namaPerusahaan = lowongan.getPerusahaan() != null
                ? lowongan.getPerusahaan().getNamaPerusahaan()
                : "Perusahaan";

        Label avatar = buatAvatar(namaPerusahaan);
        avatar.setMinSize(40, 40);
        avatar.setPrefSize(40, 40);

        Label nama = new Label(lowongan.getNamaPekerjaan() != null ? lowongan.getNamaPekerjaan() : "-");
        nama.getStyleClass().add("lamaran-terakhir-nama");
        nama.setMaxWidth(Double.MAX_VALUE);
        nama.setEllipsisString("...");

        String lokasi = lowongan.getLokasi() != null ? lowongan.getLokasi() : "-";
        String jenis = kapitalisasi(lowongan.getJenisLowongan());
        Label detail = new Label(namaPerusahaan + " · " + lokasi + " · " + jenis);
        detail.getStyleClass().add("lamaran-terakhir-detail");
        detail.setMaxWidth(Double.MAX_VALUE);
        detail.setEllipsisString("...");

        VBox identitas = new VBox(2, nama, detail);
        identitas.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(identitas, Priority.ALWAYS);

        Label gaji = new Label(formatRupiah.format(lowongan.getGaji()));
        gaji.getStyleClass().add("rekomendasi-gaji");
        gaji.setAlignment(Pos.CENTER_RIGHT);

        int[] skor = hitungSkorKecocokan(lowongan);
        Label skorLabel;
        if (skor[1] == 0) {
            skorLabel = new Label("Belum dihitung");
            skorLabel.getStyleClass().add("rekomendasi-skor-kosong");
        } else {
            skorLabel = new Label(skor[0] + " dari " + skor[1] + " keahlian cocok");
            skorLabel.getStyleClass().add("rekomendasi-skor");
        }
        skorLabel.setAlignment(Pos.CENTER_RIGHT);

        VBox kanan = new VBox(2, gaji, skorLabel);
        kanan.setAlignment(Pos.CENTER_RIGHT);
        kanan.setMinWidth(130);
        kanan.setPrefWidth(130);

        HBox kartu = new HBox(12, avatar, identitas, kanan);
        kartu.setAlignment(Pos.CENTER_LEFT);
        kartu.setPadding(new Insets(12, 14, 12, 14));
        kartu.getStyleClass().add("kartu-rekomendasi");
        kartu.setCursor(Cursor.HAND);

        kartu.setOnMouseClicked(e -> bukaDetailLowongan(lowongan));

        return kartu;
    }

    private void tampilkanLamaranTerakhir() {
        lamaranTerakhirBox.getChildren().clear();

        if (riwayatLamaran.isEmpty()) {
            lamaranKosongLabel.setVisible(true);
            lamaranKosongLabel.setManaged(true);
            return;
        }

        lamaranKosongLabel.setVisible(false);
        lamaranKosongLabel.setManaged(false);

        List<Lamaran> terbaru = riwayatLamaran.stream()
                .sorted(Comparator.comparing(Lamaran::getTanggalLamar,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(4)
                .toList();

        DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern("d MMM", new Locale("id", "ID"));

        for (Lamaran lamaran : terbaru) {
            lamaranTerakhirBox.getChildren().add(buatBarisLamaranTerakhir(lamaran, formatTanggal));
        }
    }

    private HBox buatBarisLamaranTerakhir(Lamaran lamaran, DateTimeFormatter formatTanggal) {
        Lowongan lowongan = lamaran.getLowongan();
        if (lowongan == null)
            return new HBox();

        String namaPerusahaan = lowongan.getPerusahaan() != null
                ? lowongan.getPerusahaan().getNamaPerusahaan()
                : "-";

        Label nama = new Label(lowongan.getNamaPekerjaan() != null ? lowongan.getNamaPekerjaan() : "-");
        nama.getStyleClass().add("rekomendasi-nama");
        nama.setMaxWidth(Double.MAX_VALUE);
        nama.setEllipsisString("...");

        String tanggalStr = lamaran.getTanggalLamar() != null
                ? lamaran.getTanggalLamar().format(formatTanggal)
                : "-";

        Label detail = new Label(namaPerusahaan + " · " + tanggalStr);
        detail.getStyleClass().add("rekomendasi-detail");
        detail.setMaxWidth(Double.MAX_VALUE);
        detail.setEllipsisString("...");

        VBox kiri = new VBox(2, nama, detail);
        kiri.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(kiri, Priority.ALWAYS);

        HBox statusBox = buatStatusBox(lamaran.getStatus());

        HBox baris = new HBox(10, kiri, statusBox);
        baris.setAlignment(Pos.CENTER_LEFT);
        baris.setPadding(new Insets(10, 14, 10, 14));
        baris.getStyleClass().add("lamaran-terakhir-baris");
        baris.setCursor(Cursor.HAND);

        baris.setOnMouseClicked(e -> {
            bukaDetailLowongan(lowongan);
        });

        return baris;
    }

    private HBox buatStatusBox(String status) {
        HBox box = new HBox(6);
        box.setAlignment(Pos.CENTER_LEFT);

        Circle titik = new Circle(4);
        titik.getStyleClass().add("status-titik");

        Label teks = new Label(formatStatus(status));
        teks.getStyleClass().add("status-teks");

        String jenisStatus = status == null ? "default" : status.toLowerCase();

        switch (jenisStatus) {
            case "diproses" -> {
                titik.getStyleClass().add("titik-diproses");
                teks.getStyleClass().add("teks-diproses");
            }
            case "ditinjau" -> {
                titik.getStyleClass().add("titik-ditinjau");
                teks.getStyleClass().add("teks-ditinjau");
            }
            case "diterima" -> {
                titik.getStyleClass().add("titik-diterima");
                teks.getStyleClass().add("teks-diterima");
            }
            case "ditolak" -> {
                titik.getStyleClass().add("titik-ditolak");
                teks.getStyleClass().add("teks-ditolak");
            }
            default -> {
                titik.getStyleClass().add("titik-default");
                teks.getStyleClass().add("teks-default");
            }
        }

        box.getChildren().addAll(titik, teks);
        return box;
    }

    private void perbaruiInfoChecklist() {
        int jumlah = keahlianPelamar.size();

        if (jumlah == 0) {
            checklistInfoLabel.setText("Kamu belum memilih keahlian. Isi checklist supaya rekomendasi lebih akurat.");
        } else {
            // Kalau ada tanggal terakhir update di model, bisa ditambahkan di sini
            checklistInfoLabel.setText(jumlah + " keahlian dipilih. Perbarui supaya rekomendasinya makin pas.");
        }
    }

    private int[] hitungSkorKecocokan(Lowongan lowongan) {
        List<Keahlian> dibutuhkan = lowongan.getDaftarKeahlian();
        if (dibutuhkan == null || dibutuhkan.isEmpty()) {
            return new int[] { 0, 0 };
        }
        if (keahlianPelamar == null || keahlianPelamar.isEmpty()) {
            return new int[] { 0, dibutuhkan.size() };
        }

        Set<Integer> idPelamar = new HashSet<>();
        for (Keahlian k : keahlianPelamar) {
            idPelamar.add(k.getIdKeahlian());
        }

        int cocok = 0;
        for (Keahlian k : dibutuhkan) {
            if (idPelamar.contains(k.getIdKeahlian()))
                cocok++;
        }
        return new int[] { cocok, dibutuhkan.size() };
    }

    private Label buatAvatar(String namaPerusahaan) {
        String inisial = buatInisial(namaPerusahaan);
        Label avatar = new Label(inisial);
        avatar.getStyleClass().add("avatar-lowongan");
        avatar.getStyleClass().add(tentukanWarnaAvatar(namaPerusahaan));
        return avatar;
    }

    private String buatInisial(String nama) {
        if (nama == null || nama.isBlank())
            return "?";
        String[] kata = nama.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String k : kata) {
            if (k.equalsIgnoreCase("PT") || k.equalsIgnoreCase("CV") || k.isBlank())
                continue;
            sb.append(Character.toUpperCase(k.charAt(0)));
            if (sb.length() >= 2)
                break;
        }
        if (sb.length() == 0)
            sb.append(Character.toUpperCase(nama.charAt(0)));
        return sb.toString();
    }

    private String tentukanWarnaAvatar(String nama) {
        if (nama == null)
            return "avatar-biru";
        int hash = Math.abs(nama.hashCode() % 6);
        return switch (hash) {
            case 0 -> "avatar-biru";
            case 1 -> "avatar-ungu";
            case 2 -> "avatar-cyan";
            case 3 -> "avatar-oranye";
            case 4 -> "avatar-hijau";
            default -> "avatar-pink";
        };
    }

    private String kapitalisasi(String teks) {
        if (teks == null || teks.isBlank())
            return "-";
        return teks.substring(0, 1).toUpperCase() + teks.substring(1).toLowerCase();
    }

    private String formatStatus(String status) {
        if (status == null)
            return "-";
        return switch (status.toLowerCase()) {
            case "diproses" -> "Diproses";
            case "ditinjau" -> "Ditinjau";
            case "diterima" -> "Diterima";
            case "ditolak" -> "Ditolak";
            default -> status;
        };
    }

    @FXML
    void handleBukaBeranda(ActionEvent event) {
        mainBorderPane.setCenter(halamanBeranda);
        aturMenuAktif(berandaMenu);
        muatDataBeranda();
    }

    @FXML
    void handleBukaCariLowongan(ActionEvent event) {
        tampilkanHalaman("CariLowonganView.fxml");
    }

    @FXML
    void handleBukaRiwayat(ActionEvent event) {
        tampilkanHalaman("RiwayatLamaranView.fxml");
    }

    @FXML
    void handleBukaChecklist(ActionEvent event) {
        tampilkanHalaman("CheckListMinatBakatView.fxml");
    }

    @FXML
    void handleBukaProfil(MouseEvent event) {
        tampilkanHalaman("ProfilView.fxml");
    }

    @FXML
    void handleCari(ActionEvent event) {
        handleBukaCariLowongan(event);
    }

    @FXML
    void handleKeluar(ActionEvent event) {
        DialogHelper.tampilkanDialogKeluar(
                btnKeluar.getScene().getWindow(),
                "Apakah kamu yakin ingin keluar dari akun KawanSetara?");
    }

    private void aturMenuAktif(ToggleButton menuAktif) {
        ToggleButton[] daftarMenu = { berandaMenu, cariLowonganMenu, riwayatMenu, checklistMenu };
        for (ToggleButton menu : daftarMenu) {
            menu.getStyleClass().remove("menu-aktif");
        }
        if (menuAktif == null) {
            menuGroup.selectToggle(null);
        } else {
            menuAktif.setSelected(true);
            menuAktif.getStyleClass().add("menu-aktif");
        }

        riwayatMenuLabel.getStyleClass().remove("menu-label-aktif");
        if (menuAktif == riwayatMenu) {
            riwayatMenuLabel.getStyleClass().add("menu-label-aktif");
        }
    }

    private void tampilkanHalaman(String namaFile) {
        try {
            String lokasi = "/com/example/projectpbokawansetara/view/" + namaFile;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(lokasi));
            Parent halaman = loader.load();
            BorderPane halamanBorder = (BorderPane) halaman;

            switch (namaFile) {
                case "CariLowonganView.fxml" -> {
                    halamanCariLowongan = halamanBorder.getCenter();
                    CariLowonganController controller = loader.getController();
                    controller.setDashboardController(this);
                }
                case "RiwayatLamaranView.fxml" -> {
                    RiwayatLamaranController controller = loader.getController();
                    controller.setDashboardController(this);
                }
                case "CheckListMinatBakatView.fxml" -> {
                }
                case "ProfilView.fxml" -> {
                    ProfilController controller = loader.getController();
                    controller.setDashboardController(this);
                }
            }

            mainBorderPane.setCenter(halamanBorder.getCenter());

            switch (namaFile) {
                case "CariLowonganView.fxml" -> aturMenuAktif(cariLowonganMenu);
                case "RiwayatLamaranView.fxml" -> aturMenuAktif(riwayatMenu);
                case "CheckListMinatBakatView.fxml" -> aturMenuAktif(checklistMenu);
                case "ProfilView.fxml" -> aturMenuAktif(null);
            }

        } catch (IOException | NullPointerException e) {
            System.err.println("Gagal membuka halaman: " + namaFile);
            e.printStackTrace();
        }
    }

    public void bukaDetailLowongan(Lowongan lowongan) {
        try {
            Node halamanSebelumnya = mainBorderPane.getCenter();
            ToggleButton menuSebelumnya = (ToggleButton) menuGroup.getSelectedToggle();

            String lokasi = "/com/example/projectpbokawansetara/view/DetailLowonganView.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(lokasi));
            Parent halaman = loader.load();

            DetailLowonganController controller = loader.getController();
            controller.setLowongan(lowongan);

            controller.setAksiKembali(() -> {
                mainBorderPane.setCenter(halamanSebelumnya);
                aturMenuAktif(menuSebelumnya);
            });

            BorderPane halamanBorder = (BorderPane) halaman;
            mainBorderPane.setCenter(halamanBorder.getCenter());

        } catch (IOException | NullPointerException e) {
            System.err.println("Gagal membuka detail lowongan.");
            e.printStackTrace();
        }
    }

    private void kembaliKeCariLowongan() {
        if (halamanCariLowongan != null) {
            mainBorderPane.setCenter(halamanCariLowongan);
            aturMenuAktif(cariLowonganMenu);
        }
    }
}