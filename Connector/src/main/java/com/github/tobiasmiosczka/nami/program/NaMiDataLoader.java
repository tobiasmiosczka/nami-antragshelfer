package com.github.tobiasmiosczka.nami.program;

import java.io.IOException;
import java.util.Collection;

import nami.connector.NamiConnector;
import nami.connector.exception.NamiApiException;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiMitgliedListElement;
import nami.connector.namitypes.NamiSearchedValues;

/**
 * Class for downloading Nami data packages in another thread.
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
		Collection<NamiMitgliedListElement> result = namiSearchedValues.getAllResults(connector);
		int i = 0,
			items = result.size();
		for(NamiMitgliedListElement element : result){
			NamiMitglied member = element.getFullData(connector);
			handler.onUpdate(i, items, member);
			i++;
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