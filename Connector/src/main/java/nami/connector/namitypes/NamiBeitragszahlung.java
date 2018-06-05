package nami.connector.namitypes;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nami.connector.NamiHalbjahr;

/**
 * Beschreibt eine Beitragszahlung eines Mitglieds, wie sie in NaMi erfasst ist,
 * d.h. die Berechnung des Mitgliedsbeitrages für ein bestimmtes Mitglied für
 * einen bestimmten Zeitraum.
 * 
 * @author Fabian Lipp
 * 
 */
public class NamiBeitragszahlung {

    private String id;
    private String rechnungsNummer;
    private String rechnungsPosition;
    private String buchungsText;
    private String beitragsSatz;
    private String value;
    private String beitragBis;
    private String status;
    private String beitragsKonto;

    /**
     * Liefert die ID der Buchung in NaMi.
     * 
     * @return ID
     */
    public int getId() {
        return Integer.parseInt(id);
    }

    /**
     * Liefert die Rechnungsnummer von Bundesebene, in der die Buchung enthalten
     * ist.
     * 
     * @return Rechnungsnummer
     */
    public String getRechungsnummer() {
        return rechnungsNummer;
    }

    /**
     * Liefert den Buchungsbetrag.
     * 
     * @return Buchungsbetrag
     */
    public BigDecimal getValue() {
        return new BigDecimal(value);
    }

    /**
     * Liefert den Buchungstext.
     * 
     * @return Buchungstext
     */
    public String getBuchungstext() {
        return buchungsText;
    }

    /**
     * Setzt das Gültigkeitsdatum (<tt>beitragBis</tt>) in ein <tt>Calendar</tt>
     * -Objekt um.
     * 
     * @return beitragBis
     */
    private Calendar beitragBisCal() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(df.parse(beitragBis));
        } catch (ParseException e) {
            cal.setTime(new Date(0));
        }
        return cal;
    }

    /**
     * Liefert das Halbjahr, für das die Beitragszahlung gilt.
     * 
     * @return Halbjahr
     */
    public int getHalbjahr() {
        Calendar cal = beitragBisCal();
        switch (cal.get(Calendar.MONTH)) {
        case Calendar.JUNE:
            return 1;
        case Calendar.DECEMBER:
            return 2;
        default:
            throw new IllegalArgumentException(
                    "Unexpected month in end date for Beitragszahlung: "
                            + beitragBis);
        }
    }

    /**
     * Liefert das Halbjahr, für den der Beitrag gilt.
     * 
     * @return Halbjahr
     */
    public NamiHalbjahr getZeitraum() {
        return new NamiHalbjahr(getHalbjahr(), getJahr());
    }

    /**
     * Liefert das Jahr, für das die Beitragszahlung gilt.
     * 
     * @return Jahr
     */
    public int getJahr() {
        return beitragBisCal().get(Calendar.YEAR);
    }
}
