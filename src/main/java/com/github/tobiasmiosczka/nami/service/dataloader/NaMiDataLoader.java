package com.github.tobiasmiosczka.nami.service.dataloader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nami.connector.NamiConnector;
import nami.connector.exception.NamiApiException;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiSearchedValues;

public class NaMiDataLoader {

	private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

	public static void load(NamiConnector connector, NamiSearchedValues namiSearchedValues, NamiDataLoaderHandler handler) {
		Thread t = new Thread(() -> {
			long startTimeInMillis = System.currentTimeMillis();
			try {
				List<NamiMitglied> result = new LinkedList<>(connector.getAllResults(namiSearchedValues));
				for (int i = 0; i < result.size(); ++i) {
					NamiMitglied r = connector.getMitgliedById(result.get(i).getId());
					handler.onUpdate(i, result.size(), r);
				}
				handler.onDone((System.currentTimeMillis() - startTimeInMillis));
			} catch (NamiApiException | IOException e) {
				handler.onException("Beim laden der Mitgliedsdaten ist ein Fehler aufgetreten.", e);
			}
		});
		t.setDaemon(true);
		t.start();
	}
}