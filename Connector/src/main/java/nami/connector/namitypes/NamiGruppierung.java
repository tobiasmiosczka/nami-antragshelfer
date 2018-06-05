package nami.connector.namitypes;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nami.connector.namitypes.enums.Ebene;

/**
 * Beschreibt eine Gruppierung der DPSG.
 * 
 * @author Fabian Lipp
 * 
 */
public class NamiGruppierung {
    // Die folgenden Variablen stammen aus NaMi. Keinesfalls umbenennen.
    private String descriptor;
    private int id;

    private Collection<NamiGruppierung> children;

    private static final Pattern GRPNUM_PATTERN = Pattern.compile("[\\d]+");

    /**
     * Liefert den Namen der Gruppierung ("Stamm XYZ") inkl. Gruppierungsnummer.
     * 
     * @return Name
     */
    public String getDescriptor() {
        return descriptor;
    }

    /**
     * Liefert die ID der Gruppierung (das ist nicht zwangsweise die
     * Gruppierungsnummer).
     * 
     * @return ID der Gruppierung
     */
    public int getId() {
        return id;
    }

    /**
     * Liefert die Gruppierungsnummer.
     * 
     * @return Gruppierungsnummer
     */
    public String getGruppierungsnummer() {
        // Die Gruppierungsnummer muss aus der Beschreibung ausgelesen werden,
        // da sie nicht zwangsweise mit der ID übereinstimmt. Bei den meisten
        // Gruppierungen stimmen sie überein, aber eben nicht bei allen. Das ist
        // halt eine Merkwürdigkeit in NaMi, für die wir hier einen Workaround
        // brauchen.
        Matcher match = GRPNUM_PATTERN.matcher(descriptor);
        if (!match.find()) {
            throw new IllegalArgumentException("Could not find Gruppierungsnummer in descriptior: " + descriptor);
        }
        return match.group();
    }

    /**
     * Liefert die untergeordneten Gruppierungen.
     * 
     * @return Kind-Gruppierungen
     */
    public Collection<NamiGruppierung> getChildren() {
        return children;
    }

    public void setChildren(Collection<NamiGruppierung> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return descriptor;
    }

    /**
     * Liefert die Gruppierungsnummer einer übergeordneten Gruppierung auf einer
     * vorgegebenen Ebene.
     * 
     * @param targetE
     *            gewünschte Ebene
     * @return Gruppierungsnummer der übergeordneten Ebene; <tt>null</tt>, falls
     *         eine niedrigere Ebene verlangt wird
     */
    public String getParentId(Ebene targetE) {
        // Gruppierungsnummer dieser Gruppierung
        String grpNum = getGruppierungsnummer();
        Ebene thisE = Ebene.getFromGruppierungId(grpNum);
        if (thisE.compareTo(targetE) < 0) {
            // Es wird eine niedrigere Ebene verlangt
            return null;
        } else if (thisE.compareTo(targetE) == 0) {
            // Es wird die gleiche Ebene verlangt
            return grpNum;
        } else {
            // Es wird eine höhere Ebene verlangt
            StringBuilder result = new StringBuilder(grpNum.substring(0, targetE.getSignificantChars()));
            // Fülle die GruppierungsID rechts mit Nullen auf 6 Stellen auf
            while (result.length() < 6) {
                result.append("0");
            }
            return result.toString();
        }
    }

    /**
     * Liefert die Ebene der Gruppierung.
     * 
     * @return Ebene der Gruppierung
     */
    public Ebene getEbene() {
        return Ebene.getFromGruppierungId(id);
    }

    /**
     * Sucht im Gruppierungsbaum (ausgehend von dieser Gruppierung) nach einer
     * Gruppierung mit einer vorgegebenen Nummer.
     * 
     * @param gruppierungsnummer
     *            gesuchte Gruppierungssnummer
     * @return gefundene Gruppierung; <tt>null</tt> wenn die Gruppierungsnummer
     *         nicht gefunden wird
     */
    public NamiGruppierung findGruppierung(int gruppierungsnummer) {
        if (id == gruppierungsnummer) {
            return this;
        } else {
            for (NamiGruppierung grp : children) {
                NamiGruppierung res = grp.findGruppierung(gruppierungsnummer);
                if (res != null) {
                    return res;
                }
            }
            return null;
        }
    }
}
