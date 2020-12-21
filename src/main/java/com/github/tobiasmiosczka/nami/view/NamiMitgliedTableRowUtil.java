package com.github.tobiasmiosczka.nami.view;

import com.github.tobiasmiosczka.nami.util.GenderUtil;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiGeschlecht;
import nami.connector.namitypes.NamiStufe;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NamiMitgliedTableRowUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static List<TableColumn<NamiMitglied, ?>> getCollumns() {
        TableColumn<NamiMitglied, String> firstNameColumn = new TableColumn<>("Vorname");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("vorname"));

        TableColumn<NamiMitglied, String> lastNameColumn = new TableColumn<>("Nachname");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("nachname"));

        TableColumn<NamiMitglied, NamiStufe> groupColumn = new TableColumn<>("Stufe");
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("stufe"));
        groupColumn.setVisible(false);

        TableColumn<NamiMitglied, Integer> mitgliedsNummerColumn = new TableColumn<>("Mitgliedsnummer");
        mitgliedsNummerColumn.setCellValueFactory(new PropertyValueFactory<>("mitgliedsnummer"));
        mitgliedsNummerColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty)
                    setText(null);
                else
                    setText(String.valueOf(String.valueOf(item)));
            }
        });
        mitgliedsNummerColumn.setVisible(false);

        TableColumn<NamiMitglied, String> gruppierungColumn = new TableColumn<>("Gruppierung");
        gruppierungColumn.setCellValueFactory(new PropertyValueFactory<>("gruppierung"));
        gruppierungColumn.setVisible(false);

        TableColumn<NamiMitglied, NamiGeschlecht> genderColumn = new TableColumn<>("Geschlecht");
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("geschlecht"));
        genderColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(NamiGeschlecht item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty)
                    setText(null);
                else
                    setText(String.valueOf(GenderUtil.getCharacter(item)));
            }
        });
        genderColumn.setVisible(false);

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
                mitgliedsNummerColumn,
                firstNameColumn,
                lastNameColumn,
                groupColumn,
                genderColumn,
                birthdateCollumn,
                gruppierungColumn);
    }
}
