package nami.connector;

/**
 * Beschreibt das Geschlecht eines Mitglieds.
 * 
 * @author Fabian Lipp
 * 
 */
public enum Geschlecht {
    /**
     * Männlich.
     */
    MAENNLICH("männlich", 'm'),

    /**
     * Weiblich.
     */
    WEIBLICH("weiblich", 'w');

    private final String string;
    private final char character;

    Geschlecht(String string, char character) {
        this.string = string;
        this.character = character;
    }

    public String getString() {
        return string;
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
    public static Geschlecht fromString(String str) {
        switch (str) {
        case "männlich":
        case "MAENNLICH":
            return MAENNLICH;
        case "weiblich":
        case "WEIBLICH":
            return WEIBLICH;
        case "":
            return null;
        default:
            throw new IllegalArgumentException(
                    "Unexpected String for Geschlecht: " + str);
        }
    }
}
