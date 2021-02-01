package com.github.tobiasmiosczka.nami.applicationforms;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import nami.connector.namitypes.NamiMitglied;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

public abstract class DocumentWriter {

	public void run(OutputStream outputStream, List<NamiMitglied> participants) throws Docx4JException {
		InputStream is = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream("forms/" + getResourceFileName());
		WordprocessingMLPackage doc = WordprocessingMLPackage.load(is);
		manipulateDoc(participants, doc);
		doc.save(outputStream);
	}

	protected abstract void manipulateDoc(List<NamiMitglied> participants, WordprocessingMLPackage doc);

	protected abstract String getResourceFileName();
}