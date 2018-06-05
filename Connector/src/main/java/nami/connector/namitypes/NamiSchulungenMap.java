package nami.connector.namitypes;

import nami.connector.namitypes.enums.NamiBaustein;

import java.util.Collection;
import java.util.HashMap;

public class NamiSchulungenMap extends HashMap<NamiBaustein, NamiSchulung> {

    public NamiSchulungenMap(Collection<NamiSchulung> schulungen) {
        for(NamiSchulung schulung : schulungen) {
            if (schulung != null ) {
                NamiBaustein baustein = schulung.getBaustein();
                if (baustein != null) {
                    this.put(baustein, schulung);
                }
            }
        }
    }
}
