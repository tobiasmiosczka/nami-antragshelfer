package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Participants;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Training;
import com.github.tobiasmiosczka.nami.applicationforms.api.Document;
import com.github.tobiasmiosczka.nami.applicationforms.api.Table;
import nami.connector.namitypes.NamiBaustein;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiSchulung;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.github.tobiasmiosczka.nami.util.GenderUtil.getLeiterString;
import static com.github.tobiasmiosczka.nami.util.TimeUtil.calcAge;

@Form(title = "Antrag an Diözese Münster (Leiter)")
public class ApplicationDioezeseMuensterGroupLeader extends DocumentWriter {

	private final List<NamiMitglied> participants;
	private final LocalDate datum;
	private final Map<NamiMitglied, Map<NamiBaustein, NamiSchulung>> schulungen;

	public ApplicationDioezeseMuensterGroupLeader(
			@Participants List<NamiMitglied> participants,
			@Training Map<NamiMitglied, Map<NamiBaustein, NamiSchulung>> schulungen,
			@Option(title = "datum") LocalDate datum) {
		super();
		this.participants = participants;
		this.schulungen = schulungen;
		this.datum = datum;
	}

	private static String getYear(Map<NamiBaustein, NamiSchulung> schulungMap, NamiBaustein baustein) {
		return schulungMap.containsKey(baustein) ? String.valueOf(schulungMap.get(baustein).getDate().getYear()) : "";
	}

	@Override
	public void manipulateDoc(Document document){
		Table table = document.getMainDocumentPart().getTables().get(0);
		for (NamiMitglied p : participants) {
			Map<NamiBaustein, NamiSchulung> s = schulungen.get(p);
			table.addRow(
					p.getNachname() + ", " + p.getVorname(),
					p.getStrasse(),
					p.getPlz() + " " + p.getOrt(),
					String.valueOf(calcAge(p.getGeburtsDatum(), datum)),
					getLeiterString(p.getGeschlecht()),
					getYear(s, NamiBaustein.MLK),
					getYear(s, NamiBaustein.WBK),
					getYear(s, NamiBaustein.BAUSTEIN_1B),
					getYear(s, NamiBaustein.BAUSTEIN_2D),
					getYear(s, NamiBaustein.BAUSTEIN_3B),
					getYear(s, NamiBaustein.BAUSTEIN_3C));
		}
		table.setBorders(4);
	}

	@Override
	protected String getResourceFileName() {
		return "DPSG-Münster-Jahresanmeldung.docx";
	}
}
