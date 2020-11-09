package com.github.tobiasmiosczka.nami.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Collection;

public class DialogUtil {

    public static void showError(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(IconUtil.getIcon());
        alert.setHeaderText(message);
        alert.setContentText(e.getLocalizedMessage());
        alert.showAndWait();
    }

    public static void showMessage(String title, String message, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(IconUtil.getIcon());
        alert.setHeaderText(message);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static boolean showChooseDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "",
                ButtonType.OK,
                ButtonType.CANCEL);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(IconUtil.getIcon());
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return (alert.showAndWait().orElse(ButtonType.CLOSE) == ButtonType.OK);
    }

    public static File showSaveDialog(File preview, Stage parent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open document type format (*.odt)", "*.odt"));
        if (preview != null)
            fileChooser.setInitialDirectory(preview);
        return fileChooser.showSaveDialog(parent);
    }

    public static File showSaveDialog() {
        return showSaveDialog(null, null);
    }

    public static <T> T showChoiceDialog(Collection<T> options, String title, String header, String content) {
        ChoiceDialog<T> dialog = new ChoiceDialog<>(null, options);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(IconUtil.getIcon());
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        return dialog.showAndWait().orElse(null);
    }
}
