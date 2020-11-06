package com.github.tobiasmiosczka.nami.applicationforms;

import java.time.LocalDate;
import java.util.List;

import com.github.tobiasmiosczka.nami.util.GenderUtil;
import com.github.tobiasmiosczka.nami.util.TimeUtil;
import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

public class WriterApplicationDioezeseMuenster extends AbstractTextDocumentWriter {

	private static final Font FONT = new Font(
			"Tahoma",
			StyleTypeDefinitions.FontStyle.REGULAR,
			10D,
			Color.BLACK);

	private final String 	mitgliedsVerband,
							traeger,
							plz,
							ort,
							land;

	private final LocalDate datumVon,
							datumBis;
	private final boolean  	freizeit,
							bildung,
	 						ausbildung,
	 						qualitaetssicherung,
	 						grossveranstaltung;

	public WriterApplicationDioezeseMuenster(
			String mitgliedsVerband,
			String traeger,
			LocalDate datumVon,
			LocalDate datumBis,
			String plz,
			String ort,
			String land,
			boolean freizeit,
			boolean bildung,
			boolean ausbildung,
			boolean qualitaetssicherung,
			boolean grossveranstaltung) {
		super();
		this.mitgliedsVerband = mitgliedsVerband;
		this.traeger = traeger;
		this.datumVon = datumVon;
		this.datumBis = datumBis;
		this.plz = plz;
		this.ort = ort;
		this.land = land;
		this.freizeit = freizeit;
		this.bildung = bildung;
		this.ausbildung = ausbildung;
		this.qualitaetssicherung = qualitaetssicherung;
		this.grossveranstaltung = grossveranstaltung;
	}

	@Override
	public void manipulateDoc(List<NamiMitglied> participants, TextDocument odtDoc) {
		//association data
		Table tAssociation = odtDoc.getHeader().getTableList().get(0);
		tAssociation.getCellByPosition(2, 0).setStringValue(mitgliedsVerband);
		tAssociation.getCellByPosition(2, 1).setStringValue(traeger);
		
		//event data
		Table tEvent = odtDoc.getHeader().getTableList().get(1);
		if (datumVon != null && datumBis != null)
			tEvent.getCellByPosition(2, 0)
					.setStringValue(TimeUtil.getDateString(datumVon) + " - " + TimeUtil.getDateString(datumBis));
		tEvent.getCellByPosition(8, 0).setStringValue(plz + " " + ort);
		tEvent.getCellByPosition(14, 0).setStringValue(land);
		tEvent.getCellByPosition(0, 1).setStringValue(freizeit ? "x" : "");
		tEvent.getCellByPosition(3, 1).setStringValue(bildung ? "x" : "");
		tEvent.getCellByPosition(5, 1).setStringValue(ausbildung ? "x" : "");
		tEvent.getCellByPosition(9, 1).setStringValue(qualitaetssicherung ? "x" : "");
		tEvent.getCellByPosition(11, 1).setStringValue(grossveranstaltung ? "x" : "");


		//participants data
		Table tParticipants = odtDoc.getTableList().get(0);
		double height = tParticipants.getRowByIndex(1).getHeight();
		List<Row> newRows = tParticipants.appendRows(Math.max(participants.size() - 1, 0));
		for (Row row : newRows) {
			row.setHeight(height, true);
			for (int i = 0; i < row.getCellCount(); ++i) {
				Cell cell = row.getCellByIndex(i);
				cell.setFont(FONT);
				cell.setHorizontalAlignment(StyleTypeDefinitions.HorizontalAlignmentType.CENTER);
			}
		}

		for (int i = 0; i < participants.size(); ++i) {
			Row r = tParticipants.getRowList().get(i + 1);
			NamiMitglied p = participants.get(i);
			r.getCellByIndex(2).setStringValue(p.getNachname() + ", " + p.getVorname());
			r.getCellByIndex(3).setStringValue(p.getStrasse() + ", " + p.getPLZ() + ", " + p.getOrt());
			r.getCellByIndex(4).setStringValue("" + GenderUtil.getCharacter(p.getGeschlecht()));
			if ((datumVon != null && datumBis != null))
				r.getCellByIndex(5)
						.setStringValue(TimeUtil.calcAgeRange(LocalDate.from(p.getGeburtsDatum()), datumVon, datumBis));
		}
	}

	@Override
	protected String getResourceFileName() {
		return "Land_Blanco.odt";
	}
}