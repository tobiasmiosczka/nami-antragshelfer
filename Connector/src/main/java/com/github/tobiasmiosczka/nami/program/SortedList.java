package com.github.tobiasmiosczka.nami.program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Sorted List, multithreaded
 * @param <T> type of the list
 * @author Tobias Miosczka
 */

public class SortedList<T> extends ArrayList<T> {
    private Comparator<T> comparator;

    public SortedList(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    //TODO: implement insertionsort since its faster
    public synchronized boolean add(T n) {
        super.add(n);
        update();
        return true;
    }

    public synchronized void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
        this.update();
    }

    private void update() {
        this.sort(this.comparator);
    }

    public synchronized Comparator<T> getComparator() {
        return comparator;
    }
}