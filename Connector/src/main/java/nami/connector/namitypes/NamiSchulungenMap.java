package nami.connector.namitypes;

import nami.connector.namitypes.enums.Baustein;

import java.util.Collection;
import java.util.HashMap;

public class NamiSchulungenMap extends HashMap<Baustein, NamiSchulung> {

    public NamiSchulungenMap(Collection<NamiSchulung> schulungen) {
        for(NamiSchulung schulung : schulungen) {
            if (schulung != null ) {
                Baustein baustein = schulung.getBaustein();
                if (baustein != null) {
                    this.put(baustein, schulung);
                }
            }
        }
    }
}
