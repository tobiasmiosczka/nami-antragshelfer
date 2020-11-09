package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

@Form(title = "Anwesenheitsliste")
public class ParticipationList extends DocumentWriter {

    public ParticipationList() {
        super();
    }

    @Override
    public void manipulateDoc(List<NamiMitglied> participants, TextDocument doc){
        Table tParticipants = doc.getTableList().get(0);
        for (NamiMitglied m : participants){
            Row row = tParticipants.appendRow();
            row.getCellByIndex(0).setStringValue(m.getNachname());
            row.getCellByIndex(1).setStringValue(m.getVorname());
        }
    }

    @Override
    protected String getResourceFileName() {
        return "Anwesenheitsliste.odt";
    }
}
