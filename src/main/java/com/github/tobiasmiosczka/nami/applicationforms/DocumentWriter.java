package com.github.tobiasmiosczka.nami.applicationforms;

import java.io.InputStream;
import java.io.OutputStream;

import com.github.tobiasmiosczka.nami.applicationforms.api.Document;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

public abstract class DocumentWriter {

	public void run(OutputStream outputStream) throws Docx4JException {
		InputStream is = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream("forms/" + getResourceFileName());
		WordprocessingMLPackage doc = WordprocessingMLPackage.load(is);
		manipulateDoc(new Document(doc));
		doc.save(outputStream);
	}

	protected abstract void manipulateDoc(Document document);

	protected abstract String getResourceFileName();
}