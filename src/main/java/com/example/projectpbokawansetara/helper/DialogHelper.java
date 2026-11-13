package com.example.projectpbokawansetara.helper;

import java.io.IOException;

import com.example.projectpbokawansetara.controller.KeluarDialogController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class DialogHelper {

    private static final String PATH_DIALOG_KELUAR =
            "/com/example/projectpbokawansetara/view/KeluarDialog.fxml";

    private static final String PATH_LOGIN =
            "/com/example/projectpbokawansetara/view/LoginView.fxml";

    public static void tampilkanDialogKeluar(Window owner, String pesan) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    DialogHelper.class.getResource(PATH_DIALOG_KELUAR));

            Parent root = loader.load();

            KeluarDialogController controller = loader.getController();

            if (pesan != null) {
                controller.setPesan(pesan);
            }

            controller.setOnKeluar(() -> kembaliKeLogin(owner));

            Stage dialog = new Stage();
            dialog.initOwner(owner);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.TRANSPARENT);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            dialog.setScene(scene);
            dialog.show();

        } catch (IOException | NullPointerException e) {
            System.err.println("Gagal membuka dialog keluar: " + PATH_DIALOG_KELUAR);
            e.printStackTrace();
        }
    }

    private static void kembaliKeLogin(Window owner) {
        try {
            Parent root = FXMLLoader.load(
                    DialogHelper.class.getResource(PATH_LOGIN));

            Stage stage = (Stage) owner;

            stage.setScene(new Scene(root));
            stage.setTitle("Masuk - KawanSetara");
            stage.sizeToScene();
            stage.centerOnScreen();

        } catch (IOException | NullPointerException e) {
            System.err.println("Gagal membuka halaman login: " + PATH_LOGIN);
            e.printStackTrace();
        }
    }
}
