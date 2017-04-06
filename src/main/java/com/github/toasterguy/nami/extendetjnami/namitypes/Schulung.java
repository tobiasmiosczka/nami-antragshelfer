package com.github.toasterguy.nami.extendetjnami.namitypes;

import java.util.Date;

/**
 * Created by Tobias on 25.09.2016.
 * Representation of a members courses
 */
public class Schulung {
    public class SchulungsDaten {
        private int id;
        private String vstgName;
        private String veranstalter;
        private String rowCssClass;
        private String mitglied;
        private String baustein;
        private Date vstgTag;

        public String getVstgName() {
            return vstgName;
        }

        public String getVeranstalter() {
            return veranstalter;
        }

        public String getBaustein() {
            return baustein;
        }

        public Date getVstgTag() {
            return vstgTag;
        }
    }

    private  String Descriptor;
    private String name;
    private SchulungsDaten entries;
    private String id;
    private String representedClass;

    public String getDescriptor() {
        return Descriptor;
    }

    public String getName() {
        return name;
    }

    public SchulungsDaten getSchulungsDaten() {
        return entries;
    }
}