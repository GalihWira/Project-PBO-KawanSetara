package com.example.projectpbokawansetara.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class KelolaChecklistController {

    @FXML private TableColumn<?, ?> aksiColumn;
    @FXML private TextField cariField;
    @FXML private TextField cariItemField;
    @FXML private TableColumn<?, ?> diLowonganColumn;
    @FXML private TableColumn<?, ?> dipilihColumn;
    @FXML private HBox errorBanner;
    @FXML private Label errorBannerJudulLabel;
    @FXML private Label errorBannerPesanLabel;
    @FXML private ComboBox<?> filterKategoriComboBox;
    @FXML private TableColumn<?, ?> idColumn;
    @FXML private TableView<?> itemTable;
    @FXML private TableColumn<?, ?> kategoriColumn;
    @FXML private ComboBox<?> kategoriComboBox;
    @FXML private Label kategoriErrorLabel;
    @FXML private TableColumn<?, ?> namaColumn;
    @FXML private HBox namaItemBox;
    @FXML private Label namaItemErrorLabel;
    @FXML private TextField namaItemField;
    @FXML private VBox previewBox;
    @FXML private CheckBox previewCheckBox;
    @FXML private Label previewKategoriLabel;
    @FXML private Label ringkasanLabel;
    @FXML private HBox suksesBanner;
    @FXML private Label suksesBannerJudulLabel;
    @FXML private Label suksesBannerPesanLabel;
    @FXML private Button tambahButton;

    @FXML
    public void initialize() {
        if (errorBanner != null) {
            errorBanner.setVisible(false);
            errorBanner.setManaged(false);
        }
        if (suksesBanner != null) {
            suksesBanner.setVisible(false);
            suksesBanner.setManaged(false);
        }
        if (kategoriErrorLabel != null) {
            kategoriErrorLabel.setVisible(false);
            kategoriErrorLabel.setManaged(false);
        }
        if (namaItemErrorLabel != null) {
            namaItemErrorLabel.setVisible(false);
            namaItemErrorLabel.setManaged(false);
        }
    }

    @FXML
    void handleFilter(ActionEvent event) {
    }

    @FXML
    void handleTambah(ActionEvent event) {
    }

    @FXML
    void handleCari(ActionEvent event) {
    }

    @FXML
    void handleBatal(ActionEvent event) {
    }

    @FXML
    void handleSimpan(ActionEvent event) {
    }
}