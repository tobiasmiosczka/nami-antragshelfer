package com.github.tobiasmiosczka.nami.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
        if (name != null)
            return name + ": " + phoneNumber;
         else
            return phoneNumber;
    }

    private static PhoneContact getPhoneContact(String string) {
        if(string == null || string.isEmpty())
            return null;
        String name = null;
        int openBracketIndex = string.indexOf('(');
        int closeBracketIndex = string.lastIndexOf(')');
        if(openBracketIndex >= 0 && closeBracketIndex >= 0) {
            name = string.substring(openBracketIndex, closeBracketIndex + 1);
            string = string.replace(name, "");
            name = name.substring(1, name.length() - 1).trim();
        }
        String phoneNumber = string.trim();
        return new PhoneContact(name, phoneNumber);
    }

    public static Collection<PhoneContact> getPhoneContacts(String string){
        List<PhoneContact> contacts = new LinkedList<>();
        if (string == null || string.isEmpty())
            return contacts;
        for(String s : string.split(";")) {
            PhoneContact c = getPhoneContact(s);
            if (c != null)
                contacts.add(c);
        }
        return contacts;
    }
}
