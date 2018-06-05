package nami.connector.namitypes;

import java.util.Date;

import nami.connector.namitypes.enums.Beitragsart;
import nami.connector.namitypes.enums.Geschlecht;
import nami.connector.namitypes.enums.MitgliedStatus;
import nami.connector.namitypes.enums.Mitgliedstyp;
import nami.connector.namitypes.enums.Stufe;

/**
 * Stellt ein Mitglied der DPSG dar.
 *
 * @author Fabian Lipp
 *
 */
public class NamiMitglied extends NamiAbstractMitglied{
    public String getPLZ() {
        return plz;
    }

    public Date getGeburtsDatum() {
        return geburtsDatum;
    }

    public String getTelefon1() {
        return telefon1;
    }

    public String getTelefon2() {
        return telefon2;
    }

    public String getTelefon3() {
        return telefon3;
    }

    public String getTelefax() {
        return telefax;
    }

    public Stufe getStufe() {
        return stufe;
    }


    /**
     * Beschreibt die Bankverbindung eines Mitglieds.
     */
    public static class Kontoverbindung {
        private Integer id;
        private Integer mitgliedsNummer;

        private String kontoinhaber;
        private String institut;
        private String kontonummer;
        private String bankleitzahl;

        private String iban;
        private String bic;

        private Integer zahlungsKonditionId;
        private String zahlungsKondition;

        public String getKontoinhaber() {
            return kontoinhaber;
        }

        public String getInstitut() {
            return institut;
        }

        public String getKontonummer() {
            return kontonummer;
        }

        public String getBankleitzahl() {
            return bankleitzahl;
        }

        public String getIban() {
            return iban;
        }

        public String getBic() {
            return bic;
        }
    }

    private Integer id;
    private Integer mitgliedsNummer;

    private String vorname;
    private String nachname;
    private String geschlechtId;
    private Geschlecht geschlecht;
    private Date geburtsDatum;

    private String strasse;
    private String nameZusatz;
    private String plz;
    private String ort;
    private Integer regionId;
    private String region;
    private Integer landId;
    private String land;

    private Integer staatsangehoerigkeitId;
    private String staatsangehoerigkeit;
    private String staatsangehoerigkeitText;

    private String telefon1;
    private String telefon2;
    private String telefon3;
    private String telefax;
    private String email;
    private String emailVertretungsberechtigter;

    private String mglTypeId; // ENUM?? z.B. NICHT_MITGLIED
    private Mitgliedstyp mglType;

    private String ersteTaetigkeitId;
    private String ersteTaetigkeit;
    private String ersteUntergliederungId;
    private String ersteUntergliederung;

    private Integer konfessionId;
    private String konfession; //TODO: enum?

    private boolean zeitschriftenversand;
    private boolean wiederverwendenFlag;

    private MitgliedStatus status;
    private Stufe stufe;
    private Integer gruppierungId;
    private String gruppierung;

    private Date eintrittsdatum;
    private Date austrittsDatum;

    private Integer version;
    private Date lastUpdated;

    private Integer beitragsartId;
    private String beitragsart;

    private Kontoverbindung kontoverbindung;

    //private Object fixBeitrag;
    //private Object woelfling;
    //private Object jungpfadfinder;
    //private Object pfadfinder;
    //private Object rover;
    //private String genericField1;
    //private String genericField2;
    //private boolean sonst01;
    //private boolean sonst02;
    //private Object spitzname;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getVorname() {
        return vorname;
    }

    @Override
    public String getNachname() {
        return nachname;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public int getMitgliedsnummer() {
        return mitgliedsNummer;
    }

    @Override
    public MitgliedStatus getStatus() {
        return status;
    }

    @Override
    public Mitgliedstyp getMitgliedstyp() {
        return mglType;
    }

    @Override
    public Geschlecht getGeschlecht() {
        return geschlecht;
    }

    @Override
    public int getGruppierungId() {
        return gruppierungId;
    }

    @Override
    public String getGruppierung() {
        return gruppierung;
    }

    @Override
    public int getVersion() {
        return version;
    }

    public Beitragsart getBeitragsart() {
        return Beitragsart.fromString(beitragsart);
    }

    public Date getEintrittsdatum() {
        return eintrittsdatum;
    }

    public String getStrasse() {
        return strasse;
    }

    public String getPlz() {
        return plz;
    }

    public String getOrt() {
        return ort;
    }

    public Kontoverbindung getKontoverbindung() {
        return kontoverbindung;
    }

}
