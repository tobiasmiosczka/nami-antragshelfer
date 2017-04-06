package nami.connector.exception;

/**
 * Allgemeine Exception beim Zugriff auf den NaMi-Server.
 * 
 * @author Fabian Lipp
 * 
 */
public class NamiException extends Exception {
    private static final long serialVersionUID = -4261770474727109255L;

    /**
     * Erzeugt die Exception ohne Meldung.
     */
    public NamiException() {
        super();
    }

    /**
     * Erzeugt die Exception mit einem beliebigen String als Meldung.
     * 
     * @param str
     *            Meldung.
     */
    public NamiException(String str) {
        super(str);
    }

    /**
     * Erzeugt die Exception mit einer weiteren Exception als Grund.
     * 
     * @param cause
     *            .
     */
    public NamiException(Throwable cause) {
        super(cause);
    }

}
