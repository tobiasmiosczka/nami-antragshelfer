package nami.connector.namitypes;

import java.io.IOException;
import java.lang.reflect.Type;

import nami.connector.NamiConnector;
import nami.connector.NamiResponse;
import nami.connector.NamiURIBuilder;
import nami.connector.exception.NamiApiException;

import org.apache.http.client.methods.HttpGet;

import com.google.gson.reflect.TypeToken;

/**
 * Stellt eine Tätigkeitszuordnung für ein Mitglied dar.
 * 
 * @author Fabian Lipp
 * 
 */
public class NamiTaetigkeitAssignment {
    // Die folgenden Variablen stammen aus NaMi. Keinesfalls umbenennen.
    private int id;
    private String gruppierung;
    private int gruppierungId;
    private String taetigkeit;
    private int taetigkeitId;
    private String caeaGroup;
    private int caeaGroupId;
    private String caeaGroupForGf;
    private int caeaGroupForGfId;
    private String untergliederung;
    private int untergliederungId;
    private String aktivVon;
    private String aktivBis;

    /**
     * Liefert die Gruppierungsnummer, in der die Tätigkeit ausgeübt wird.
     * 
     * @return Gruppierungsnummer
     */
    public int getGruppierungId() {
        return gruppierungId;
    }

    /**
     * Liefert die Tätigkeits-ID.
     * 
     * @return Tätigkeit-ID
     */
    public int getTaetigkeitId() {
        return taetigkeitId;
    }

    /**
     * Liefert die ID der Untergliederung (Stufe/Abteilung).
     * 
     * @return Untergliederung-ID
     */
    public int getUntergliederungId() {
        return untergliederungId;
    }

    /**
     * Gibt an, ob die Tätigkeit aktuell noch aktiv ist.
     * 
     * @return <code>true</code>, falls die Tätigkeit aktiv ist
     */
    public boolean isAktiv() {
        return aktivBis.isEmpty();
    }

    /**
     * Holt eine Tätigkeitszuordnung aus NaMi.
     * 
     * @param con
     *            Verbindung zum NaMi-Server
     * @param personId
     *            ID des Mitglieds, dessen Tätigkeit abgefragt werden soll
     * @param taetigkeitId
     *            ID der Tätigkeitszuordnung
     * @return Tätigkeits-Datensatz
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    public static NamiTaetigkeitAssignment getTaetigkeit(NamiConnector con,
            int personId, int taetigkeitId) throws IOException,
            NamiApiException {

        NamiURIBuilder builder = con
                .getURIBuilder(NamiURIBuilder.URL_NAMI_TAETIGKEIT);
        builder.appendPath(Integer.toString(personId));
        builder.appendPath(Integer.toString(taetigkeitId));
        HttpGet httpGet = new HttpGet(builder.build());
        Type type = new TypeToken<NamiResponse<NamiTaetigkeitAssignment>>() {
        }.getType();
        NamiResponse<NamiTaetigkeitAssignment> resp = con.executeApiRequest(
                httpGet, type);

        if (resp.isSuccess()) {
            return resp.getData();
        } else {
            return null;
        }
    }
}
