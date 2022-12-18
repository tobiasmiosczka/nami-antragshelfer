package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Participants;
import com.github.tobiasmiosczka.nami.applicationforms.api.Document;
import com.github.tobiasmiosczka.nami.applicationforms.api.Table;
import nami.connector.namitypes.NamiMitglied;

@Form(title = "Anwesenheitsliste")
public class ParticipationList extends DocumentWriter {

    private final List<NamiMitglied> participants;

    public ParticipationList(@Participants List<NamiMitglied> participants) {
        super();
        this.participants = participants;
    }

    @Override
    public void manipulateDoc(Document document){
        Table table = document.getMainDocumentPart().getTables().get(0);
        for (NamiMitglied p : participants)
            table.addRow(
                    p.getNachname(),
                    p.getVorname(),
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
            );
    }

    @Override
    protected String getResourceFileName() {
        return "Anwesenheitsliste.docx";
    }
}
