package com.github.toasterguy.nami.program;

import java.io.IOException;
import java.util.*;

import com.github.toasterguy.nami.extendetjnami.ExtendedJNaMi;
import com.github.toasterguy.nami.extendetjnami.namitypes.Gruppierung;
import com.github.toasterguy.nami.extendetjnami.namitypes.SchulungenMap;
import nami.connector.NamiConnector;
import nami.connector.NamiServer;
import nami.connector.credentials.NamiCredentials;
import nami.connector.exception.NamiApiException;
import nami.connector.exception.NamiLoginException;
import nami.connector.namitypes.NamiMitglied;
import com.github.toasterguy.nami.program.NaMiDataLoader.NamiDataLoaderHandler;

/**
 * Program
 * 
 * @author Tobias  Miosczka
 *
 */
public class Program implements NamiDataLoaderHandler{

	public interface ProgramHandler {
		void loaderUpdate(int percents, String info);
		void loaderDone(long timeMS);
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
		SORT_BY_AGE((n1, n2) -> {
			return n1.getGeburtsDatum().compareTo(n2.getGeburtsDatum());
		}),
		SORT_BY_ID((n1, n2) -> {
			return n1.getMitgliedsnummer() - n2.getMitgliedsnummer();
		});

		private Comparator<NamiMitglied> comparator;

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
		member.setComparator(s);
	}
	
	private NamiConnector 	connector;
	private final SortedList<NamiMitglied> member;
	private final SortedList<NamiMitglied> participants;
	private ProgramHandler handler;
	
	/**
	 * Constructor for Program
	 */
	public Program(ProgramHandler handler){
		this.handler = handler;
		member = new SortedList<>(Sortation.SORT_BY_LASTNAME.getComparator());
		participants = new SortedList<>(Sortation.SORT_BY_LASTNAME.getComparator());
	}
	
	/**
	 * procedure for logging into the nami database
	 * 
	 * @param user 
	 * 				valid username (membership number)
	 * @param pass 
	 * 				valid password
	 */
	public void login(String user, String pass) throws NamiLoginException, IOException{
		NamiCredentials credentials = new NamiCredentials(user, pass);
		connector = new NamiConnector(NamiServer.LIVESERVER, credentials);
		connector.namiLogin();
	}
	
	
	/**
	 * loads data from the nami database and displays it in the main frame
	 * program must be logged in first
	 *
	 */
	public void loadData() throws IOException, NamiApiException {
		member.clear();
		participants.clear();
		Collection<Gruppierung> groups = ExtendedJNaMi.getGruppierungen(connector);
		Gruppierung group = handler.selectGruppierung(groups);
		NaMiDataLoader dl = new NaMiDataLoader(connector, group, this);
		dl.start();
	}
	
	/**
	 * Puts the the given element from the members list to the participants list
	 * 
	 * @param n
	 * 				object in member list
	 */
	public void putMemberToParticipants(NamiMitglied n){
		if(member.contains(n)) {
			member.remove(n);
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
			member.add(n);
		}
	}
	
	/**
	 * returns the member list
	 *
	 * @return
	 * 				member list
	 */
	public List<NamiMitglied> getMember(){
		return member;
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
	public void update(int percent, NamiMitglied e) {
		synchronized (this) {
			member.add(e);
			handler.loaderUpdate(percent, "Lädt "+ e.getVorname() + " " + e.getNachname() + " " + percent + "%");
		}
	}

	@Override
	public void done(long time) {
		synchronized (this) {
			handler.loaderDone(time);
		}
	}
}
