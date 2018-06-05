package nami.connector.namitypes;

/**
 * Stellt eine Tätigkeitszuordnung für ein Mitglied dar.
 * 
 * @author Fabian Lipp
 * 
 */
public class NamiTaetigkeitAssignment {
    // Die folgenden Variablen stammen aus NaMi. Keinesfalls umbenennen.
    private int id;
    private String gruppierung;
    private int gruppierungId;
    private String taetigkeit;
    private int taetigkeitId;
    private String caeaGroup;
    private int caeaGroupId;
    private String caeaGroupForGf;
    private int caeaGroupForGfId;
    private String untergliederung;
    private int untergliederungId;
    private String aktivVon;
    private String aktivBis;

    /**
     * Liefert die Gruppierungsnummer, in der die Tätigkeit ausgeübt wird.
     * 
     * @return Gruppierungsnummer
     */
    public int getGruppierungId() {
        return gruppierungId;
    }

    /**
     * Liefert die Tätigkeits-ID.
     * 
     * @return Tätigkeit-ID
     */
    public int getTaetigkeitId() {
        return taetigkeitId;
    }

    /**
     * Liefert die ID der Untergliederung (Stufe/Abteilung).
     * 
     * @return Untergliederung-ID
     */
    public int getUntergliederungId() {
        return untergliederungId;
    }

    /**
     * Gibt an, ob die Tätigkeit aktuell noch aktiv ist.
     * 
     * @return <code>true</code>, falls die Tätigkeit aktiv ist
     */
    public boolean isAktiv() {
        return aktivBis.isEmpty();
    }
}
