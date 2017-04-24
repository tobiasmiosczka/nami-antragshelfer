package nami.connector.namitypes.enums;

/**
 * Stati, die es in NaMi gibt.
 * 
 * @author Fabian Lipp
 *
 * 
 */
public enum MitgliedStatus {
    // Die Werte des Enums entsprechen genau den Mitgliedsstatus-Konstanten aus
    // NaMi (müssen so von der toString-Methode geliefert werden, da sie
    // beispielsweise direkt in Suchanfragen eingefügt werden)
    /**
     * Mitglied ist aktiv.
     */
    AKTIV("Aktiv"),
    /**
     * Mitglied ist nicht aktiv.
     */
    INAKTIV("Inaktiv");

    private String tag;

    MitgliedStatus(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    /**
     * Setzt einen String in den entsprechenden Mitgliedstyp um.
     * 
     * @param str
     *            String-Repräsentation des Mitgliedstyps
     * @return entsprechender Mitgliedstyp; <code>null</code>, wenn der String
     *         nicht umgesetzt werden kann
     */
    public static MitgliedStatus fromString(String str) {
        if (str == null ||str.equals("")) {
            throw new IllegalArgumentException("Unexpected String for MitgliedStatus: " + str);
        }
        for (MitgliedStatus mitgliedStatus : MitgliedStatus.values()) {
            if (mitgliedStatus.getTag().equals(str)) {
                return mitgliedStatus;
            }
        }
        throw new IllegalArgumentException("Unexpected String for MitgliedStatus: " + str);
    }
}
