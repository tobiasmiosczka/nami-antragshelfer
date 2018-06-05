package nami.connector.namitypes;

import nami.connector.namitypes.enums.Baustein;

import java.util.Collection;
import java.util.HashMap;

public class SchulungenMap extends HashMap<Baustein, Schulung> {

    public SchulungenMap(Collection<Schulung> schulungen) {
        for(Schulung schulung : schulungen) {
            if (schulung != null ) {
                Baustein baustein = schulung.getBaustein();
                if (baustein != null) {
                    this.put(baustein, schulung);
                }
            }
        }
    }
}
