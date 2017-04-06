package com.github.toasterguy.nami.applicationForms;

import com.github.toasterguy.nami.extendetjnami.namitypes.SchulungenMap;
import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Table;

import java.util.Date;
import java.util.List;

import static com.github.toasterguy.nami.extendetjnami.TimeHelp.calcAge;
import static com.github.toasterguy.nami.extendetjnami.TimeHelp.getDateYearString;


public class WriterAntragLandLeiter extends WriterAntrag{

	private final boolean keinDatum;

	private final Date datum;

	private final List<SchulungenMap> schulungen;

	public WriterAntragLandLeiter(List<SchulungenMap> schulungen, boolean keinDatum, Date datum) {
		super();
		this.schulungen = schulungen;
		this.keinDatum = keinDatum;
		this.datum = datum;
	}

	@Override
	public void doTheMagic(List<NamiMitglied> participants, TextDocument odtDoc){
		//participants data
		Table tParticipants = odtDoc.getTableList().get(0);

		for(int i = 0; i < participants.size(); i++){
			int row = i + 3;
			NamiMitglied m = participants.get(i);
			SchulungenMap s = schulungen.get(i);
			//Name, Vorname
			tParticipants.getCellByPosition(0, row).setStringValue(m.getNachname() + ", " + m.getVorname());
			//Straße
			tParticipants.getCellByPosition(1, row).setStringValue(m.getStrasse());
			//PLZ Ort
			tParticipants.getCellByPosition(2, row).setStringValue(m.getPLZ() + m.getOrt());
			//Alter
			if(!keinDatum){
				Date birthday = m.getGeburtsDatum();
				int diffInYears = calcAge(birthday, datum);
				tParticipants.getCellByPosition(3, row).setStringValue(String.valueOf(diffInYears));
			}
			//Funktion im Stamm

			//MLK(Jahr)

			//WBK(Jahr/Stufe)
			if(schulungen.get(i).containsKey(SchulungenMap.Baustein.WBK)) {
				tParticipants.getCellByPosition(6, row).setStringValue(getDateYearString(schulungen.get(i).get(SchulungenMap.Baustein.WBK).getSchulungsDaten().getVstgTag()));
			}
			//1b
			if(schulungen.get(i).containsKey(SchulungenMap.Baustein.BAUSTEIN_1B)) {
				tParticipants.getCellByPosition(7, row).setStringValue(getDateYearString(schulungen.get(i).get(SchulungenMap.Baustein.BAUSTEIN_1B).getSchulungsDaten().getVstgTag()));
			}
			//2d(+)
			if(schulungen.get(i).containsKey(SchulungenMap.Baustein.BAUSTEIN_2D)) {
				tParticipants.getCellByPosition(8, row).setStringValue(getDateYearString(schulungen.get(i).get(SchulungenMap.Baustein.BAUSTEIN_2D).getSchulungsDaten().getVstgTag()));
			}
			//3b
			if(schulungen.get(i).containsKey(SchulungenMap.Baustein.BAUSTEIN_3B)) {
				tParticipants.getCellByPosition(9, row).setStringValue(getDateYearString(schulungen.get(i).get(SchulungenMap.Baustein.BAUSTEIN_3B).getSchulungsDaten().getVstgTag()));
			}
			//3c
			if(schulungen.get(i).containsKey(SchulungenMap.Baustein.BAUSTEIN_3C)) {
				tParticipants.getCellByPosition(10, row).setStringValue(getDateYearString(schulungen.get(i).get(SchulungenMap.Baustein.BAUSTEIN_3C).getSchulungsDaten().getVstgTag()));
			}
		}
	}

	@Override
	public int getMaxParticipantsPerPage() {
		return 0;
	}

	@Override
	protected String getResourceFileName() {
		return "Land_Leiter_Blanco.odt";
	}
}
