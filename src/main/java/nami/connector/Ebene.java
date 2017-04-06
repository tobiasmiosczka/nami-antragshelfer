package nami.connector;

/**
 * Beschreibt eine Ebene der DPSG.
 * 
 * @author Fabian Lipp
 * 
 */
public enum Ebene {
    /**
     * Bundesebene.
     */
    BUND(0),

    /**
     * Diözesanebene.
     */
    DIOEZESE(2),

    /**
     * Bezirksebene.
     */
    BEZIRK(4),

    /**
     * Stammesebene.
     */
    STAMM(6);

    private int significantChars;

    Ebene(int significantChars) {
        this.significantChars = significantChars;
    }

    /**
     * Beschreibt die Anzahl der Ziffern in der Gruppierungsnummer (von links),
     * die für diese Ebene relevant sind.
     * 
     * @return Anzahl der signifikanten Ziffern
     */
    public int getSignificantChars() {
        return significantChars;
    }

    /**
     * Liefert die Ebene zu einer Gruppierung.
     * 
     * @param gruppierungId
     *            Gruppierungsnummer
     * @return Ebene, zu der die Gruppierungsnummer gehört
     */
    public static Ebene getFromGruppierungId(int gruppierungId) {
        // Fülle die GruppierungsID links mit Nullen auf 6 Stellen auf
        String gruppierungsString = Integer.toString(gruppierungId);
        while (gruppierungsString.length() < 6) {
            gruppierungsString = "0" + gruppierungsString;
        }

        return getFromGruppierungId(gruppierungsString);
    }

    /**
     * Liefert die Ebene zu einer Gruppierung.
     * 
     * @param gruppierungId
     *            Gruppierungsnummer
     * @return Ebene, zu der die Gruppierungsnummer gehört
     */
    public static Ebene getFromGruppierungId(String gruppierungId) {
        if (gruppierungId.equals("000000")) {
            return BUND;
        } else if (gruppierungId.substring(2).equals("0000")) {
            return DIOEZESE;
        } else if (gruppierungId.substring(4).equals("00")) {
            return BEZIRK;
        } else {
            return STAMM;
        }
    }

    /**
     * Setzt einen String in die entsprechende Ebene um.
     * 
     * @param str
     *            String-Repräsentation der Ebene
     * @return entsprechende Ebene; <code>null</code>, wenn der String nicht
     *         umgesetzt werden kann
     */
    public static Ebene getFromString(String str) {
        if (str == null) {
            return null;
        }

        switch (str) {
        case "stamm":
            return STAMM;
        case "bezirk":
            return BEZIRK;
        case "dioezese":
            return DIOEZESE;
        case "bund":
            return BUND;
        default:
            return null;
        }
    }
}
