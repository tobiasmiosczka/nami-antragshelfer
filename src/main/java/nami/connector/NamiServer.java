package nami.connector;

/**
 * Beschreibt einen NaMi-Server.
 * 
 * @author Fabian Lipp
 * 
 */
public class NamiServer {

    /**
     * Daten des NaMi-Testservers.
     */
    public static final NamiServer TESTSERVER = new NamiServer(
            "namitest.dpsg.de", false, "ica", true);

    /**
     * Daten des Produktiv-Servers. Der offizielle API-Zugang nicht genutzt.
     */
    public static final NamiServer LIVESERVER = new NamiServer("nami.dpsg.de",
            true, "ica", false);

    /**
     * Daten des Produktiv-Servers mit Benutzung der API.
     */
    public static final NamiServer LIVESERVER_WITH_API = new NamiServer(
            "nami.dpsg.de", true, "ica", true);

    private String namiServer = "namitest.dpsg.de";
    private boolean useSsl = false;
    private String namiDeploy = "ica";
    private boolean useApiAccess = true;

    /**
     * Erstellt die Beschreibung eines NaMi-Servers.
     * 
     * @param namiServer
     *            Hostname des Servers
     * @param useSsl
     *            legt fest, ob SSL für die Verbindung zum Server genutzt wird
     * @param namiDeploy
     *            Installationsverzeichnis auf dem Server
     * @param useApiAccess
     *            gibt an, ob der offizielle API-Zugang genutzt werden soll
     */
    public NamiServer(String namiServer, boolean useSsl, String namiDeploy,
            boolean useApiAccess) {
        this.namiServer = namiServer;
        this.useSsl = useSsl;
        this.namiDeploy = namiDeploy;
        this.useApiAccess = useApiAccess;
    }

    /**
     * Liefert den Hostname des Servers.
     * 
     * @return Hostname
     */
    public String getNamiServer() {
        return namiServer;
    }

    /**
     * Gibt an, ob die Verbindung SSL nutzen soll.
     * 
     * @return <code>true</code>, falls SSL aktiv ist
     */
    public boolean getUseSsl() {
        return useSsl;
    }

    /**
     * Gibt das Installationsverzeichnis auf dem Server an.
     * 
     * @return Installationsverzeichnis
     */
    public String getNamiDeploy() {
        return namiDeploy;
    }

    /**
     * Gibt an, ob der offizielle API-Zugang genutzt werden soll.
     * 
     * @return <code>true</code>, falls der API-Zugang genutzt werden soll
     */
    public boolean useApiAccess() {
        return useApiAccess;
    }
}
