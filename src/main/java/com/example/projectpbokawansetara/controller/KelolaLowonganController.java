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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

public class KelolaLowonganController {

    @FXML
    private TableColumn<?, ?> aksiColumn;

    @FXML
    private Button berikutnyaButton;

    @FXML
    private TextField cariField;

    @FXML
    private TextField cariPekerjaanField;

    @FXML
    private TableColumn<?, ?> durasiColumn;

    @FXML
    private TableColumn<?, ?> gajiColumn;

    @FXML
    private Label infoHalamanLabel;

    @FXML
    private Label infoTampilLabel;

    @FXML
    private TableColumn<?, ?> jenisColumn;

    @FXML
    private ComboBox<?> jenisComboBox;

    @FXML
    private TableColumn<?, ?> lokasiColumn;

    @FXML
    private ComboBox<?> lokasiComboBox;

    @FXML
    private TableView<?> lowonganTable;

    @FXML
    private TableColumn<?, ?> pelamarColumn;

    @FXML
    private ComboBox<?> perusahaanComboBox;

    @FXML
    private TableColumn<?, ?> posisiColumn;

    @FXML
    private Label ringkasanLabel;

    @FXML
    private Button sebelumnyaButton;

    @FXML
    private ToggleButton semuaTab;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private ToggleGroup statusTabGroup;

    @FXML
    private Button tambahLowonganButton;

    @FXML
    private ToggleButton tersediaTab;

    @FXML
    private ToggleButton tidakTersediaTab;

    @FXML
    void handleTambah(ActionEvent event){
        pindahHalaman(event, "/com/example/projectpbokawansetara/view/FormLowonganView.fxml");
    }
    
    @FXML
    void handleFilter(ActionEvent event) {
        // nanti diisi: filter tabel
    }

    @FXML
    void handleSebelumnya(ActionEvent event) {
        // nanti diisi: halaman tabel sebelumnya
    }

    @FXML
    void handleBerikutnya(ActionEvent event) {
        // nanti diisi: halaman tabel berikutnya
    }

    void pindahHalaman(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent halamanBaru = loader.load();

            BorderPane mainBorderPane = (BorderPane) ((Node) event.getSource()).getScene().lookup("#mainBorderPane");
            if (mainBorderPane != null) {
                mainBorderPane.setCenter(halamanBaru);
            }
        } catch (IOException e) {
            System.err.println("Gagal memuat halaman: " + fxmlPath);
            e.printStackTrace();
        }

}
}