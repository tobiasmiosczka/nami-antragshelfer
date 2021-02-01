package com.github.tobiasmiosczka.nami.util;

import nami.connector.namitypes.NamiGeschlecht;

public class GenderUtil {

    public static char getCharacter(NamiGeschlecht gender) {
        if (gender == null)
            return ' ';
        return switch (gender) {
            case WEIBLICH -> 'w';
            case MAENNLICH -> 'm';
            default -> ' ';
        };
    }

    public static String getLeiterString(NamiGeschlecht gender) {
        if (gender == null)
            return "";
        return switch (gender) {
            case MAENNLICH -> "Leiter";
            case WEIBLICH -> "Leiterin";
            default -> "";
        };
    }
}
