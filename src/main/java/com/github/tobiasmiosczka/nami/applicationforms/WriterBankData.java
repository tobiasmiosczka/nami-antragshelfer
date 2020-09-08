package com.github.tobiasmiosczka.nami.applicationforms;

import java.util.List;

import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.enums.NamiBeitragsart;
import nami.connector.namitypes.enums.NamiMitgliedstyp;
import nami.connector.namitypes.enums.NamiStufe;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

public class WriterBankData extends AbstractTextDocumentWriter {

    private final float vollerBeitrag,
                        familienermaessigt,
                        sozialermaessigt;

    public WriterBankData(float vollerBeitrag, float familienermaessigt, float sozialermaessigt) {
        super();
        this.vollerBeitrag = vollerBeitrag;
        this.familienermaessigt = familienermaessigt;
        this.sozialermaessigt = sozialermaessigt;

    }

    @Override
    public void manipulateDoc(List<NamiMitglied> participants, TextDocument odtDoc){
        Table tParticipants = odtDoc.getTableList().get(0);
        for (NamiMitglied m : participants){
            Row row = tParticipants.appendRow();
            if (m != null) {
                //Mitgliedsnummer
                row.getCellByIndex(0).setStringValue(String.valueOf(m.getMitgliedsnummer()));
                //Vorname
                row.getCellByIndex(1).setStringValue(m.getVorname());
                //Nachname
                row.getCellByIndex(2).setStringValue(m.getNachname());
                //Stufe
                row.getCellByIndex(3).setStringValue(getStufeAsString(m.getStufe()));

                //Mitgliedstyp
                row.getCellByIndex(4).setStringValue(m.getMitgliedstyp().getTag());

                //Kontoinhaber
                row.getCellByIndex(5).setStringValue(m.getKontoverbindung().getKontoinhaber());
                //KontoNr.
                row.getCellByIndex(6).setStringValue(m.getKontoverbindung().getKontonummer());
                //BLZ
                row.getCellByIndex(7).setStringValue(m.getKontoverbindung().getBankleitzahl());
                //IBAN
                row.getCellByIndex(8).setStringValue(m.getKontoverbindung().getIban());
                //BIC
                row.getCellByIndex(9).setStringValue(m.getKontoverbindung().getBic());
                //Beitragsart
                row.getCellByIndex(10).setStringValue(m.getBeitragsart().getTag());
                //Beitragshöhe
                row.getCellByIndex(11).setStringValue(String.valueOf(getBeitragshoehe(m.getMitgliedstyp(), m.getBeitragsart())));
            }
        }
    }

    private String getStufeAsString(NamiStufe stufe) {
        if (stufe == null || stufe == NamiStufe.ANDERE) {
            return "";
        }
        else return stufe.toString();
    }

    private float getBeitragshoehe(NamiMitgliedstyp mitgliedstyp, NamiBeitragsart beitragsart) {
        if (mitgliedstyp != NamiMitgliedstyp.MITGLIED) {
            return 0.0f;
        }
        switch (beitragsart) {
            case KEIN_BEITRAG:
                return 0.0f;
            case VOLLER_BEITRAG:
                return vollerBeitrag;
            case FAMILIEN_BEITRAG:
                return familienermaessigt;
            case SOZIALERMAESSIGUNG:
                return sozialermaessigt;
            case VOLLER_BEITRAG_STIFTUNGSEURO:
                return vollerBeitrag + 1.0f;
            case FAMILIEN_BEITRAG_STIFTUNGSEURO:
                return familienermaessigt + 1.0f;
            case SOZIALERMAESSIGUNG_STIFTUNGSEURO:
                return sozialermaessigt + 1.0f;
            default:
                return 0.0f;
        }
    }

    @Override
    public int getMaxParticipantsPerPage() {
        return 0;
    }

    @Override
    protected String getResourceFileName() {
        return "Bankdaten.odt";
    }
}