package com.github.tobiasmiosczka.nami.program;

import nami.connector.namitypes.NamiMitglied;

import java.util.Comparator;

public enum Sorting{
    SORT_BY_FIRSTNAME((n1, n2) -> {
        String s1 = n1.getVorname() + n1.getNachname();
        String s2 = n2.getVorname() + n2.getNachname();
        return s1.toLowerCase().compareTo(s2.toLowerCase());
    }),
    SORT_BY_LASTNAME((n1, n2) -> {
        String s1 = n1.getNachname() + n1.getVorname();
        String s2 = n2.getNachname() + n2.getVorname();
        return s1.toLowerCase().compareTo(s2.toLowerCase());
    }),
    SORT_BY_AGE(Comparator.comparing(NamiMitglied::getGeburtsDatum)),
    SORT_BY_ID(Comparator.comparingInt(NamiMitglied::getMitgliedsnummer));

    private final Comparator<NamiMitglied> comparator;

    Sorting(Comparator<NamiMitglied> comparator) {
        this.comparator = comparator;
    }

    protected Comparator<NamiMitglied> getComparator() {
        return comparator;
    }
}