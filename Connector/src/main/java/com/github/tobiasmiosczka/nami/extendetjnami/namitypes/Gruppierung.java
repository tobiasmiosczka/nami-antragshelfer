package com.github.tobiasmiosczka.nami.extendetjnami.namitypes;

public class Gruppierung {

    private String descriptor;
    private String name;
    private String representedClass;
    private int id;

    public String getDescriptor() {
        return descriptor;
    }

    public int getId() {
        return id;
    }

    public String getRepresentedClass() {
        return representedClass;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return descriptor;
    }
}
