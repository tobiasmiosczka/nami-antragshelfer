package com.github.tobiasmiosczka.nami.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import nami.connector.NamiConnector;
import nami.connector.NamiServer;
import nami.connector.exception.NamiException;
import nami.connector.namitypes.NamiBaustein;
import nami.connector.namitypes.NamiGruppierung;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiMitgliedStatus;
import nami.connector.namitypes.NamiSchulung;
import nami.connector.namitypes.NamiSearchedValues;

public class NamiService {

    public interface Listener {
        void onMemberLoaded(int current, int count, NamiMitglied member);
        void onDone(long timeMS);
        void onException(String message, Throwable e);
        void onMemberListUpdated();
        void onParticipantsListUpdated();
        NamiGruppierung selectGroup(Collection<NamiGruppierung> groups);
    }

    private NamiConnector connector;
    private final Listener gui;
    private final List<NamiMitglied> members = Collections.synchronizedList(new ArrayList<>());
    private final List<NamiMitglied> participants = Collections.synchronizedList(new ArrayList<>());
    private boolean isLoggedIn;

    public NamiService(Listener gui){
        this.gui = gui;
    }

    public List<Map<NamiBaustein, NamiSchulung>> loadSchulungen(List<NamiMitglied> member) throws InterruptedException, ExecutionException {
        //TODO: make multithreaded
        List<Map<NamiBaustein, NamiSchulung>> result = new ArrayList<>();
        for (NamiMitglied participant : member)
            result.add(connector.getLatestTrainingsByUser(participant.getId()).get());
        return result;
    }

    public void login(String username, String password) throws NamiException, IOException, InterruptedException {
        connector = new NamiConnector(NamiServer.getLiveserver());
        connector.login(username, password);
        isLoggedIn = true;
    }

    public void loadData(boolean loadInactive) throws IOException, NamiException, InterruptedException, ExecutionException {
        members.clear();
        participants.clear();
        NamiGruppierung group = gui.selectGroup(connector.getAccessibleGroups().get());
        NamiSearchedValues namiSearchedValues = new NamiSearchedValues();
        if (group != null)
            namiSearchedValues.setGruppierungsnummer(String.valueOf(group.getGroupId()));
        if (!loadInactive)
            namiSearchedValues.setMitgliedStatus(NamiMitgliedStatus.AKTIV);
        NamiDataLoader.load(connector, namiSearchedValues, new NamiDataLoader.Listener() {

            @Override
            public void onUpdate(int current, int count, NamiMitglied newMember) {
                members.add(newMember);
                gui.onMemberLoaded(current, count, newMember);
                gui.onMemberListUpdated();
            }

            @Override
            public void onDone(long time) {
                gui.onDone(time);
            }

            @Override
            public void onException(String message, Throwable e) {
                gui.onException(message, e);
            }
        });
    }

    public synchronized List<NamiMitglied> getMember(){
        return new ArrayList<>(this.members);
    }

    public synchronized List<NamiMitglied> getParticipants(){
        return participants;
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public synchronized void putMembersToParticipants(Collection<NamiMitglied> newMembers) {
        Collection<NamiMitglied> filtered = newMembers.stream()
                .filter(this.members::contains)
                .toList();
        this.members.removeAll(filtered);
        this.participants.addAll(filtered);
        gui.onMemberListUpdated();
        gui.onParticipantsListUpdated();
    }

    public synchronized void putParticipantsToMembers(Collection<NamiMitglied> newParticipants) {
        Collection<NamiMitglied> filtered = newParticipants.stream()
                .filter(this.participants::contains)
                .toList();
        this.participants.removeAll(filtered);
        this.members.addAll(filtered);
        gui.onMemberListUpdated();
        gui.onParticipantsListUpdated();
    }
}
