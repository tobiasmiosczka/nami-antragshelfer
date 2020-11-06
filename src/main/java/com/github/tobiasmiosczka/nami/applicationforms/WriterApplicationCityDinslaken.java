package com.github.tobiasmiosczka.nami.applicationforms;

import java.time.LocalDate;
import java.util.List;

import com.github.tobiasmiosczka.nami.util.TimeUtil;
import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

public class WriterApplicationCityDinslaken extends AbstractTextDocumentWriter {

	private static final Font FONT = new Font("Calibri", StyleTypeDefinitions.FontStyle.REGULAR, 10, Color.BLACK);
	private static final double HEIGHT = 0.7D;

	private final String 	massnahme,
							ort;
	private final LocalDate datumVon,
							datumBis;

	public WriterApplicationCityDinslaken(String massnahme, LocalDate datumVon, LocalDate datumBis, String ort) {
		super();
		this.massnahme = massnahme;
		this.datumVon = datumVon;
		this.datumBis = datumBis;
		this.ort = ort;
	}

	@Override
	public void manipulateDoc(List<NamiMitglied> participants, TextDocument odtDoc) {
		//event data
		Table tEvent = odtDoc.getHeader().getTableList().get(0);
		//Maßnahme
		tEvent.getCellByPosition(0, 0).setStringValue(tEvent.getCellByPosition(0, 0).getStringValue() + massnahme);
		//Datum von bis
		if (datumVon != null && datumBis != null) {
			tEvent.getCellByPosition(0, 1).setStringValue(tEvent.getCellByPosition(0, 1).getStringValue() + TimeUtil.getDateString(datumVon) + " - " + TimeUtil.getDateString(datumBis));
		}
		tEvent.getCellByPosition(1, 1).setStringValue(tEvent.getCellByPosition(1, 1).getStringValue() + ort);

		//participants data
		Table tParticipants = odtDoc.getTableList().get(0);

		boolean first = true;
		for (NamiMitglied participant : participants) {
			Row row;
			if (first) {
				row = tParticipants.getRowByIndex(1);
				first = false;
			} else {
				row = tParticipants.appendRow();
			}

			//set row style
			row.setHeight(HEIGHT, true);
			for(int i = 0; i < row.getCellCount(); ++i)
				row.getCellByIndex(i).setFont(FONT);

			//Nr.

			//Nachnahme
			row.getCellByIndex(1).setStringValue(participant.getNachname());
			//Vornahme
			row.getCellByIndex(2).setStringValue(participant.getVorname());
			//Strasse
			row.getCellByIndex(3).setStringValue(participant.getStrasse());
			//PLZ
			row.getCellByIndex(4).setStringValue(participant.getPLZ());
			//Ort
			row.getCellByIndex(5).setStringValue(participant.getOrt());
			//Geburtsdatum
			row.getCellByIndex(6).setStringValue(TimeUtil.getDateString(LocalDate.from(participant.getGeburtsDatum())));
			//Alter
			if (datumVon != null && datumBis != null) {
				row.getCellByIndex(7).setStringValue(TimeUtil.calcAgeRange(LocalDate.from(participant.getGeburtsDatum()), datumVon, datumBis));
			}
		}
	}

	@Override
	protected String getResourceFileName() {
		return "Stadt_Dinslaken_Blanco.odt";
	}
}