package nami.connector.credentials;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import nami.connector.exception.CredentialsInitiationException;

/**
 * Stellt die Zugangsdaten für NaMi bereit.
 * 
 * @author Fabian Lipp
 * 
 */
public class NamiCredentials {
    private final String apiUser;
    private final String apiPass;

    /**
     * Erzeugt ein neues Credentials-Objekt, wobei die Zugangsdaten für NaMi
     * fest vorgegeben werden.
     * 
     * @param apiUser
     *            Benutzername (Mitgliedsnummer) für NaMi
     * @param apiPass
     *            Passwort
     */
    public NamiCredentials(String apiUser, String apiPass) {
        this.apiUser = apiUser;
        this.apiPass = apiPass;
    }

    /**
     * Liefert den Benutzernamen.
     * 
     * @return Benutzername
     */
    public String getApiUser() {
        return apiUser;
    }

    /**
     * Liefert das Passwort.
     * 
     * @return Passwort
     */
    public String getApiPass() {
        return apiPass;
    }

    /**
     * Erzeugt ein NamiCredentials-Objekt ausgehend vom Klassennamen (z. B. aus
     * Konfigurationsdatei gelesen). Diese Klasse muss im Package
     * <tt>nami.connector.credentials</tt> liegen.
     * 
     * Wenn ein Benutzername als Parameter übergeben wird, wird der Konstruktor
     * mit genau einem String-Parameter aufgerufen. Wenn kein Benutzername
     * übergeben wird, wird der Konstruktor ohne Parameter aufgerufen. Wenn der
     * jeweilige Konstruktor nicht existiert, wird eine Exception geworfen.
     * 
     * @param className
     *            Name der zu verwendenden Klasse
     * @param username
     *            Benutzername, der an den Konstruktor übergeben werden soll.
     *            Falls dieser Parameter <tt>null</tt> ist, wird der Konstruktor
     *            ohne Parameter aufgerufen.
     * @throws CredentialsInitiationException
     *             Fehler beim Erzeugen des Credentials-Objekts
     * @return Erzeugtes <tt>NamiCredentials</tt>-Objekt
     */
    public static NamiCredentials getCredentialsFromClassname(String className, String username) throws CredentialsInitiationException {
        String fullClassname = "nami.connector.credentials." + className;
        try {
            Class<? extends NamiCredentials> credClass;
            credClass = Class.forName(fullClassname).asSubclass(NamiCredentials.class);
            Constructor<? extends NamiCredentials> constr;

            // find suitable constructor
            if (username != null) {
                // Konstruktor mit einem String-Parameter
                constr = credClass.getConstructor(String.class);
                return constr.newInstance(username);
            } else {
                // Konstruktor ohne Parameter
                constr = credClass.getConstructor();
                return constr.newInstance();
            }
        } catch (ClassNotFoundException e) {
            throw new CredentialsInitiationException("Could not find requested credentials class: " + fullClassname, e);
        } catch (NoSuchMethodException e) {
            throw new CredentialsInitiationException("Credentials class doesn't have supported constructor", e);
        } catch (InvocationTargetException e) {
            throw new CredentialsInitiationException("Exception in constructor", e);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CredentialsInitiationException(e);
        }

    }

    /**
     * Liest die Credential-Konfiguration aus einer Properties-Datei. Dabei
     * werden die folgenden Schlüssel genutzt:
     * <ul>
     * <li><tt>nami.credentialsType</tt>: Klasse, die geladen werden soll (ohne
     * Angabe des Packages, dafür wird nami.connector.credentials verwendet)</li>
     * <li><tt>nami.username</tt>: Username, der ggf. an die Klasse übergeben
     * werden soll
     * </ul>
     * 
     * @param p
     *            Properties-Objekt, aus dem die Konfiguration gelesen werden
     *            soll
     * @throws CredentialsInitiationException
     *             Fehler beim Erzeugen des Credentials-Objekts
     * @return Erzeugtes <tt>NamiCredentials</tt>-Objekt
     */
    public static NamiCredentials getCredentialsFromProperties(Properties p) throws CredentialsInitiationException {
        String className = p.getProperty("nami.credentialsType");
        String username = p.getProperty("nami.username");
        return getCredentialsFromClassname(className, username);
    }
}
