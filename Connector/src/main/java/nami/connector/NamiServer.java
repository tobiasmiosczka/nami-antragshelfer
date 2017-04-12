package nami.connector;

/**
 * Beschreibt einen NaMi-Server.
 * 
 * @author Fabian Lipp
 * 
 */
public enum NamiServer {

    //Daten des NaMi-Testservers.
    TESTSERVER("namitest.dpsg.de", false, "ica", true),

    //Daten des Produktiv-Servers. Der offizielle API-Zugang nicht genutzt.
    LIVESERVER("nami.dpsg.de", true, "ica", false),

    //Daten des Produktiv-Servers mit Benutzung der API.
    LIVESERVER_WITH_API("nami.dpsg.de", true, "ica", true);

    final private String namiServer;
    final private boolean useSsl;
    final private String namiDeploy;
    final private boolean useApiAccess;

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
    NamiServer(String namiServer, boolean useSsl, String namiDeploy,
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
