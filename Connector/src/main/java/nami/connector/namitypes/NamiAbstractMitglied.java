package nami.connector.namitypes;

import java.io.IOException;

import nami.connector.*;
import nami.connector.exception.NamiApiException;

public abstract class NamiAbstractMitglied {
    public abstract int getId();

    public abstract String getVorname();

    public abstract String getNachname();

    public abstract String getEmail();

    public abstract int getMitgliedsnummer();

    public abstract MitgliedStatus getStatus();

    public abstract Mitgliedstyp getMitgliedstyp();

    public abstract Geschlecht getGeschlecht();

    public abstract int getGruppierungId();

    public abstract String getGruppierung();

    public abstract int getVersion();

    /**
     * Liefert den vollständigen Mitgliedsdatensatz. Dazu ist evtl. noch eine
     * Anfrage an NaMi notwendig.
     * 
     * @param con
     *            Verbindung zum NaMi-Server
     * @return vollständige Stammdaten des Mitglieds
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     * @throws IOException
     *             IOException
     */
    public abstract NamiMitglied getFullData(NamiConnector con)
            throws NamiApiException, IOException;

    @Override
    public final String toString() {
        return String.format("%s %s", getVorname(), getNachname());
    }
}
