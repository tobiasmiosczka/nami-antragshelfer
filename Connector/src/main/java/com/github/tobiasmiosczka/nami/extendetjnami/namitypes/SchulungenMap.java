package com.github.tobiasmiosczka.nami.extendetjnami.namitypes;

import java.util.Collection;
import java.util.HashMap;

public class SchulungenMap extends HashMap<SchulungenMap.Baustein, Schulung> {
    public enum Baustein {
        MLK, WBK, BAUSTEIN_1B, BAUSTEIN_2D, BAUSTEIN_3B, BAUSTEIN_3C;

        @Override
        public String toString() {
            switch (this) {
                case MLK:
                    return "";
                case WBK:
                    return "Woodbadge-Kurs oder Woodbadge-Kurs II";
                case BAUSTEIN_1B:
                    return "Baustein 1b";
                case BAUSTEIN_2D:
                    return "Baustein 2d";
                case BAUSTEIN_3B:
                    return "Baustein 3b";
                case BAUSTEIN_3C:
                    return "Baustein 3c";
                default:
                    return "";
            }
        }

        public static Baustein fromString(String baustein) {
            switch (baustein) {
                case "Woodbadge-Kurs oder Woodbadge-Kurs II":
                    return WBK;
                case "Baustein 1b":
                    return BAUSTEIN_1B;
                case "Baustein 2d":
                    return BAUSTEIN_2D;
                case "Baustein 3b":
                    return BAUSTEIN_3B;
                case "Baustein 3c":
                    return BAUSTEIN_3C;
                default:
                    return null;
            }
        }
    }
    public SchulungenMap(Collection<Schulung> schulungen) {
        for(Schulung s : schulungen) {
            this.put(Baustein.fromString(s.getSchulungsDaten().getBaustein()), s);
        }
    }
}
