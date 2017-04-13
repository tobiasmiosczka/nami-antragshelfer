package nami.connector.namitypes.enums;

/**
 * Stufen, die es in NaMi gibt.
 *
 * @author Tobias Miosczka
 *
 */
public enum Stufe {

    WOELFLING("Wölfling"),
    JUNGPFADFINDER("Jungpfadfinder"),
    PFADFINDER("Pfadfinder"),
    ROVER("Rover"),
    ANDERE("Andere");

    private final String stufe;

    Stufe(String stufe) {
        this.stufe = stufe;
    }

    @Override
    public String toString() {
        return stufe;
    }

    public static Stufe fromString(String string) {
        if(string == null) {
            return ANDERE;
        }
        for(Stufe stufe : Stufe.values()) {
            if(stufe.toString().equals(string)) {
                return stufe;
            }
        }
        return ANDERE;
    }
}
