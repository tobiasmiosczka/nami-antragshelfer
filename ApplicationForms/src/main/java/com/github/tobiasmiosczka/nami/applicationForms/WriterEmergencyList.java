package com.github.tobiasmiosczka.nami.applicationForms;

import java.util.Collection;
import java.util.List;

import com.github.tobiasmiosczka.nami.program.PhoneNumberHelper;
import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

import static com.github.tobiasmiosczka.nami.extendetjnami.TimeHelp.getDateString;

public class WriterEmergencyList extends Writer {

	public WriterEmergencyList() {
		super();
	}

	@Override
	public void manipulateDoc(List<NamiMitglied> participants, TextDocument odtDoc){
		//participants data
		Table tParticipants = odtDoc.getTableList().get(0);
		for (NamiMitglied m : participants){
			Row row = tParticipants.appendRow();
			if (m != null) {
				//Vorname
				row.getCellByIndex(0).setStringValue(m.getVorname());
				//Nachname
				row.getCellByIndex(1).setStringValue(m.getNachname());
				//Telefonnummern
				String phoneNumbers = getEmergencyContactsString(m);
				row.getCellByIndex(2).setStringValue(phoneNumbers);
				//Stadt
				row.getCellByIndex(3).setStringValue(m.getOrt());
				//PLZ
				row.getCellByIndex(4).setStringValue(m.getPLZ());
				//Straße
				row.getCellByIndex(5).setStringValue(m.getStrasse());
				//Geburtsdatum
				row.getCellByIndex(6).setStringValue(getDateString( m.getGeburtsDatum()));
			}
		}
	}

	private String getEmergencyContactsString(NamiMitglied member) {
		StringBuilder sb = new StringBuilder();

		//Phone1
		sb.append("Telefon 1:\n");
		Collection<PhoneNumberHelper.Contact> contactsPhone1 = PhoneNumberHelper.getPhoneContacts(member.getTelefon1());
		for (PhoneNumberHelper.Contact c : contactsPhone1) {
			sb.append(c)
					.append("\n");
		}

		//Phone2
		sb.append("Telefon 2:\n");
		Collection<PhoneNumberHelper.Contact> contactsPhone2 = PhoneNumberHelper.getPhoneContacts(member.getTelefon2());
		for (PhoneNumberHelper.Contact c : contactsPhone2) {
			sb.append(c)
					.append("\n");
		}

		//Phone3
		sb.append("Telefon 3:\n");
		Collection<PhoneNumberHelper.Contact> contactsPhone3 = PhoneNumberHelper.getPhoneContacts(member.getTelefon3());
		for (PhoneNumberHelper.Contact c : contactsPhone3) {
			sb.append(c)
					.append("\n");
		}

		//Fax
		sb.append("Fax:\n");
		Collection<PhoneNumberHelper.Contact> contactsFax = PhoneNumberHelper.getPhoneContacts(member.getTelefax());
		for (PhoneNumberHelper.Contact c : contactsFax) {
			sb.append(c)
					.append("\n");
		}

		return sb.toString();
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