package com.github.tobiasmiosczka.nami.applicationforms.api;

import org.docx4j.wml.Tbl;

import static com.github.tobiasmiosczka.nami.applicationforms.api.DocUtil.*;

public class Table {

    private final Tbl tbl;
    public Table(Tbl tbl) {
        this.tbl = tbl;
    }

    public Cell cellAt(int row, int column) {
        return new Cell(getTableCellP(tbl, row, column));
    }

    public void addRow(String...contents) {
        tbl.getContent().add(createTr(contents));
    }

    public void setBorders(int size) {
        addBorders(tbl, size);
    }
}
