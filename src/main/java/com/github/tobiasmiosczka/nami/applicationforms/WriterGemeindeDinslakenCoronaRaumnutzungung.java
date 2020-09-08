package com.github.tobiasmiosczka.nami.applicationforms;

import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WriterGemeindeDinslakenCoronaRaumnutzungung extends AbstractTextDocumentWriter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private LocalDate date;
    private String timeFrom;
    private String timeTo;

    public WriterGemeindeDinslakenCoronaRaumnutzungung(
            LocalDate date,
            String timeFrom,
            String timeTo){
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    @Override
    protected int getMaxParticipantsPerPage() {
        return 0;
    }

    @Override
    protected void manipulateDoc(List<NamiMitglied> participants, TextDocument odtDoc) {
        Table tParticipants = odtDoc.getTableList().get(0);
        boolean first = true;
        for (NamiMitglied m : participants){
            Row row;
            if (first) {
                row = tParticipants.getRowByIndex(1);
                first = false;
            } else {
                row = tParticipants.appendRow();
            }
            if (m == null)
                continue;
            //Datum
            row.getCellByIndex(0).setStringValue(DATE_TIME_FORMATTER.format(date));
            //Uhrzeit von
            row.getCellByIndex(1).setStringValue(timeFrom);
            //Uhrzeit bis
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
