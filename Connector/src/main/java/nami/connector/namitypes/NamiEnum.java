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

    public NamiEnum() {
    }

    public NamiEnum(String descriptor, Integer id) {
        this.descriptor = descriptor;
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("%-2d %s", id, descriptor);
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
