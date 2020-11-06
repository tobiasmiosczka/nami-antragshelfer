package com.github.tobiasmiosczka.nami.service;

import nami.connector.namitypes.NamiGruppierung;
import nami.connector.namitypes.NamiMitglied;

import java.util.Collection;

public interface NamiServiceListener {
    void onMemberLoaded(int current, int count, NamiMitglied member);
    void onDone(long timeMS);
    void onException(String message, Exception e);
    void onMemberListUpdated();
    void onParticipantsListUpdated();
    NamiGruppierung selectGroup(Collection<NamiGruppierung> groups);
}