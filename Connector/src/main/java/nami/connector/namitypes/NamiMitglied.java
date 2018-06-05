package nami.connector.namitypes;

import java.util.Date;

import nami.connector.namitypes.enums.NamiBeitragsart;
import nami.connector.namitypes.enums.NamiGeschlecht;
import nami.connector.namitypes.enums.NamiMitgliedStatus;
import nami.connector.namitypes.enums.NamiMitgliedstyp;
import nami.connector.namitypes.enums.NamiStufe;

/**
 * Stellt ein Mitglied der DPSG dar.
 *
 * @author Fabian Lipp
 *
 */
public class NamiMitglied {
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

    public NamiStufe getStufe() {
        return stufe;
    }

    private Integer id;
    private Integer mitgliedsNummer;

    private String vorname;
    private String nachname;
    private String geschlechtId;
    private NamiGeschlecht geschlecht;
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
    private NamiMitgliedstyp mglType;

    private String ersteTaetigkeitId;
    private String ersteTaetigkeit;
    private String ersteUntergliederungId;
    private String ersteUntergliederung;

    private Integer konfessionId;
    private String konfession; //TODO: enum?

    private boolean zeitschriftenversand;
    private boolean wiederverwendenFlag;

    private NamiMitgliedStatus status;
    private NamiStufe stufe;
    private Integer gruppierungId;
    private String gruppierung;

    private Date eintrittsdatum;
    private Date austrittsDatum;

    private Integer version;
    private Date lastUpdated;

    private Integer beitragsartId;
    private String beitragsart;

    private NamiKontoverbindung kontoverbindung;

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

    public int getId() {
        return id;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public String getEmail() {
        return email;
    }

    public int getMitgliedsnummer() {
        return mitgliedsNummer;
    }

    public NamiMitgliedStatus getStatus() {
        return status;
    }

    public NamiMitgliedstyp getMitgliedstyp() {
        return mglType;
    }

    public NamiGeschlecht getGeschlecht() {
        return geschlecht;
    }

    public int getGruppierungId() {
        return gruppierungId;
    }

    public String getGruppierung() {
        return gruppierung;
    }

    public int getVersion() {
        return version;
    }

    public NamiBeitragsart getBeitragsart() {
        return NamiBeitragsart.fromString(beitragsart);
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

    public NamiKontoverbindung getKontoverbindung() {
        return kontoverbindung;
    }

    @Override
    public final String toString() {
        return String.format("%s %s", getVorname(), getNachname());
    }
}
