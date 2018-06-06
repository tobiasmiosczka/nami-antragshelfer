package com.github.tobiasmiosczka.nami.program;

import nami.connector.namitypes.NamiMitglied;

public interface NamiDataLoaderHandler{
    void onUpdate(int current, int count, NamiMitglied e);
    void onDone(long time);
    void onException(String message, Exception e);
}