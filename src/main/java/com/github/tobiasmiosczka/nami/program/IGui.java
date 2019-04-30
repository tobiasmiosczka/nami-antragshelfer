package com.github.tobiasmiosczka.nami.program;

import nami.connector.namitypes.NamiGruppierung;
import nami.connector.namitypes.NamiMitglied;

import java.util.Collection;

public interface IGui {
    void onUpdate(int current, int count, NamiMitglied member);
    void onDone(long timeMS);
    void onException(String message, Exception e);
    NamiGruppierung selectGruppierung(Collection<NamiGruppierung> gruppierungen);
}