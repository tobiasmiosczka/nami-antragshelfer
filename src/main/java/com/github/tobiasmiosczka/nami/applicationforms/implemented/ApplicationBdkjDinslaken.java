package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import java.time.LocalDate;
import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.DocUtil;
import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import com.github.tobiasmiosczka.nami.util.TimeUtil;
import nami.connector.namitypes.NamiMitglied;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Tbl;

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
		Tbl tblMain = DocUtil.findTables(doc.getMainDocumentPart().getContent()).get(0);
		if (datumVon != null)
			DocUtil.getTableCellP(tblMain, 5, 1).getContent().add(DocUtil.createR(getDateString(datumVon)));
		if (datumBis != null)
			DocUtil.getTableCellP(tblMain, 5, 3).getContent().add(DocUtil.createR(getDateString(datumBis)));
		DocUtil.getTableCellP(tblMain, 5, 5).getContent().add(DocUtil.createR(plz + " " + ort));
		DocUtil.getTableCellP(tblMain, 7, 1).getContent().add(DocUtil.createR(tagungsstaette));

		Tbl tblEvents = DocUtil.findTables(DocUtil.findHeaders(doc).get(0).getContent()).get(0);
		if (datumVon != null && datumBis != null)
			DocUtil.getTableCellP(tblEvents, 1, 0).getContent()
					.add(DocUtil.createR(getDateString(datumVon) + " - " + getDateString(datumBis)));
		DocUtil.getTableCellP(tblEvents, 1, 1).getContent()
				.add(DocUtil.createR(plz + " " + ort));

		Tbl tblParticipants = DocUtil.findTables(doc.getMainDocumentPart().getContent()).get(1);
		for (NamiMitglied p : participants) {
			tblParticipants.getContent().add(DocUtil.createTr(
					"",
					p.getNachname(),
					p.getVorname(),
					p.getStrasse(),
					p.getPLZ(),
					p.getOrt(),
					getDateString(LocalDate.from(p.getGeburtsDatum())),
					(datumVon == null || datumBis == null) ? "" : TimeUtil.calcAgeRange(p.getGeburtsDatum(), datumVon, datumBis),
					"", ""));
		}
	}

	@Override
	protected String getResourceFileName() {
		return "BDKJ-Dinslaken-16.12.2020.docx";
	}
}