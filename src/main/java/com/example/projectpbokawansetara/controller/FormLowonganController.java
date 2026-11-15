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

    @FXML private Button batalButton;
    @FXML private Label breadcrumbLabel;
    @FXML private TextField cariField;
    @FXML private TextArea deskripsiArea;
    @FXML private HBox errorBanner;
    @FXML private Label errorBannerLabel;
    @FXML private RadioButton fullTimeRadio;
    @FXML private HBox gajiBox;
    @FXML private Label gajiErrorLabel;
    @FXML private TextField gajiField;
    @FXML private ToggleGroup jenisGroup;
    @FXML private Label judulHalamanLabel;
    @FXML private Label jumlahDipilihLabel;
    @FXML private GridPane keahlianGrid;
    @FXML private Label lokasiErrorLabel;
    @FXML private TextField lokasiField;
    @FXML private HBox namaPekerjaanBox;
    @FXML private Label namaPekerjaanErrorLabel;
    @FXML private TextField namaPekerjaanField;
    @FXML private RadioButton partTimeRadio;
    @FXML private TextArea persyaratanArea;
    @FXML private Label persyaratanErrorLabel;
    @FXML private ComboBox<?> perusahaanComboBox;
    @FXML private Label perusahaanErrorLabel;
    @FXML private Button simpanButton;
    @FXML private ToggleGroup statusGroup;
    @FXML private RadioButton tersediaRadio;
    @FXML private RadioButton tidakTersediaRadio;

    @FXML
    void handleBatal(ActionEvent event) {
        pindahHalaman(event, "/com/example/projectpbokawansetara/view/KelolaLowonganView.fxml");
    }

    @FXML
    void handleSimpan(ActionEvent event) {
        System.out.println("Tombol Simpan diklik");
    }

    private void pindahHalaman(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent halamanBaru = loader.load();

            Node kontenTengah = halamanBaru;
            if (halamanBaru instanceof BorderPane halamanBorder && halamanBorder.getCenter() != null) {
                kontenTengah = halamanBorder.getCenter();
            }

            BorderPane mainBorderPane = (BorderPane) ((Node) event.getSource()).getScene().lookup("#mainBorderPane");
            if (mainBorderPane != null) {
                mainBorderPane.setCenter(kontenTengah);
            } else {
                ((Node) event.getSource()).getScene().setRoot(halamanBaru);
            }
        } catch (IOException e) {
            System.err.println("Gagal pindah ke: " + fxmlPath);
            e.printStackTrace();
        }
    }
}