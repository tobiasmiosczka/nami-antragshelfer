package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import java.time.LocalDate;
import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.annotations.Participants;
import com.github.tobiasmiosczka.nami.applicationforms.api.Document;
import com.github.tobiasmiosczka.nami.applicationforms.api.Table;
import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import nami.connector.namitypes.NamiMitglied;

import static com.github.tobiasmiosczka.nami.util.GenderUtil.getCharacter;
import static com.github.tobiasmiosczka.nami.util.TimeUtil.*;

@Form(title = "Antrag an Diözese Münster")
public class ApplicationDioezeseMuenster extends DocumentWriter {

    private final List<NamiMitglied> participants;
    private final String mitgliedsVerband;
    private final String traeger;
    private final String plz;
    private final String ort;
    private final String land;
    private final LocalDate datumVon;
    private final LocalDate datumBis;
    private final boolean freizeit;
    private final boolean bildung;
    private final boolean ausbildung;
    private final boolean qualitaetssicherung;
    private final boolean grossveranstaltung;

    public ApplicationDioezeseMuenster(
            @Participants List<NamiMitglied> participants,
            @Option(title = "Mitgliedsverband") String mitgliedsVerband,
            @Option(title = "Träger") String traeger,
            @Option(title = "Postleitzahl") String plz,
            @Option(title = "Ort") String ort,
            @Option(title = "Land") String land,
            @Option(title = "Datum (von)") LocalDate datumVon,
            @Option(title = "Datum (bis)") LocalDate datumBis,
            @Option(title = "Freizeit") boolean freizeit,
            @Option(title = "Bildung") boolean bildung,
            @Option(title = "Aus-/Fortbildung") boolean ausbildung,
            @Option(title = "Qualitätssicherung") boolean qualitaetssicherung,
            @Option(title = "Großveranstaltung") boolean grossveranstaltung) {
        super();
        this.participants = participants;
        this.mitgliedsVerband = mitgliedsVerband;
        this.traeger = traeger;
        this.datumVon = datumVon;
        this.datumBis = datumBis;
        this.plz = plz;
        this.ort = ort;
        this.land = land;
        this.freizeit = freizeit;
        this.bildung = bildung;
        this.ausbildung = ausbildung;
        this.qualitaetssicherung = qualitaetssicherung;
        this.grossveranstaltung = grossveranstaltung;
    }

    @Override
    public void manipulateDoc(Document document) {
        String dateFromToString = datumVon != null && datumBis != null ? getDateString(datumVon) + " - " + getDateString(datumBis) : "";
        List<Table> tables = document.getMainDocumentPart().getTables();

        Table table = tables.get(0);
        table.cellAt(0, 2).add(mitgliedsVerband);
        table.cellAt(1, 2).add(traeger);

        table = tables.get(1);
        table.cellAt(1, 0).add(dateFromToString);
        table.cellAt(1, 1).add(plz + " " + ort);

        table = tables.get(5);
        table.cellAt(0, 2).add(mitgliedsVerband);
        table.cellAt(1, 2).add(traeger);

        table = tables.get(6);
        table.cellAt(1, 0).add(dateFromToString);
        table.cellAt(1, 1).add(plz + " " + ort);

        fillParticipants(tables.get(9));

        table = tables.get(10);
        table.cellAt(0, 2).add(mitgliedsVerband);
        table.cellAt(1, 2).add(traeger);

        table = tables.get(11);
        table.cellAt(2, 0).add(dateFromToString);
        table.cellAt(2, 1).add(plz + " " + ort);

        //tables = findTables(findHeaders(doc).get(2).getContent());
        tables = document.getHeaders().get(2).getTables();

        table = tables.get(0);
        table.cellAt(0, 2).add(mitgliedsVerband);
        table.cellAt(1, 2).add(traeger);
        table = tables.get(1);
        table.cellAt(0, 1).add(dateFromToString);
        table.cellAt(0, 3).add(plz + " " + ort);
        table.cellAt(0, 5).add(land);
        table.cellAt(1, 0).add(freizeit ? "x" : "");
        table.cellAt(1, 2).add(bildung ? "x" : "");
        table.cellAt(1, 4).add(ausbildung ? "x" : "");
        table.cellAt(1, 6).add(qualitaetssicherung ? "x" : "");
        table.cellAt(1, 8).add(grossveranstaltung ? "x" : "");
    }

    private void fillParticipants(Table table) {
        for (NamiMitglied e : participants) {
            table.addRow("",
                    "",
                    e.getNachname() + ", " + e.getVorname(),
                    e.getPlz() + " " + e.getOrt(),
                    String.valueOf(getCharacter(e.getGeschlecht())),
                    calcAgeRange(e.getGeburtsDatum(), datumVon, datumBis),
                    "");
        }
    }

	@Override
    protected String getResourceFileName() {
        return "DPSG-Münster-16.12.2020.docx";
    }
}