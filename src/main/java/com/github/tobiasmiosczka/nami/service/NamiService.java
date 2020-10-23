package com.github.tobiasmiosczka.nami.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.github.tobiasmiosczka.nami.service.dataloader.NaMiDataLoader;
import com.github.tobiasmiosczka.nami.service.dataloader.NamiDataLoaderHandler;
import com.github.tobiasmiosczka.nami.util.SortedSynchronizedList;
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

public class NamiService implements NamiDataLoaderHandler {

    private NamiConnector connector;
    private final SortedSynchronizedList<NamiMitglied> members = new SortedSynchronizedList<>(Comparator.comparingInt(NamiMitglied::getId));
    private final SortedSynchronizedList<NamiMitglied> participants = new SortedSynchronizedList<>(Comparator.comparingInt(NamiMitglied::getId));
    private final NamiServiceListener gui;

    public NamiService(NamiServiceListener gui){
        this.gui = gui;
    }

    public List<NamiSchulungenMap> loadSchulungen(List<NamiMitglied> participants) throws IOException, NamiApiException {
        //TODO: make multithreaded
        List<NamiSchulungenMap> result = new ArrayList<>();
        for (NamiMitglied participant : participants)
            result.add(connector.getSchulungen(participant));
        return result;
    }

    public void login(String user, String pass, NamiServer server) throws NamiLoginException, IOException{
        connector = new NamiConnector(server);
        connector.login(new NamiCredentials(user, pass));
    }

    public void loadData(boolean loadInaktive) throws IOException, NamiApiException {
        members.clear();
        participants.clear();
        NamiGruppierung group = gui.selectGroup(connector.getGruppierungenFromUser());
        NamiSearchedValues namiSearchedValues = new NamiSearchedValues();
        if (group != null)
            namiSearchedValues.setGruppierungsnummer(String.valueOf(group.getGruppierungsnummer()));
        if (!loadInaktive)
            namiSearchedValues.setMitgliedStatus(NamiMitgliedStatus.AKTIV);
        new NaMiDataLoader(connector).load(namiSearchedValues, this);
    }

    public synchronized void putMemberToParticipants(NamiMitglied n){
        if(members.contains(n)) {
            members.remove(n);
            participants.add(n);
        }
    }

    public synchronized void putParticipantToMember(NamiMitglied n){
        if(participants.contains(n)) {
            participants.remove(n);
            members.add(n);
        }
    }

    public synchronized List<NamiMitglied> getMember(){
        return new ArrayList<>(this.members);
    }

    public synchronized List<NamiMitglied> getParticipants(){
        return participants;
    }

    @Override
    public synchronized void onUpdate(int current, int count, NamiMitglied namiMitglied) {
        members.add(namiMitglied);
        gui.onMemberLoaded(current, count, namiMitglied);
    }

    @Override
    public synchronized void onDone(long time) {
        gui.onDone(time);
    }

    @Override
    public synchronized void onException(String message, Exception e) {
        gui.onException(message, e);
    }
}
