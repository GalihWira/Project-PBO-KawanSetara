package com.example.projectpbokawansetara.controller;

import java.sql.SQLException;

import com.example.projectpbokawansetara.dao.UserDAO;
import com.example.projectpbokawansetara.model.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

public class KelolaAkunAdminController {

    @FXML
    private TableView<Admin> adminTable;

    @FXML
    private TextField cariField;

    @FXML
    private TableColumn<Admin, String> emailColumn;

    @FXML
    private TableColumn<Admin, String> namaColumn;

    private final UserDAO userDAO = new UserDAO();
    private final ObservableList<Admin> semuaAdmin = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        muatDataAdmin();
    }

    private void muatDataAdmin() {
        try {
            semuaAdmin.setAll(userDAO.getSemuaAdmin());
            adminTable.setItems(semuaAdmin);
        } catch (SQLException e) {
            adminTable.setPlaceholder(new Label("Gagal memuat data admin."));
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCari(KeyEvent event) {
        String kataKunci = cariField.getText().trim().toLowerCase();
        ObservableList<Admin> hasilCari = FXCollections.observableArrayList();

        for (Admin admin : semuaAdmin) {
            boolean cocokNama = admin.getNama().toLowerCase().contains(kataKunci);
            boolean cocokEmail = admin.getEmail().toLowerCase().contains(kataKunci);

            if (cocokNama || cocokEmail) {
                hasilCari.add(admin);
            }
        }

        adminTable.setItems(hasilCari);
    }
}