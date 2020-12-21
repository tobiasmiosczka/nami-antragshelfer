package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import java.time.LocalDate;
import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.DocUtil;
import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import com.github.tobiasmiosczka.nami.util.GenderUtil;
import nami.connector.namitypes.NamiMitglied;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.wml.Tbl;

import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.createR;
import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.createTr;
import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.findTables;
import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.getTableCellP;
import static com.github.tobiasmiosczka.nami.util.TimeUtil.calcAgeRange;
import static com.github.tobiasmiosczka.nami.util.TimeUtil.getDateString;

@Form(title = "Antrag an Diözese Münster")
public class ApplicationDioezeseMuenster extends DocumentWriter {

	private final String 	mitgliedsVerband,
							traeger,
							plz,
							ort,
							land;
	private final LocalDate datumVon,
							datumBis;
	private final boolean  	freizeit,
							bildung,
	 						ausbildung,
	 						qualitaetssicherung,
	 						grossveranstaltung;

	public ApplicationDioezeseMuenster(
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
	public void manipulateDoc(List<NamiMitglied> participants, WordprocessingMLPackage doc) {
		boolean dateSet = datumVon != null && datumBis != null;
		String dateFromToString = dateSet ? getDateString(datumVon) + " - " + getDateString(datumBis) : "";
		List<Tbl> tblList = findTables(doc.getMainDocumentPart().getContent());
		Tbl tbl = tblList.get(0);
		getTableCellP(tbl, 0, 2).getContent().add(createR(mitgliedsVerband));
		getTableCellP(tbl, 1, 2).getContent().add(createR(traeger));

		tbl = tblList.get(1);
		getTableCellP(tbl, 1, 0).getContent().add(createR(dateFromToString));
		getTableCellP(tbl, 1, 1).getContent().add(createR(plz + " " + ort));

		tbl = tblList.get(5);
		getTableCellP(tbl, 0, 2).getContent().add(createR(mitgliedsVerband));
		getTableCellP(tbl, 1, 2).getContent().add(createR(traeger));

		tbl = tblList.get(6);
		getTableCellP(tbl, 1, 0).getContent().add(createR(dateFromToString));
		getTableCellP(tbl, 1, 1).getContent().add(createR(plz + " " + ort));

		tbl = tblList.get(9);
		for (NamiMitglied p : participants)
			tbl.getContent().add(createTr(
					"",
					"",
					p.getNachname() + ", " + p.getVorname(),
					p.getPLZ() + " " + p.getOrt(),
					"" + GenderUtil.getCharacter(p.getGeschlecht()),
					calcAgeRange(p.getGeburtsDatum(), datumVon, datumBis),
					""));

		tbl = tblList.get(10);
		getTableCellP(tbl, 0, 2).getContent().add(createR(mitgliedsVerband));
		getTableCellP(tbl, 1, 2).getContent().add(createR(traeger));

		tbl = tblList.get(11);
		getTableCellP(tbl, 2, 0).getContent().add(createR(dateFromToString));
		getTableCellP(tbl, 2, 1).getContent().add(createR(plz + " " + ort));

		HeaderPart headerPart = DocUtil.findHeaders(doc).get(2);
		tblList = findTables(headerPart.getContent());
		tbl = tblList.get(0);
		getTableCellP(tbl, 0, 2).getContent().add(createR(mitgliedsVerband));
		getTableCellP(tbl, 1, 2).getContent().add(createR(traeger));

		tbl = tblList.get(1);
		getTableCellP(tbl, 0, 1).getContent().add(createR(dateFromToString));
		getTableCellP(tbl, 0, 3).getContent().add(createR(plz + " " + ort));
		getTableCellP(tbl, 0, 5).getContent().add(createR(land));
		getTableCellP(tbl, 1, 0).getContent().add(createR(freizeit ? "x" : ""));
		getTableCellP(tbl, 1, 2).getContent().add(createR(bildung ? "x" : ""));
		getTableCellP(tbl, 1, 4).getContent().add(createR(ausbildung ? "x" : ""));
		getTableCellP(tbl, 1, 6).getContent().add(createR(qualitaetssicherung ? "x" : ""));
		getTableCellP(tbl, 1, 8).getContent().add(createR(grossveranstaltung ? "x" : ""));
	}

	@Override
	protected String getResourceFileName() {
		return "DPSG-Münster-16.12.2020.docx";
	}
}