package com.github.tobiasmiosczka.nami.model;

import java.util.*;
import java.util.stream.Collectors;

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
        if (string == null || string.isEmpty())
            return new LinkedList<>();
        return Arrays.stream(string.split(";"))
                .map(PhoneContact::getPhoneContact)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
