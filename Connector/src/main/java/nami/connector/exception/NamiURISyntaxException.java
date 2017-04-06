package nami.connector.exception;

import java.net.URISyntaxException;

/**
 * Tritt auf, wenn Probleme beim Erstellen/Einlesen einer URI auftreten. Wird
 * als Ersatz zu {@link URISyntaxException} verwendet. Im Gegensatz zur anderen
 * Exception ist diese hier eine Runtime-Exception, muss also nicht abgefangen
 * werden. Wenn ein Fehler beim Zusammenbauen der URI auftritt, dürfte das an
 * einem Fehler im Programm liegen, nicht an einer falschen Eingabe des
 * Anwenders.
 * 
 * @author Fabian Lipp
 * 
 */
public class NamiURISyntaxException extends RuntimeException {

    private static final long serialVersionUID = -4918799375724605164L;

    /**
     * Erzeugt die Exception, wobei alle Eigenschaften der
     * <code>URISyntaxException</code> übernommen werden.
     * 
     * @param cause
     *            aufgetretene URISyntaxException
     */
    public NamiURISyntaxException(URISyntaxException cause) {
        super(cause);
    }

}
