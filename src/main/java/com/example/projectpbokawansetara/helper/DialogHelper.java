package com.example.projectpbokawansetara.helper;

import java.io.IOException;

import com.example.projectpbokawansetara.controller.KeluarDialogController;
import com.example.projectpbokawansetara.controller.LowonganBerhasilController;
import com.example.projectpbokawansetara.model.Session;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class DialogHelper {

    private static final String PATH_DIALOG_KELUAR = "/com/example/projectpbokawansetara/view/KeluarDialog.fxml";
    private static final String PATH_DIALOG_LOWONGAN_BERHASIL = "/com/example/projectpbokawansetara/view/LowonganBerhasilDialog.fxml";

    public static void tampilkanDialogKeluar(Window owner, String pesan) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    DialogHelper.class.getResource(PATH_DIALOG_KELUAR));

            Parent root = loader.load();

            KeluarDialogController controller = loader.getController();

            if (pesan != null) {
                controller.setPesan(pesan);
            }

            Stage stage = (Stage) owner;
            Scene scene = stage.getScene();

            Parent halamanAwal = scene.getRoot();

            StackPane pembungkus = new StackPane();
            StackPane overlay = new StackPane();

            pembungkus.getStylesheets().add(
                    DialogHelper.class.getResource("/css/style.css").toExternalForm());

            overlay.getStyleClass().add("overlay-dialog");

            overlay.setPickOnBounds(true);

            if (root instanceof Region) {
                ((Region) root).setMaxSize(
                        Region.USE_PREF_SIZE,
                        Region.USE_PREF_SIZE);
            }

            overlay.getChildren().add(root);

            scene.setRoot(pembungkus);

            pembungkus.getChildren().addAll(
                    halamanAwal,
                    overlay);

            Runnable tutupOverlay = () -> {
                pembungkus.getChildren().clear();
                scene.setRoot(halamanAwal);
            };

            controller.setOnBatal(tutupOverlay);

            controller.setOnKeluar(() -> {
                Session.logout();
                tutupOverlay.run();
                kembaliKeLogin();
            });

        } catch (IOException | NullPointerException e) {
            System.err.println("Gagal membuka dialog keluar: " + PATH_DIALOG_KELUAR);
            e.printStackTrace();
        }
    }

    private static void kembaliKeLogin() {
        SceneManager.pindah("LoginView.fxml", "Masuk - KawanSetara");
    }

    public static void tampilkanDialogLowonganBerhasil(Window owner, String posisi, String namaPerusahaan,
            Runnable onTambahLagi, Runnable onLihatDaftar) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    DialogHelper.class.getResource(PATH_DIALOG_LOWONGAN_BERHASIL));

            Parent root = loader.load();

            LowonganBerhasilController controller = loader.getController();
            controller.setData(posisi, namaPerusahaan);

            Stage stage = (Stage) owner;
            Scene scene = stage.getScene();

            Parent halamanAwal = scene.getRoot();

            StackPane pembungkus = new StackPane();
            StackPane overlay = new StackPane();

            pembungkus.getStylesheets().add(
                    DialogHelper.class.getResource("/css/style.css").toExternalForm());

            overlay.getStyleClass().add("overlay-dialog");
            overlay.setPickOnBounds(true);

            if (root instanceof Region) {
                ((Region) root).setMaxSize(
                        Region.USE_PREF_SIZE,
                        Region.USE_PREF_SIZE);
            }

            overlay.getChildren().add(root);

            scene.setRoot(pembungkus);

            pembungkus.getChildren().addAll(
                    halamanAwal,
                    overlay);

            Runnable tutupOverlay = () -> {
                pembungkus.getChildren().clear();
                scene.setRoot(halamanAwal);
            };

            controller.setOnTambahLagi(() -> {
                tutupOverlay.run();
                onTambahLagi.run();
            });

            controller.setOnLihatDaftar(() -> {
                tutupOverlay.run();
                onLihatDaftar.run();
            });

        } catch (IOException | NullPointerException e) {
            System.err.println("Gagal membuka dialog: " + PATH_DIALOG_LOWONGAN_BERHASIL);
            e.printStackTrace();
            onLihatDaftar.run();
        }
    }
}