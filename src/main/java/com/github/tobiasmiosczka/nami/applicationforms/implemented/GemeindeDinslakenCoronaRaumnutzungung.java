package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Form(title = "Corona Raumnutzung der Gemeinde Dinslaken")
public class GemeindeDinslakenCoronaRaumnutzungung extends DocumentWriter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final LocalDate date;
    private final String timeFrom;
    private final String timeTo;

    public GemeindeDinslakenCoronaRaumnutzungung(
            @Option(title = "Datum")LocalDate date,
            @Option(title = "Zeit (von)(HH:MM)")String timeFrom,
            @Option(title = "Zeit (bis)(HH:MM)")String timeTo){
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    @Override
    protected void manipulateDoc(List<NamiMitglied> participants, TextDocument doc) {
        Table tParticipants = doc.getTableList().get(0);
        tParticipants.appendRows(Math.max(participants.size() - 1, 0));
        for (int i = 0; i < participants.size(); ++i){
            NamiMitglied m = participants.get(i);
            Row row = tParticipants.getRowList().get(i + 1);
            if (m == null)
                continue;
            //Datum
            if (date != null)
                row.getCellByIndex(0).setStringValue(DATE_TIME_FORMATTER.format(date));
            //Uhrzeit von
            if (timeFrom != null)
                row.getCellByIndex(1).setStringValue(timeFrom);
            //Uhrzeit bis
            if (timeTo != null)
                row.getCellByIndex(2).setStringValue(timeTo);
            //Vorname
            row.getCellByIndex(3).setStringValue(m.getVorname());
            //Nachname
            row.getCellByIndex(4).setStringValue(m.getNachname());
            //Straße
            row.getCellByIndex(5).setStringValue(m.getStrasse());
            //PLZ, Ort
            row.getCellByIndex(6).setStringValue(m.getPLZ() + ", " + m.getOrt());
            //Telefonnummer
            row.getCellByIndex(7).setStringValue(m.getTelefon1());
        }
    }

    @Override
    protected String getResourceFileName() {
        return "GemeindeDinslakenCoronaRaumnutzung.odt";
    }
}
