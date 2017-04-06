package com.github.toasterguy.nami.applicationForms;

import java.util.Date;
import java.util.List;

import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

import static com.github.toasterguy.nami.extendetjnami.TimeHelp.calcAge;
import static com.github.toasterguy.nami.extendetjnami.TimeHelp.getDateString;


public class WriterAntragStadtDinslaken extends WriterAntrag {

	private final String 	massnahme;
	private final String ort;
	private final boolean keinDatum;
	private final Date	datumVon;
	private final Date datumBis;

	public WriterAntragStadtDinslaken(String massnahme, boolean keinDatum, Date datumVon, Date datumBis, String ort) {
		super();
		this.massnahme = massnahme;
		this.keinDatum = keinDatum;
		this.datumVon = datumVon;
		this.datumBis = datumBis;
		this.ort = ort;
	}

	@Override
	public void doTheMagic(List<NamiMitglied> participants, TextDocument odtDoc) {
		//event data
		Table tEvent = odtDoc.getHeader().getTableList().get(0);
		//Maßnahme
		tEvent.getCellByPosition(0, 0).setStringValue(tEvent.getCellByPosition(0, 0).getStringValue() + massnahme);
		//Datum von bis
		if (!keinDatum) {
			tEvent.getCellByPosition(0, 1).setStringValue(tEvent.getCellByPosition(0, 1).getStringValue() + getDateString(datumVon) + " - " + getDateString(datumBis));
		}
		tEvent.getCellByPosition(1, 1).setStringValue(tEvent.getCellByPosition(1, 1).getStringValue() + ort);

		//participants data
		Table tParticipants = odtDoc.getTableList().get(0);

		Font font = new Font("Calibri", StyleTypeDefinitions.FontStyle.REGULAR, 10, Color.BLACK);
		double height = 0.7;

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

			Date birthday = participant.getGeburtsDatum();
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
			row.getCellByIndex(6).setStringValue(getDateString(birthday));
			//Alter
			if (!keinDatum) {
				//Alter
				//compute age
				int diffInYearsStart = calcAge(birthday, datumVon);
				int diffInYearsEnd = calcAge(birthday, datumBis);
				if (diffInYearsEnd > diffInYearsStart) {
					//participant has his/her birthday at the event
					row.getCellByIndex(7).setStringValue(String.valueOf(diffInYearsStart) + "-" + String.valueOf(diffInYearsEnd));
				} else {
					row.getCellByIndex(7).setStringValue(String.valueOf(diffInYearsStart));
				}
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