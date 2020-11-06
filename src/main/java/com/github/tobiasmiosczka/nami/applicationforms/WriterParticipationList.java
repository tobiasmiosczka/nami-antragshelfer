package com.github.tobiasmiosczka.nami.applicationforms;

import java.util.List;

import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

public class WriterParticipationList extends AbstractTextDocumentWriter {

    public WriterParticipationList() {
        super();
    }

    @Override
    public void manipulateDoc(List<NamiMitglied> participants, TextDocument odtDoc){
        //participants data
        Table tParticipants = odtDoc.getTableList().get(0);
        for (NamiMitglied m : participants){
            Row row = tParticipants.appendRow();
            if (m != null) {
                //Nachname
                row.getCellByIndex(0).setStringValue(m.getNachname());
                //Vorname
                row.getCellByIndex(1).setStringValue(m.getVorname());
            }
        }
    }

    @Override
    protected String getResourceFileName() {
        return "Anwesenheitsliste.odt";
    }
}
