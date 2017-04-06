package nami.connector.exception;

/**
 * Fehler beim Instantiieren des <tt>NamiCredentials</tt>-Objekts.
 * 
 * @author Fabian Lipp
 * 
 */
public class CredentialsInitiationException extends Exception {
    private static final long serialVersionUID = -4031437531154395032L;

    /**
     * Erzeugt die Exception, wobei eine weitere Exception als Grund angegeben
     * wird.
     * 
     * @param message
     *            Meldung
     * @param cause
     *            verursachende Exception
     */
    public CredentialsInitiationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Erzeugt die Exception, wobei eine weitere Exception als Grund angegeben
     * wird.
     * 
     * @param cause
     *            verursachende Exception
     */
    public CredentialsInitiationException(Throwable cause) {
        super(cause);
    }

    /**
     * Erzeugt die Exception mit einem beliebigen String als Meldung.
     * 
     * @param message
     *            Meldung
     */
    public CredentialsInitiationException(String message) {
        super(message);
    }
}
