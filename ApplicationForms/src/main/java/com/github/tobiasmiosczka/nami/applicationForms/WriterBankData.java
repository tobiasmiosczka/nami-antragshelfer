package com.github.tobiasmiosczka.nami.applicationForms;

import java.util.List;

import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.enums.Beitragsart;
import nami.connector.namitypes.enums.Stufe;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

public class WriterBankData extends TextDocumentWriter {

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
        //participants data
        Table tParticipants = odtDoc.getTableList().get(0);
        for (NamiMitglied m : participants){
            Row row = tParticipants.appendRow();
            if (m != null) {
                //Vorname
                row.getCellByIndex(0).setStringValue(m.getVorname());
                //Nachname
                row.getCellByIndex(1).setStringValue(m.getNachname());
                //Stufe
                row.getCellByIndex(2).setStringValue(getStufeAsString(m.getStufe()));
                //Kontoinhaber
                row.getCellByIndex(3).setStringValue(m.getKontoverbindung().getKontoinhaber());
                //IBAN
                row.getCellByIndex(4).setStringValue(m.getKontoverbindung().getIban());
                //BIC
                row.getCellByIndex(5).setStringValue(m.getKontoverbindung().getBic());
                //Beitragsart
                row.getCellByIndex(6).setStringValue(m.getBeitragsart().getTag());
                //Beitragshöhe
                row.getCellByIndex(7).setStringValue(String.valueOf(getBeitragshöhe(m.getBeitragsart())));
            }
        }
    }

    public String getStufeAsString(Stufe stufe) {
        if (stufe == null || stufe == Stufe.ANDERE) {
            return "";
        }
        else return stufe.toString();
    }

    public float getBeitragshöhe(Beitragsart beitragsart) {
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