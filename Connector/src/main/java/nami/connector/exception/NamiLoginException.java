package nami.connector.exception;

import nami.connector.NamiApiResponse;

/**
 * Exception, die bei einem Misserfolg beim Login geworfen wird. Die
 * Fehlermeldung von NaMi wird in die Exception gespeichert.
 * 
 * @author Fabian Lipp
 * 
 */
public class NamiLoginException extends NamiApiException {
    private static final long serialVersionUID = -5171203317792006455L;

    /**
     * Erzeugt die Exception mit der Antwort von NaMi.
     * 
     * @param resp
     *            Antwort vom NaMi-Server
     */
    public NamiLoginException(NamiApiResponse<?> resp) {
        super(resp);
    }

    /**
     * Erzeugt die Exception mit einer beliebigen Fehlermeldung.
     * 
     * @param message
     *            .
     */
    public NamiLoginException(String message) {
        super(message);
    }

    /**
     * Erzeugt die Exception, wenn vor der Angabe des Grundes eine weitere
     * Exception auftritt. Das heißt beim Ermitteln des Grundes für den
     * gescheiterten Login tritt eine andere Exception auf (z. B. Fehler beim
     * XML parsen).
     * 
     * @param cause .
     */
    public NamiLoginException(Throwable cause) {
        super(cause);
    }

}
