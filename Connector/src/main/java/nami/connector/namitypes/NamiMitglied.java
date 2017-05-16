package nami.connector.namitypes;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;

import nami.connector.*;
import nami.connector.exception.NamiApiException;
import nami.connector.exception.NamiException;

import nami.connector.namitypes.enums.*;
import org.apache.http.client.methods.HttpGet;

import com.google.gson.reflect.TypeToken;

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

    /**
     * Holt den Datensatz eines Mitglieds aus NaMi.
     *
     * @param con
     *            Verbindung zum NaMi-Server
     * @param id
     *            ID des Mitglieds
     * @return Mitgliedsdatensatz
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    public static NamiMitglied getMitgliedById(NamiConnector con, int id) throws IOException, NamiApiException {
        NamiURIBuilder builder = con.getURIBuilder(NamiURIBuilder.URL_NAMI_MITGLIED);
        builder.appendPath(Integer.toString(id));
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<NamiMitglied>>() {}.getType();
        NamiResponse<NamiMitglied> resp = con.executeApiRequest(httpGet, type);
        return (resp.isSuccess() ? resp.getData() : null);
    }

    /**
     * Fragt die ID eines Mitglieds anhand der Mitgliedsnummer ab.
     *
     * @param con
     *            Verbindung zum NaMi-Server
     * @param mitgliedsnummer
     *            Mitgliedsnummer des Mitglieds
     * @return Mitglieds-ID
     * @throws IOException
     *             IOException
     * @throws NamiException
     *             Fehler der bei der Anfrage an NaMi auftritt
     */
    public static int getIdByMitgliedsnummer(NamiConnector con, String mitgliedsnummer) throws IOException, NamiException {
        NamiSearchedValues search = new NamiSearchedValues();
        NamiResponse<Collection<NamiMitgliedListElement>> resp = search.getSearchResult(con, 1, 1, 0);

        if (resp.getTotalEntries() == 0) {
            return -1;
        } else if (resp.getTotalEntries() > 1) {
            throw new NamiException("Mehr als ein Mitglied mit Mitgliedsnummer " + mitgliedsnummer);
        } else {
            // genau ein Ergebnis -> Hol das erste Element aus Liste
            NamiMitgliedListElement result = resp.getData().iterator().next();
            return result.getId();
        }
    }
}
