package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import java.time.LocalDate;
import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.TableUtil;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import com.github.tobiasmiosczka.nami.util.TimeUtil;
import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

@Form(title = "Antrag an Stadt Dinslaken")
public class ApplicationCityDinslaken extends DocumentWriter {

	private static final Font FONT = new Font(
			"Calibri",
			StyleTypeDefinitions.FontStyle.REGULAR,
			10D,
			Color.BLACK);
	private static final double HEIGHT = 0.7D;

	private final String 	massnahme,
							ort;
	private final LocalDate datumVon,
							datumBis;

	public ApplicationCityDinslaken(
			@Option(title = "Maßnahme") String massnahme,
			@Option(title = "Datum (von)")LocalDate datumVon,
			@Option(title = "Datum (bis)")LocalDate datumBis,
			@Option(title = "Ort")String ort) {
		super();
		this.massnahme = massnahme;
		this.datumVon = datumVon;
		this.datumBis = datumBis;
		this.ort = ort;
	}

	@Override
	public void manipulateDoc(List<NamiMitglied> participants, TextDocument doc) {
		//event data
		Table tEvent = doc.getHeader().getTableList().get(0);
		tEvent.getCellByPosition(0, 0)
				.setStringValue(tEvent.getCellByPosition(0, 0).getStringValue() + massnahme);
		if (datumVon != null && datumBis != null)
			tEvent.getCellByPosition(0, 1)
					.setStringValue(
							tEvent.getCellByPosition(0, 1).getStringValue()
									+ TimeUtil.getDateString(datumVon) + " - " + TimeUtil.getDateString(datumBis));
		tEvent.getCellByPosition(1, 1)
				.setStringValue(tEvent.getCellByPosition(1, 1).getStringValue() + ort);

		//participants data
		Table tParticipants = doc.getTableList().get(0);
		TableUtil.appendRows(
				tParticipants,
				participants.size(),
				HEIGHT,
				FONT,
				StyleTypeDefinitions.HorizontalAlignmentType.LEFT);
		for (int i = 0; i < participants.size(); ++i) {
			NamiMitglied p = participants.get(i);
			Row r = tParticipants.getRowList().get(i + 1);
			r.getCellByIndex(1).setStringValue(p.getNachname());
			r.getCellByIndex(2).setStringValue(p.getVorname());
			r.getCellByIndex(3).setStringValue(p.getStrasse());
			r.getCellByIndex(4).setStringValue(p.getPLZ());
			r.getCellByIndex(5).setStringValue(p.getOrt());
			r.getCellByIndex(6).setStringValue(TimeUtil.getDateString(LocalDate.from(p.getGeburtsDatum())));
			if (datumVon != null && datumBis != null)
				r.getCellByIndex(7).setStringValue(TimeUtil.calcAgeRange(p.getGeburtsDatum(), datumVon, datumBis));
		}
	}

	@Override
	protected String getResourceFileName() {
		return "Stadt_Dinslaken_Blanco.odt";
	}
}