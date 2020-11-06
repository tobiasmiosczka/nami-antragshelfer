package com.github.tobiasmiosczka.nami.view;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class DialogUtil {

    public static void showError(String message, Exception e) {
        showMessage(Alert.AlertType.ERROR, "Fehler", message, e.getLocalizedMessage());
    }

    public static void showMessage(Alert.AlertType type, String title, String message, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static File showSaveDialog(File preview, Stage parent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open document type format (*.odt)", "*.odt"));
        if (preview != null)
            fileChooser.setInitialDirectory(preview);
        return fileChooser.showSaveDialog(parent);
    }

}
