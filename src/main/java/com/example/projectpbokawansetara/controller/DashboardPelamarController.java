package com.example.projectpbokawansetara.controller;
import com.example.projectpbokawansetara.helper.DialogHelper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
public class DashboardPelamarController {

    @FXML
    private Button btnKeluar;

    @FXML
    void handleKeluar(ActionEvent event) {
        DialogHelper.tampilkanDialogKeluar(
                btnKeluar.getScene().getWindow(),
                "Apakah kamu yakin ingin keluar dari akun KawanSetara?");
    }
}
