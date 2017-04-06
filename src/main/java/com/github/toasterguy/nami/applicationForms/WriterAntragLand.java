package com.github.toasterguy.nami.applicationForms;

import java.util.Date;
import java.util.List;

import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

import static com.github.toasterguy.nami.extendetjnami.TimeHelp.calcAge;
import static com.github.toasterguy.nami.extendetjnami.TimeHelp.getDateString;


public class WriterAntragLand extends WriterAntrag{

	private final String 	mitgliedsVerband;
	private final String träger;
	private final String plz;
	private final String ort;
	private final String land;

	private final boolean keinDatum;

	private final Date 	datumVon;
	private final Date datumBis;

	public WriterAntragLand(String mitgliedsVerband, String träger, boolean keinDatum, Date datumVon, Date datumBis, String plz, String ort, String land) {
		super();
		this.mitgliedsVerband = mitgliedsVerband;
		this.träger = träger;
		this.keinDatum = keinDatum;
		this.datumVon = datumVon;
		this.datumBis = datumBis;
		this.plz = plz;
		this.ort = ort;
		this.land = land;
	}

	@Override
	public void doTheMagic(List<NamiMitglied> participants, TextDocument odtDoc){
		//association data
		Table tAssociation = odtDoc.getHeader().getTableList().get(0);
		//Mitgliedsverband
		tAssociation.getCellByPosition(2, 0).setStringValue(mitgliedsVerband);
		//Träger
		tAssociation.getCellByPosition(2, 1).setStringValue(träger);
		
		//event data
		Table tEvent = odtDoc.getHeader().getTableList().get(1);
		//Datum (von-bis)
		if(!keinDatum){
			tEvent.getCellByPosition(2, 0).setStringValue(getDateString(datumVon) + " - " + getDateString(datumBis));
		}
		//PLZ Ort
		tEvent.getCellByPosition(8, 0).setStringValue(plz + " " + ort);
		//Land
		tEvent.getCellByPosition(14, 0).setStringValue(land);
		
		//participants data


		Table tParticipants = odtDoc.getTableList().get(0);

		double height = tParticipants.getRowByIndex(1).getHeight();

		for (NamiMitglied participant : participants) {

			int row = tParticipants.getRowCount() - 1;
			//Lfd. Nr.

			//Kursleiter K= Kursleiter, R= Referent, L= Leiter

			//Name, Vorname
			tParticipants.getCellByPosition(2, row).setStringValue(participant.getNachname() + ", " + participant.getVorname());
			//Anschrift: Straße, PLZ, Wohnort
			tParticipants.getCellByPosition(3, row).setStringValue(participant.getStrasse() + ", " + participant.getPLZ() + ", " + participant.getOrt());
			//w=weibl. m=männl.
			String geschlecht;
			switch (participant.getGeschlecht()) {
				case MAENNLICH:
					geschlecht = "m";
					break;
				case WEIBLICH:
					geschlecht = "W";
					break;
				default:
					geschlecht = "";
					break;
			}
			tParticipants.getCellByPosition(4, row).setStringValue(geschlecht);
			//Alter
			if (!keinDatum) {
				Date birthday = participant.getGeburtsDatum();
				int diffInYearsStart = calcAge(birthday, datumBis);
				int diffInYearsEnd = calcAge(birthday, datumBis);
				if (diffInYearsEnd > diffInYearsStart) {//participant has his/her birthday at the event
					tParticipants.getCellByPosition(5, row).setStringValue(String.valueOf(diffInYearsStart) + "-" + String.valueOf(diffInYearsEnd));
				} else {
					tParticipants.getCellByPosition(5, row).setStringValue(String.valueOf(diffInYearsStart));
				}
			}
			Row r = tParticipants.appendRow();
			r.setHeight(height, true);
		}
		tParticipants.removeRowsByIndex(tParticipants.getRowCount() -1, tParticipants.getRowCount() - 1);
	}

	@Override
	public int getMaxParticipantsPerPage() {
		return 0;
	}

	@Override
	protected String getResourceFileName() {
		return "Land_Blanco.odt";
	}
}