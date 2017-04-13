package nami.connector.namitypes;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

import nami.connector.namitypes.enums.Geschlecht;
import nami.connector.namitypes.enums.MitgliedStatus;
import nami.connector.namitypes.enums.Mitgliedstyp;
import nami.connector.NamiConnector;
import nami.connector.NamiResponse;
import nami.connector.NamiURIBuilder;
import nami.connector.exception.NamiApiException;

import org.apache.http.client.methods.HttpGet;

import com.google.gson.reflect.TypeToken;

public class NamiMitgliedListElement extends NamiAbstractMitglied implements Comparable<NamiMitgliedListElement> {
    public static class EntriesType {
        private String id;

        private String vorname;
        private String nachname;

        private String email;
        private String emailVertretungsberechtigter;
        private String telefon1;
        private String telefon2;
        private String telefon3;
        private String telefax;

        // nur in Suche, nicht in Mitgliederverwaltung
        private String gruppierungId;
        private String gruppierung;

        private String stufe;
        private String geburtsDatum;

        private String mglType;
        private String status;

        private String staatsangehoerigkeit;
        private String staatangehoerigkeitText;
        private Geschlecht geschlecht;
        private String konfession;
        private String rowCssClass;
        private String lastUpdated;
        private String version;
        private String wiederverwendenFlag;
        private String mitgliedsNummer;
    }

    private String descriptor;
    private EntriesType entries;
    private int id;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getGruppierungId() {
        return Integer.parseInt(entries.gruppierungId);
    }

    @Override
    public String getGruppierung() {
        return entries.gruppierung;
    }

    @Override
    public String getVorname() {
        return entries.vorname;
    }

    @Override
    public String getNachname() {
        return entries.nachname;
    }

    @Override
    public String getEmail() {
        return entries.email;
    }

    @Override
    public MitgliedStatus getStatus() {
        return MitgliedStatus.fromString(entries.status);
    }

    @Override
    public Mitgliedstyp getMitgliedstyp() {
        return Mitgliedstyp.fromString(entries.mglType);
    }

    @Override
    public Geschlecht getGeschlecht() {
        return entries.geschlecht;
    }

    @Override
    public int getMitgliedsnummer() {
        return Integer.parseInt(entries.mitgliedsNummer);
    }

    @Override
    public int getVersion() {
        return Integer.parseInt(entries.version);
    }

    public NamiMitglied getFullData(NamiConnector con) throws NamiApiException, IOException {
        return NamiMitglied.getMitgliedById(con, id);
    }

    @Override
    public int compareTo(NamiMitgliedListElement o) {
        return (this.id - o.id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof NamiMitgliedListElement)) {
            return false;
        }
        NamiMitgliedListElement other = (NamiMitgliedListElement) obj;
        return id == other.id;
    }

    /**
     * Liefert die Mitglieder, die einer bestimmten Gruppierung angehören
     * (entweder als Stammgruppierung oder sie üben dort eine Tätigkeit aus).
     * 
     * @param con
     *            Verbindung zum NaMi-Server
     * @param gruppierungsnummer
     *            Nummer der Gruppierung, in der gesucht werden soll
     * @return gefundene Mitglieder
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     * @throws IOException
     *             IOException
     */
    public static Collection<NamiMitgliedListElement> getMitgliederFromGruppierung(
            NamiConnector con, String gruppierungsnummer)
            throws NamiApiException, IOException {

        String url = String.format(
                NamiURIBuilder.URL_MITGLIEDER_FROM_GRUPPIERUNG,
                gruppierungsnummer);
        NamiURIBuilder builder = con.getURIBuilder(url);
        builder.setParameter("limit", "5000");
        builder.setParameter("page", "1");
        builder.setParameter("start", "0");
        HttpGet httpGet = new HttpGet(builder.build());

        Type type = new TypeToken<NamiResponse<Collection<NamiMitgliedListElement>>>() {
        }.getType();
        NamiResponse<Collection<NamiMitgliedListElement>> resp = con.executeApiRequest(httpGet, type);

        if (resp.isSuccess()) {
            return resp.getData();
        } else {
            throw new NamiApiException("Could not get member list from Nami: "
                    + resp.getMessage());
        }

    }

}
