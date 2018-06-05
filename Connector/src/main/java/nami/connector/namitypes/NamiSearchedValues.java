package nami.connector.namitypes;

import nami.connector.namitypes.enums.MitgliedStatus;
import nami.connector.namitypes.enums.Mitgliedstyp;
import org.jdom2.Element;

/**
 * Beschreibt eine Anfrage für die Suchfunktion in NaMi.
 * 
 * @author Fabian Lipp
 * 
 */
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
}
