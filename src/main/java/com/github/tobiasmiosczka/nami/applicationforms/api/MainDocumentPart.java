package com.github.tobiasmiosczka.nami.applicationforms.api;

import java.util.List;

public class MainDocumentPart {

    private final org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart mainDocumentPart;

    public MainDocumentPart(org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart mainDocumentPart) {
        this.mainDocumentPart = mainDocumentPart;
    }

    public List<Table> getTables() {
        return DocUtil.findTables(mainDocumentPart.getContent()).stream()
                .map(Table::new)
                .toList();
    }
}
