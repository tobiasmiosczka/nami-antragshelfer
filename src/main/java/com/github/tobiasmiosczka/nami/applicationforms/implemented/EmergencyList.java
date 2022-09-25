package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import java.time.LocalDate;
import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.model.PhoneContact;
import nami.connector.namitypes.NamiMitglied;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tr;

import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.createTr;
import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.findTables;
import static com.github.tobiasmiosczka.nami.model.PhoneContact.getPhoneContacts;
import static com.github.tobiasmiosczka.nami.util.TimeUtil.getDateString;

@Form(title = "Notfallliste")
public class EmergencyList extends DocumentWriter {

	public EmergencyList() {
		super();
	}

	private static String getEmergencyContactsString(NamiMitglied member) {
		StringBuilder sb = new StringBuilder("Telefon 1:\n");
		for (PhoneContact c : getPhoneContacts(member.getTelefon1()))
			sb.append(c).append("\n");
		sb.append("Telefon 2:\n");
		for (PhoneContact c : getPhoneContacts(member.getTelefon2()))
			sb.append(c).append("\n");
		sb.append("Telefon 3:\n");
		for (PhoneContact c : getPhoneContacts(member.getTelefon3()))
			sb.append(c).append("\n");
		sb.append("Fax:\n");
		for (PhoneContact c : getPhoneContacts(member.getTelefax()))
			sb.append(c).append("\n");
		return sb.toString();
	}

	@Override
	public void manipulateDoc(List<NamiMitglied> participants, WordprocessingMLPackage doc){
		Tbl tbl = findTables(doc.getMainDocumentPart().getContent()).get(0);
		for (NamiMitglied p : participants)
			tbl.getContent().add(memberToTr(p));
	}

	private Tr memberToTr(NamiMitglied p) {
		return createTr(
				p.getVorname(),
				p.getNachname(),
				getEmergencyContactsString(p),
				p.getOrt(),
				p.getPLZ(),
				p.getStrasse(),
				getDateString(LocalDate.from(p.getGeburtsDatum())));
	}

	@Override
	protected String getResourceFileName() {
		return "Notfallliste.docx";
	}
}