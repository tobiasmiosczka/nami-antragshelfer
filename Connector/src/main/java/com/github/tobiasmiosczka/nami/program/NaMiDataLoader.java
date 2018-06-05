package com.github.tobiasmiosczka.nami.program;

import java.io.IOException;
import java.util.Collection;

import nami.connector.NamiConnector;
import nami.connector.exception.NamiApiException;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiSearchedValues;

/**
 * Class for downloading NaMi data packages in another thread.
 * 
 * @author Tobias Miosczka
 * 
 */
public class NaMiDataLoader extends Thread {
	
	public interface NamiDataLoaderHandler{
		void onUpdate(int current, int count, NamiMitglied e);
		void onDone(long time);
		void onException(Exception e);
	}
	
	private final NamiDataLoaderHandler handler;
	private final NamiConnector connector;
	private final NamiSearchedValues namiSearchedValues;
	
	public NaMiDataLoader(NamiConnector connector, NamiSearchedValues namiSearchedValues, NamiDataLoaderHandler handler){
		super();
		this.handler = handler;
		this.connector = connector;
		this.namiSearchedValues = namiSearchedValues;
	}
	
	private void load() throws NamiApiException, IOException{
		long startTimeInMillis = System.currentTimeMillis();
		Collection<NamiMitglied> result = connector.getAllResults(namiSearchedValues);
		int i = 0;
		for(NamiMitglied element : result){
			NamiMitglied member = connector.getMitgliedById(element.getId());
			handler.onUpdate(++i, result.size(), member);
		}
		handler.onDone((System.currentTimeMillis() - startTimeInMillis));
	}

	@Override
	public void run() {
		try {
			load();
		} catch (NamiApiException | IOException e) {
			handler.onException(e);
		}
	}	
}