package com.github.tobiasmiosczka.nami.extendetjnami.namitypes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Schulung {

    @SerializedName("entries_vstgTag")
    @Expose
    private Date date;
    @SerializedName("entries_veranstalter")
    @Expose
    private String veranstalter;
    @SerializedName("entries_vstgName")
    @Expose
    private String veranstaltungsname;
    @SerializedName("entries_baustein")
    @Expose
    private Baustein baustein;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("descriptor")
    @Expose
    private String descriptor;
    @SerializedName("entries_id")
    @Expose
    private Integer entriesId;
    @SerializedName("representedClass")
    @Expose
    private String representedClass;
    @SerializedName("entries_mitglied")
    @Expose
    private String entriesMitglied;

    public Baustein getBaustein() {
        return baustein;
    }

    public Date getDate() {
        return date;
    }
}