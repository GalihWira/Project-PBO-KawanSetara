package com.example.projectpbokawansetara.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    void handleBukaLowongan(MouseEvent event) {
        gantiKontenTengah("/com/example/projectpbokawansetara/view/KelolaLowonganView.fxml");
    }

    @FXML
    void handleBukaPerusahaan(MouseEvent event) {
        gantiKontenTengah("/com/example/projectpbokawansetara/view/KelolaPerusahaanView.fxml");
    }

    @FXML
    void handleBukaItemChecklist(MouseEvent event) {
        gantiKontenTengah("/com/example/projectpbokawansetara/view/KelolaChecklistView.fxml");
    }

    @FXML 
    void handleKelolaDisabilitas(MouseEvent event){
        gantiKontenTengah("/com/example/projectpbokawansetara/view/KelolaDisabilitasView.fxml");
    }

    @FXML 
    void handleKelolaAkunAdmin(MouseEvent event){
        gantiKontenTengah("/com/example/projectpbokawansetara/view/KelolaAkunAdminView.fxml");
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
}


