package com.github.tobiasmiosczka.nami.program;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneContact {
    private final String phoneNumber;
    private final String name;

    public PhoneContact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
            return name;
        }

    public String getPhoneNumber() {
            return phoneNumber;
        }

    public String toString() {
        if (name != null) {
            return name + " : " + phoneNumber;
        } else {
            return phoneNumber;
        }
    }

    private static PhoneContact getPhoneContact(String string) {
        if(string == null || string.isEmpty())
            return null;
        String name = null;
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(string);
        if(m.find()) {
            name = m.group(1);
        }
        String phoneNumber = string.replaceAll("\\(.*\\)", "");
        return new PhoneContact(name, phoneNumber);
    }

    public static Collection<PhoneContact> getPhoneContacts(String string){
        List<PhoneContact> contacts = new LinkedList<>();
        if (string == null || string.isEmpty())
            return contacts;
        string = string.replace(" ", "");
        String[] strings = string.split(";");
        for(String s : strings) {
            PhoneContact c = getPhoneContact(s);
            if (c != null) {
                contacts.add(c);
            }
        }
        return contacts;
    }
}
