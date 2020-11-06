package com.github.tobiasmiosczka.nami.applicationforms;

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


public class WriterApplicationDioezeseMuensterGroupLeader extends AbstractTextDocumentWriter {

	private static final Font FONT = new Font(
			"Calibri",
			StyleTypeDefinitions.FontStyle.REGULAR,
			10D,
			Color.BLACK);
	private static final double HEIGHT = 0.7D;

	private final LocalDate datum;
	private final List<NamiSchulungenMap> schulungen;

	public WriterApplicationDioezeseMuensterGroupLeader(List<NamiSchulungenMap> schulungen, LocalDate datum) {
		super();
		this.schulungen = schulungen;
		this.datum = datum;
	}

	@Override
	public void manipulateDoc(List<NamiMitglied> participants, TextDocument odtDoc){
		Table tParticipants = odtDoc.getTableList().get(0);
		List<Row> newRows = tParticipants.appendRows(Math.max(participants.size() - 1, 0));
		for (Row row : newRows) {
			row.setHeight(HEIGHT, true);
			for(int cell = 0; cell < row.getCellCount(); ++cell)
				row.getCellByIndex(cell).setFont(FONT);
		}

		for(int i = 0; i < participants.size(); ++i){
			Row r = tParticipants.getRowList().get(i + 3);
			NamiMitglied p = participants.get(i);
			NamiSchulungenMap s = schulungen.get(i);
			r.getCellByIndex(0).setStringValue(p.getNachname() + ", " + p.getVorname());
			r.getCellByIndex(1).setStringValue(p.getStrasse());
			r.getCellByIndex(2).setStringValue(p.getPLZ() + " " + p.getOrt());
			if(datum != null){
				int diffInYears = calcAge(LocalDate.from(p.getGeburtsDatum()), datum);
				r.getCellByIndex(3).setStringValue(String.valueOf(diffInYears));
			}
			if(schulungen.get(i).containsKey(NamiBaustein.MLK))
				r.getCellByIndex(6).setStringValue(getDateYearString(s.get(NamiBaustein.MLK).getDate()));
			if(schulungen.get(i).containsKey(NamiBaustein.WBK))
				r.getCellByIndex(6).setStringValue(getDateYearString(s.get(NamiBaustein.WBK).getDate()));
			if(schulungen.get(i).containsKey(NamiBaustein.BAUSTEIN_1B))
				r.getCellByIndex(7).setStringValue(getDateYearString(s.get(NamiBaustein.BAUSTEIN_1B).getDate()));
			if(schulungen.get(i).containsKey(NamiBaustein.BAUSTEIN_2D))
				r.getCellByIndex(8).setStringValue(getDateYearString(s.get(NamiBaustein.BAUSTEIN_2D).getDate()));
			if(schulungen.get(i).containsKey(NamiBaustein.BAUSTEIN_3B))
				r.getCellByIndex(9).setStringValue(getDateYearString(s.get(NamiBaustein.BAUSTEIN_3B).getDate()));
			if(schulungen.get(i).containsKey(NamiBaustein.BAUSTEIN_3C))
				r.getCellByIndex(10).setStringValue(getDateYearString(s.get(NamiBaustein.BAUSTEIN_3C).getDate()));
		}
	}

	@Override
	protected String getResourceFileName() {
		return "Land_Leiter_Blanco.odt";
	}
}
