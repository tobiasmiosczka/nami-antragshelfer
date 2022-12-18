package com.github.tobiasmiosczka.nami.applicationforms.api;

import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;

import java.util.List;

import static com.github.tobiasmiosczka.nami.applicationforms.api.DocUtil.findTables;

public class Header {

    private final HeaderPart headerPart;

    public Header(HeaderPart headerPart) {
        this.headerPart = headerPart;
    }

    public List<Table> getTables() {
        return findTables(headerPart.getContent()).stream()
                .map(Table::new)
                .toList();
    }
}
