package nami.connector;

/**
 * Mitgliedstypen, die es in NaMi gibt.
 * 
 * @author Fabian Lipp
 * 
 */
public enum Mitgliedstyp {
    // Die Werte des Enums entsprechen genau den Mitgliedstyp-Konstanten aus
    // NaMi (müssen so von der toString-Methode geliefert werden, da sie
    // beispielsweise direkt in Suchanfragen eingefügt werden)
    /**
     * Normale Mitgliedschaft.
     */
    MITGLIED,

    /**
     * Keine Mitgliedschaft.
     */
    NICHT_MITGLIED,

    /**
     * Schnuppermitgliedschaft.
     */
    SCHNUPPER_MITGLIED;

    /**
     * Setzt einen String in den entsprechenden Mitgliedstyp um.
     * 
     * @param str
     *            String-Repräsentation des Mitgliedstyps
     * @return entsprechender Mitgliedstyp; <code>null</code>, wenn kein
     *         Mitgliedstyp angegeben ist
     */
    public static Mitgliedstyp fromString(String str) {
        switch (str) {
        case "Mitglied":
        case "MITGLIED":
            return MITGLIED;
        case "Nicht-Mitglied":
        case "Nicht Mitglied":
        case "NICHT_MITGLIED":
            return NICHT_MITGLIED;
        case "Schnuppermitglied":
        case "SCHNUPPERMITGLIED":
        case "SCHNUPPER_MITGLIED":
            return SCHNUPPER_MITGLIED;
        case "":
            return null;
        default:
            throw new IllegalArgumentException(
                    "Unexpected String for Mitgliedstyp: " + str);
        }
    }
}
