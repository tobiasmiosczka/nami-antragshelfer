package nami.connector.namitypes;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import nami.connector.*;
import nami.connector.exception.NamiApiException;
import nami.connector.exception.NamiException;

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
        private String id;
        private String mitgliedsNummer;

        private String kontoinhaber;
        private String kontonummer;
        private String bankleitzahl;
        private String institut;

        private String iban;
        private String bic;
    }

    private int id;
    private int mitgliedsNummer;

    private String beitragsarten;
    private Collection<Integer> beitragsartenId;
    private String statusId; // ENUM??
    private String status; // ENUM?? (z.B. AKTIV)

    private String vorname;
    private String nachname;
    private String strasse;
    private String plz;
    private String ort;
    private String telefon1;
    private String telefon2;
    private String telefon3;
    private String telefax;
    private String email;
    private String emailVertretungsberechtigter;

    private String staatsangehoerigkeitId; // int?
    private String staatsangehoerigkeit;
    private String staatsangehoerigkeitText;

    private String mglTypeId; // ENUM?? z.B. NICHT_MITGLIED
    private String mglType;

    private Date geburtsDatum;

    private String regionId; // int? (null möglich)
    private String region;

    private String landId; // int?
    private String land;

    private String gruppierung;
    private int gruppierungId;
    // private String ersteUntergliederungId" : null, //?
    private String ersteUntergliederung;

    private String ersteTaetigkeitId;
    private String ersteTaetigkeit;
    private Stufe stufe;

    private boolean wiederverwendenFlag;
    private boolean zeitschriftenversand;

    private String konfessionId; // int?
    private String konfession; // ENUM?
    private String geschlechtId;
    private Geschlecht geschlecht;

    private Date eintrittsdatum;
    private String zahlungsweise;
    private int version;
    private Date lastUpdated;

    private Kontoverbindung kontoverbindung;

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
        return MitgliedStatus.fromString(status);
    }

    @Override
    public Mitgliedstyp getMitgliedstyp() {
        return Mitgliedstyp.fromString(mglType);
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

    /**
     * Liefert die Beitragsart des Mitglieds.
     *
     * @return Beitragsart
     */
    public Beitragsart getBeitragsart() {
        return Beitragsart.fromString(beitragsarten);
    }

    /**
     * Liefert das Eintrittsdatum des Mitglieds.
     *
     * @return Eintrittsdatum
     */
    public Date getEintrittsdatum() {
        return eintrittsdatum;
    }

    /**
     * Liefert die Straße der Wohnung des Mitglieds.
     *
     * @return Straße
     */
    public String getStrasse() {
        return strasse;
    }

    /**
     * Liefert die PLZ der Wohnung des Mitglieds.
     *
     * @return PLZ
     */
    public String getPlz() {
        return plz;
    }

    /**
     * Liefert den Ort der Wohnung des Mitglieds.
     *
     * @return Ort
     */
    public String getOrt() {
        return ort;
    }

    @Override
    public NamiMitglied getFullData(NamiConnector con) throws NamiApiException, IOException {
        // do nothing (this object already contains the full data)
        return this;
    }

    /**
     * Gibt die Stammdaten dieses Mitglieds als ausführlichen Text zurück. Der
     * Rückgabewert enthält also mehr Angaben als die Ausgabe der
     * <tt>toString</tt>-Method.
     *
     * @return für die Ausgabe formatierte Mitgliedsdaten
     */
    public String toLongString() {
        StringBuilder str = new StringBuilder();

        /*
          Speichert eine Zeile, die ausgegeben werden soll.
         */
        final class Row {
            private String key;
            private String value;

            private Row(String key, String value) {
                this.key = key;
                this.value = value;
            }
        }
        List<Row> rows = new LinkedList<>();
        rows.add(new Row("Nachname", nachname));
        rows.add(new Row("Vorname", vorname));
        rows.add(new Row("Straße", strasse));
        rows.add(new Row("PLZ, Ort", plz + " " + ort));
        rows.add(new Row("E-Mail", email));
        rows.add(new Row("E-Mail Vertr.", emailVertretungsberechtigter));
        rows.add(new Row("Telefon 1", telefon1));
        rows.add(new Row("Telefon 2", telefon2));
        rows.add(new Row("Telefon 3", telefon3));
        rows.add(new Row("Telefax", telefax));
        //TODO: integrate
        rows.add(new Row("Geburtsdatum", geburtsDatum.toString()));
        rows.add(new Row("Stammgruppierung", gruppierung));
        rows.add(new Row("Stufe", stufe.toString()));
        // TODO: integrate
        rows.add(new Row("Eintrittsdatum", eintrittsdatum.toString()));

        int longestKey = 0;
        for (Row row : rows) {
            if (row.key.length() > longestKey) {
                longestKey = row.key.length();
            }
        }
        String formatString = "  %-" + (longestKey + 1) + "s %s\n";
        for (Row row : rows) {
            str.append(String.format(formatString, row.key + ":", row.value));
        }

        return str.toString();
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
     *
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
