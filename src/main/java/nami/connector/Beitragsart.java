package nami.connector;

/**
 * Beschreibt die möglichen Beitragsarten. Ein möglicher Stiftungseuro wird
 * ignoriert, weil er für die lokalen Anwendungen keine Rolle spielt.
 * 
 * @author Fabian Lipp
 * 
 */
public enum Beitragsart {
    /**
     * Normaler Beitragssatz.
     */
    VOLLER_BEITRAG,

    /**
     * Familienermäßigung.
     */
    FAMILIEN_BEITRAG,

    /**
     * Sozialermäßigung.
     */
    SOZIALERMAESSIGUNG,

    /**
     * Personen, die keinen Mitgliedsbeitrag bezahlen müssen.
     */
    KEIN_BEITRAG;

    /**
     * Setzt einen String in die entsprechende Beitragsart um.
     * 
     * @param str
     *            String-Repräsentation der Beitragsart
     * @return entsprechende Beitragsart; <code>null</code>, wenn keine
     *         Beitragsart angegeben ist
     */
    public static Beitragsart fromString(String str) {
        switch (str) {
        case "Voller Beitrag":
        case "Voller Beitrag - Stiftungseuro":
        case "VOLLER_BEITRAG":
            return VOLLER_BEITRAG;
        case "Familienermäßigt":
        case "Familienermäßigt - Stiftungseuro":
        case "FAMILIEN_BEITRAG":
            return FAMILIEN_BEITRAG;
        case "Sozialermäßigt":
        case "Sozialermäßigt - Stiftungseuro":
        case "SOZIALERMAESSIGUNG":
            return SOZIALERMAESSIGUNG;
        case "NICHT_MITGLIEDER":
        case "KEIN_BEITRAG":
        case "(keine Beitragsarten zugeordnet)":
            return KEIN_BEITRAG;
        case "":
            return null;
        default:
            throw new IllegalArgumentException(
                    "Unexpected String for Beitragsart:" + str);
        }
    }
}
