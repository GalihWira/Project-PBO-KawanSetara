package com.example.projectpbokawansetara.controller;

import java.io.IOException;

import com.example.projectpbokawansetara.helper.DialogHelper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class DashboardAdminController {

    @FXML
    private ToggleGroup adminMenuGroup;

    @FXML
    private ToggleButton akunAdminMenu;

    @FXML
    private ImageView avatarImage;

    @FXML
    private TextField cariField;

    @FXML
    private VBox daftarLamaranBox;

    @FXML
    private ToggleButton dashboardMenu;

    @FXML
    private ToggleButton itemChecklistMenu;

    @FXML
    private ToggleButton jenisDisabilitasMenu;

    @FXML
    private Label jumlahAdminMenuLabel;

    @FXML
    private Label jumlahChecklistMenuLabel;

    @FXML
    private Label jumlahDisabilitasMenuLabel;

    @FXML
    private Label jumlahLowonganMenuLabel;

    @FXML
    private Label jumlahPerusahaanMenuLabel;

    @FXML
    private Button keluarButton;

    @FXML
    private Label lamaranKosongLabel;

    @FXML
    private Label lamaranMasukInfoLabel;

    @FXML
    private Label lamaranMasukLabel;

    @FXML
    private Hyperlink lihatSemuaLink;

    @FXML
    private ToggleButton lowonganMenu;

    @FXML
    private Label lowonganTersediaInfoLabel;

    @FXML
    private Label lowonganTersediaLabel;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Label namaUserLabel;

    @FXML
    private Label pelamarTerdaftarInfoLabel;

    @FXML
    private Label pelamarTerdaftarLabel;

    @FXML
    private Label peranUserLabel;

    @FXML
    private ToggleButton perusahaanMenu;

    @FXML
    private Label perusahaanMitraInfoLabel;

    @FXML
    private Label perusahaanMitraLabel;

    @FXML
    private Label tanggalLabel;

    @FXML
    private VBox tindakLanjutBox;

    @FXML
    private Label tindakLanjutKosongLabel;

    @FXML 
    void handleDashboardMenu(ActionEvent event) {
        setMenuAktif(dashboardMenu);
        mainBorderPane.setCenter(kontenDashboardAwal);
    }

    @FXML
    void handleBukaLowongan(ActionEvent event) {
        setMenuAktif(lowonganMenu);
        gantiKontenTengah("/com/example/projectpbokawansetara/view/KelolaLowonganView.fxml");
    }

    @FXML
    void handleBukaPerusahaan(ActionEvent event) {
        setMenuAktif(perusahaanMenu);
        gantiKontenTengah("/com/example/projectpbokawansetara/view/KelolaPerusahaanView.fxml");
    }

    @FXML
    void handleBukaItemChecklist(ActionEvent event) {
        setMenuAktif(itemChecklistMenu);
        gantiKontenTengah("/com/example/projectpbokawansetara/view/KelolaChecklistView.fxml");
    }

    @FXML
    void handleKelolaDisabilitas(ActionEvent event) {
        setMenuAktif(jenisDisabilitasMenu);
        gantiKontenTengah("/com/example/projectpbokawansetara/view/KelolaDisabilitasView.fxml");
    }

    @FXML
    void handleKelolaAkunAdmin(ActionEvent event) {
        setMenuAktif(akunAdminMenu);
        gantiKontenTengah("/com/example/projectpbokawansetara/view/KelolaAkunAdminView.fxml");
    }

    @FXML
    void handleKeluar(ActionEvent event) {
        DialogHelper.tampilkanDialogKeluar(
                keluarButton.getScene().getWindow(),
                "Kamu perlu masuk lagi untuk mengelola data lowongan dan pelamar.");
    }


    private void gantiKontenTengah(String pathFXML) {
        try {
            Parent newPage = FXMLLoader.load(getClass().getResource(pathFXML));
            mainBorderPane.setCenter(newPage);
        } catch (IOException e) {
            System.err.println("Gagal memuat file FXML di lokasi: " + pathFXML);
            e.printStackTrace();
        }
    }

    private Node kontenDashboardAwal;
    @FXML
    public void initialize() {
        kontenDashboardAwal = mainBorderPane.getCenter();
    }

    private void setMenuAktif(ToggleButton menuAktif) {
    ToggleButton[] semuaMenu = {
        dashboardMenu,
        lowonganMenu,
        perusahaanMenu,
        itemChecklistMenu,
        jenisDisabilitasMenu,
        akunAdminMenu
    };

    for (ToggleButton menu : semuaMenu) {
        if (menu != null) {
            menu.setStyle("-fx-background-color: transparent; -fx-background-radius: 8;");
        }
    }

    if (menuAktif != null) {
        menuAktif.setStyle("-fx-background-color: #E2ECF7; -fx-background-radius: 8;");
    }
}


}



