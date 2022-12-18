package com.github.tobiasmiosczka.nami.applicationforms.api;

import org.docx4j.wml.P;

import static com.github.tobiasmiosczka.nami.applicationforms.api.DocUtil.createR;

public class Cell {

    private final P p;
    public Cell(P p) {
        this.p = p;
    }

    public void add(String content) {
        p.getContent().add(createR(content));
    }

    public void add(long content) {
        add(String.valueOf(content));
    }
}
