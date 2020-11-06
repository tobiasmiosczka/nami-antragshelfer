package com.github.tobiasmiosczka.nami.util;

import nami.connector.namitypes.enums.NamiGeschlecht;

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

}
