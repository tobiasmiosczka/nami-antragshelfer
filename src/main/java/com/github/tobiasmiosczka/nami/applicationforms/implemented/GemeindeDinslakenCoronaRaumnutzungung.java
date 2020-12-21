package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import nami.connector.namitypes.NamiMitglied;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Tbl;

import java.time.LocalDate;
import java.util.List;

import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.createTr;
import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.findTables;
import static com.github.tobiasmiosczka.nami.util.TimeUtil.getDateString;

@Form(title = "Corona Raumnutzung der Gemeinde Dinslaken")
public class GemeindeDinslakenCoronaRaumnutzungung extends DocumentWriter {

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
    protected void manipulateDoc(List<NamiMitglied> participants, WordprocessingMLPackage doc) {
        Tbl tbl = findTables(doc.getMainDocumentPart().getContent()).get(0);
        for (NamiMitglied p : participants)
            tbl.getContent().add(createTr(
                    getDateString(date),
                    timeFrom,
                    timeTo,
                    p.getVorname(),
                    p.getNachname(),
                    p.getStrasse(),
                    p.getPLZ() + " " + p.getOrt(),
                    p.getTelefon1(),
                    ""));
    }

    @Override
    protected String getResourceFileName() {
        return "GemeindeDinslakenCoronaRaumnutzung.docx";
    }
}
