package com.github.tobiasmiosczka.nami.applicationforms;

import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

import java.util.List;

public class TableUtil {
    public static void appendRows(Table table, int count, double height, Font font, StyleTypeDefinitions.HorizontalAlignmentType horizontalAlignment) {
        List<Row> newRows = table.appendRows(Math.max(count - 1, 0));
        for (Row row : newRows) {
            row.setHeight(height, true);
            for (int i = 0; i < row.getCellCount(); ++i) {
                Cell cell = row.getCellByIndex(i);
                cell.setFont(font);
                cell.setHorizontalAlignment(horizontalAlignment);
            }
        }
    }
}
