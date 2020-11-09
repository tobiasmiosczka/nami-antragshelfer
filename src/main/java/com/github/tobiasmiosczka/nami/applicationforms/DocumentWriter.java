package com.github.tobiasmiosczka.nami.applicationforms;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import nami.connector.namitypes.NamiMitglied;
import org.odftoolkit.simple.TextDocument;

public abstract class DocumentWriter {

	static {
		Logger.getLogger("org.apache.zookeeper").setLevel(Level.OFF);
		Logger.getLogger("org.apache.hadoop.hbase.zookeeper").setLevel(Level.OFF);
		Logger.getLogger("org.apache.hadoop.hbase.client").setLevel(Level.OFF);
	}

	public void run(OutputStream outputStream, List<NamiMitglied> participants) throws Exception {
		InputStream s = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream("forms/" + getResourceFileName());
		TextDocument doc = TextDocument.loadDocument(s);
		manipulateDoc(participants, doc);
		doc.save(outputStream);
	}

	protected abstract void manipulateDoc(List<NamiMitglied> participants, TextDocument doc);

	protected abstract String getResourceFileName();
}