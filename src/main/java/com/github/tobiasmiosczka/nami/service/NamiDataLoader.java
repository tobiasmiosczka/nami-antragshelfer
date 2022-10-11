package com.github.tobiasmiosczka.nami.service;

import nami.connector.NamiConnector;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiSearchedValues;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class NamiDataLoader {

	public interface Listener {
		void onUpdate(int current, int count, NamiMitglied newMember);
		void onDone(long time);
		void onException(String message, Throwable t);
	}

	public static void load(NamiConnector connector, NamiSearchedValues searchedValues, Listener listener) {
		Thread thread = new Thread(() -> {
			try {
				long startTime = System.currentTimeMillis();
				Collection<NamiMitglied> result = connector.getAllResults(searchedValues).get();
				AtomicInteger i = new AtomicInteger();
				for (NamiMitglied member : result) {
					connector.getMitgliedById(member.getId())
							.thenAccept(m -> listener.onUpdate(i.getAndIncrement(), result.size(), m))
							.exceptionally(e -> {
								listener.onException("Fehler beim Laden der Mitgliedsdaten.", e);
								return null; });
				}
				listener.onDone((System.currentTimeMillis() - startTime));
			} catch (Exception e) {
				listener.onException("Fehler beim Laden der Mitgliedsdaten.", e);
			}
		});
		thread.setDaemon(true);
		thread.start();
	}
}