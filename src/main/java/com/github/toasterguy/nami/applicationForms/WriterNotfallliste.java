package com.github.toasterguy.nami.applicationForms;

import java.util.Date;
import java.util.List;

import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Table;

import static com.github.toasterguy.nami.extendetjnami.TimeHelp.getDateString;


public class WriterNotfallliste extends WriterAntrag{

	public WriterNotfallliste() {
		super();
	}

	@Override
	public void doTheMagic(List<NamiMitglied> participants, TextDocument odtDoc){
		//participants data
		Table tParticipants = odtDoc.getTableList().get(0);
		for(int i=0; i<participants.size(); i++){
			tParticipants.appendRow();
			int row = i+1;
			NamiMitglied m = participants.get(i);
			if(m!=null){
				//Vorname
				tParticipants.getCellByPosition(0, row).setStringValue(m.getVorname());
				//Nachname
				tParticipants.getCellByPosition(1, row).setStringValue(m.getNachname());
				//Telefonnummern
				//TODO:Magic Code to seperate all phone numbers
				
				tParticipants.getCellByPosition(2, row).setStringValue(m.getTelefon1() + " " + m.getTelefon2() + " " + m.getTelefon3() + " " + m.getTelefax());
				
				//Stadt
				tParticipants.getCellByPosition(3, row).setStringValue(m.getOrt());
				//PLZ
				tParticipants.getCellByPosition(4, row).setStringValue(m.getPLZ());
				//Straße
				tParticipants.getCellByPosition(5, row).setStringValue(m.getStrasse());
				//Geburtsdatum
				Date birthday = m.getGeburtsDatum();
				tParticipants.getCellByPosition(6, row).setStringValue(getDateString(birthday));
			}
		}
	}

	@Override
	public int getMaxParticipantsPerPage() {
		return 0;
	}

	@Override
	protected String getResourceFileName() {
		return "Notfallliste.odt";
	}

}