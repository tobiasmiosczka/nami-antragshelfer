package com.github.tobiasmiosczka.nami.program;

import java.io.IOException;
import java.util.Collection;

import com.github.tobiasmiosczka.nami.extendetjnami.namitypes.Gruppierung;

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
class NaMiDataLoader extends Thread {
	
	public interface NamiDataLoaderHandler{
		void update(int percent, NamiMitglied e);
		void done(long time);
	}
	
	private final NamiDataLoaderHandler handler;
	private final NamiConnector connector;
	private final Gruppierung group;
	
	public NaMiDataLoader(NamiConnector connector, Gruppierung group, NamiDataLoaderHandler handler){
		super();
		this.handler = handler;
		this.connector = connector;
		this.group = group;
	}
	
	private void load() throws NamiApiException, IOException{
		long t1 = System.currentTimeMillis();
		NamiSearchedValues search = new NamiSearchedValues();
		if(group != null) {
			search.setGruppierungsnummer(String.valueOf(group.getId()));
		}
		Collection<NamiMitgliedListElement> result = search.getAllResults(connector);
		int i = 0;
		int items = result.size();

		for(NamiMitgliedListElement element : result){
			NamiMitglied e = element.getFullData(connector);
			handler.update(100 * i / items, e);
			i++;
		}

		handler.done((System.currentTimeMillis() - t1));
	}

	@Override
	public void run() {
		try {
			load();
		} catch (NamiApiException | IOException e) {
			e.printStackTrace();
		}
	}	
}