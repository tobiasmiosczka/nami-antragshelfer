package com.github.tobiasmiosczka.nami.applicationforms;

import java.time.LocalDate;
import java.util.List;

import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.enums.NamiGeschlecht;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

public class WriterApplicationDioezeseMuenster extends AbstractTextDocumentWriter {

	private final String 	mitgliedsVerband,
							traeger,
							plz,
							ort,
							land;

	private final boolean 	noDate;

	private final LocalDate datumVon,
							datumBis;

	public WriterApplicationDioezeseMuenster(String mitgliedsVerband, String traeger, boolean noDate, LocalDate datumVon, LocalDate datumBis, String plz, String ort, String land) {
		super();
		this.mitgliedsVerband = mitgliedsVerband;
		this.traeger = traeger;
		this.noDate = noDate;
		this.datumVon = datumVon;
		this.datumBis = datumBis;
		this.plz = plz;
		this.ort = ort;
		this.land = land;
	}

	private static char getCharacter(NamiGeschlecht geschlecht) {
		switch (geschlecht) {
			case WEIBLICH: return 'w';
			case MAENNLICH: return 'm';
			default: return ' ';
		}
	}

	@Override
	public void manipulateDoc(List<NamiMitglied> participants, TextDocument odtDoc) {
		//association data
		Table tAssociation = odtDoc.getHeader().getTableList().get(0);
		//Mitgliedsverband
		tAssociation.getCellByPosition(2, 0).setStringValue(mitgliedsVerband);
		//Träger
		tAssociation.getCellByPosition(2, 1).setStringValue(traeger);
		
		//event data
		Table tEvent = odtDoc.getHeader().getTableList().get(1);
		//Datum (von-bis)
		if (!noDate) {
			tEvent.getCellByPosition(2, 0).setStringValue(TimeUtil.getDateString(datumVon) + " - " + TimeUtil.getDateString(datumBis));
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
			row.getCellByIndex(4).setStringValue("" + getCharacter(participant.getGeschlecht()));
			//Alter
			if (!noDate)
				row.getCellByIndex(5).setStringValue(TimeUtil.calcAgeRange(LocalDate.from(participant.getGeburtsDatum()), datumVon, datumBis));
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