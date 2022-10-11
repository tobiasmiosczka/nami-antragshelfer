package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Training;
import nami.connector.namitypes.NamiBaustein;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiSchulung;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Tbl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.addBorders;
import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.createTr;
import static com.github.tobiasmiosczka.nami.applicationforms.DocUtil.findTables;
import static com.github.tobiasmiosczka.nami.util.GenderUtil.getLeiterString;
import static com.github.tobiasmiosczka.nami.util.TimeUtil.calcAge;

@Form(title = "Antrag an Diözese Münster (Leiter)")
public class ApplicationDioezeseMuensterGroupLeader extends DocumentWriter {

	private final LocalDate datum;
	private final List<Map<NamiBaustein, NamiSchulung>> schulungen;

	public ApplicationDioezeseMuensterGroupLeader(
			@Training List<Map<NamiBaustein, NamiSchulung>> schulungen,
			@Option(title = "datum") LocalDate datum) {
		super();
		this.schulungen = schulungen;
		this.datum = datum;
	}

	private static String getYear(Map<NamiBaustein, NamiSchulung> schulungMap, NamiBaustein baustein) {
		return schulungMap.containsKey(baustein) ? String.valueOf(schulungMap.get(baustein).getDate().getYear()) : "";
	}

	@Override
	public void manipulateDoc(List<NamiMitglied> participants, WordprocessingMLPackage doc){
		Tbl tbl = findTables(doc.getMainDocumentPart().getContent()).get(0);
		for (int i = 0; i < participants.size(); ++i) {
			NamiMitglied p = participants.get(i);
			Map<NamiBaustein, NamiSchulung> s = schulungen.get(i);
			tbl.getContent().add(createTr(
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
					getYear(s, NamiBaustein.BAUSTEIN_3C)));
		}
		addBorders(tbl);
	}

	@Override
	protected String getResourceFileName() {
		return "DPSG-Münster-Jahresanmeldung.docx";
	}
}
