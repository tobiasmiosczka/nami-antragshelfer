package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.TableUtil;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Training;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.enums.NamiBaustein;
import nami.connector.namitypes.NamiSchulungenMap;
import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

import java.time.LocalDate;
import java.util.List;

import static com.github.tobiasmiosczka.nami.util.TimeUtil.calcAge;
import static com.github.tobiasmiosczka.nami.util.TimeUtil.getDateYearString;

@Form(title = "Antrag an Diözese Münster (Leiter)")
public class ApplicationDioezeseMuensterGroupLeader extends DocumentWriter {

	private static final Font FONT = new Font(
			"Calibri",
			StyleTypeDefinitions.FontStyle.REGULAR,
			10D,
			Color.BLACK);
	private static final double HEIGHT = 0.7D;

	private final LocalDate datum;
	private final List<NamiSchulungenMap> schulungen;

	public ApplicationDioezeseMuensterGroupLeader(
			@Training List<NamiSchulungenMap> schulungen,
			@Option(title = "datum") LocalDate datum) {
		super();
		this.schulungen = schulungen;
		this.datum = datum;
	}

	@Override
	public void manipulateDoc(List<NamiMitglied> participants, TextDocument doc){
		Table tParticipants = doc.getTableList().get(0);
		TableUtil.appendRows(
				tParticipants,
				participants.size(),
				HEIGHT,
				FONT,
				StyleTypeDefinitions.HorizontalAlignmentType.CENTER);

		for (int i = 0; i < participants.size(); ++i){
			Row r = tParticipants.getRowList().get(i + 3);
			NamiMitglied p = participants.get(i);
			NamiSchulungenMap s = schulungen.get(i);
			r.getCellByIndex(0).setStringValue(p.getNachname() + ", " + p.getVorname());
			r.getCellByIndex(1).setStringValue(p.getStrasse());
			r.getCellByIndex(2).setStringValue(p.getPLZ() + " " + p.getOrt());
			if (datum != null){
				int diffInYears = calcAge(p.getGeburtsDatum(), datum);
				r.getCellByIndex(3).setStringValue(String.valueOf(diffInYears));
			}
			if (schulungen.get(i).containsKey(NamiBaustein.MLK))
				r.getCellByIndex(6).setStringValue(getDateYearString(s.get(NamiBaustein.MLK).getDate()));
			if (schulungen.get(i).containsKey(NamiBaustein.WBK))
				r.getCellByIndex(6).setStringValue(getDateYearString(s.get(NamiBaustein.WBK).getDate()));
			if (schulungen.get(i).containsKey(NamiBaustein.BAUSTEIN_1B))
				r.getCellByIndex(7).setStringValue(getDateYearString(s.get(NamiBaustein.BAUSTEIN_1B).getDate()));
			if (schulungen.get(i).containsKey(NamiBaustein.BAUSTEIN_2D))
				r.getCellByIndex(8).setStringValue(getDateYearString(s.get(NamiBaustein.BAUSTEIN_2D).getDate()));
			if (schulungen.get(i).containsKey(NamiBaustein.BAUSTEIN_3B))
				r.getCellByIndex(9).setStringValue(getDateYearString(s.get(NamiBaustein.BAUSTEIN_3B).getDate()));
			if (schulungen.get(i).containsKey(NamiBaustein.BAUSTEIN_3C))
				r.getCellByIndex(10).setStringValue(getDateYearString(s.get(NamiBaustein.BAUSTEIN_3C).getDate()));
		}
	}

	@Override
	protected String getResourceFileName() {
		return "Land_Leiter_Blanco.odt";
	}
}
