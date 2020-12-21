package com.github.tobiasmiosczka.nami.util;

import nami.connector.namitypes.NamiGeschlecht;

public class GenderUtil {

    public static char getCharacter(NamiGeschlecht gender) {
        if (gender == null)
            return ' ';
        switch (gender) {
            case WEIBLICH: return  'w';
            case MAENNLICH: return  'm';
            default: return  ' ';
        }
    }

    public static String getLeiterString(NamiGeschlecht gender) {
        if (gender == null)
            return "";
        switch (gender) {
            case MAENNLICH: return "Leiter";
            case WEIBLICH: return "Leiterin";
            default: return "";
        }
    }
}
