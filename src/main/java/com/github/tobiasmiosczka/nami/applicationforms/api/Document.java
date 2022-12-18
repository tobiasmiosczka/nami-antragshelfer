package com.github.tobiasmiosczka.nami.applicationforms.api;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.util.List;

import static com.github.tobiasmiosczka.nami.applicationforms.api.DocUtil.findHeaders;

public class Document {

    private final WordprocessingMLPackage wordprocessingMLPackage;
    public Document(WordprocessingMLPackage wordprocessingMLPackage) {
        this.wordprocessingMLPackage = wordprocessingMLPackage;
    }

    public MainDocumentPart getMainDocumentPart() {
        return new MainDocumentPart(wordprocessingMLPackage.getMainDocumentPart());
    }

    public List<Header> getHeaders() {
        return findHeaders(wordprocessingMLPackage).stream().map(Header::new).toList();
    }
}
