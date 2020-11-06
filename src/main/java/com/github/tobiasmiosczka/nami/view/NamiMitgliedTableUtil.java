package com.github.tobiasmiosczka.nami.view;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.enums.NamiGeschlecht;
import nami.connector.namitypes.enums.NamiStufe;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NamiMitgliedTableUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static char getGenderChar(NamiGeschlecht gender) {
        switch (gender) {
            case MAENNLICH:  return 'm';
            case WEIBLICH: return 'w';
            case KEINE_ANGABE: return '-';
            default: return ' ';
        }
    }

    public static List<TableColumn<NamiMitglied, ?>> getCollumns() {
        TableColumn<NamiMitglied, String> firstNameColumn = new TableColumn<>("Vorname");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("vorname"));

        TableColumn<NamiMitglied, String> lastNameColumn = new TableColumn<>("Nachname");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("nachname"));

        TableColumn<NamiMitglied, NamiStufe> stufeCollumn = new TableColumn<>("Stufe");
        stufeCollumn.setCellValueFactory(new PropertyValueFactory<>("stufe"));
        stufeCollumn.setVisible(false);

        TableColumn<NamiMitglied, NamiGeschlecht> genderCollumn = new TableColumn<>("Geschlecht");
        genderCollumn.setCellValueFactory(new PropertyValueFactory<>("geschlecht"));
        genderCollumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(NamiGeschlecht item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty)
                    setText(null);
                else
                    setText(String.valueOf(getGenderChar(item)));
            }
        });
        genderCollumn.setVisible(false);

        TableColumn<NamiMitglied, LocalDateTime> birthdateCollumn = new TableColumn<>("Geburtsdatum");
        birthdateCollumn.setCellValueFactory(new PropertyValueFactory<>("geburtsDatum"));
        birthdateCollumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty)
                    setText(null);
                else
                    setText(DATE_FORMATTER.format(item));
            }
        });

        birthdateCollumn.setVisible(false);

        return List.of(
                firstNameColumn,
                lastNameColumn,
                stufeCollumn,
                genderCollumn,
                birthdateCollumn);
    }

}
