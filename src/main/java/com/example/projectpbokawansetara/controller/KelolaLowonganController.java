package com.example.projectpbokawansetara.controller;

import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

public class KelolaLowonganController {

    @FXML private TableColumn<?, ?> aksiColumn;
    @FXML private Button berikutnyaButton;
    @FXML private TextField cariField;
    @FXML private TextField cariPekerjaanField;
    @FXML private TableColumn<?, ?> durasiColumn;
    @FXML private TableColumn<?, ?> gajiColumn;
    @FXML private Label infoHalamanLabel;
    @FXML private Label infoTampilLabel;
    @FXML private TableColumn<?, ?> jenisColumn;
    @FXML private ComboBox<String> jenisComboBox;
    @FXML private TableColumn<?, ?> lokasiColumn;
    @FXML private ComboBox<String> lokasiComboBox;
    @FXML private TableView<?> lowonganTable;
    @FXML private TableColumn<?, ?> pelamarColumn;
    @FXML private TableColumn<?, ?> perusahaanColumn;
    @FXML private ComboBox<String> perusahaanComboBox;
    @FXML private TableColumn<?, ?> posisiColumn;
    @FXML private Label ringkasanLabel;
    @FXML private Button sebelumnyaButton;
    @FXML private ToggleButton semuaTab;
    @FXML private TableColumn<?, ?> statusColumn;
    @FXML private ToggleGroup statusTabGroup;
    @FXML private Button tambahLowonganButton;
    @FXML private ToggleButton tersediaTab;
    @FXML private ToggleButton tidakTersediaTab;

    @FXML
    void handleTambah(ActionEvent event) {
        pindahHalaman(event, "/com/example/projectpbokawansetara/view/FormLowonganView.fxml");
    }

    @FXML
    void handleFilter(ActionEvent event) {
    }

    @FXML
    void handleSebelumnya(ActionEvent event) {
    }

    @FXML
    void handleBerikutnya(ActionEvent event) {
    }

    private void pindahHalaman(ActionEvent event, String fxmlPath) {
        try {
            URL url = getClass().getResource(fxmlPath);
            if (url == null) {
                System.err.println("File FXML tidak ditemukan: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent halamanBaru = loader.load();

            Node kontenTengah = halamanBaru;
            if (halamanBaru instanceof BorderPane halamanBorder && halamanBorder.getCenter() != null) {
                kontenTengah = halamanBorder.getCenter();
            }

            Scene scene = ((Node) event.getSource()).getScene();
            BorderPane mainBorderPane = (BorderPane) scene.lookup("#mainBorderPane");

            if (mainBorderPane != null) {
                mainBorderPane.setCenter(kontenTengah);
            } else {
                scene.setRoot(halamanBaru);
            }
        } catch (Exception e) {
            System.err.println("Gagal pindah ke: " + fxmlPath);
            e.printStackTrace();
        }
    }
}