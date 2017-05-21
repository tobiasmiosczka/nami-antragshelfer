package com.github.tobiasmiosczka.nami.program;

import java.io.IOException;
import java.util.*;

import com.github.tobiasmiosczka.nami.extendetjnami.ExtendedJNaMi;
import com.github.tobiasmiosczka.nami.extendetjnami.namitypes.Gruppierung;
import com.github.tobiasmiosczka.nami.extendetjnami.namitypes.SchulungenMap;
import nami.connector.NamiConnector;
import nami.connector.NamiServer;
import nami.connector.credentials.NamiCredentials;
import nami.connector.exception.NamiApiException;
import nami.connector.exception.NamiLoginException;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiSearchedValues;

/**
 * Program
 *
 * @author Tobias  Miosczka
 *
 */
public class Program implements NaMiDataLoader.NamiDataLoaderHandler {

    public interface ProgramHandler {
        void onUpdate(int current, int count, NamiMitglied member);
        void onDone(long timeMS);
        void onException(Exception e);
        Gruppierung selectGruppierung(Collection<Gruppierung> gruppierungen);
    }

    public List<SchulungenMap> loadSchulungen(List<NamiMitglied> participants) throws IOException, NamiApiException {
        //TODO: make multithreaded
        List<SchulungenMap> result = new ArrayList<>();
        for (NamiMitglied participant : participants) {
            SchulungenMap s = ExtendedJNaMi.getSchulungen(connector, participant.getId());
            result.add(s);
        }
        return result;
    }

    public enum Sortation{
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

        Sortation(Comparator<NamiMitglied> comparator) {
            this.comparator = comparator;
        }

        protected Comparator<NamiMitglied> getComparator() {
            return comparator;
        }
    }

    public void sortBy(Sortation sortation) {
        Comparator<NamiMitglied> s = sortation.getComparator();
        participants.setComparator(s);
        members.setComparator(s);
    }

    private NamiConnector 	connector;
    private final SortedList<NamiMitglied> members = new SortedList<>(Sortation.SORT_BY_LASTNAME.getComparator());
    private final SortedList<NamiMitglied> participants = new SortedList<>(Sortation.SORT_BY_LASTNAME.getComparator());
    private final ProgramHandler handler;

    /**
     * Constructor for Program
     */
    public Program(ProgramHandler handler){
        this.handler = handler;
    }

    /**
     * procedure for logging into the nami database
     *
     * @param user
     * 				valid username (membership number)
     * @param pass
     * 				valid password
     */
    public void login(String user, String pass, NamiServer server) throws NamiLoginException, IOException{
        NamiCredentials credentials = new NamiCredentials(user, pass);
        connector = new NamiConnector(server, credentials);
        connector.namiLogin();
    }

    /**
     * loads data from the nami database and displays it in the main frame
     * program must be logged in first
     *
     */
    public void loadData() throws IOException, NamiApiException {
        members.clear();
        participants.clear();
        Collection<Gruppierung> groups = ExtendedJNaMi.getGruppierungen(connector);
        Gruppierung group = handler.selectGruppierung(groups);
        NamiSearchedValues namiSearchedValues = new NamiSearchedValues();
        if (group != null) {
            namiSearchedValues.setGruppierungsnummer(String.valueOf(group.getId()));
        }
        NaMiDataLoader dl = new NaMiDataLoader(connector, namiSearchedValues, this);
        dl.start();
    }

    /**
     * Puts the the given element from the members list to the participants list
     *
     * @param n
     * 				object in member list
     */
    public void putMemberToParticipants(NamiMitglied n){
        if(members.contains(n)) {
            members.remove(n);
            participants.add(n);
        }
    }

    /**
     * Puts the the given element from the participants list to the members list
     *
     * @param n
     * 				object in member list
     */
    public void putParticipantToMember(NamiMitglied n){
        if(participants.contains(n)) {
            participants.remove(n);
            members.add(n);
        }
    }

    /**
     * returns the member list
     *
     * @return
     * 				member list
     */
    public List<NamiMitglied> getMember(){
        return members;
    }

    /**
     * returns the participants list
     *
     * @return
     * 				participants list
     */
    public List<NamiMitglied> getParticipants(){
        return participants;
    }

    @Override
    public void onUpdate(int current, int count, NamiMitglied namiMitglied) {
        synchronized (this) {
            members.add(namiMitglied);
            handler.onUpdate(current, count, namiMitglied);
        }
    }

    @Override
    public void onDone(long time) {
        synchronized (this) {
            handler.onDone(time);
        }
    }

    @Override
    public void onException(Exception e) {
        synchronized (this) {
            handler.onException(e);
        }
    }
}
