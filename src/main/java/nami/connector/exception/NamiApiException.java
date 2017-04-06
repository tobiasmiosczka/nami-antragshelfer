package nami.connector.exception;

import nami.connector.NamiApiResponse;

/**
 * Allgemeine Exception beim Zugriff auf die API.
 * 
 * @author Fabian Lipp
 * 
 */
public class NamiApiException extends NamiException {

    private static final long serialVersionUID = -1855332145425346423L;

    /**
     * Erzeugt die Exception mit einem beliebigen String als Meldung.
     * 
     * @param str
     *            Meldung.
     */
    public NamiApiException(String str) {
        super(str);
    }

    /**
     * Erzeugt die Exception, wobei die Antwort von NaMi als Meldung verwendet
     * wird. Es wird der gelieferte StatusCode und die zugehörige Meldung in die
     * Exception gespeichert.
     * 
     * @param resp
     *            Antwort vom NaMi-Server
     */
    public NamiApiException(NamiApiResponse<?> resp) {
        super(resp.getStatusCode() + ": " + resp.getStatusMessage());
    }

    /**
     * Erzeugt die Exception, wobei eine weitere Exception als Grund angegeben
     * wird.
     * 
     * @param cause
     *            .
     */
    public NamiApiException(Throwable cause) {
        super(cause);
    }
}
