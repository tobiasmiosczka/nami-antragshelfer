package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import java.time.LocalDate;
import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import nami.connector.namitypes.NamiMitglied;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Tbl;

import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.createR;
import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.createTr;
import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.findHeaders;
import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.findTables;
import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.getTableCellP;
import static com.github.tobiasmiosczka.nami.util.TimeUtil.calcAgeRange;
import static com.github.tobiasmiosczka.nami.util.TimeUtil.getDateString;

@Form(title = "Antrag an BDKJ Dinslaken")
public class ApplicationBdkjDinslaken extends DocumentWriter {

	private final String 	massnahme,
							plz,
							ort,
							tagungsstaette;
	private final LocalDate datumVon,
							datumBis;

	public ApplicationBdkjDinslaken(
			@Option(title = "Maßnahme") String massnahme,
			@Option(title = "Datum (von)") LocalDate datumVon,
			@Option(title = "Datum (bis)") LocalDate datumBis,
			@Option(title = "PLZ") String plz,
			@Option(title = "Ort") String ort,
			@Option(title = "Tagungsstätte") String tagungsstaette) {
		super();
		this.massnahme = massnahme;
		this.datumVon = datumVon;
		this.datumBis = datumBis;
		this.plz = plz;
		this.ort = ort;
		this.tagungsstaette = tagungsstaette;
	}

	@Override
	public void manipulateDoc(List<NamiMitglied> participants, WordprocessingMLPackage doc) {
		Tbl tblMain = findTables(doc.getMainDocumentPart().getContent()).get(0);
		if (datumVon != null)
			getTableCellP(tblMain, 5, 1).getContent().add(createR(getDateString(datumVon)));
		if (datumBis != null)
			getTableCellP(tblMain, 5, 3).getContent().add(createR(getDateString(datumBis)));
		getTableCellP(tblMain, 5, 5).getContent().add(createR(plz + " " + ort));
		getTableCellP(tblMain, 7, 1).getContent().add(createR(tagungsstaette));

		Tbl tblEvents = findTables(findHeaders(doc).get(0).getContent()).get(0);
		getTableCellP(tblEvents, 0, 0).getContent().add(createR(massnahme));
		if (datumVon != null && datumBis != null)
			getTableCellP(tblEvents, 1, 0).getContent()
					.add(createR(getDateString(datumVon) + " - " + getDateString(datumBis)));
		getTableCellP(tblEvents, 1, 1).getContent()
				.add(createR(plz + " " + ort));

		Tbl tblParticipants = findTables(doc.getMainDocumentPart().getContent()).get(1);
		for (NamiMitglied p : participants) {
			tblParticipants.getContent().add(createTr(
					"",
					p.getNachname(),
					p.getVorname(),
					p.getStrasse(),
					p.getPLZ(),
					p.getOrt(),
					getDateString(LocalDate.from(p.getGeburtsDatum())),
					(datumVon == null || datumBis == null) ? "" : calcAgeRange(p.getGeburtsDatum(), datumVon, datumBis),
					"", ""));
		}
	}

	@Override
	protected String getResourceFileName() {
		return "BDKJ-Dinslaken-16.12.2020.docx";
	}
}