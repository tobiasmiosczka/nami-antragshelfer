package nami.connector.namitypes;

/**
 * Beschreibt einen Eintrag aus einem Enum in NaMi. Zum Beispiel eine Tätigkeit
 * oder eine Untergliederung.
 * 
 * @author Fabian Lipp
 * 
 */
public class NamiEnum {
    private String descriptor;
    private Integer id;

    @Override
    public String toString() {
        return String.format("%-2d %s", id, descriptor);
    }
}
