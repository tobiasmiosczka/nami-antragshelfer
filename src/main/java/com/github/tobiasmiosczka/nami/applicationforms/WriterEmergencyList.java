package com.github.tobiasmiosczka.nami.applicationforms;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import com.github.tobiasmiosczka.nami.model.PhoneContact;
import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

import static com.github.tobiasmiosczka.nami.applicationforms.TimeUtil.getDateString;

public class WriterEmergencyList extends AbstractTextDocumentWriter {

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
				row.getCellByIndex(6).setStringValue(getDateString(LocalDate.from(m.getGeburtsDatum())));
			}
		}
	}

	private String getEmergencyContactsString(NamiMitglied member) {
		StringBuilder sb = new StringBuilder();

		//Phone1
		sb.append("Telefon 1:\n");
		Collection<PhoneContact> contactsPhone1 = PhoneContact.getPhoneContacts(member.getTelefon1());
		for (PhoneContact c : contactsPhone1) {
			sb.append(c)
					.append("\n");
		}

		//Phone2
		sb.append("Telefon 2:\n");
		Collection<PhoneContact> contactsPhone2 = PhoneContact.getPhoneContacts(member.getTelefon2());
		for (PhoneContact c : contactsPhone2) {
			sb.append(c)
					.append("\n");
		}

		//Phone3
		sb.append("Telefon 3:\n");
		Collection<PhoneContact> contactsPhone3 = PhoneContact.getPhoneContacts(member.getTelefon3());
		for (PhoneContact c : contactsPhone3) {
			sb.append(c)
					.append("\n");
		}

		//Fax
		sb.append("Fax:\n");
		Collection<PhoneContact> contactsFax = PhoneContact.getPhoneContacts(member.getTelefax());
		for (PhoneContact c : contactsFax) {
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