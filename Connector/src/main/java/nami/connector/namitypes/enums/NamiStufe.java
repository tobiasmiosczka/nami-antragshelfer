package nami.connector.namitypes.enums;

/**
 * Stufen, die es in NaMi gibt.
 *
 * @author Tobias Miosczka
 *
 */
public enum NamiStufe {

    WOELFLING("Wölfling"),
    JUNGPFADFINDER("Jungpfadfinder"),
    PFADFINDER("Pfadfinder"),
    ROVER("Rover"),
    ANDERE("Andere");

    private final String stufe;

    NamiStufe(String stufe) {
        this.stufe = stufe;
    }

    @Override
    public String toString() {
        return stufe;
    }

    public static NamiStufe fromString(String string) {
        if(string == null) {
            return ANDERE;
        }
        for(NamiStufe stufe : NamiStufe.values()) {
            if(stufe.toString().equals(string)) {
                return stufe;
            }
        }
        return ANDERE;
    }
}
