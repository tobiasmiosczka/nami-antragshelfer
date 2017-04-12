package com.github.tobiasmiosczka.nami.applicationForms;

import java.util.Date;
import java.util.List;

import com.github.tobiasmiosczka.nami.extendetjnami.TimeHelp;
import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

public class WriterAntragLand extends Writer {

	private final String 	mitgliedsVerband,
							träger,
							plz,
							ort,
							land;

	private final boolean keinDatum;

	private final Date 	datumVon,
						datumBis;

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
	public void manipulateDoc(List<NamiMitglied> participants, TextDocument odtDoc) {
		//association data
		Table tAssociation = odtDoc.getHeader().getTableList().get(0);
		//Mitgliedsverband
		tAssociation.getCellByPosition(2, 0).setStringValue(mitgliedsVerband);
		//Träger
		tAssociation.getCellByPosition(2, 1).setStringValue(träger);
		
		//event data
		Table tEvent = odtDoc.getHeader().getTableList().get(1);
		//Datum (von-bis)
		if (!keinDatum) {
			tEvent.getCellByPosition(2, 0).setStringValue(TimeHelp.getDateString(datumVon) + " - " + TimeHelp.getDateString(datumBis));
		}
		//PLZ Ort
		tEvent.getCellByPosition(8, 0).setStringValue(plz + " " + ort);
		//Land
		tEvent.getCellByPosition(14, 0).setStringValue(land);
		
		//participants data


		Table tParticipants = odtDoc.getTableList().get(0);

		double height = tParticipants.getRowByIndex(1).getHeight();

		for (NamiMitglied participant : participants) {

			Row row = tParticipants.getRowByIndex(tParticipants.getRowCount() - 1);
			//Lfd. Nr.

			//Kursleiter K= Kursleiter, R= Referent, L= Leiter

			//Name, Vorname
			row.getCellByIndex(2).setStringValue(participant.getNachname() + ", " + participant.getVorname());
			//Anschrift: Straße, PLZ, Wohnort
			row.getCellByIndex(3).setStringValue(participant.getStrasse() + ", " + participant.getPLZ() + ", " + participant.getOrt());
			//w=weibl. m=männl.
			row.getCellByIndex(4).setStringValue("" + participant.getGeschlecht().getCharacter());
			//Alter
			if (!keinDatum) {
				row.getCellByIndex(5).setStringValue(TimeHelp.calcAgeRange(participant.getGeburtsDatum(), datumVon, datumBis));
			}
			Row r = tParticipants.appendRow();
			r.setHeight(height, true);
		}
		tParticipants.removeRowsByIndex(tParticipants.getRowCount() - 1, tParticipants.getRowCount() - 1);
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