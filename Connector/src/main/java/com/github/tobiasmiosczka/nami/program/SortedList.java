package com.github.tobiasmiosczka.nami.program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SortedList<T> extends ArrayList<T> {
    private Comparator<T> comparator;

    public SortedList(Comparator<T> comparator) {
        this.comparator = comparator;
    }

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
        Collections.sort(this, this.comparator);
    }

    public Comparator<T> getComparator() {
        return comparator;
    }
}