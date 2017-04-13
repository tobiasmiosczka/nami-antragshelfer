package nami.connector.namitypes.enums;

/**
 * Beschreibt das Geschlecht eines Mitglieds.
 * 
 * @author Fabian Lipp
 * @author Tobias Miosczka
 * 
 */
public enum Geschlecht {
    //Männlich.
    MAENNLICH("männlich", 'm'),

    //Weiblich.
    WEIBLICH("weiblich", 'w');

    private final String tag;
    private final char character;

    Geschlecht(String tag, char character) {
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
    public static Geschlecht fromString(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Unexpected String for Geschlecht: " + str);
        }
        for (Geschlecht geschlecht : Geschlecht.values()) {
            if (geschlecht.getTag().equals(str)) {
                return geschlecht;
            }
        }
        throw new IllegalArgumentException("Unexpected String for Geschlecht: " + str);
    }
}
