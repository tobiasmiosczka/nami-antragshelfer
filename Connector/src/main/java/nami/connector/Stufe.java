package nami.connector;

/**
 * Stufen, die es in NaMi gibt.
 *
 * @author Tobias Miosczka
 *
 */
public enum Stufe {

    WOELFLING,
    JUNGPFADFINDER,
    PFADFINDER,
    ROVER,
    ANDERE;

    public static Stufe fromString(String str) {
        if(str == null) {
            return ANDERE;
        }
        switch (str) {
            case "Wölfling":
                return WOELFLING;
            case "Jungpfadfinder":
                return JUNGPFADFINDER;
            case "Pfadfinder":
                return PFADFINDER;
            case "Rover":
                return ROVER;
            default:
                return ANDERE;
        }
    }
}
