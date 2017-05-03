package nami.connector.namitypes.enums;

/**
 * Mitgliedstypen, die es in NaMi gibt.
 * 
 * @author Fabian Lipp
 * @author Tobias Miosczka
 * 
 */
public enum Mitgliedstyp {
    // Normale Mitgliedschaft.
    MITGLIED("Mitglied"),

    // Keine Mitgliedschaft.
    NICHT_MITGLIED("Nicht-Mitglied"),

    // Schnuppermitgliedschaft.
    SCHNUPPER_MITGLIED("Schnuppermitglied");

    private final String tag;

    Mitgliedstyp(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    // Die Werte des Enums entsprechen genau den Mitgliedstyp-Konstanten aus
    // NaMi (müssen so von der toString-Methode geliefert werden, da sie
    // beispielsweise direkt in Suchanfragen eingefügt werden)
    public String toString() {
        return this.name();
    }

    /**
     * Setzt einen String in den entsprechenden Mitgliedstyp um.
     * 
     * @param str
     *            String-Repräsentation des Mitgliedstyps
     * @return entsprechender Mitgliedstyp; <code>null</code>, wenn kein
     *         Mitgliedstyp angegeben ist
     */
    public static Mitgliedstyp fromString(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Unexpected String for Mitgliedstyp: null");
        }
        for (Mitgliedstyp mitgliedstyp : Mitgliedstyp.values()) {
            if (mitgliedstyp.getTag().equals(str)) {
                return mitgliedstyp;
            }
        }
        throw new IllegalArgumentException("Unexpected String for Mitgliedstyp: " + str);
    }
}
