package nami.connector;

/**
 * Objekt, das die Ergebnisse aus NaMi enthält.
 * 
 * @author Fabian Lipp
 * 
 * @param <DataT>
 *            Typ der enthaltenen Daten
 */
public class NamiResponse<DataT> {
    // Die folgenden Variablen stammen aus NaMi. Keinesfalls umbenennen.
    private boolean success;
    private DataT data;
    private int totalEntries;
    private String message;
    // private String responseType;

    /**
     * Liefert den Erfolgswert der Anfrage.
     * 
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Liefert die enthaltenen Daten.
     * 
     * @return the data
     */
    public DataT getData() {
        return data;
    }

    /**
     * Liefert die Anzahl der gefundenen Einträge.
     * 
     * @return the totalEntries
     */
    public int getTotalEntries() {
        return totalEntries;
    }

    /**
     * Liefert eine evtl. vorhandene Fehlermeldung.
     * 
     * @return Fehlermeldung
     */
    public String getMessage() {
        return message;
    }
}
