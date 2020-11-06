package com.github.tobiasmiosczka.nami.applicationforms;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.simple.TextDocument;

public abstract class AbstractTextDocumentWriter {

	static {
		Logger.getLogger("org.apache.zookeeper").setLevel(Level.OFF);
		Logger.getLogger("org.apache.hadoop.hbase.zookeeper").setLevel(Level.OFF);
		Logger.getLogger("org.apache.hadoop.hbase.client").setLevel(Level.OFF);
	}

	public void run(OutputStream outputStream, List<NamiMitglied> participants) throws Exception {
		InputStream s = Thread.currentThread().getContextClassLoader().getResourceAsStream("applicationForms/"+getResourceFileName());
		TextDocument odtDoc = TextDocument.loadDocument(s);
		manipulateDoc(participants, odtDoc);
		odtDoc.save(outputStream);
	}

	protected abstract void manipulateDoc(List<NamiMitglied> participants, TextDocument odtDoc);

	protected abstract String getResourceFileName();
}