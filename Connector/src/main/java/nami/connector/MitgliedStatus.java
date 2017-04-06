package nami.connector;

/**
 * Stati, die es in NaMi gibt.
 * 
 * @author Fabian Lipp
 * 
 */
public enum MitgliedStatus {
    // Die Werte des Enums entsprechen genau den Mitgliedsstatus-Konstanten aus
    // NaMi (müssen so von der toString-Methode geliefert werden, da sie
    // beispielsweise direkt in Suchanfragen eingefügt werden)
    /**
     * Mitglied ist aktiv.
     */
    AKTIV,
    /**
     * Mitglied ist nicht aktiv.
     */
    INAKTIV;

    /**
     * Setzt einen String in den entsprechenden Mitgliedstyp um.
     * 
     * @param str
     *            String-Repräsentation des Mitgliedstyps
     * @return entsprechender Mitgliedstyp; <code>null</code>, wenn der String
     *         nicht umgesetzt werden kann
     */
    public static MitgliedStatus fromString(String str) {
        switch (str) {
        case "Aktiv":
        case "aktiv":
        case "AKTIV":
            return AKTIV;
        case "Inaktiv":
        case "inaktiv":
        case "INAKTIV":
            return INAKTIV;
        case "":
            return null;
        default:
            throw new IllegalArgumentException(
                    "Unexpected String for MitgliedStatus: " + str);
        }
    }
}
