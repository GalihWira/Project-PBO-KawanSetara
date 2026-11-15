package com.example.projectpbokawansetara.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class KelolaDisabilitasController {

    @FXML private TableColumn<?, ?> aksiColumn;
    @FXML private TextField cariField;
    @FXML private TableView<?> disabilitasTable;
    @FXML private TableColumn<?, ?> idColumn;
    @FXML private Label judulFormLabel;
    @FXML private TableColumn<?, ?> namaColumn;
    @FXML private HBox namaJenisBox;
    @FXML private Label namaJenisErrorLabel;
    @FXML private TextField namaJenisField;
    @FXML private TableColumn<?, ?> pelamarColumn;
    @FXML private Button tambahButton;

    @FXML
    public void initialize() {
        if (namaJenisErrorLabel != null) {
            namaJenisErrorLabel.setVisible(false);
            namaJenisErrorLabel.setManaged(false);
        }
    }

    @FXML
    void handleTambah(ActionEvent event) {
    
    }

    @FXML
    void handleBatal(ActionEvent event) {
    }

    @FXML
    void handleSimpan(ActionEvent event) {
    
    }

    @FXML
    void handleCari(ActionEvent event) {
        
    }
}