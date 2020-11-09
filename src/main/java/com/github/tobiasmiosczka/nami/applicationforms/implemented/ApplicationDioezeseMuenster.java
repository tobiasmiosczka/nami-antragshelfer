package com.github.tobiasmiosczka.nami.applicationforms.implemented;

import java.time.LocalDate;
import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.TableUtil;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import com.github.tobiasmiosczka.nami.util.GenderUtil;
import com.github.tobiasmiosczka.nami.util.TimeUtil;
import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

@Form(title = "Antrag an Diözese Münster")
public class ApplicationDioezeseMuenster extends DocumentWriter {

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

	public ApplicationDioezeseMuenster(
			@Option(title = "Mitgliedsverband") String mitgliedsVerband,
			@Option(title = "Träger") String traeger,
			@Option(title = "Datum (von)") LocalDate datumVon,
			@Option(title = "Datum (bis)") LocalDate datumBis,
			@Option(title = "Postleitzahl") String plz,
			@Option(title = "Ort") String ort,
			@Option(title = "Land") String land,
			@Option(title = "Freizeit") boolean freizeit,
			@Option(title = "Bildung") boolean bildung,
			@Option(title = "Aus-/Fortbildung") boolean ausbildung,
			@Option(title = "Qualitätssicherung") boolean qualitaetssicherung,
			@Option(title = "Großveranstaltung") boolean grossveranstaltung) {
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
	public void manipulateDoc(List<NamiMitglied> participants, TextDocument doc) {
		//association data
		Table tAssociation = doc.getHeader().getTableList().get(0);
		tAssociation.getCellByPosition(2, 0).setStringValue(mitgliedsVerband);
		tAssociation.getCellByPosition(2, 1).setStringValue(traeger);
		
		//event data
		Table tEvent = doc.getHeader().getTableList().get(1);
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
		Table tParticipants = doc.getTableList().get(0);
		double height = tParticipants.getRowByIndex(1).getHeight();
		TableUtil.appendRows(tParticipants, participants.size(), height, FONT, StyleTypeDefinitions.HorizontalAlignmentType.CENTER);
		for (int i = 0; i < participants.size(); ++i) {
			Row r = tParticipants.getRowList().get(i + 1);
			NamiMitglied p = participants.get(i);
			r.getCellByIndex(2).setStringValue(p.getNachname() + ", " + p.getVorname());
			r.getCellByIndex(3).setStringValue(p.getStrasse() + ", " + p.getPLZ() + ", " + p.getOrt());
			r.getCellByIndex(4).setStringValue("" + GenderUtil.getCharacter(p.getGeschlecht()));
			if (datumVon != null && datumBis != null)
				r.getCellByIndex(5)
						.setStringValue(TimeUtil.calcAgeRange(p.getGeburtsDatum(), datumVon, datumBis));
		}
	}

	@Override
	protected String getResourceFileName() {
		return "Land_Blanco.odt";
	}
}