package com.github.tobiasmiosczka.nami.view;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

public class CustomDialog {

    private final Dialog<List<Object>> dialog;
    private final ObservableList<Node> content;

    public CustomDialog() {
        dialog = new Dialog<>();
        DialogPane dialogPane = dialog.getDialogPane();
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(IconUtil.getIcon());
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        VBox vBox = new VBox(8);
        dialogPane.setContent(vBox);
        content = vBox.getChildren();
    }

    public CustomDialog setTitle(String title) {
        dialog.setTitle(title);
        return this;
    }

    public CustomDialog setHeaderText(String headerText) {
        dialog.setHeaderText(headerText);
        return this;
    }

    public CustomDialog addStringOption(String text) {
        TextField i = new TextField();
        i.setPromptText(text);
        content.add(i);
        return this;
    }

    public CustomDialog addDateOption(String text) {
        DatePicker i = new DatePicker();
        i.setPromptText(text);
        content.add(i);
        return this;
    }

    public CustomDialog addBooleanOption(String text) {
        CheckBox i = new CheckBox(text);
        content.add(i);
        return this;
    }

    private List<Object> getValues() {
        List<Object> result = new LinkedList<>();
        for (Node node : content) {
            if (node instanceof DatePicker)
                result.add(((DatePicker)node).getValue());
            if (node instanceof  CheckBox)
                result.add(((CheckBox)node).isSelected());
            if (node instanceof TextField)
                result.add(((TextField)node).getText());
        }
        return result;
    }

    public List<Object> getResult() {
        dialog.setResultConverter((ButtonType bt) -> {
            if (bt == ButtonType.OK)
                return getValues();
            return null;
        });
        return dialog.showAndWait().orElse(null);
    }

}
