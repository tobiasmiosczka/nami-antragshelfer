package com.github.tobiasmiosczka.nami.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.tobiasmiosczka.nami.service.dataloader.NaMiDataLoader;
import com.github.tobiasmiosczka.nami.service.dataloader.NamiDataLoaderHandler;
import nami.connector.namitypes.NamiGruppierung;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiSchulungenMap;
import nami.connector.NamiConnector;
import nami.connector.NamiServer;
import nami.connector.credentials.NamiCredentials;
import nami.connector.exception.NamiApiException;
import nami.connector.exception.NamiLoginException;
import nami.connector.namitypes.NamiSearchedValues;
import nami.connector.namitypes.enums.NamiMitgliedStatus;

public class NamiService {

    private NamiConnector connector;
    private final NamiServiceListener gui;
    private final List<NamiMitglied> members = Collections.synchronizedList(new ArrayList<>());
    private final List<NamiMitglied> participants = Collections.synchronizedList(new ArrayList<>());
    private boolean isLoggedIn;

    public NamiService(NamiServiceListener gui){
        this.gui = gui;
    }

    public List<NamiSchulungenMap> loadSchulungen(List<NamiMitglied> member) throws IOException, NamiApiException {
        //TODO: make multithreaded
        List<NamiSchulungenMap> result = new ArrayList<>();
        for (NamiMitglied participant : member)
            result.add(connector.getSchulungen(participant));
        return result;
    }

    public void login(String username, String password) throws NamiLoginException, IOException{
        connector = new NamiConnector(NamiServer.getLiveserver());
        connector.login(new NamiCredentials(username, password));
        isLoggedIn = true;
    }

    public void loadData(boolean loadInactive) throws IOException, NamiApiException {
        members.clear();
        participants.clear();
        NamiGruppierung group = gui.selectGroup(connector.getGruppierungenFromUser());
        NamiSearchedValues namiSearchedValues = new NamiSearchedValues();
        if (group != null)
            namiSearchedValues.setGruppierungsnummer(String.valueOf(group.getGruppierungsnummer()));
        if (!loadInactive)
            namiSearchedValues.setMitgliedStatus(NamiMitgliedStatus.AKTIV);
        NaMiDataLoader.load(connector, namiSearchedValues, new NamiDataLoaderHandler() {
            @Override
            public void onUpdate(int current, int count, NamiMitglied namiMitglied) {
                members.add(namiMitglied);
                gui.onMemberLoaded(current, count, namiMitglied);
                gui.onMemberListUpdated();
            }

            @Override
            public void onDone(long time) {
                gui.onDone(time);
            }

            @Override
            public void onException(String message, Exception e) {
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
                .collect(Collectors.toList());
        this.members.removeAll(filtered);
        this.participants.addAll(filtered);
        gui.onMemberListUpdated();
        gui.onParticipantsListUpdated();
    }

    public synchronized void putParticipantsToMembers(Collection<NamiMitglied> newParticipants) {
        Collection<NamiMitglied> filtered = newParticipants.stream()
                .filter(this.participants::contains)
                .collect(Collectors.toList());
        this.participants.removeAll(filtered);
        this.members.addAll(filtered);
        gui.onMemberListUpdated();
        gui.onParticipantsListUpdated();
    }
}
