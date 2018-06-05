package nami.connector;

import java.util.Calendar;
import java.util.Date;


/**
 * <p>
 * Beschreibt ein Halbjahr (mit zugehörigem Jahr), das beispielsweise für die
 * Beitragsberechnung eine Rolle spielt.
 * </p>
 * 
 * <p>
 * Auf diesen Objekten ist eine Ordnung definiert (mittels
 * {@link #compareTo(NamiHalbjahr)}). Dabei bedeutet <tt>o1 &lt; o2</tt>, dass das
 * Halbjahr o1 früher als das Halbjahr o2 ist.
 * </p>
 * 
 * @author Fabian Lipp
 * 
 */
public class NamiHalbjahr implements Comparable<NamiHalbjahr> {
    private final int halbjahr;
    private final int jahr;

    /**
     * Erzeugt ein neues Halbjahr, gegeben durch Halbjahr (1/2) und Jahr.
     * 
     * @param halbjahr
     *            Halbjahr
     * @param jahr
     *            Jahr
     */
    public NamiHalbjahr(int halbjahr, int jahr) {
        this.halbjahr = halbjahr;
        this.jahr = jahr;
    }

    /**
     * Liefert das Halbjahr, in dem das übergebene Datum liegt.
     * 
     * @param date
     *            Datum
     */
    public NamiHalbjahr(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        if (month >= 0 && month <= 5) {
            halbjahr = 1;
        } else {
            halbjahr = 2;
        }
        jahr = cal.get(Calendar.YEAR);
    }

    @Override
    public int compareTo(NamiHalbjahr o) {
        if (this.jahr > o.jahr) {
            return 1;
        }
        if (this.jahr < o.jahr) {
            return -1;
        }
        return Integer.compare(this.halbjahr, o.halbjahr);
    }

    @Override
    public String toString() {
        return halbjahr + "/" + jahr;
    }
}
