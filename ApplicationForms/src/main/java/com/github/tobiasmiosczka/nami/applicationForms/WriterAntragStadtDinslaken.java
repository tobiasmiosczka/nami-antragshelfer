package com.github.tobiasmiosczka.nami.applicationForms;

import java.util.Date;
import java.util.List;

import com.github.tobiasmiosczka.nami.extendetjnami.TimeHelp;
import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;



public class WriterAntragStadtDinslaken extends Writer {

	private static final Font font = new Font("Calibri", StyleTypeDefinitions.FontStyle.REGULAR, 10, Color.BLACK);
	private static final double height = 0.7;

	private final String 	massnahme,
							ort;
	private final boolean 	keinDatum;
	private final Date		datumVon,
							datumBis;

	public WriterAntragStadtDinslaken(String massnahme, boolean keinDatum, Date datumVon, Date datumBis, String ort) {
		super();
		this.massnahme = massnahme;
		this.keinDatum = keinDatum;
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
		if (!keinDatum) {
			tEvent.getCellByPosition(0, 1).setStringValue(tEvent.getCellByPosition(0, 1).getStringValue() + TimeHelp.getDateString(datumVon) + " - " + TimeHelp.getDateString(datumBis));
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
			row.setHeight(height, true);
			for(int i = 0; i < row.getCellCount(); ++i) {
				row.getCellByIndex(i).setFont(font);
			}

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
			row.getCellByIndex(6).setStringValue(TimeHelp.getDateString(participant.getGeburtsDatum()));
			//Alter
			if (!keinDatum) {
				row.getCellByIndex(7).setStringValue(TimeHelp.calcAgeRange(participant.getGeburtsDatum(), datumVon, datumBis));
			}
		}
	}

	@Override
	public int getMaxParticipantsPerPage() {
		return 0;
	}

	@Override
	protected String getResourceFileName() {
		return "Stadt_Dinslaken_Blanco.odt";
	}
}