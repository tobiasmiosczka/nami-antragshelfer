package com.github.tobiasmiosczka.nami.view;


import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class TableViewDragAndDropUtil {

    private static final DataFormat DATA_FORMAT = new DataFormat("application/x-java-serialized-object");

    private static final EventHandler<DragEvent> ON_DRAG_OVER = event -> {
        if (!event.getDragboard().hasContent(DATA_FORMAT))
            return;
        event.acceptTransferModes(TransferMode.MOVE);
        event.consume();
    };

    private static <T> EventHandler<DragEvent> moveEvent(List<T> buffer, Consumer<List<T>> moveAction) {
        return event -> {
            if (!event.getDragboard().hasContent(DATA_FORMAT))
                return;
            moveAction.accept(buffer);
            buffer.clear();
            event.consume();
        };
    }

    private static <T> EventHandler<MouseEvent> doubleClickEvent(TableRow<T> row, Consumer<List<T>> moveAction) {
        return event -> {
            if (event.getClickCount() != 2 || row.isEmpty())
                return;
            moveAction.accept(List.of(row.getItem()));
            event.consume();
        };
    }

    private static <T> EventHandler<MouseEvent> dragDetectedEvent(List<T> buffer, TableView<T> table, TableRow<T> row) {
        return event -> {
            if (row.isEmpty())
                return;
            Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
            db.setDragView(row.snapshot(null, null));
            ClipboardContent cc = new ClipboardContent();
            cc.put(DATA_FORMAT, "");
            db.setContent(cc);
            buffer.clear();
            buffer.addAll(table.getSelectionModel().getSelectedItems());
            event.consume();
        };
    }

    private static <T> Callback<TableView<T>, TableRow<T>> rowFactory(List<T> buffer, TableView<T> table, Consumer<List<T>> moveAction) {
        return tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnDragDetected(dragDetectedEvent(buffer, table, row));
            row.setOnMouseClicked(doubleClickEvent(row, moveAction));
            return row;
        };
    }

    public static <T> void init(TableView<T> tvA, TableView<T> tvB, Consumer<List<T>> aToB, Consumer<List<T>> bToA) {
        List<T> buffer = new LinkedList<>();
        tvA.setRowFactory(rowFactory(buffer, tvA, aToB));
        tvA.setOnDragOver(ON_DRAG_OVER);
        tvA.setOnDragDropped(moveEvent(buffer, bToA));
        tvA.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tvB.setRowFactory(rowFactory(buffer, tvB, bToA));
        tvB.setOnDragOver(ON_DRAG_OVER);
        tvB.setOnDragDropped(moveEvent(buffer, aToB));
        tvB.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

}
