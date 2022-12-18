package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Participants;
import com.github.tobiasmiosczka.nami.applicationforms.api.Document;
import com.github.tobiasmiosczka.nami.applicationforms.api.Table;
import nami.connector.namitypes.NamiMitglied;

import java.time.LocalDate;
import java.util.List;

import static com.github.tobiasmiosczka.nami.util.TimeUtil.getDateString;

@Form(title = "Corona Raumnutzung der Gemeinde Dinslaken")
public class GemeindeDinslakenCoronaRaumnutzungung extends DocumentWriter {

    private final List<NamiMitglied> participants;
    private final LocalDate date;
    private final String timeFrom;
    private final String timeTo;

    public GemeindeDinslakenCoronaRaumnutzungung(
            @Participants List<NamiMitglied> participants,
            @Option(title = "Datum")LocalDate date,
            @Option(title = "Zeit (von)(HH:MM)")String timeFrom,
            @Option(title = "Zeit (bis)(HH:MM)")String timeTo){
        this.participants = participants;
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    @Override
    protected void manipulateDoc(Document document) {
        Table table = document.getMainDocumentPart().getTables().get(0);
        for (NamiMitglied p : participants)
            table.addRow(
                    getDateString(date),
                    timeFrom,
                    timeTo,
                    p.getVorname(),
                    p.getNachname(),
                    p.getStrasse(),
                    p.getPlz() + " " + p.getOrt(),
                    p.getTelefon1(),
                    "");
        table.setBorders(4);
    }

    @Override
    protected String getResourceFileName() {
        return "GemeindeDinslakenCoronaRaumnutzung.docx";
    }
}
