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
import java.util.Optional;

public class CustomDialog {

    public enum OptionType {
        BOOLEAN,
        STRING,
        DATE
    }

    private final Dialog<List<Object>> dialog;
    private final ObservableList<Node> content;
    private final List<OptionType> options;

    public CustomDialog() {
        dialog = new Dialog<>();
        options = new LinkedList<>();
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
        options.add(OptionType.STRING);
        TextField i = new TextField();
        i.setPromptText(text);
        content.add(i);
        return this;
    }

    public CustomDialog addDateOption(String text) {
        options.add(OptionType.DATE);
        DatePicker i = new DatePicker();
        i.setPromptText(text);
        content.add(i);
        return this;
    }

    public CustomDialog addBooleanOption(String text) {
        options.add(OptionType.BOOLEAN);
        CheckBox i = new CheckBox(text);
        content.add(i);
        return this;
    }

    private List<Object> getValues() {
        List<Object> result = new LinkedList<>();
        for (int i = 0; i < options.size(); ++i) {
            Node n = content.get(i);
            switch (options.get(i)) {
                case DATE: result.add(((DatePicker)n).getValue()); break;
                case BOOLEAN: result.add(((CheckBox)n).isSelected()); break;
                case STRING: result.add(((TextField)n).getText()); break;
            }
        }
        return result;
    }

    public List<Object> getResult() {
        dialog.setResultConverter((ButtonType bt) -> {
            if (bt == ButtonType.OK)
                return getValues();
            return null;
        });
        Optional<List<Object>> optionalResult = dialog.showAndWait();
        return optionalResult.orElse(null);
    }

}
