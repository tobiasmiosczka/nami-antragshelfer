package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import java.time.LocalDate;
import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import nami.connector.namitypes.NamiMitglied;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tr;

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
	private final LocalDate dateFrom,
							dateTo;
	private final boolean dateSet;

	public ApplicationBdkjDinslaken(
			@Option(title = "Maßnahme") String massnahme,
			@Option(title = "Datum (von)") LocalDate dateFrom,
			@Option(title = "Datum (bis)") LocalDate dateTo,
			@Option(title = "PLZ") String plz,
			@Option(title = "Ort") String ort,
			@Option(title = "Tagungsstätte") String tagungsstaette) {
		super();
		this.massnahme = massnahme;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.plz = plz;
		this.ort = ort;
		this.tagungsstaette = tagungsstaette;
		this.dateSet = dateFrom != null && dateTo != null;
	}

	@Override
	public void manipulateDoc(List<NamiMitglied> participants, WordprocessingMLPackage doc) {
		String plzOrt = plz + " " + ort;
		Tbl tblMain = findTables(doc.getMainDocumentPart().getContent()).get(0);
		if (dateFrom != null) {
			getTableCellP(tblMain, 5, 1).getContent().add(createR(getDateString(dateFrom)));
		}
		if (dateTo != null) {
			getTableCellP(tblMain, 5, 3).getContent().add(createR(getDateString(dateTo)));
		}
		getTableCellP(tblMain, 5, 5).getContent().add(createR(plzOrt));
		getTableCellP(tblMain, 7, 1).getContent().add(createR(tagungsstaette));

		Tbl tblEvents = findTables(findHeaders(doc).get(0).getContent()).get(0);
		getTableCellP(tblEvents, 0, 0).getContent().add(createR(massnahme));
		if (dateSet) {
			getTableCellP(tblEvents, 1, 0).getContent()
					.add(createR(getDateString(dateFrom) + " - " + getDateString(dateTo)));
		}
		getTableCellP(tblEvents, 1, 1).getContent().add(createR(plzOrt));
		Tbl tblParticipants = findTables(doc.getMainDocumentPart().getContent()).get(1);
		for (NamiMitglied p : participants) {
			tblParticipants.getContent().add(memberToTr(p));
		}
	}

	private Tr memberToTr(NamiMitglied member) {
		return createTr(
				"",
				member.getNachname(),
				member.getVorname(),
				member.getStrasse(),
				member.getPLZ(),
				member.getOrt(),
				getDateString(LocalDate.from(member.getGeburtsDatum())),
				(dateSet) ? calcAgeRange(member.getGeburtsDatum(), dateFrom, dateTo) : "",
				"",
				"");
	}

	@Override
	protected String getResourceFileName() {
		return "BDKJ-Dinslaken-16.12.2020.docx";
	}
}