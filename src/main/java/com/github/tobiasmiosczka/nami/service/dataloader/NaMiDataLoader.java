package com.github.tobiasmiosczka.nami.service.dataloader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import nami.connector.NamiConnector;
import nami.connector.exception.NamiApiException;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiSearchedValues;

public class NaMiDataLoader {

	private final NamiConnector connector;
	
	public NaMiDataLoader(NamiConnector connector){
		this.connector = connector;
	}

	public void load(NamiSearchedValues namiSearchedValues, NamiDataLoaderHandler handler) {
		new Thread(() -> {
			long startTimeInMillis = System.currentTimeMillis();
			try {
				List<NamiMitglied> result = new LinkedList<>(connector.getAllResults(namiSearchedValues));
				int count = result.size();
				for (int i = 0; i < result.size(); ++i) {
					NamiMitglied r = connector.getMitgliedById(result.get(i).getId());
					handler.onUpdate(i, count, r);
				}
				handler.onDone((System.currentTimeMillis() - startTimeInMillis));
			} catch (NamiApiException | IOException e) {
				handler.onException("Beim laden der Mitgliedsdaten ist ein Fehler aufgetreten.", e);
			}
		}).start();
	}
}