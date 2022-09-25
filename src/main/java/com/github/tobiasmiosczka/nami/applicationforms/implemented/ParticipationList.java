package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import nami.connector.namitypes.NamiMitglied;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tr;

import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.createTr;
import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.findTables;

@Form(title = "Anwesenheitsliste")
public class ParticipationList extends DocumentWriter {

    public ParticipationList() {
        super();
    }

    @Override
    public void manipulateDoc(List<NamiMitglied> participants, WordprocessingMLPackage doc){
        findTables(doc.getMainDocumentPart().getContent()).get(0)
                .getContent()
                .addAll(memberToTr(participants));
    }

    private List<Tr> memberToTr(List<NamiMitglied> member) {
        return member.stream()
                .map(this::memberToTr)
                .toList();
    }

    private Tr memberToTr(NamiMitglied member) {
        return createTr(
                member.getNachname(),
                member.getVorname(),
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
        );
    }

    @Override
    protected String getResourceFileName() {
        return "Anwesenheitsliste.docx";
    }
}
