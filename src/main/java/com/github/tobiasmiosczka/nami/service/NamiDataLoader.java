package com.github.tobiasmiosczka.nami.service;

import nami.connector.NamiConnector;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiSearchedValues;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class NamiDataLoader {

	public interface Listener {
		void onUpdate(int current, int count, NamiMitglied e);
		void onDone(long time);
		void onException(String message, Throwable e);
	}

	private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

	public static void load(NamiConnector connector, NamiSearchedValues searchedValues, Listener listener) {
		Thread thread = new Thread(() -> {
			long startTime = System.currentTimeMillis();
			try {
				Collection<NamiMitglied> result = connector.getAllResults(searchedValues);
				AtomicInteger i = new AtomicInteger();
				for (NamiMitglied member : result) {
					CompletableFuture
							.supplyAsync(() -> {
								try {
									return connector.getMitgliedById(member.getId());
								} catch (Exception e) {
									throw new RuntimeException(e);
								}}, EXECUTOR)
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