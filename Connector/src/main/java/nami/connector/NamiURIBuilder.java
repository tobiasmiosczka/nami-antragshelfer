package nami.connector;

import java.net.URI;
import java.net.URISyntaxException;

import nami.connector.exception.NamiURISyntaxException;

import org.apache.http.client.utils.URIBuilder;

/**
 * Baut URIs für die Anfragen an NaMi zusammen.
 * 
 * @author Fabian Lipp
 * 
 */
public class NamiURIBuilder extends URIBuilder {
    /**
     * URL, die zum Login in NaMi verwendet wird.
     */
    private static final String URL_NAMI_STARTUP = "/rest/nami/auth/manual/sessionStartup";
    /**
     * URL, mit der die Root-Gruppierung und die Kinder für jede Gruppierung
     * abgefragt werden.
     */
    public static final String URL_NAMI_GRP = "/nami/gruppierungen/filtered-for-navigation/gruppierung/node";

    /**
     * URL, mit der der Datensatz eines Mitglieds (identifiziert durch seine ID)
     * abgefragt wird.
     */
    // Am Ende der URL müsste eigentlich die GruppierungsID angegeben sein.
    // Scheinbar kann man aber auch immer "0" angeben und bekommt
    // trotzdem jedes Mitglied geliefert
    public static final String URL_NAMI_MITGLIED = "/nami/mitglied/filtered-for-navigation/gruppierung/gruppierung/0";

    /**
     * URL, mit der eine Tätigkeitszuordnung eines Mitglieds abgefragt wird.
     */
    public static final String URL_NAMI_TAETIGKEIT = "/nami/zugeordnete-taetigkeiten/filtered-for-navigation/gruppierung-mitglied/mitglied";

    /**
     * URL, mit der die Mitglieder ausgelesen werden können, die einer
     * bestimmten Gruppierung angehören (entweder als Stammgruppierung oder sie
     * üben dort eine Tätigkeit aus).
     */
    public static final String URL_MITGLIEDER_FROM_GRUPPIERUNG = "/nami/mitglied/filtered-for-navigation/gruppierung/gruppierung/%s/flist";

    /**
     * URL, mit der die Beitragszahlungen eines Mitglieds abgefragt werden
     * können.
     */
    public static final String URL_BEITRAGSZAHLUNGEN = "/mgl-verwaltungS/beitrKonto-anzeigen";

    /**
     * URL, um eine Suchanfrage an NaMi zu senden.
     */
    public static final String URL_NAMI_SEARCH = "/nami/search/result-list";

    /**
     * URL, mit der alle verfügbaren Tätigkeiten abgefragt werden können.
     */
    public static final String URL_TAETIGKEITEN = "/system/taetigkeit";

    /**
     * URL, mit der alle verfügbaren Untergliederungen abgefragt werden können.
     */
    public static final String URL_UNTERGLIEDERUNGEN = "/orgadmin/untergliederung";


    /**
     * Erzeugt einen URIBuilder für den gegebenen Server und hängt sofort einen
     * Pfad an die URI an.
     * 
     * @param server
     *            Server, auf den die URI zeigt
     * @param path
     *            Pfad, der an die URI angefügt wird. Das NamiDeploy-Verzeichnis
     *            (z.B. ica) aus der Server-Konfiguration wird automatisch vorne
     *            angefügt.
     * @param restUrl
     *            gibt an, ob vor dem übergebenen Pfad noch die Pfadkomponente
     *            "rest/" und ggf. die API-Version eingefügt werden soll
     */
    public NamiURIBuilder(NamiServer server, String path, boolean restUrl) {
        super();

        if (server.getUseSsl()) {
            setScheme("https");
        } else {
            setScheme("http");
        }
        setHost(server.getNamiServer());

        setPath("/" + server.getNamiDeploy());
        if (restUrl) {
            appendPath("rest");
            if (server.useApiAccess()) {
                appendPath("api/2/2/service");
            }
        }

        appendPath(path);
    }

    /**
     * Hängt einen weiteren Abschnitt an den Pfad an. Bei Bedarf wird ein '/'
     * vorne eingefügt.
     * 
     * @param pathAppendix
     *            zu ergänzender Pfad
     */
    public void appendPath(String pathAppendix) {
        String path = getPath();
        if (path.isEmpty()) {
            path = "/";
        }

        if ((path.charAt(path.length() - 1) != '/')
                && (pathAppendix.charAt(0) != '/')) {
            setPath(path + "/" + pathAppendix);
        } else {
            setPath(path + pathAppendix);
        }
    }

    @Override
    public URI build() {
        try {
            return super.build();
        } catch (URISyntaxException e) {
            throw new NamiURISyntaxException(e);
        }
    }

    /**
     * Liefert einen NamiURIBuilder, der den Pfad für den Login in NaMi enthält.
     * 
     * Diese Methode ist notwendig, da dieser Pfad immer gleich bleibt
     * (unabhängig davon, ob der Zugang über die API genutzt wird oder nicht).
     * 
     * @param server
     *            Server, mit dem die Verbindung aufgebaut werden soll
     * @return NamiURIBuilder für den NaMi-Login
     */
    public static NamiURIBuilder getLoginURIBuilder(NamiServer server) {
        return new NamiURIBuilder(server, URL_NAMI_STARTUP, false);
    }
}
