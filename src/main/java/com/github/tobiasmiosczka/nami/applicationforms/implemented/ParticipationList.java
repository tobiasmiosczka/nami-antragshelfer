package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import nami.connector.namitypes.NamiMitglied;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Tbl;

import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.createTr;
import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.findTables;

@Form(title = "Anwesenheitsliste")
public class ParticipationList extends DocumentWriter {

    public ParticipationList() {
        super();
    }

    @Override
    public void manipulateDoc(List<NamiMitglied> participants, WordprocessingMLPackage doc){
        Tbl tbl = findTables(doc.getMainDocumentPart().getContent()).get(0);
        for (NamiMitglied p : participants)
            tbl.getContent().add(createTr(
                    p.getNachname(),
                    p.getVorname(),
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
            ));
    }

    @Override
    protected String getResourceFileName() {
        return "Anwesenheitsliste.docx";
    }
}
