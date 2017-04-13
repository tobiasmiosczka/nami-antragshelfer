package nami.connector.namitypes;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

import nami.connector.namitypes.enums.MitgliedStatus;
import nami.connector.namitypes.enums.Mitgliedstyp;
import nami.connector.NamiConnector;
import nami.connector.NamiResponse;
import nami.connector.NamiURIBuilder;
import nami.connector.exception.NamiApiException;

import nami.connector.json.JsonHelp;
import org.apache.http.client.methods.HttpGet;
import org.jdom2.Element;

import com.google.gson.reflect.TypeToken;

/**
 * Beschreibt eine Anfrage für die Suchfunktion in NaMi.
 * 
 * @author Fabian Lipp
 * 
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class NamiSearchedValues {
    private String vorname = "";
    private String nachname = "";
    private String alterVon = "";
    private String alterBis = "";
    private String mglWohnort = "";
    private String mitgliedsNummber = ""; // Rechtschreibfehler in NaMi
    private String mglStatusId = null;
    private String mglTypeId = null;
    private Integer tagId = null;
    private Integer bausteinIncludedId = null;
    private boolean zeitschriftenversand = false;

    private Integer untergliederungId = null;
    private Integer taetigkeitId = null;
    private boolean mitAllenTaetigkeiten = false;
    private boolean withEndedTaetigkeiten = false;
    private Integer ebeneId = null;
    private String grpNummer = "";
    private String grpName = "";
    private Integer gruppierungDioezeseId = null;
    private Integer gruppierungBezirkId = null;
    private Integer gruppierungStammId = null;
    private boolean inGrp = false;
    private boolean unterhalbGrp = false;

    private String id = "";
    private String searchName = "";

    /**
     * Maximale Anzahl der gefundenen Datensätze, wenn kein Limit vorgegeben
     * wird.
     */
    // transient bewirkt, dass die Variable nicht in die JSON-Darstellung
    // aufgenommen wird
    private static final transient int INITIAL_LIMIT = 1000;

    /**
     * Setzt die Mitgliedsnummer, nach der gesucht werden soll.
     * 
     * @param mitgliedsnummer
     *            .
     */
    public void setMitgliedsnummer(String mitgliedsnummer) {
        this.mitgliedsNummber = mitgliedsnummer;
    }

    /**
     * Setzt die Untergliederungs-ID (Stufe/Abteilung), nach der gesucht werden
     * soll.
     * 
     * @param untergliederungId
     *            .
     */
    public void setUntergliederungId(Integer untergliederungId) {
        this.untergliederungId = untergliederungId;
    }

    /**
     * Setzt die Tätigkeits-ID, nach der gesucht werden soll.
     * 
     * @param taetigkeitId
     *            .
     */
    public void setTaetigkeitId(Integer taetigkeitId) {
        this.taetigkeitId = taetigkeitId;
    }

    /**
     * Setzt den Nachnamen, nach dem gesucht werden soll.
     * 
     * @param nachname
     *            .
     */
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    /**
     * Setzt den Vornamen, nach dem gesucht werden soll.
     * 
     * @param vorname
     *            .
     */
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    /**
     * Setzt den Status, nach dem gesucht werden soll.
     * 
     * @param status
     *            .
     */
    public void setMitgliedStatus(MitgliedStatus status) {
        this.mglStatusId = status.toString();
    }

    /**
     * Setzt den Mitgliedstyp, nach dem gesucht werden soll.
     * 
     * @param mgltype
     *            .
     */
    public void setMitgliedstyp(Mitgliedstyp mgltype) {
        this.mglTypeId = mgltype.toString();
    }

    /**
     * Legt die Gruppierungsnummer fest, nach der gesucht werden soll.
     * 
     * @param gruppierungsnummer
     *            Gruppierungsnummer
     */
    public void setGruppierungsnummer(String gruppierungsnummer) {
        this.grpNummer = gruppierungsnummer;
    }

    /**
     * Legt fest, ob nach allen aktiven Tätigkeiten gesucht wird.
     * 
     * @param mitAllenTaetigkeiten Suche nach allen aktiven Tätigkeiten
     */
    public void setMitAllenTaetigkeiten(boolean mitAllenTaetigkeiten) {
        this.mitAllenTaetigkeiten = mitAllenTaetigkeiten;
    }

    /**
     * Erzeugt ein <tt>NamiSearchedValues</tt>-Objekt aus der Beschreibung in
     * einem XML-Dokument.
     * 
     * Typische Fehler in der XML-Beschreibung (z. B. fehlende Attribute,
     * doppelte Elemente, falsche Datentypen) werden nicht abgefangen. Deswegen
     * sollte das Dokument vorher validiert sein.
     * 
     * @param namiSearchEl
     *            XML-Element, das die Suche beschreibt. Das Element muss ein
     *            <tt><namiSearch></tt>-Element sein
     * @return <tt>NamiSearchedValues</tt> mit den entsprechend der
     *         XML-Beschreibung gesetzten Parametern.
     */
    public static NamiSearchedValues fromXml(Element namiSearchEl) {
        if (!namiSearchEl.getName().equals("namiSearch")) {
            throw new IllegalArgumentException("Wrong root element given.");
        }

        NamiSearchedValues res = new NamiSearchedValues();
        Element el;

        // Tätigkeit
        el = namiSearchEl.getChild("taetigkeit");
        if (el != null) {
            res.setTaetigkeitId(Integer.parseInt(el.getAttributeValue("id")));
            res.mitAllenTaetigkeiten = true;
        }

        // Checkbox für aktive Tätigkeiten
        el = namiSearchEl.getChild("aktiveTaetigkeiten");
        if (el != null) {
            res.mitAllenTaetigkeiten = true;
        }

        // Untergliederung
        el = namiSearchEl.getChild("untergliederung");
        if (el != null) {
            res.setUntergliederungId(Integer.parseInt(el.getAttributeValue("id")));
            res.mitAllenTaetigkeiten = true;
        }

        // Mitgliedstyp
        el = namiSearchEl.getChild("mitgliedstyp");
        if (el != null) {
            res.setMitgliedstyp(Mitgliedstyp.fromString(el.getAttributeValue("id")));
        }

        // MitgliedStatus
        el = namiSearchEl.getChild("mitgliedStatus");
        if (el != null) {
            res.setMitgliedStatus(MitgliedStatus.fromString(el.getAttributeValue("id")));
        }

        return res;
    }

    /**
     * Liefert ein Suchobjekt, das alle Mitglieder findet, die eine gegebene
     * Gruppierung als Stammgruppierung besitzen.
     * 
     * @param gruppierungsnummer
     *            Nummer der Gruppierung, deren Mitglieder gesucht werden
     * @return ein Suchobjekt, das die gewünschte Anfrage in NaMi ausführt
     */
    public static NamiSearchedValues withStammgruppierung(String gruppierungsnummer) {
        NamiSearchedValues search = new NamiSearchedValues();
        search.setGruppierungsnummer(gruppierungsnummer);
        return search;
    }

    /**
     * Liefert einen Teil der Mitglieder, die der Suchanfrage entsprechen.
     * 
     * @param con
     *            Verbindung zum NaMi-Server
     * @param limit
     *            maximale Anzahl an gelieferten Ergebnissen
     * @param page
     *            Seite
     * @param start
     *            Index des ersten zurückgegeben Datensatzes in der gesamten
     *            Ergebnismenge
     * @return gefundene Mitglieder //TODO: stimmt momentan nicht exakt wegen
     *         NamiRepsonse
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    // TODO: Warum NamiResponse nötig
    // -> gebe stattdessen direkt die Collection zurück oder null, wenn kein
    // success
    // TODO: wird hier überhaupt von außen zugegriffen oder reicht diese Methode
    // private?
    public NamiResponse<Collection<NamiMitgliedListElement>> getSearchResult(NamiConnector con, int limit, int page, int start) throws IOException, NamiApiException {

        NamiURIBuilder builder = con.getURIBuilder(NamiURIBuilder.URL_NAMI_SEARCH);
        builder.setParameter("limit", Integer.toString(limit));
        builder.setParameter("page", Integer.toString(page));
        builder.setParameter("start", Integer.toString(start));
        builder.setParameter("searchedValues", JsonHelp.toJson(this));
        HttpGet httpGet = new HttpGet(builder.build());

        Type type = new TypeToken<NamiResponse<Collection<NamiMitgliedListElement>>>() {
        }.getType();

        return con.executeApiRequest(httpGet, type);
    }

    // TODO: Teste was passiert, wenn es keine Treffer gibt bzw. die Suchanfrage ungültig ist
    /**
     * Liefert alle Mitglieder, die der Suchanfrage entsprechen.
     * 
     * @param con
     *            Verbindung zum NaMi-Server
     * @return gefundene Mitglieder
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    public Collection<NamiMitgliedListElement> getAllResults(NamiConnector con) throws IOException, NamiApiException {
        NamiResponse<Collection<NamiMitgliedListElement>> resp = getSearchResult(con, INITIAL_LIMIT, 1, 0);

        if (resp.getTotalEntries() > INITIAL_LIMIT) {
            resp = getSearchResult(con, resp.getTotalEntries(), 1, 0);
        }
        return resp.getData();
    }

    /**
     * Liefert die Anzahl der Mitglieder, die der Suchanfrage entsprechen.
     * 
     * @param con
     *            Verbindung zum NaMi-Server
     * @return Anzahl gefundener Mitglieder
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    public int getCount(NamiConnector con) throws IOException, NamiApiException {
        NamiResponse<Collection<NamiMitgliedListElement>> resp = getSearchResult(con, 0, 1, 0);
        return resp.getTotalEntries();
    }
}
