package com.example.projectpbokawansetara.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class FormLowonganController {

    @FXML
    private Button batalButton;

    @FXML
    private Label breadcrumbLabel;

    @FXML
    private TextField cariField;

    @FXML
    private TextArea deskripsiArea;

    @FXML
    private ComboBox<?> durasiComboBox;

    @FXML
    private HBox errorBanner;

    @FXML
    private Label errorBannerLabel;

    @FXML
    private RadioButton fullTimeRadio;

    @FXML
    private HBox gajiBox;

    @FXML
    private Label gajiErrorLabel;

    @FXML
    private TextField gajiField;

    @FXML
    private ToggleGroup jenisGroup;

    @FXML
    private Label judulHalamanLabel;

    @FXML
    private Label jumlahDipilihLabel;

    @FXML
    private GridPane keahlianGrid;

    @FXML
    private TextField lokasiField;

    @FXML
    private HBox namaPekerjaanBox;

    @FXML
    private Label namaPekerjaanErrorLabel;

    @FXML
    private TextField namaPekerjaanField;

    @FXML
    private RadioButton partTimeRadio;

    @FXML
    private TextArea persyaratanArea;

    @FXML
    private Label persyaratanErrorLabel;

    @FXML
    private ComboBox<?> perusahaanComboBox;

    @FXML
    private Button simpanButton;

    @FXML
    private ToggleGroup statusGroup;

    @FXML
    private RadioButton tersediaRadio;

    @FXML
    private RadioButton tidakTersediaRadio;

    @FXML
    void handleBatal(ActionEvent event){
        pindahHalaman(event,"/com/example/projectpbokawansetara/view/KelolaLowonganView.fxml");
    }

    void pindahHalaman(ActionEvent event, String fxmlPath){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent halamanBaru = loader.load();

            Parent rootUtama = ((Node) event.getSource()).getScene().getRoot();
            if (rootUtama instanceof BorderPane) {
                ((BorderPane) rootUtama).setCenter(halamanBaru);
            }
        } catch (IOException e) {
            System.err.println("Gagal memuat halaman: " + fxmlPath);
            e.printStackTrace();
        }
    }
}