package com.github.tobiasmiosczka.nami.service;

import nami.connector.NamiConnector;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiSearchedValues;

import java.util.concurrent.atomic.AtomicInteger;

public class NamiDataLoader {

	public interface Listener {
		void onUpdate(int current, int count, NamiMitglied newMember);
		void onDone(long time);
		void onException(String message, Throwable t);
	}

	public static void load(NamiConnector connector, NamiSearchedValues searchedValues, Listener listener) {
		long startTime = System.currentTimeMillis();
		AtomicInteger i = new AtomicInteger();
		connector.getAllResults(searchedValues).thenAccept(list -> {
			for (NamiMitglied member : list) {
				connector.getMitgliedById(member.getId())
						.thenAccept(m -> listener.onUpdate(i.getAndIncrement(), list.size(), m))
						.exceptionally(e -> {
							listener.onException("Fehler beim Laden der Mitgliedsdaten.", e);
							return null;
						});
			}
			listener.onDone((System.currentTimeMillis() - startTime));
		});
	}
}