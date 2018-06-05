package nami.connector.namitypes.enums;

/**
 * Beschreibt das Geschlecht eines Mitglieds.
 *
 * @author Fabian Lipp
 *
 */
public enum NamiGeschlecht {
    /**
     * Männlich.
     */
    MAENNLICH("männlich", 'm'),

    /**
     * Weiblich.
     */
    WEIBLICH("weiblich", 'w'),

    /**
     * Keine Angabe.
     */
    KEINE_ANGABE("", ' ');

    private String tag;
    private char character;

    private NamiGeschlecht(String tag, char character) {
        this.tag = tag;
        this.character = character;
    }

    public String getTag() {
        return tag;
    }

    public char getCharacter() {
        return character;
    }

    /**
     * Setzt einen String ins entsprechende Geschlecht um.
     *
     * @param str
     *            String-Repräsentation des Geschlechts
     * @return entsprechende Ebene; <code>null</code>, wenn der String nicht
     *         umgesetzt werden kann
     */
    public static NamiGeschlecht fromString(String str) {
        switch (str) {
            case "männlich":
            case "MAENNLICH":
                return MAENNLICH;
            case "weiblich":
            case "WEIBLICH":
                return WEIBLICH;
            case "keine Angabe":
            case "Keine Angabe":
            case "KEINE_ANGABE":
                return KEINE_ANGABE;
            case "":
                return null;
            default:
                throw new IllegalArgumentException("Unexpected String for Geschlecht: " + str);
        }
    }
}