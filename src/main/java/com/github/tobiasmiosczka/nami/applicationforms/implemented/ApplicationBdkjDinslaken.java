package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import java.time.LocalDate;
import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Participants;
import com.github.tobiasmiosczka.nami.applicationforms.api.Document;
import com.github.tobiasmiosczka.nami.applicationforms.api.Table;
import nami.connector.namitypes.NamiMitglied;

import static com.github.tobiasmiosczka.nami.util.TimeUtil.calcAgeRange;
import static com.github.tobiasmiosczka.nami.util.TimeUtil.getDateString;

@Form(title = "Antrag an BDKJ Dinslaken")
public class ApplicationBdkjDinslaken extends DocumentWriter {

	private final List<NamiMitglied> participants;
	private final String 	massnahme,
							plz,
							ort,
							tagungsstaette;
	private final LocalDate datumVon,
							datumBis;

	public ApplicationBdkjDinslaken(
			@Participants List<NamiMitglied> participants,
			@Option(title = "Maßnahme") String massnahme,
			@Option(title = "Datum (von)") LocalDate datumVon,
			@Option(title = "Datum (bis)") LocalDate datumBis,
			@Option(title = "PLZ") String plz,
			@Option(title = "Ort") String ort,
			@Option(title = "Tagungsstätte") String tagungsstaette) {
		super();
		this.participants = participants;
		this.massnahme = massnahme;
		this.datumVon = datumVon;
		this.datumBis = datumBis;
		this.plz = plz;
		this.ort = ort;
		this.tagungsstaette = tagungsstaette;
	}

	@Override
	public void manipulateDoc(Document document) {
		String plzOrt = plz + " " + ort;
		boolean dateSet = datumVon != null && datumBis != null;
		Table tableMain = document.getMainDocumentPart().getTables().get(0);

		if (datumVon != null)
			tableMain.cellAt(5, 1).add(getDateString(datumVon));
		if (datumBis != null)
			tableMain.cellAt(5, 3).add(getDateString(datumBis));
		tableMain.cellAt(5, 5).add(plzOrt);
		tableMain.cellAt(7, 1).add(tagungsstaette);

		Table tableEvents = document.getHeaders().get(0).getTables().get(0);
		tableEvents.cellAt(0, 0).add(massnahme);
		if (dateSet)
			tableEvents.cellAt(1, 0).add(getDateString(datumVon) + " - " + getDateString(datumBis));
		tableEvents.cellAt(1, 1).add(plzOrt);

		Table tableParticipants = document.getMainDocumentPart().getTables().get(1);
		for (NamiMitglied p : participants) {
			tableParticipants.addRow(
					"",
					p.getNachname(),
					p.getVorname(),
					p.getStrasse(),
					p.getPlz(),
					p.getOrt(),
					getDateString(LocalDate.from(p.getGeburtsDatum())),
					(dateSet) ? calcAgeRange(p.getGeburtsDatum(), datumVon, datumBis) : "",
					"", "");
		}
	}

	@Override
	protected String getResourceFileName() {
		return "BDKJ-Dinslaken-16.12.2020.docx";
	}
}