package nami.connector.namitypes;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import nami.connector.NamiConnector;
import nami.connector.NamiResponse;
import nami.connector.NamiURIBuilder;
import nami.connector.exception.NamiApiException;

import org.apache.http.client.methods.HttpGet;

import com.google.gson.reflect.TypeToken;

/**
 * Beschreibt einen Eintrag aus einem Enum in NaMi. Zum Beispiel eine Tätigkeit
 * oder eine Untergliederung.
 * 
 * @author Fabian Lipp
 * 
 */
public class NamiEnum {
    private String descriptor;
    // private String representedClass;
    private Integer id;

    /**
     * Liest alle verfügbaren Tätigkeiten aus NaMi aus.
     * 
     * @param con
     *            Verbindung zum NaMi-Server.
     * @return Liste der verfügbaren Tätigkeiten
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     * @throws IOException
     *             IOException
     */
    public static List<NamiEnum> getTaetigkeiten(NamiConnector con)
            throws NamiApiException, IOException {
        NamiURIBuilder builder = con
                .getURIBuilder(NamiURIBuilder.URL_TAETIGKEITEN);
        HttpGet httpGet = new HttpGet(builder.build());

        Type type = new TypeToken<NamiResponse<List<NamiEnum>>>() {
        }.getType();
        NamiResponse<List<NamiEnum>> resp = con
                .executeApiRequest(httpGet, type);

        return resp.getData();
    }

    /**
     * Liest alle verfügbaren Untergliederungen (Stufe/Abteilung) aus NaMi aus.
     * 
     * @param con
     *            Verbindung zum NaMi-Server.
     * @return Liste der verfügbaren Untergliederungen
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     * @throws IOException
     *             IOException
     */
    public static List<NamiEnum> getUntergliederungen(NamiConnector con)
            throws NamiApiException, IOException {
        NamiURIBuilder builder = con
                .getURIBuilder(NamiURIBuilder.URL_UNTERGLIEDERUNGEN);
        HttpGet httpGet = new HttpGet(builder.build());

        Type type = new TypeToken<NamiResponse<List<NamiEnum>>>() {
        }.getType();
        NamiResponse<List<NamiEnum>> resp = con
                .executeApiRequest(httpGet, type);

        return resp.getData();
    }

    @Override
    public String toString() {
        return String.format("%-2d %s", id, descriptor);
    }
}
