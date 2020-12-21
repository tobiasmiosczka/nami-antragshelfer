package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.DocUtil;
import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.model.PhoneContact;
import nami.connector.namitypes.NamiMitglied;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Tbl;

import static com.github.tobiasmiosczka.nami.util.TimeUtil.getDateString;

@Form(title = "Notfallliste")
public class EmergencyList extends DocumentWriter {

	public EmergencyList() {
		super();
	}

	private static String getEmergencyContactsString(NamiMitglied member) {
		StringBuilder sb = new StringBuilder();

		//Phone1
		sb.append("Telefon 1:\n");
		Collection<PhoneContact> contactsPhone1 = PhoneContact.getPhoneContacts(member.getTelefon1());
		for (PhoneContact c : contactsPhone1)
			sb.append(c).append("\n");

		//Phone2
		sb.append("Telefon 2:\n");
		Collection<PhoneContact> contactsPhone2 = PhoneContact.getPhoneContacts(member.getTelefon2());
		for (PhoneContact c : contactsPhone2)
			sb.append(c).append("\n");

		//Phone3
		sb.append("Telefon 3:\n");
		Collection<PhoneContact> contactsPhone3 = PhoneContact.getPhoneContacts(member.getTelefon3());
		for (PhoneContact c : contactsPhone3)
			sb.append(c).append("\n");

		//Fax
		sb.append("Fax:\n");
		Collection<PhoneContact> contactsFax = PhoneContact.getPhoneContacts(member.getTelefax());
		for (PhoneContact c : contactsFax)
			sb.append(c).append("\n");

		return sb.toString();
	}

	@Override
	public void manipulateDoc(List<NamiMitglied> participants, WordprocessingMLPackage doc){
		Tbl tbl = DocUtil.findTables(doc.getMainDocumentPart().getContent()).get(0);
		for (NamiMitglied p : participants) {
			tbl.getContent().add(DocUtil.createTr(
					p.getVorname(),
					p.getNachname(),
					getEmergencyContactsString(p),
					p.getOrt(),
					p.getPLZ(),
					p.getStrasse(),
					getDateString(LocalDate.from(p.getGeburtsDatum()))));
		}
	}

	@Override
	protected String getResourceFileName() {
		return "Notfallliste.docx";
	}
}