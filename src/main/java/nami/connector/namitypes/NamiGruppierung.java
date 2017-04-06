package nami.connector.namitypes;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nami.connector.Ebene;
import nami.connector.NamiConnector;
import nami.connector.NamiResponse;
import nami.connector.NamiURIBuilder;
import nami.connector.exception.NamiApiException;

import org.apache.http.client.methods.HttpGet;

import com.google.gson.reflect.TypeToken;

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

    // die Kindknoten werden nicht automatisch von NaMi geliefert, sondern
    // müssen extra abgefragt werden.
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
            throw new IllegalArgumentException(
                    "Could not find Gruppierungsnummer in descriptior: "
                            + descriptor);
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
            String result = grpNum.substring(0, targetE.getSignificantChars());

            // Fülle die GruppierungsID rechts mit Nullen auf 6 Stellen auf
            while (result.length() < 6) {
                result = result + "0";
            }
            return result;
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
     * Liest den kompletten Gruppierungsbaum aus, auf den der Benutzer Zugriff
     * hat.
     * 
     * @param con
     *            Verbindung zum NaMi-Server
     * @return Root-Gruppierung (in dieser sind die Kinder gespeichert)
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    public static NamiGruppierung getGruppierungen(NamiConnector con)
            throws IOException, NamiApiException {
        NamiGruppierung rootGrp = getRootGruppierungWithoutChildren(con);

        rootGrp.children = getChildGruppierungen(con,
                Integer.toString(rootGrp.id));

        return rootGrp;
    }

    /**
     * Liest den Gruppierungsbaum ausgehend von einer vorgegebenen Wurzel aus.
     * 
     * @param con
     *            Verbindung zum NaMi-Server
     * @param gruppierungsnummer
     *            Gruppierungsnummer der Gruppierung, die die Wurzel des Baumes
     *            bilden soll
     * @return vorgegebene Wurzel-Gruppierung (in dieser sind die Kinder
     *         gespeichert)
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    public static NamiGruppierung getGruppierungen(NamiConnector con,
            String gruppierungsnummer) throws IOException, NamiApiException {
        NamiGruppierung rootGrp = getGruppierungen(con);

        // nicht sehr effizient, da trotzdem der gesamte Baum aus NaMi geladen
        // wird
        // auf Diözesanebene sollte das aber kein Problem sein, da die Anzahl
        // der Bezirke doch sehr begrenzt ist
        NamiGruppierung found = rootGrp
                .findGruppierungInTree(gruppierungsnummer);
        if (found == null) {
            throw new NamiApiException("Gruppierung not found: "
                    + gruppierungsnummer);
        } else {
            return found;
        }
    }

    /**
     * Liest die Root-Gruppierung aus NaMi aus. Es wird nicht der gesamte
     * Gruppierungsbaum gelesen, d.h. in der Root-Gruppierung wird anstelle der
     * Liste der Kinder nur <code>null</code> gespeichert.
     * 
     * @param con
     *            Verbindung zum NaMi-Server
     * @return Root-Gruppierung ohne Kinder
     * @throws IOException
     *             IOException
     * @throws NamiApiException
     *             API-Fehler beim Zugriff auf NaMi
     */
    private static NamiGruppierung getRootGruppierungWithoutChildren(
            NamiConnector con) throws IOException, NamiApiException {
        NamiURIBuilder builder = con.getURIBuilder(NamiURIBuilder.URL_NAMI_GRP);
        builder.appendPath("root");
        builder.addParameter("node", "root");
        HttpGet httpGet = new HttpGet(builder.build());

        Type type = new TypeToken<NamiResponse<Collection<NamiGruppierung>>>() {
        }.getType();
        NamiResponse<Collection<NamiGruppierung>> resp = con.executeApiRequest(
                httpGet, type);

        if (!resp.isSuccess()) {
            throw new NamiApiException("Could not get root Gruppierung");
        }
        NamiGruppierung rootGrp = resp.getData().iterator().next();

        rootGrp.children = null;

        return rootGrp;
    }

    private static Collection<NamiGruppierung> getChildGruppierungen(
            NamiConnector con, String rootGruppierung) throws IOException,
            NamiApiException {
        NamiURIBuilder builder = con.getURIBuilder(NamiURIBuilder.URL_NAMI_GRP);
        builder.appendPath(rootGruppierung);
        builder.addParameter("node", rootGruppierung);
        HttpGet httpGet = new HttpGet(builder.build());

        Type type = new TypeToken<NamiResponse<Collection<NamiGruppierung>>>() {
        }.getType();
        NamiResponse<Collection<NamiGruppierung>> resp = con.executeApiRequest(
                httpGet, type);

        Collection<NamiGruppierung> allChildren = resp.getData();
        Collection<NamiGruppierung> activeChildren = new LinkedList<>();
        for (NamiGruppierung child : allChildren) {
            activeChildren.add(child);
            // Kinder brauchen nur abgefragt werden, wenn es sich nicht um
            // einen Stamm handelt (denn Stämme haben keine Kinder)
            if (child.getEbene() == Ebene.STAMM) {
                child.children = new LinkedList<>();
            } else {
                child.children = getChildGruppierungen(con,
                        Integer.toString(child.id));
            }
        }

        return activeChildren;
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
    private NamiGruppierung findGruppierungInTree(String gruppierungsnummer) {
        if (Integer.toString(id).equals(gruppierungsnummer)) {
            return this;
        } else {
            for (NamiGruppierung grp : children) {
                NamiGruppierung res = grp
                        .findGruppierungInTree(gruppierungsnummer);
                if (res != null) {
                    return res;
                }
            }
            // in keiner der Kinder wurde die gesuchte Nummer gefunden (sonst
            // wäre diese bereits oben zurückgegeben worden
            return null;
        }
    }
}
